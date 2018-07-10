package com.accenture.salvo;


import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private Date creationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;



    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER,cascade = CascadeType.ALL)
    Set<Ship> ships;


    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER,cascade = CascadeType.ALL)
    Set<Salvo> salvoes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    public GamePlayer(){}

    public GamePlayer(Date date, Game game, Player player){
            this.creationDate=date;
            this.game=game;
            this.player=player;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }

    public void setSalvoes(Set<Salvo> salvoes) {
        this.salvoes = salvoes;
    }


    public Map<String, Object> gamePlayerDto(){
        Map<String,Object> list = new LinkedHashMap<>();
        list.put("id",this.id);
        list.put("player",this.player.playerDto());
        list.put("joinDate",game.getGameDate());
        list.put("ships",this.ships.stream()
                .map(ship -> ship.shipDto())
                .collect(Collectors.toList()));
        return list;
    }
    public Map<String, Object> gamePlayerDtoNoShips(){
        Map<String,Object> list = new LinkedHashMap<>();
        list.put("id",this.id);
        list.put("player",this.player.playerDto());
        list.put("joinDate",game.getGameDate());
        return list;
    }
    public List<Object> gamePlayerShipsDto(){
        return this.ships
                .stream()
                .map(ship -> ship.shipDto())
                .collect(Collectors.toList());
    }
    public List<Object> gamePlayerSalvosDto(){
        return this.salvoes
                .stream()
                .map(salvo -> salvo.salvoDto())
                .collect(Collectors.toList());
    }
    public Map<String, Object> gamePlayerDtoPlayers(){
        Map<String,Object> list = new LinkedHashMap<>();
        list.put("id",this.id);
        list.put("joinDate", this.creationDate);
        list.put("player",this.player.playerDto());
        return list;
    }

    public Score getScore() {
       return this.game.getScores().stream()
               .findFirst().get();
    }
    public void setShip(List<Ship> ships) {
        ships.forEach(ship ->{
            ship.setGamePlayer(this);
            this.ships.add(ship);
        });
    }

    public void setSalvo(Salvo salvo) {
        salvo.setGamePlayer(this);
        this.salvoes.add(salvo);
    }
    public Map<String,Object> dtoHits (){
        Map<String,Object> hits = new LinkedHashMap<>();
        GamePlayer opponent = this.getGame().getGamePlayers().stream().filter(gamePlayer1 -> gamePlayer1!=this).findFirst().get();
        hits.put("self",this.salvoes.stream().map(salvo -> salvo.hits(ships)).toArray());
        hits.put("opponent",opponent.salvoes.stream().map(salvo -> salvo.hits(ships)).toArray());
        return  hits;
}


}

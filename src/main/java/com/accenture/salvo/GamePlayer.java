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

    private final String playerString = "player";
    private final String joinDateString = "joinDate";

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
        list.put(playerString,this.player.playerDto());
        list.put(joinDateString,game.getGameDate());
        list.put("ships",this.ships.stream()
                .map(Ship::shipDto)
                .collect(Collectors.toList()));
        return list;
    }
    public Map<String, Object> gamePlayerDtoNoShips(){
        Map<String,Object> list = new LinkedHashMap<>();
        list.put("id",this.id);
        list.put(playerString,this.player.playerDto());
        list.put(joinDateString,game.getGameDate());
        return list;
    }
    public List<Object> gamePlayerShipsDto(){
        return this.ships
                .stream()
                .map(Ship::shipDto)
                .collect(Collectors.toList());
    }
    public List<Object> gamePlayerSalvosDto(){
        return this.salvoes
                .stream()
                .map(Salvo::salvoDto)
                .collect(Collectors.toList());
    }
    public Map<String, Object> gamePlayerDtoPlayers(){
        Map<String,Object> list = new LinkedHashMap<>();
        list.put("id",this.id);
        list.put(joinDateString, this.creationDate);
        list.put(playerString,this.player.playerDto());
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
        GamePlayer opponent = getOpponent();
        if(opponent!=null){
            hits.put("self",opponent.salvoes.stream().sorted(Comparator.comparingLong(Salvo::getTurn)).map(salvo -> salvo.hits(ships)).toArray());
            hits.put("opponent",this.salvoes.stream().sorted(Comparator.comparingLong(Salvo::getTurn)).map(salvo -> salvo.hits(opponent.ships)).toArray());
        }else{
            List<Object> list = new LinkedList<>();
            hits.put("self",list);
            hits.put("opponent",list);
        }

        return  hits;

    }

    public String gameState(){

        GamePlayer opponent = getOpponent();
        int lastTurnSelf = this.salvoes.size();
        int lastTurnOpponent = 0;
        boolean gamePlayerSelfLost = this.GamePlayerLost();
        boolean gamePlayerOpponentLost = false;
        if(opponent!=null){
           lastTurnOpponent = opponent.salvoes.size();
           gamePlayerOpponentLost = opponent.GamePlayerLost();
        }

        boolean scoresSetted = Scores();

        if(this.ships.isEmpty()&& (this.getGame().getScores().size()<=0) ||(this.ships.isEmpty()&& (this.getGame().getScores().size()<=0) && opponent==null)){
            return "PLACESHIPS";
        }
        else if(!gamePlayerOpponentLost && gamePlayerSelfLost && scoresSetted && (this.salvoes.size()==opponent.salvoes.size())){
            return "LOST";        }
        else if(!gamePlayerSelfLost && gamePlayerOpponentLost && scoresSetted && (this.salvoes.size()==opponent.salvoes.size())){
            return "WON";
        }
        else if((gamePlayerOpponentLost && gamePlayerSelfLost) && (lastTurnOpponent==lastTurnSelf) && scoresSetted && (this.salvoes.size()==opponent.salvoes.size())){
            return "TIE";
        }
        else if((this.getGame().getScores().isEmpty()) && (this.getGame().getGamePlayers().size()==1 && this.getGame().getGamePlayers().contains(this)) || opponent==null ||  (!(this.ships.isEmpty())) && opponent.ships.isEmpty() || (lastTurnSelf>lastTurnOpponent)){
            return "WAIT";
        }
        else {
            return "PLAY";
        }
    }

    public GamePlayer getOpponent (){
        return this.getGame().getGamePlayers().stream().filter(gamePlayer1 -> gamePlayer1!=this).findFirst().orElse(null);
    }

    public boolean GamePlayerLost (){
        Map<String,Integer> shipsLength = new LinkedHashMap<>();
        shipsLength.put(ShipTypes.KEY_CARRIER,5);
        shipsLength.put(ShipTypes.KEY_BATTLESHIP,4);
        shipsLength.put(ShipTypes.KEY_SUBMARINE,3);
        shipsLength.put(ShipTypes.KEY_DESTROYER,3);
        shipsLength.put(ShipTypes.KEY_PATROLBOAT,2);

        Map<String,Integer> shipsHits = new LinkedHashMap<>();
        shipsHits.put(ShipTypes.KEY_CARRIER,0);
        shipsHits.put(ShipTypes.KEY_BATTLESHIP,0);
        shipsHits.put(ShipTypes.KEY_SUBMARINE,0);
        shipsHits.put(ShipTypes.KEY_DESTROYER,0);
        shipsHits.put(ShipTypes.KEY_PATROLBOAT,0);

        List<String> shipsTypes=new ArrayList<>();
        List<String> shipsLost = new ArrayList<>();
        GamePlayer opponent = getOpponent();
        if(opponent!=null) {
            List<Map<String, Integer>> damages = opponent.salvoes.stream().map(salvo -> salvo.damagesMap(this.ships)).collect(Collectors.toList());
            for (Map<String, Integer> map : damages) {
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();

                    shipsAndShipsHitted(key, shipsHits, shipsTypes, value);

                }
            }
            shipsSunken(shipsLength, shipsLost, shipsHits);

            return shipsLost.size() == shipsTypes.size();
        }else{
            return false;
        }

    }
    private boolean Scores(){
        GamePlayer opponent = getOpponent();
        boolean gamePlayerSelfLost = this.GamePlayerLost();
        boolean gamePlayerOpponentLost = false;
        if(opponent!=null){
            gamePlayerOpponentLost = opponent.GamePlayerLost();
        }

        int lastTurnSelf = this.salvoes.size();
        int lastTurnOpponent = 0;
        if(opponent!=null){
           lastTurnOpponent = opponent.salvoes.size();
        }


        if (this.getGame().getScores().size()>0){
            return true;
        }
        else if(opponent==null){
            return false;
        }
        else if(this.ships.isEmpty() || opponent.ships.isEmpty() || this.salvoes.isEmpty() || opponent.salvoes.isEmpty() || (this.getGame().getGamePlayers().size()==1 && this.getGame().getGamePlayers().contains(this)) || (lastTurnSelf>lastTurnOpponent)){
            return false;
        }
        else if(!gamePlayerOpponentLost && gamePlayerSelfLost && (this.salvoes.size()==opponent.salvoes.size())){
              this.game.getScores().add(new Score(1,new Date(),this.game,opponent.getPlayer()));
              return true;
        }
        else if(!gamePlayerSelfLost && gamePlayerOpponentLost && (this.salvoes.size()==opponent.salvoes.size())){
            this.game.getScores().add(new Score(1,new Date(),this.game,this.getPlayer()));
            return true;
        }
        else if((gamePlayerOpponentLost && gamePlayerSelfLost) && (lastTurnOpponent==lastTurnSelf) && (this.getGame().getScores().size()==0)){
            this.game.getScores().add(new Score(0.5,new Date(),this.game,opponent.getPlayer()));
            this.game.getScores().add(new Score(0.5,new Date(),this.game,this.getPlayer()));

            return true;
        }
        return false;
    }

    private void shipsAndShipsHitted (String key, Map<String,Integer> shipsHits, List<String> shipsTypes,Integer value){
       if ((!key.contains("Hits")) && value>0 && (!(shipsTypes.contains(key)))){
           shipsTypes.add(key);
       }
       else{
        switch (key){
            case "carrierHits":
                shipsHits.put(ShipTypes.KEY_CARRIER, shipsHits.get(ShipTypes.KEY_CARRIER) + value);
                break;
            case "battleshipHits":
                shipsHits.put(ShipTypes.KEY_BATTLESHIP, shipsHits.get(ShipTypes.KEY_BATTLESHIP) + value);
                break;
            case "submarineHits":
                shipsHits.put(ShipTypes.KEY_SUBMARINE, shipsHits.get(ShipTypes.KEY_SUBMARINE) + value);
                break;
            case "destroyerHits":
                shipsHits.put(ShipTypes.KEY_DESTROYER, shipsHits.get(ShipTypes.KEY_DESTROYER) + value);
                break;
            case "patrolboatHits":
                shipsHits.put(ShipTypes.KEY_PATROLBOAT, shipsHits.get(ShipTypes.KEY_PATROLBOAT) + value);
                break;
            default:
                break;
        }
       }
    }

    public void shipsSunken (Map<String,Integer> shipsLength, List<String> shipsLost, Map<String,Integer> shipsHits){
        for (Map.Entry<String, Integer> entry : shipsHits.entrySet()) {
            String shipType = entry.getKey();
            Integer hits = entry.getValue();

            if((shipsLength.get(shipType))==hits){
                shipsLost.add(shipType);
            }
        }
    }


}

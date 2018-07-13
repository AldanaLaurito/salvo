package com.accenture.salvo;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;



@Entity
public class Game {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private Date gameDate;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Score> scores;

    public Game(){ this.gameDate = new Date();    }
    public Game(Date date){
        this.gameDate=date;
    }

    public Date getGameDate() {
        return gameDate;
    }

    public void setGameDate(Date date) {
        this.gameDate = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public Map<String, Object> gameDtoNoShips(){
        Map<String,Object> list = new HashMap<>();
        list.put("id",this.id);
        list.put("created",this.gameDate);
        list.put("gamePlayers",this.gamePlayers
                .stream()
                .map(GamePlayer::gamePlayerDtoNoShips)
                .collect(Collectors.toList()));
        list.put("scores",this.scores.stream().map(Score::scoreDto).collect(Collectors.toList()));
        return list;
    }
    
    public List<Object> gameDtoPlayers(){
        return this.gamePlayers
                .stream()
                .map(GamePlayer::gamePlayerDtoPlayers)
                .collect(Collectors.toList());
    }
    public List<Object> gameDtoSalvo(){
        return this.gamePlayers
                .stream()
                .flatMap(gamePlayer -> gamePlayer.getSalvoes().stream().map(Salvo::salvoDto))
                .collect(Collectors.toList());
    }


}

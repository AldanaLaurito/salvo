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

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

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

    public Map<String, Object> gameDto(){
        Map<String,Object> list = new HashMap<>();
        list.put("id",this.id);
        list.put("created",this.gameDate);
        list.put("gamePlayers",this.gamePlayers.stream().map(gamePlayer -> gamePlayer.gamePlayerDto()).collect(Collectors.toList()));
        return list;
    }

}

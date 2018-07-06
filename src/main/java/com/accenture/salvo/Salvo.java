package com.accenture.salvo;

import javax.persistence.*;
import java.util.*;

@Entity
public class Salvo {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private long turn;

    @ElementCollection
    @Column(name="listLocation")
    private List<String> locations =new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;


    public Salvo() {
    }
    public Salvo(long turn, GamePlayer gamePlayer) {
        this.turn=turn;
        // this.locations=new ArrayList<>();
        this.gamePlayer=gamePlayer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTurn() {
        return turn;
    }

    public void setTurn(long turn) {
        this.turn = turn;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public Map<String, Object> salvoDto(){
        Map<String,Object> list = new HashMap<>();
        list.put("turn", this.turn);
        list.put("player",this.gamePlayer.getPlayer().getId());
        list.put("locations",this.locations);
        return list;
    }

    public Map<String, Object> hits(Set<Ship> ships){
        Map<String,Object> listHits = new HashMap<>();
        listHits.put("turn",this.turn);
        //this.locations.containsAll()
        List<String> hitLocations = new ArrayList<>();
        long missed = 0;


        return listHits;
    }

    public Map<String, Object> damages (List<Object> hitLocations,Set<Ship> ships){
        Map<String,Object> listHits = new HashMap<>();

        return listHits;
    }
}

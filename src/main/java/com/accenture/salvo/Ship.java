package com.accenture.salvo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;


    private String shipType;

    @ElementCollection
    @Column(name="shipLocations")
    private List<String> shipLocations =new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;


    public Ship() {
    }
    public Ship(String type, GamePlayer gamePlayer) {
        this.shipType =type;
        this.gamePlayer=gamePlayer;
    }

   public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return shipType;
    }

    public void setType(String type) {
        this.shipType = type;
    }
    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public List<String> getLocations() {
        return shipLocations;
    }

    public void setLocations(List<String> locations) {
        this.shipLocations = locations;
    }
    public void setShipLocations(List<String> shipLocations) {
        this.shipLocations = shipLocations;
    }

    public Map<String, Object> shipDto(){
        Map<String,Object> list = new HashMap<>();
        list.put("type", this.shipType);
        list.put("locations",this.shipLocations);
        return list;
    }



}

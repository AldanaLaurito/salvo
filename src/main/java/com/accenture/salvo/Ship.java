package com.accenture.salvo;

import com.accenture.salvo.GamePlayer;

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
    @Column(name="shipLocation")
    private List<String> listLocations=new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;


    public Ship() {
    }
    public Ship(String type, GamePlayer gamePlayer) {
        this.shipType=type;
       // this.listLocations=new ArrayList<>();
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

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public List<String> getListLocations() {
        return listLocations;
    }

    public void setListLocations(List<String> listLocations) {
        this.listLocations = listLocations;
    }

    public Map<String, Object> shipDto(){
        Map<String,Object> list = new HashMap<>();
        list.put("type", this.shipType);
        list.put("locations",this.listLocations);
        return list;
    }



}

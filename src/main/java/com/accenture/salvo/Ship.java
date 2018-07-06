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


    private String type;

    @ElementCollection
    @Column(name="shipLocation")
    private List<String> locations =new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;


    public Ship() {
    }
    public Ship(String type, GamePlayer gamePlayer) {
        this.type =type;
       // this.locations=new ArrayList<>();
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
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public Map<String, Object> shipDto(){
        Map<String,Object> list = new HashMap<>();
        list.put("type", this.type);
        list.put("locations",this.locations);
        return list;
    }



}

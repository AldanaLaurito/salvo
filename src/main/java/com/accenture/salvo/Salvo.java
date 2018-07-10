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
        Map<String,Object> list = new LinkedHashMap<>();
        list.put("turn", this.turn);
        list.put("player",this.gamePlayer.getPlayer().getId());
        list.put("locations",this.locations);
        return list;
    }

    public Map<String, Object> hits(Set<Ship> ships){
        Map<String,Object> listHits = new LinkedHashMap<>();
        Map<String,Integer>damages = initializeDamages();
        listHits.put("turn",this.turn);
        List<String> hitLocations = new ArrayList<>();
        List<String> missLocations = new ArrayList<>();
        List<String> salvoLocations=this.locations;


     /*ships.forEach(ship -> getLocations().forEach(location->{
            for(int j=0;j<salvoLocations.size();j++){
                //          listHits.put("location",ship.getLocations().get(i));
                //          listHits.put("salvoLocation",salvoLocations.get(j));
                String type = ship.getType();
                Boolean hitted = false;
                if (location==salvoLocations.get(j)){
                    hitLocations.add(location);
                    hitted = true;
                }
                else{
                    missLocations.add(location);
                }
                switch (type){
                    case "carrier":
                        damages.put(type,damages.getOrDefault(type,0)+1);
                        if (hitted){
                            damages.put(type+"Hits",damages.get(type+"Hits"+1));
                        }
                        break;
                    case "battleship":
                        damages.put(type,damages.getOrDefault(type,0)+1);
                        if (hitted){
                            damages.put(type+"Hits",damages.get(type+"Hits"+1));
                        }
                        break;
                    case "submarine":
                        damages.put(type,damages.getOrDefault(type,0)+1);
                        if (hitted){
                            damages.put(type+"Hits",damages.get(type+"Hits"+1));
                        }
                        break;
                    case "destroyer":
                        damages.put(type,damages.getOrDefault(type,0)+1);
                        if (hitted){
                            damages.put(type+"Hits",damages.get(type+"Hits"+1));
                        }
                        break;
                    case "patrolboat":
                        damages.put(type,damages.getOrDefault(type,0)+1);
                        if (hitted){
                            damages.put(type+"Hits",damages.get(type+"Hits"+1));
                        }
                        break;
                }

            }}));  */


        ships.forEach(ship -> {
                    for(int i=0; i<ship.getLocations().size();i++){
                 //         listHits.put("location",ship.getLocations().get(i));
                        for(int j=0;j<salvoLocations.size();j++){
                //          listHits.put("location",ship.getLocations().get(i));
                //          listHits.put("salvoLocation",salvoLocations.get(j));
                            String type = ship.getType();
                            Boolean hitted = false;
                            if (ship.getLocations().get(i)==salvoLocations.get(j)){
                                hitLocations.add(ship.getLocations().get(i));
                                hitted = true;
                            }
                            else{
                                missLocations.add(salvoLocations.get(j));
                            }
                            switch (type){
                                case "carrier":
                                    damages.put(type,damages.get(type)+1);
                                    if (hitted){
                                        damages.put(type+"Hits",damages.get(type+"Hits")+1);
                                    }
                                    break;
                                case "battleship":
                                    damages.put(type,damages.get(type)+1);
                                    if (hitted){
                                        damages.put(type+"Hits",damages.get(type+"Hits")+1);
                                    }
                                    break;
                                case "submarine":
                                    damages.put(type,damages.get(type)+1);
                                    if (hitted){
                                        damages.put(type+"Hits",damages.get(type+"Hits")+1);
                                    }
                                    break;
                                case "destroyer":
                                    damages.put(type,damages.get(type)+1);
                                    if (hitted){
                                        damages.put(type+"Hits",damages.get(type+"Hits")+1);
                                    }
                                    break;
                                case "patrolboat":
                                    damages.put(type,damages.get(type)+1);
                                    if (hitted){
                                        damages.put(type+"Hits",damages.get(type+"Hits")+1);
                                    }
                                    break;
                            }

                        }
                    }});

        long missed = missLocations.size();
        listHits.put("hitLocations",hitLocations);
        listHits.put("damages",damages);
        listHits.put("missed",missed);
        return listHits;
    }

    public Map<String,Integer> initializeDamages(){
        Map<String,Integer>damages = new HashMap<>();
        damages.put("carrierHits",0);
        damages.put("carrier",0);
        damages.put("battleshipHits",0);
        damages.put("battleship",0);
        damages.put("submarineHits",0);
        damages.put("submarine",0);
        damages.put("destroyer",0);
        damages.put("destroyerHits",0);
        damages.put("patrolboatHits",0);
        damages.put("patrolboat",0);
        return damages;
    }

   public Map<String,Object> test (Set<Ship> ships){
       Map<String,Object> listHits = new HashMap<>();
       listHits.put("turn",this.turn);
       ships.forEach(ship -> listHits.put("type",ship.getType()));

       return listHits;


   }
}

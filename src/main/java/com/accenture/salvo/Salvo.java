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
    private List<String> salvoLocations =new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;


    public Salvo() {
    }
    public Salvo(long turn, GamePlayer gamePlayer) {
        this.turn=turn;
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
        return salvoLocations;
    }


    public void setSalvoLocations(List<String> locations) {
        this.salvoLocations = locations;
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
        list.put("locations",this.salvoLocations);
        return list;
    }

    public Map<String, Object> hits(Set<Ship> ships){
        Map<String,Object> listHits = new LinkedHashMap<>();
        Map<String,Integer> damages = damagesMap(ships);
        listHits.put("turn",this.turn);
        List<String> hitLocations = hitLocations(ships);
        List<String> salvosLocations=this.salvoLocations;

        listHits.put("hitLocations",hitLocations);
        listHits.put("damages",damages);
        listHits.put("missed",salvosLocations.size() - hitLocations.size());
        return listHits;
    }


    public List<String> hitLocations (Set<Ship> ships){
        List<String> hitLocations = new ArrayList<>();
        List<String> salvosLocations=this.salvoLocations;

        for(Ship ship : ships) {
            for (int i = 0; i < ship.getLocations().size(); i++) {
                for (String salvoLocation : salvosLocations) {
                    if (ship.getLocations().get(i).equals(salvoLocation)) {
                        hitLocations.add(ship.getLocations().get(i));
                    }

                }
            }

        }
        return hitLocations;
    }

    public Map<String,Integer> damagesMap(Set<Ship> ships){
        Map<String,Integer>damages = initializeDamages();
        List<String> salvosLocations=this.salvoLocations;

        for(Ship ship : ships) {
            int loop = 0;
            for (int i = 0; i < ship.getLocations().size(); i++) {
                for (String salvoLocation : salvosLocations) {
                    String type = ship.getType();
                    Boolean hitted = false;
                    if (ship.getLocations().get(i).equals(salvoLocation)) {
                        hitted = true;
                    }


                    switch (type) {
                        case "carrier":
                            if (loop == 0) {
                                damages.put(ShipTypes.KEY_CARRIER, damages.get(ShipTypes.KEY_CARRIER) + 1);
                            }
                            if (hitted) {
                                damages.put(ShipHits.KEY_CARRIER, damages.get(ShipHits.KEY_CARRIER) + 1);
                            }
                            break;
                        case "battleship":
                            if (loop == 0) {
                                damages.put(ShipTypes.KEY_BATTLESHIP, damages.get(ShipTypes.KEY_BATTLESHIP) + 1);
                            }
                            if (hitted) {
                                damages.put(ShipHits.KEY_BATTLESHIP, damages.get(ShipHits.KEY_BATTLESHIP) + 1);
                            }
                            break;
                        case "submarine":
                            if (loop == 0) {
                                damages.put(ShipTypes.KEY_SUBMARINE, damages.get(ShipTypes.KEY_SUBMARINE) + 1);
                            }
                            if (hitted) {
                                damages.put(ShipHits.KEY_SUBMARINE, damages.get(ShipHits.KEY_SUBMARINE) + 1);
                            }
                            break;
                        case "destroyer":
                            if (loop == 0) {
                                damages.put(ShipTypes.KEY_DESTROYER, damages.get(ShipTypes.KEY_DESTROYER) + 1);
                            }
                            if (hitted) {
                                damages.put(ShipHits.KEY_DESTROYER, damages.get(ShipHits.KEY_DESTROYER) + 1);
                            }
                            break;
                        case "patrolboat":
                            if (loop == 0) {
                                damages.put(ShipTypes.KEY_PATROLBOAT, damages.get(ShipTypes.KEY_PATROLBOAT) + 1);
                            }
                            if (hitted) {
                                damages.put(ShipHits.KEY_PATROLBOAT, damages.get(ShipHits.KEY_PATROLBOAT) + 1);
                            }
                            break;
                        default:
                            break;
                    }
                    loop++;
                }
            }

        }
        return damages;
    }

    private Map<String,Integer> initializeDamages(){
        Map<String,Integer>damages = new HashMap<>();
        damages.put(ShipHits.KEY_CARRIER,0);
        damages.put(ShipTypes.KEY_CARRIER,0);
        damages.put(ShipHits.KEY_BATTLESHIP,0);
        damages.put(ShipTypes.KEY_BATTLESHIP,0);
        damages.put(ShipHits.KEY_SUBMARINE,0);
        damages.put(ShipTypes.KEY_SUBMARINE,0);
        damages.put(ShipTypes.KEY_DESTROYER,0);
        damages.put(ShipHits.KEY_DESTROYER,0);
        damages.put(ShipHits.KEY_PATROLBOAT,0);
        damages.put(ShipTypes.KEY_PATROLBOAT,0);
        return damages;
    }

}

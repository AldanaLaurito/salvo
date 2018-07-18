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

    public Map<String, Object> hits(Set<Ship> ships, Set<Salvo> salvos,Map<String,Integer>damages){
        Map<String,Object> listHits = new LinkedHashMap<>();
        damages = damagesMap(ships, salvos,damages,this.getTurn());
        listHits.put("turn",this.turn);
        List<String> hitLocations = hitLocations(ships);
        List<String> salvosLocations=this.salvoLocations;

        listHits.put("hitLocations",hitLocations);
        listHits.put("damages",new LinkedHashMap<>(damages));
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

    public Map<String,Integer> damagesMap(Set<Ship> ships, Set<Salvo> salvos,Map<String,Integer>damages, long turn){
        initializeDamages1(damages);
        List<String> salvosLocations=this.salvoLocations;


            for (Ship ship : ships) {
                int loop = 0;
                String type = ship.getType();
                for (int i = 0; i < ship.getLocations().size(); i++) {
                    for (String salvoLocation : salvosLocations) {

                        if (ship.getLocations().get(i).equals(salvoLocation)) {
                            damagesAndShips(loop, damages, type);
                        }
                    }
                }
                loop ++;
            }

        allDamages(damages,ships,salvos, turn);
        return damages;
    }

    private void initializeDamages1(Map<String,Integer> damages){
        damages.put(ShipHits.KEY_CARRIER,0);
        damages.put(ShipHits.KEY_BATTLESHIP,0);
        damages.put(ShipHits.KEY_SUBMARINE,0);
        damages.put(ShipHits.KEY_DESTROYER,0);
        damages.put(ShipHits.KEY_PATROLBOAT,0);
    }

    private void damagesAndShips (int loop, Map<String,Integer> damages, String type){
                if (loop == 0) {
                    damages.put(type+"Hits", damages.get(type+"Hits") + 1);
                }
        }

        private void allDamages (Map<String,Integer> damages,Set<Ship> ships, Set<Salvo> salvos,long turn){
            for (Ship ship : ships) {
                String type = ship.getType();
                for (int i = 0; i < ship.getLocations().size(); i++) {
                    for (Salvo salvo : salvos) {
                        for (int j = 0; j < salvo.getLocations().size(); j++) {

                            if ((ship.getLocations().get(i).equals(salvo.getLocations().get(j))) && (salvo.getTurn()<=turn)) {
                                damages.put(type, damages.get(type) + 1);
                            }
                        }


                    }
                }
            }

        }


}

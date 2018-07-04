package com.accenture.salvo;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String userName;
    private String password;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<Score> scores;

    public Player(){    }
    public Player(String user, String pass){
        this.userName=user;
        this.password=pass;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }
    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Map<String, Object> playerDto(){
        Map<String,Object> list = new HashMap<>();
        list.put("id", this.id);
        list.put("email",this.userName);
        list.put("scores", this.playerLeaderBoard());
        return list;
    }

    public Score getScore(Game game){
        if (game.getScores().stream().findFirst()==null){
            return null;
        }
        else{
            return this.scores.stream()
                    .filter(score -> score.getGame()
                            .equals(game))
                    .findFirst().get();
        }
    }

    public Map<String,Object> playerLeaderBoard(){
        Map<String,Object> list = new HashMap<>();
        list.put("total", this.scores
                .stream().mapToDouble(Score::getScore).sum());
        list.put("won", this.scores
                .stream()
                .filter(score -> score.getScore()==1).count());
        list.put("lost", this.scores
                .stream()
                .filter(score -> score.getScore()==0).count());
        list.put("tied", this.scores
                .stream()
                .filter(score -> score.getScore()==0.5).count());
        return list;
    }
    public Map<String,Object> playerLeaderBoardDto(){
        Map<String,Object> list = new HashMap<>();
        list.put("name", this.getUserName());
        list.put("score", this.playerLeaderBoard());
        return list;
    }

    public Map<String,Object> playerLogged(){
        Map<String,Object> list = new HashMap<>();
        list.put("id", this.getId());
        list.put("name", this.getUserName());
        return list;
    }


}

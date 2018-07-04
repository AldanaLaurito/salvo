package com.accenture.salvo;

import com.accenture.salvo.Game;
import com.accenture.salvo.Player;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Score {


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private double score;

    private Date finishDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    Score(){

    }

    Score(double score, Date finish, Game game, Player player){
        this.score=score;
        this.finishDate=finish;
        this.game=game;
        this.player=player;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Map<String, Object> scoreDto(){
        Map<String,Object> list = new HashMap<>();
        list.put("playerID", this.player.getId());
        list.put("score",this.score);
        list.put("finishDate",this.finishDate);
        return list;
    }

}

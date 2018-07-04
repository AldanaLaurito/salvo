package com.accenture.salvo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {
    @Autowired
    private GameRepository repo;

    @Autowired
    private GamePlayerRepository gamePlayerRepo;

    @Autowired
    private PlayerRepository playerRepo;


    @RequestMapping(path = "/players",method = RequestMethod.POST)
    public ResponseEntity<Object> createPlayer(@RequestParam String email, @RequestParam String password){
        Map<String,String> msg=new HashMap<>();

        Player player = playerRepo.findByUserName(email);
        if (player != null) {
            msg.put("error","Name in use");
            return new ResponseEntity<>(msg, HttpStatus.FORBIDDEN);
        }

        playerRepo.save(new Player(email,password));
        msg.put("name",email);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @RequestMapping("/games")
    public Map<String, Object> getAll(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        if (!(isGuest(authentication))){

            dto.put("player",getPlayerLogged(authentication).playerLogged());
        }
        else {
            dto.put("player", "guest");
        }

            dto.put("games", repo.findAll().stream()
                    .map(Game::gameDtoNoShips)
                    .collect(Collectors.toList()));
            return dto;

    }
    @RequestMapping("/player/{idPlayer}/leaderBoard")
    public Map<String, Object> getPlayerLeaderboard(@PathVariable long idPlayer) {
        Player player = playerRepo.findOne(idPlayer);
        return player.playerDto();
    }
    @RequestMapping("/leaderBoard")
    public List<Object> getLeaderboard() {
        List<Player> players = playerRepo.findAll();
       return  players.stream().map(Player::playerLeaderBoardDto).collect(Collectors.toList());
    }


    @RequestMapping("/game_view/{idGamePlayer}")
    public Map<String, Object> gamePlayer (@PathVariable long idGamePlayer){
        Map<String, Object> list= new HashMap<String, Object>();
        GamePlayer gamePlayer = gamePlayerRepo.findOne(idGamePlayer);
        list.put("id",gamePlayer.getGame().getId());
        list.put("created",gamePlayer.getGame().getGameDate());
        list.put("gamePlayers",gamePlayer.getGame().gameDtoPlayers());
        list.put("ships", gamePlayer.gamePlayerShipsDto());
        list.put("salvoes", gamePlayer.getGame().gameDtoSalvo());
        return list;
    }

    private Player getPlayerLogged (Authentication authentication){
        return playerRepo.findByUserName(authentication.getName());

    }
    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication.getName()=="anonymousUser" || authentication instanceof AnonymousAuthenticationToken;
    }


}

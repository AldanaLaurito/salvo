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
    @Autowired
    private ShipRepository shipRepo;
    @Autowired
    private SalvoRepository salvoRepo;


    @RequestMapping(path = "/players",method = RequestMethod.POST)
    public ResponseEntity<Object> createPlayer(@RequestParam String email, @RequestParam String password){
        Map<String,String> msg=new HashMap<>();

        Player player = playerRepo.findByUserName(email);
        if (player != null) {
            msg.put("error","Name in use");
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        }

        playerRepo.save(new Player(email,password));
        msg.put("name",email);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }
    @RequestMapping(path = "/games",method = RequestMethod.POST)
    public ResponseEntity<Object> createGame(Authentication authentication){
        Map<String,Object> msg=new HashMap<>();

        if (isGuest(authentication)) {
            msg.put("error","No user has logged");
            return new ResponseEntity<>(msg, HttpStatus.UNAUTHORIZED);
        }
        Game game = new Game();
        repo.save(game);
        Player player = playerRepo.findByUserName(authentication.getName());
        GamePlayer gamePlayer = new GamePlayer(game.getGameDate(),game,player);
        gamePlayerRepo.save(gamePlayer);
        msg.put("gpid",gamePlayer.getId());
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }
    @RequestMapping(path = "/game/{idGame}/players",method = RequestMethod.POST)
    public ResponseEntity<Object> joinGame(Authentication authentication,@PathVariable long idGame){
        Map<String,Object> msg=new HashMap<>();
        Game game = repo.findById(idGame);
        if (isGuest(authentication)) {
            msg.put("error","No user has logged");
            return new ResponseEntity<>(msg, HttpStatus.UNAUTHORIZED);
        }
        else if(game==null){
            msg.put("error","No such game");
            return new ResponseEntity<>(msg, HttpStatus.FORBIDDEN);
        }
        else if(game.getGamePlayers().stream().count()==2){
            msg.put("error","Game is full");
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        }
        Player player = playerRepo.findByUserName(authentication.getName());
        GamePlayer gamePlayer = new GamePlayer(game.getGameDate(),game,player);
        gamePlayerRepo.save(gamePlayer);
        msg.put("gpid",gamePlayer.getId());
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }
    @RequestMapping(path = "/games/players/{gamePlayerId}/ships",method = RequestMethod.POST)
    public ResponseEntity<Object> placeShip(@RequestBody List <Ship> listShips,Authentication authentication,@PathVariable long gamePlayerId){
        Map<String,Object> msgNet=new HashMap<>();
        GamePlayer gamePlayer = gamePlayerRepo.findById(gamePlayerId);

        if ((isGuest(authentication))|| (gamePlayer==null)|| ((gamePlayer!=null) && (authentication.getName()!=gamePlayer.getPlayer().getUserName()))) {
            msgNet.put("error","Ships couldn't be saved");
            return new ResponseEntity<>(msgNet, HttpStatus.UNAUTHORIZED);
        }
        else if(gamePlayer.getShips().stream().count()==5){
            msgNet.put("error","The user has already placed their ships");
            return new ResponseEntity<>(msgNet, HttpStatus.CONFLICT);
        }
        shipRepo.save(listShips);
        gamePlayer.setShips(listShips.stream().collect(Collectors.toSet()));
        gamePlayerRepo.save(gamePlayer);
        msgNet.put("success","The ships were successfully placed");
        return new ResponseEntity<>(msgNet, HttpStatus.CREATED);
    }
    @RequestMapping(path = "/games/players/{gamePlayerId}/salvoes", method= RequestMethod.POST)
        public ResponseEntity<Map<String, Object>> setSalvoes(@PathVariable Long gamePlayerId, @RequestBody Salvo salvo, Authentication authentication) {
        Map<String,Object> msgNet=new HashMap<>();
        GamePlayer gamePlayer = gamePlayerRepo.findById(gamePlayerId);

        if ((isGuest(authentication))|| (gamePlayer==null)|| (gamePlayer!=null && authentication.getName()!=gamePlayer.getPlayer().getUserName())) {
            msgNet.put("error","The salvo couldn´t be saved");
            return new ResponseEntity<>(msgNet, HttpStatus.UNAUTHORIZED);
        }

        else if((gamePlayer.getSalvoes().stream().filter(salvo1 -> salvo1.getTurn()==salvo.getTurn()).count()!=0)||(gamePlayer.getSalvoes().stream().count()==5)){
            msgNet.put("error","The user has already placed a salvo in this turn");
            return new ResponseEntity<>(msgNet, HttpStatus.CONFLICT);
        }
           salvoRepo.save(salvo);
            gamePlayer.getSalvoes().add(salvo);
        gamePlayerRepo.save(gamePlayer);
        msgNet.put("success","The salvo was successfully placed");
        return new ResponseEntity<>(msgNet, HttpStatus.CREATED);
        }

    @RequestMapping("/games")
    public Map<String, Object> getAll(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        if (!(isGuest(authentication))){

            dto.put("player",getPlayerLogged(authentication).playerLogged());
        }
        else {
            dto.put("player", "Guest");
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


    /*@RequestMapping("/game_view/{idGamePlayer}")
    public Map<String, Object> gamePlayer (Authentication authentication,@PathVariable long idGamePlayer){
        Map<String, Object> list= new HashMap<String, Object>();
        GamePlayer gamePlayer = gamePlayerRepo.findOne(idGamePlayer);
        if(authentication.getName()==gamePlayer.getPlayer().getUserName())
        {
            list.put("id",gamePlayer.getGame().getId());
            list.put("created",gamePlayer.getGame().getGameDate());
            list.put("gamePlayers",gamePlayer.getGame().gameDtoPlayers());
            list.put("ships", gamePlayer.gamePlayerShipsDto());
            list.put("salvoes", gamePlayer.getGame().gameDtoSalvo());
        }
        else{
            list.put("Forbidden",HttpStatus.UNAUTHORIZED);
        }
        return list;
    }*/
    @RequestMapping("/game_view/{idGamePlayer}")
    public ResponseEntity<Object> gamePlayer (Authentication authentication,@PathVariable long idGamePlayer){

        Map<String, Object> list= new HashMap<String, Object>();
        GamePlayer gamePlayer = gamePlayerRepo.findOne(idGamePlayer);
        if(authentication.getName()==gamePlayer.getPlayer().getUserName())
        {
            list.put("id",gamePlayer.getGame().getId());
            list.put("created",gamePlayer.getGame().getGameDate());
            list.put("gamePlayers",gamePlayer.getGame().gameDtoPlayers());
            list.put("ships", gamePlayer.gamePlayerShipsDto());
            list.put("salvoes", gamePlayer.getGame().gameDtoSalvo());
            return new ResponseEntity<>(list,HttpStatus.ACCEPTED);
        }
        else{
            list.put("error","unauthorized");
            return new ResponseEntity<>(list, HttpStatus.UNAUTHORIZED);
        }
    }

    private Player getPlayerLogged (Authentication authentication){
        return playerRepo.findByUserName(authentication.getName());

    }
    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication.getName()=="anonymousUser" || authentication instanceof AnonymousAuthenticationToken;
    }


}

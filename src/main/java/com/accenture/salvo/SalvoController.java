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
    @Autowired
    private ScoreRepository scoreRepo;


    @RequestMapping(path = "/players",method = RequestMethod.POST)
    public ResponseEntity<Object> createPlayer(@RequestParam String email, @RequestParam String password){
        Map<String,String> msg=new LinkedHashMap<>();

        Player player = playerRepo.findByUserName(email);
        if (player != null) {
            msg.put(ApiMessage.KEY_ERROR,"Name in use");
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
            msg.put(ApiMessage.KEY_ERROR,ApiMessage.MSG_NO_USER);
            return new ResponseEntity<>( msg, HttpStatus.UNAUTHORIZED);
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
        Map<String,Object> msg=new LinkedHashMap<>();
        Game game = repo.findById(idGame);
        if (isGuest(authentication)) {
            msg.put(ApiMessage.KEY_ERROR,ApiMessage.MSG_NO_USER);
            return new ResponseEntity<>( msg, HttpStatus.UNAUTHORIZED);
        }
        else if(game==null){
            msg.put(ApiMessage.KEY_ERROR,"No such game");
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        }
        else if((long) game.getGamePlayers().size() ==2){
            msg.put(ApiMessage.KEY_ERROR,"Game is full");
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        }
        Player player = playerRepo.findByUserName(authentication.getName());
        GamePlayer gamePlayer = new GamePlayer(game.getGameDate(),game,player);
        gamePlayerRepo.save(gamePlayer);
        msg.put("gpid",gamePlayer.getId());
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }
    @RequestMapping(path = "/games/players/{gamePlayerId}/ships",method = RequestMethod.POST)
    public ResponseEntity<Object> placeShip(Authentication authentication,@RequestBody List <Ship> listShips,@PathVariable long gamePlayerId){
        Map<String,Object> msgNet=new LinkedHashMap<>();
        GamePlayer gamePlayer = gamePlayerRepo.findById(gamePlayerId);

        if ((isGuest(authentication))|| (gamePlayer==null)|| ((gamePlayer!=null) && (authentication.getName()!=gamePlayer.getPlayer().getUserName()))) {
            msgNet.put(ApiMessage.KEY_ERROR,"Ships couldn't be saved. Check if user is logged in");
            return new ResponseEntity<>(msgNet, HttpStatus.UNAUTHORIZED);
        }
        else if((long) gamePlayer.getShips().size() ==5){
            msgNet.put(ApiMessage.KEY_ERROR,"The user has already placed their ships");
            return new ResponseEntity<>(msgNet, HttpStatus.CONFLICT);
        }
        gamePlayer.setShip(listShips);
        gamePlayerRepo.save(gamePlayer);
        msgNet.put(ApiMessage.KEY_SUCCESS,"The ships were successfully placed");
        return new ResponseEntity<>(msgNet, HttpStatus.CREATED);
    }
    @RequestMapping(path = "/games/players/{gamePlayerId}/salvoes", method= RequestMethod.POST)
        public ResponseEntity<Map<String, Object>> setSalvoes(@PathVariable Long gamePlayerId, @RequestBody Salvo salvo, Authentication authentication) {
        Map<String,Object> msgNet=new LinkedHashMap<>();
        GamePlayer gamePlayer = gamePlayerRepo.findById(gamePlayerId);

        if ((isGuest(authentication))|| (gamePlayer==null)|| (gamePlayer!=null && authentication.getName()!=gamePlayer.getPlayer().getUserName())) {
            msgNet.put(ApiMessage.KEY_ERROR,"The salvo couldn´t be saved. Check if user is logged in");
            return new ResponseEntity<>(msgNet, HttpStatus.UNAUTHORIZED);
        }
        else if(gamePlayer.getGame().getScores().size()>0){
            msgNet.put(ApiMessage.KEY_ERROR,"The game is over");
            return new ResponseEntity<>(msgNet, HttpStatus.UNAUTHORIZED);
        }

        else if((gamePlayer.getSalvoes().stream().anyMatch(salvo1 -> salvo1.getTurn() == salvo.getTurn()))/*||((gamePlayer.getSalvoes().stream().count()==5)&&(gamePlayer.salvoes.size()==))*/){
            msgNet.put(ApiMessage.KEY_ERROR,"The user has already placed a salvo in this turn");
            return new ResponseEntity<>(msgNet, HttpStatus.CONFLICT);
        }

        if(gamePlayer.salvoes.isEmpty()){
            salvo.setTurn(1);
        }
        else{
            salvo.setTurn(gamePlayer.salvoes.size()+1);
        }

        gamePlayer.setSalvo(salvo);
        gamePlayerRepo.save(gamePlayer);
        msgNet.put(ApiMessage.KEY_SUCCESS,"The salvo was successfully placed");
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

    @RequestMapping("/game_view/{idGamePlayer}")
    public ResponseEntity<Object> gamePlayer (Authentication authentication,@PathVariable long idGamePlayer){

        Map<String, Object> list= new LinkedHashMap<>();
        GamePlayer gamePlayer = gamePlayerRepo.findOne(idGamePlayer);
        Game game = gamePlayer.getGame();
        if(authentication.getName()==gamePlayer.getPlayer().getUserName())
        {
            if(((gamePlayer.gameState()=="WON")||(gamePlayer.gameState()=="LOST")||(gamePlayer.gameState()=="TIE"))){
                    game.getScores().forEach(score -> scoreRepo.save(score));
            }
            list.put("id",game.getId());
            list.put("created",game.getGameDate());
            list.put("gameState",gamePlayer.gameState());
            list.put("gamePlayers",game.gameDtoPlayers());
            list.put("ships", gamePlayer.gamePlayerShipsDto());
            list.put("salvoes", game.gameDtoSalvo(gamePlayer.salvoes));
            list.put("hits",gamePlayer.dtoHits());
            return new ResponseEntity<>(list,HttpStatus.ACCEPTED);
        }
        else{
            list.put(ApiMessage.KEY_ERROR,ApiMessage.MSG_UNAUTHORIZED);
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

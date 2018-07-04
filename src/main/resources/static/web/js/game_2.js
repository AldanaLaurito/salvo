$(function () {
 loadData();
});

function getParameterByName(name) {
 var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
 return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}
function isShipOrientationVertical(locations){
      var firstPiece = locations[0];
      var secondPiece = locations[1];
      return (firstPiece.charAt(0) != secondPiece.charAt(0));
/*
      if(firstPiece.charAt(0) != secondPiece.charAt(0)){
           return true;
      } else{
           return false;
      }*/
 }

function loadData() {
 $.get('/api/game_view/' + getParameterByName('gp'))
   .done(function (data) {
     var playerInfo;
     var verticalOrientation;
     if (data.gamePlayers[0].id == getParameterByName('gp'))
       playerInfo = [data.gamePlayers[0].player, data.gamePlayers[1].player];
     else
       playerInfo = [data.gamePlayers[1].player, data.gamePlayers[0].player];

     $('#playerInfo').text(playerInfo[0].email + '(you) vs ' + playerInfo[1].email);

     data.ships.forEach(function (shipPiece) {
     verticalOrientation = isShipOrientationVertical(shipPiece.locations)
       shipPiece.locations.forEach(function (shipLocation) {
         if(isHit(shipLocation,data.salvoes,playerInfo[0].id)!=-1) {
           $('#B_' + shipLocation).addClass('ship-piece-hited');
           $('#B_' + shipLocation).html(isHit(shipLocation,data.salvoes,playerInfo[0].id));


           }
         else{
           /*if (shipLocation=="F3" || shipLocation=="H3"){
                    $('#B_' + shipLocation).addClass('ship-piece-vertical');
                    }
                    else{
                    $('#B_' + shipLocation).addClass('ship-piece');
                     }*/
                     if(verticalOrientation){
                            $('#B_' + shipLocation).addClass('ship-piece-vertical');
                     }else{
                       $('#B_' + shipLocation).addClass('ship-piece');
                     }
           }
       });
     });
     data.salvoes.forEach(function (salvo) {
       if (playerInfo[0].id === salvo.player) {
         salvo.locations.forEach(function (location) {
           $('#S_' + location).addClass('salvo');
           $('#S_' + location).html(salvo.turn);
         });
       } else {
         salvo.locations.forEach(function (location) {
           $('#_' + location).addClass('salvo');
         });
       }
     });
   })
   .fail(function (jqXHR, textStatus) {
     alert('Failed: ' + textStatus);
   });
}

function isHit(shipLocation,salvoes,playerId) {
 var hit = -1;
 salvoes.forEach(function (salvo) {
   if(salvo.player != playerId){
     salvo.locations.forEach(function (location) {
       if(shipLocation === location) {
         hit = salvo.turn;
       }
     });

    }
 });
 return hit;

 var v = document.getElementsByTagName("video")[0];
 v.play();



}
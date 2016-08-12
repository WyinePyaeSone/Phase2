/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import caunogame.Deck;
import java.util.HashMap;
import java.util.Map;
import caunogame.UnoGame;
import caunogame.Player;
import caunogame.UNOCard;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/game")
public class unoWebGame {
 
    public static Map<String, UnoGame> gm = new HashMap<>();
    public static String waitingGId = "";
    public static String  playerId;
    public static Player  player;
    public static Deck deck;
    
    @POST
    @Path("/addgame")
    @Produces("text/plain")
    public Response addGame(@FormParam("title") String name){        
        String strgname= name;
        String strgStatus="waiting";
        String strgid = UUID.randomUUID().toString().substring(0,8);
        
        System.out.println(strgname + strgid);
        waitingGId = strgid;
        UnoGame g = new UnoGame(strgid, strgname, strgStatus); 
        System.out.println(waitingGId);
        gm.put(strgid, g);       
        try {
            URI location = new java.net.URI("http://localhost:8383/PlayUno/WaitPartner.html");            
            return Response.seeOther(location).build();
        } catch (URISyntaxException ex) {
            return Response.status(404).entity("Redirect Fail").build();
        }  
    }

    @GET
    @Path("/waitPartner")
    @Produces("text/plain")
    public Response newGame(){
        JsonObject job ;
        //JsonArrayBuilder jsa = Json.createArrayBuilder();

        UnoGame g = gm.get(waitingGId);
        
        job = Json.createObjectBuilder()
            .add("gid",g.getGameId())
            .add("name", g.getgName())
            .add("status", g.getgStatus())
            .build();

        Response resp = Response.ok(job.toString())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        return resp;
    }
    
    @GET
    @Path("/gamelist")
    @Produces("text/plain")
    public Response listGame(){
        JsonObject job ;
        JsonArrayBuilder jab = Json.createArrayBuilder();

        Iterator entries = gm.entrySet().iterator();
        while (entries.hasNext()) {
          Map.Entry gentry = (Map.Entry) entries.next();
          //Object key = thisEntry.getKey();
          //Object value = thisEntry.getValue();
          
           job = Json.createObjectBuilder()
            .add("gameId",gentry.getKey().toString())
            .add("name", ((UnoGame)gentry.getValue()).getgName())
            .add("gstatus", ((UnoGame)gentry.getValue()).getgStatus())
            .build();
           
           jab.add(job);
        }

        Response resp = Response.ok(jab.build().toString())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        return resp;        
        
    }
    
    @POST
    @Path("/ownerStart")
    @Produces("text/plain")
    public Response startTablePlay(@FormParam("gid") String gid){
        
        waitingGId = gid;
        
        
        UnoGame g = gm.get(waitingGId); 
        g.startGame();
        
        try {
            URI location = new java.net.URI("http://localhost:8383/PlayUno/StartOwner.html");            
            return Response.seeOther(location).build();
        } catch (URISyntaxException ex) {
            return Response.status(404).entity("Redirect Fail").build();
        }
    }
    
    
    @POST
    @Path("/addplayer")
    @Produces("text/plain")
    public Response addPlayer(@FormParam("gid") String gid, @FormParam("pname") String pname){         
        
        waitingGId = gid;
        String strpname = pname;
        String strpid = UUID.randomUUID().toString().substring(0,8);
        
        Player p = new Player(strpname, strpid);
        playerId = strpid;
        
        gm.get(waitingGId).addPlayer(p);    
        
        try {
            URI location = new java.net.URI("http://localhost:8383/PlayUno/JoinPlayer.html"); 
            return Response.seeOther(location).build();
        } catch (URISyntaxException ex) {
            return Response.status(404).entity("Redirect Fail").build();
        }  
    }
   
    @GET
    @Path("/playerlist")
    @Produces("text/plain")
    public Response listPlayer(){
        JsonObject job ;
        JsonArrayBuilder jab = Json.createArrayBuilder();
        
        UnoGame g = gm.get(waitingGId); 
                
        List<Player> lstPlayer = g.getLstPlayer();
        
        for (int i = 0; i < lstPlayer.size(); i++) {
            player = lstPlayer.get(i);            
            job = Json.createObjectBuilder()
            .add("GameId", g.getGameId())
            .add("pid", player.getPId())
            .add("name", player.getpName())
            .add("GameTitle",g.getgStatus())
            .build();           
           jab.add(job);            
        }

        Response resp = Response.ok(jab.build().toString())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        return resp;
    }
    
    
    
//    @POST
//    @Path("/startGame")
//    @Produces("text/plain")
//    public void startPlay(@FormParam("gid") String gid, @FormParam("pid") String pid){
//        
//        waitingGId = gid;
//        unoWebGame.playerId = pid;
//        
//        UnoGame g = gm.get(waitingGId); 
//        g.startGame();
//        
//        List<Player> lstPlayer = g.getLstPlayer();
//                
//        for (int i = 0; i < lstPlayer.size(); i++) {            
//            player = lstPlayer.get(i);
//            if(player.getPId().equals(playerId)){
//                break;
//            }
//            else{
//                player =null;
//            }
//            
//        }       
//        
//    }   
    
    @GET
    @Path("/discardPile")
    @Produces("text/plain")
    public Response showDiscardPile(){
        JsonObject job;
        
        UnoGame g = gm.get(waitingGId); 
        UNOCard card = g.createDiscardPile();
        
        job = Json.createObjectBuilder()
            .add("gid", g.getGameId())
            .add("gname",g.getgName())
            .add("color",card.getColor())
            .add("type", card.getType())
            .add("value",card.getScore())
            .add("image",card.getImgurl())
            .build();

        Response resp = Response.ok(job.toString())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        return resp;
    }
  
    
    @POST
    @Path("/playerStart")
    @Produces("text/plain")
    public Response startPlay(@FormParam("gid") String gid, @FormParam("pid") String pid){
        
        waitingGId = gid;
        playerId = pid;

        try {
            URI location = new java.net.URI("http://localhost:8383/PlayUno/UnoStartDeal.html");            
            return Response.seeOther(location).build();
        } catch (URISyntaxException ex) {
            return Response.status(404).entity("Redirect Fail").build();
        }
    }    

    
    @GET
    @Path("/wait")
    @Produces("text/plain")
    public Response showGamePlayer(){
        JsonObject jso ;
    
        UnoGame g = gm.get(waitingGId);
        
        jso = Json.createObjectBuilder()
            .add("gid",g.getGameId())
            .add("gname",g.getgName())
            .add("pid",playerId)
            .build();

        Response resp = Response.ok(jso.toString())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        return resp;
    }
    
    @GET
    @Path("/deal")
    @Produces("text/plain")
    public Response showPlayerCards(){
        JsonObject job ;
        JsonArrayBuilder jab = Json.createArrayBuilder();
        Player gplayer=null; 
        UnoGame g = gm.get(waitingGId);
        
        List<Player> lstPlayer = g.getLstPlayer();
        System.out.println(lstPlayer);
        for (int i = 0; i < lstPlayer.size(); i++) {
            System.out.println("Player ID id" +playerId);
            gplayer = lstPlayer.get(i);
            if(gplayer.getPId().equals(playerId)){
                
                break;
            }
            else{
                gplayer=null;
            }
            
        }        
        List<UNOCard> cardinhand = gplayer.getLstholdInHand();
        
        for (int i = 0; i < cardinhand.size(); i++) {
            UNOCard card = cardinhand.get(i);
            
            job = Json.createObjectBuilder()
            .add("color",card.getColor())
            .add("type", card.getType())
            .add("value",card.getScore())
            .add("image",card.getImgurl())
            .build();

           jab.add(job);
            
        }

        Response resp = Response.ok(jab.build().toString())
                .header("Access-Control-Allow-Origin", "*")
                .build();        
        return resp;
    }
}

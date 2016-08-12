/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import caunogame.UnoGame;
import caunogame.Player;
/**
 *
 * @author Tun Lwin Aung
 */
public class Test{
public static void main(String[] args) {
        UnoGame ug = new UnoGame();
        Player p=new Player("Bee", "123");
        Player p1=new Player("Chuu Chuu","345");
        Player p2=new Player("hua hua","213");
        ug.addPlayer(p);
        ug.addPlayer(p1);
        ug.addPlayer(p2);       
        ug.startGame();
        System.out.println(ug);
    }
}

package com.budies.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.budies.entities.Player;
import com.budies.main.Game;

public class UI {
	public void render (Graphics g ) { //método que renderiza a vida e muda de cor 
		g.setColor(Color.red);
		g.fillRect(8,4,70, 7);
		g.setColor(Color.green);
		g.fillRect(8,4,(int)((Game.player.life/Game.player.maxlife) *70), 7);
		g.setColor(Color.white);
		
		//g.setFont(new Font ("arial",Font.BOLD,7)); estes comandos foram implementados na classe game
		//g.drawString ((int) Game.player.life+"/"+(int)Game.player.maxlife,8, 10);
		
	}

}

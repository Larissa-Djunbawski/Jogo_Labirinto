package com.budies.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.budies.main.Game;
import com.budies.world.Camera;

public class BulletShoot extends Entity { // classe que representa os tiros
	 private double dx; //direção x
	 private double dy; //direção y
	 private double spd = 4;
	 private int life = 30, curLife = 0; //alcance das munições
	 
	 
	 public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
			super(x, y, width, height, sprite);
			this.dx = dx; //para arremessar o tiro
			this.dy = dy;
		
	  }
 
	public void tick () {
	  x+= dx*spd;
	  y+= dy*spd;
	  curLife++;
	  if (curLife == life) {
		  Game.bullets.remove(this); //remove a bala
		  return;
	  }
      }
     
     public void render (Graphics g) {
    	 g.setColor(new Color(191,134,0));
    	 g.fillOval(getX() - Camera.x, getY() - Camera.y , width, height);
    	 
     }
}

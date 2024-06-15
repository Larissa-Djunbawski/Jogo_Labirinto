package com.budies.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.budies.graficos.Spritesheet;
import com.budies.main.Game;
import com.budies.world.Camera;
import com.budies.world.World;

public class Player extends Entity {
	
    public boolean right,up,left,down;
    public int right_dir = 0, left_dir = 1;
    public int dir = right_dir ;
    public double speed = 1.4;
    
    private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
    private boolean moved = false;
    private BufferedImage[] rightPlayer ;
    private BufferedImage[] leftPlayer;
    
    private BufferedImage playerDamage; 
    
    private boolean arma = false;
    
    public int ammo = 0 ;//munição
    public boolean isDamage = false; //Dano
    private int damageFrames = 0;//tomar dano
    public  boolean shoot = false, mouseShoot = false;
    public  double life = 100, maxlife = 100;
    public int mx,my;
    
    public boolean jump = false;
    
    public int z = 0;
    
    public int jumpFrames = 0, jumbCur = 0;
    
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer =  new BufferedImage[4];
		playerDamage = Game.spritesheet.getSprite(0, 16, 16, 16); //imagem do player 
		for (int i = 0; i < 4; i++) { //imagem direita
		     rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
		       }
		for (int i= 0; i < 4; i++) { //imagem esquerda
		     leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
		       }
		}
	
	public void tick() { //movimentação e colisão 
		
		if (jump) {
			if(isJumping == false) {
				jump = false;
			}
		}
		
		
		moved = false;
		if (right && World.isFree ((int)(x+speed),this.getY())) {
			moved = true;
			dir = right_dir;
			x+=speed;

			}
		else if (left&& World.isFree ((int)(x-speed),this.getY())){
			moved = true;
			dir = left_dir;
	        x-=speed; 
	        }
		if (up&& World.isFree (this.getX(), (int)(y-speed))){
			moved = true;
			y-=speed;
			}
		else if (down&& World.isFree (this.getX(), (int)(y+speed))){
			moved = true;
			y+=speed ;
		   }	
		if (moved) {//variavel moved para vereficar se consegue mover o jogador
			frames++; // a cada tempo muda a animação
			 if (frames == maxFrames) {
				 frames = 0;
				 index ++;
				 if (index > maxIndex) //quando a animação atingir ao maximo
				   index = 0; //ela reinicia do zero
			 }
		}
		
		checkCollisionLifePack(); //vida
		checkCollisionAmmo(); //chamar métodos para funcionar no jogo bala-munição
		checkCollisionGun(); //colide colide com arma
		checkCollisionBulletShoot();
		
		
		if (isDamage) { //se for atacado
			damageFrames ++;
			if (damageFrames == 8) {
				damageFrames = 0;
				isDamage = false;
			}
		}
		if  (shoot ) { //se estiver com a arma atira
			//Criar bala e atirar
			if (arma && ammo > 0) {
			shoot = false;
			ammo --;
			
			int dx = 0;
			int px = 0;
			int py = 6;
			if (dir == right_dir) { //se direita for igual ao lado direito 
				 px = 18;
				 dx = 1;	
			} else {
				 px = -8;
				 dx = -1;				
			}
			BulletShoot bullet= new BulletShoot(this.getX()+ px,this.getY()+py,3,2,null,dx,0);
			Game.bullets.add(bullet);
			}
		}if (mouseShoot) { //ATIRAR COM O MOUSE
			mouseShoot = false;						
			if (arma && ammo > 0) {
			ammo --;		
			int px = 0, py = 8;
			double angle = 0;
			if (dir == right_dir) { //se direita for igual ao lado direito 
				 px = 18;
				 angle = Math.atan2(my - (this.getY()+py -Camera.y),mx - (this.getX()+px -Camera.x));				 					
			} else {
				  px = -8; 
				  angle = Math.atan2(my - (this.getY()+py -Camera.y),mx - (this.getX()+px -Camera.x));												
			}		
			double dx = Math.cos(angle);
			double dy = Math.sin(angle);
			BulletShoot bullet= new BulletShoot(this.getX()+ px,this.getY()+py,3,2,null,dx,dy);
			Game.bullets.add(bullet);
			}			
		}
		
		
		
		if (life <= 0) { //quando a vida chega em zero reinicia o jogo - game over
			life = 0;
			Game.gameState = "GAME_OVER";			
		}
		   updateCamera(); 
		
	    }  public void updateCamera() {
	    	Camera.x= Camera.clamp(this.getX()-(Game.WIDTH/2),0, World.WIDTH * 16- Game.WIDTH); //tamanho do mundo menos a minha tela 
			Camera.y= Camera.clamp(this.getY()-(Game.HEIGHT/2),0, World.HEIGHT* 16 - Game.HEIGHT);//A TELA DIVIDIDO POR DOIS 
	    }
	public void checkCollisionGun() {
		for (int i = 0; i < Game.entities.size(); i++){
			    Entity atual = Game.entities.get(i);
			if (atual instanceof Weapon) {
					if (Entity.isColidding(this, atual)) {
						    arma = true;
							Game.entities.remove(atual);
					}
				}
			}
		}
	
	public void  checkCollisionAmmo() {
		for (int i = 0; i < Game.entities.size(); i++){
		    Entity atual = Game.entities.get(i);
		if (atual instanceof Bullet) {
				if (Entity.isColidding(this, atual)) {
					ammo+=20; // valor de cada munição 
						Game.entities.remove(atual);
						

				}
			}
		}
	}
	@SuppressWarnings("unlikely-arg-type")
	public void  checkCollisionBulletShoot() {
		for (int i = 0; i < Game.entities.size(); i++){
		    Entity atual = Game.entities.get(i);
		if (atual instanceof Bullet) {
				if (Entity.isColidding(this, atual)) {
					 // valor de cada munição 
					Game.bullets.remove(this); //remove a bala

				}
			}
		}
	}					
						
	public void checkCollisionLifePack() {
		for (int i = 0; i < Game.entities.size(); i++){
			    Entity atual = Game.entities.get(i);
			if (atual instanceof Lifepack) {
					if (Entity.isColidding(this, atual) && life < 100) { //só pega vida se tiver com dano 
						life+=8;
					    if (life >= 100) 
							life = 100;
							Game.entities.remove(atual);
							return;

					}
				}
			}
		}
		
	
		
	
	public void render (Graphics g) {
		if (!isDamage) { //SEM ATAQUE
		if (dir == right_dir ) {
		  g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		  if (arma) { //desenhar arma para direita 9
			g.drawImage(Entity.GUN_RIGHT, this.getX() +8  - Camera.x, this.getY() - Camera.y, null);  
			  
		  }
		  }else if (dir == left_dir ) {	    
		   g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		   if (arma ) { //desenhar arma para esquerda 
			   g.drawImage(Entity.GUN_LEFT, this.getX() - 8 - Camera.x, this.getY() - Camera.y, null);  
				  
			   
		   }
	      }
	     } else { //imagem dela SENDO ATACADA
	    	 g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
	    	  
			  if (arma) { //desenhar arma para direita com ela sendo atacada
				g.drawImage(Entity.GUN_RIGHT, this.getX() +8  - Camera.x, this.getY() - Camera.y, null);  
				  
			  }else if (arma ) { //desenhar arma para esquerda com ela sendo atacada
				   g.drawImage(Entity.GUN_LEFT, this.getX() - 8 - Camera.x, this.getY() - Camera.y, null);
			  }
	     }
	  }
	}



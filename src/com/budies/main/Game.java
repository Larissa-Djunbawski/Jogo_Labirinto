package com.budies.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.budies.entities.BulletShoot;
import com.budies.entities.Enemy;
import com.budies.entities.Entity;
import com.budies.entities.Player;
import com.budies.graficos.Spritesheet;
import com.budies.graficos.UI;
import com.budies.world.Camera;
import com.budies.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener{

	
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 240; //DECLARAÇÃO DE CONSTANTE E VALOR INTEIRO
	public static final int HEIGHT = 160; //DECLARAÇÃO DE CONSTANTE DE VALOR INTEIRO
	public static final int SCALE = 3; //DECLARAÇÃO DE CONSTANTE DIMENSÃO MULTIPLICADA
	
	private int CUR_LEVEL = 1,MAX_LEVEL = 4;//numero de leveis manual
	
	private BufferedImage image;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;
	public static Spritesheet spritesheet;
	
	public static World world;
	
    public static Player player;
    
    public static Random rand;
    
    public UI ui;
    
	public static String gameState = "MENU";
	public boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	
	public Menu  menu;
	public Game() {
		Sound.musicBackground.loop();
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		//Inicializando objeto
		ui= new UI();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<BulletShoot>();		
		spritesheet = new Spritesheet ("/spritesheet.png");
		player = new Player (0,0,16,16,spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/level1.png");
		menu = new Menu();
	}
	 public void initFrame() {
		 frame = new JFrame("ILary Game"); //criação e titulo da janela
			frame.add(this);
			frame.setResizable(false);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//CLICOU FECHA
			frame.setVisible(true);
	 }
	 
	 public synchronized void start() {
		 thread = new Thread(this);
		 isRunning = true;
		 thread.start();
		 
	}
	 public  synchronized void stop ()  {
		 isRunning = false;
		try {
			thread.join(); 
					} catch (InterruptedException e ) {
						e.printStackTrace();
					}
	 }
	public static void main(String args[]) {
		 Game game= new Game();
		 game.start();
		
	}
	public void tick (){ //lógica do jogo		
		if (gameState == "NORMAL") {
	     restartGame = false;	
	     for ( int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
    }
	for (int i = 0; i < bullets.size(); i++) {
		bullets.get(i).tick();
	}
	if (enemies.size()== 0)	{
		//Avançar para o proximo nivel
		CUR_LEVEL++;
		if (CUR_LEVEL > MAX_LEVEL) {
			CUR_LEVEL = 1;
			
		}
		String newWorld = "level" +CUR_LEVEL+ ".png";
		World.restartGame(newWorld);
		}
	}    else if (gameState=="GAME_OVER") {
		framesGameOver++;
		if (framesGameOver == 30) { //a mensagem para reiniciar o jogo pisca
			framesGameOver = 0;
			if (showMessageGameOver) 
				showMessageGameOver = false;
				else
					showMessageGameOver = true;
			
	   }if (restartGame) {
			restartGame = false;
			gameState = "NORMAL";
			CUR_LEVEL = 1;
			String newWorld = "level" +CUR_LEVEL+ ".png";
			World.restartGame(newWorld);		
		}
	} else if (gameState == "MENU") {
		menu.tick();
	  }
}
	
	
	
	public void render () { //renderização do jogo 
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3); //sequencia de buffers para otimizar a renderização
			return; //retornar
		}
		Graphics g = image.getGraphics(); //sempre necessario qUando necessitar renderizar
		g.setColor(new Color(0,0,0)); //cor red green e blue  
		g.fillRect(0,0,WIDTH,HEIGHT); //largura e altura de um retangulo 
		
		world.render(g);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}ui.render(g);
		g.dispose(); //metodo de otimização
		g = bs.getDrawGraphics(); //primeiro manipula desenho dentro dessa imagem e depois desenha essa imagem na tela
		g.drawImage(image,0,0, WIDTH*SCALE,HEIGHT*SCALE, null);
		g.setFont(new Font ("arial", Font.BOLD,20));
		g.setColor(Color.white);
		g.drawString("Munição: " + player.ammo, 590, 20);//desenhar a quantia de munição
		g.drawString((int) Game.player.life+"/"+(int)Game.player.maxlife,25, 30); //desenhar a quantia de vida
		if (gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
			g.setFont(new Font ("arial", Font.BOLD,40));
			g.setColor(Color.white);
			g.drawString("GAME OVER ", 250, 250);//desenhar posição do game over
			g.setFont(new Font ("arial", Font.BOLD,30));
			if (showMessageGameOver) 
			g.drawString("PRESSIONE ENTER PARA REINICIAR ", 100, 100);
		} else if (gameState == "MENU") {
			menu.render(g);
		}
		bs.show();
		
	}
	public void run() {
		long lastTime = System.nanoTime(); //ultimo momento em nano time
		double amountOfTicks = 60.0; //define constantes, 60 ticks por segundo
		double ns = 1000000000/amountOfTicks;  //divide nanosegundo, momento certo para o update do jogo
		double  delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while (isRunning ) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;//diminuir tempo atual menos a ultima vez 
			lastTime = now ; 
				if (delta >= 1) { //se for igual a 1 é porque passou um segundo 
				tick(); //e esta na hora de redenrizar o nosso jogo
				render();
				frames ++;
				delta --; //para diminuir algarismos, incrementa o delta e volta no looping
			}
				
			if (System.currentTimeMillis()- timer >= 1000) {
				System.out.println("FPS: " + frames );
				frames = 0;
				timer += 1000;
			}
		
		}
		
		stop();
	}
	@Override
	public void keyPressed(KeyEvent e) { //Se tal tecla estiver pressionada faça tal ação :
		if ( e.getKeyCode() == KeyEvent.VK_RIGHT || //direita
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right=true;
		//execute tal ação	
		}   else if ( e.getKeyCode() == KeyEvent.VK_LEFT ||//esquerda
					e.getKeyCode() == KeyEvent.VK_A){
			 player.left=true;
		}if (e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W) {
			player.up=true;
			if(gameState == "MENU") {
				menu.up =true;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S ) {
			player.down=true;
			
			if(gameState == "MENU") {
				menu.down =true;
			}
	    } if (e.getKeyCode() == KeyEvent.VK_SPACE ||
	    		e.getKeyCode() == KeyEvent.VK_X ) {
	    	player.shoot = true; //só fica true quando aperta as teclas correspondentes ai aparece o tiro
	    	
	    } if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	    	restartGame = true;
	    	if(gameState == "MENU") {
	    		menu.enter = true;
	    	}
	    } if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { //pausar o jogo
	    	gameState = "MENU";
	    	menu.pause = true;
	    } 	
		     
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if ( e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right=false;
		//execute tal ação	
		}   else if ( e.getKeyCode() == KeyEvent.VK_LEFT ||
					e.getKeyCode() == KeyEvent.VK_A){
			 
			player.left=false;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W) {
			player.up=false;
			
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down=false;
	    }
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseShoot = true;
		player.mx = (e.getX()/3);
		player.my=(e.getY()/3);
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}


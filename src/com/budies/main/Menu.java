package com.budies.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Menu {
	public String[] options = {"novo jogo", "carregar jogo","sair"};
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	
	public boolean up,down, enter;
    public boolean pause = false;
    public void tick () {
	  if (up) {
		  up = false;
		  currentOption--;
		  if (currentOption < 0) 
			  currentOption = maxOption;
		  
	  } if (down) {
		  down = false;
		  currentOption++;
		  if (currentOption > maxOption) 
			  currentOption = 0;
	  } if(enter) {
		  enter = false;
		  if (options[currentOption] == ("novo jogo") || options[currentOption] == "continuar") {
			  Game.gameState = "NORMAL";
			  pause = false;			 
		  } else if (options[currentOption] == "sair") {
			  System.exit(1);
		  }
	  }
    }
    public void render(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;	//chamar metodo
      g2.setColor(new Color (0,0,0,100)); //fundo transparente no menu
      g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
	  //g.setColor(Color.black); tela de fundo colorida
	  //g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE); //desenhar tela colorida
	  g.setColor(Color.red);
	  g.setFont(new Font ("arial", Font.BOLD,40));
	  g.drawString("Labirinto zumbi", 290,  100);
	  //opções de menu
	  g.setColor(Color.white);
	  g.setFont(new Font ("arial", Font.BOLD,40));
	  if (pause == false )
	      g.drawString("Novo Jogo", 290,  200);
	  else 
		  g.drawString("Resumir", 290,  200);  
	  g.drawString("Carregar Jogo", 290,  300);
	  g.drawString("Sair", 290,  400);
	  
	  if(options[currentOption] == "novo jogo") {
		  g.drawString(">", 290 - 90,  200 ); //mesma posição - 90 pixels
	  } else if (options[currentOption] == "carregar jogo") {
		  g.drawString(">", 290 - 90,  300 );
	  } else if (options[currentOption] == "sair") {
		  g.drawString(">", 290 - 90,  400 ); //mesma posição - 90
	  }
	 
  }
}

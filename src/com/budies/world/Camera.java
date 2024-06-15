package com.budies.world;

public class Camera {
 public static int x;
 public static int y;
 
 public static int clamp(int Atual, int xMin, int xMax) {
	 if(Atual < xMin) {
		 Atual = xMin;
	 }if(Atual > xMax) {
		 Atual = xMax;
	 } return Atual;
 }
}

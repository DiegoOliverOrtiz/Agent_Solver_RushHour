package rushhour;

import java.util.ArrayList;
import java.awt.Color;

public class Coche {
	private char nombre;
	private ArrayList<int[]> coordenadas = new ArrayList<>();
	private boolean orientacion; //false es horizontal y true es vertical
	private Color col;
	
	public Coche() {
	}
	
	public Coche(char nombre, Color col) {
		this.nombre=nombre;
		if(nombre=='A')
			this.col=Color.RED;
		else
			this.col=col;
	}

	public char getNombre() {
		return nombre;
	}
	
	public Color getColor() {
		return col;
	}
	
	public ArrayList<int[]> getCoordenadas(){
		return coordenadas;
	}
	
	public String getStringCoordenadas() {
		String coordsToString="";
		if(coordenadas.size()>0) {
			for(int i=0;i<coordenadas.size();i++) {
				coordsToString=coordsToString+"("+coordenadas.get(i)[0]+","+coordenadas.get(i)[1]+")";
			}
		}
		else
			coordsToString="Sin coordenadas cargadas";
		return coordsToString;
	}
	
	public boolean getOrientacion() {
		return orientacion;
	}
	
	public void addCoordenada(int[] nuevasCoords) {
		coordenadas.add(nuevasCoords);
	}
	
	public boolean definirOrientacion() { //Devuelve false si la posición es ilegal (está duplicado o lo que sea) y true si es legal
		boolean legal=true;
		int[] filas = new int[coordenadas.size()];
		int[] columnas = new int[coordenadas.size()];
		boolean diferentesFilas=false;
		boolean diferentesColumnas=false;
		
		for(int i=0;i<coordenadas.size();i++) {
			filas[i]=coordenadas.get(i)[0];
			columnas[i]=coordenadas.get(i)[1];
		}
		
		for (int i = 1; i < filas.length; i++) {
	        if (filas[i] != filas[0]) {
	            diferentesFilas=true;
	        }
	        if (columnas[i] != columnas[0]) {
	            diferentesColumnas=true;
	        }
	    }
		
		if(diferentesFilas==diferentesColumnas) {
			legal=false;
		}
		else if(diferentesFilas==true && diferentesColumnas==false){
			orientacion=true;
		}
		else if(diferentesFilas==false && diferentesColumnas==true){
			orientacion=false;
		}
		return legal;
	}
}

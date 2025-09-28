package rushhour;

import java.util.ArrayList;

public class Coche {
	private char nombre;
	private ArrayList<int[]> coordenadas = new ArrayList<>();
	private boolean orientacion; //false es horizontal y true es vertical
	
	public Coche(char nombre) {
		this.nombre=nombre;
	}

	public char getNombre() {
		return nombre;
	}
	
	public ArrayList<int[]> getCoordenadas(){
		return coordenadas;
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

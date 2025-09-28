package rushhour;

import java.util.ArrayList;

public class Coche {
	private char nombre;
	private ArrayList<int[]> coordenadas;
	
	public Coche(char nombre) {
		this.nombre=nombre;
	}

	public char getNombre() {
		return nombre;
	}
	
	public ArrayList<int[]> getCoordenadas(){
		return coordenadas;
	}
	
	public void addCoordenada(int[] nuevasCoords) {
		coordenadas.add(nuevasCoords);
	}
}

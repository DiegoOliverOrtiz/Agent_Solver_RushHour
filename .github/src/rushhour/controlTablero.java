package rushhour;

import java.util.*;

public class controlTablero {
	private char tablero[][]=new char[6][6];
	private ArrayList<Coche> coches=new ArrayList<>();
	
	public controlTablero() {
	}
	
	public int crearTablero(String semilla) {
		int resultado=-1;
		if(semilla.length()<36) //Comprobar requisito 1 - Tamaño de 36 caracteres
			resultado=1;
		if(resultado!=-1) { //Comprobar requisito 2 -vSolo letras las letras 'A-Z' u 'o'
			int cuentaAux=0;
			for(int i=0;i<semilla.length();i++) {
				char letra=semilla.charAt(i);
				tablero[i][cuentaAux]=letra;
				if(!(Character.isLetter(letra) && Character.isUpperCase(letra)) || letra!='o') {
					resultado=2;
				}
				if ((i+1)%5==0)
					cuentaAux++;
			}
		}
		generarCoches();
		if(resultado!=-1) { //Comrpobar requisito 3 - ¿Está el coche rojo?
			int resultadoAuxiliar=resultado;
			resultado = 3;
			for(int i=0;i<coches.size();i++) {
				if (coches.get(i).getNombre()=='A')
					resultado=resultadoAuxiliar;
					
			}
		}
		
		if (resultado!=-1) { //Comprobar requisito 4 - ¿Está el coche rojo en la tercera fila?
			int resultadoAuxiliar=resultado;
			resultado = 4;
			for(int i=0;i<coches.size();i++) {
				if (coches.get(i).getNombre()=='A') {
					ArrayList<int[]> coordenadas=coches.get(i).getCoordenadas();
					int[] posicion = coordenadas.get(0); //se puede con cualquier coordenada
					int fila=posicion[0]; //Si de verdad está en la tercera fila con comprobarlo en un valor es suficiente (el siguiente punto mira que sea horizontal)
					if(fila==3)
						resultado=resultadoAuxiliar;
				}
			}
		}
		
		
		return resultado;
	}
	
	public void generarCoches() {
		for(int i=0;i<tablero.length;i++) {
			for(int j=0;j<tablero[0].length;i++) {
				if(tablero[i][j]!='o') {
					int[]coordenadas = {i, j};
					boolean existeCoche=false;
					for(int k=0;k<coches.size();k++) {
						if(coches.get(k) != null){
							Coche c=coches.get(k);
							if(c.getNombre()==tablero[i][j]) {
								existeCoche=true;
								c.addCoordenada(coordenadas);
							}
						}
					}
					if(existeCoche==false) {
						Coche c = new Coche(tablero[i][j]);
						c.addCoordenada(coordenadas);
					}
				}
			}
		}
	}
}

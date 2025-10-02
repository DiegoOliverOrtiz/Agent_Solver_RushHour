package rushhour;

import java.awt.Color;
import java.util.*;

public class controlTablero {
	private char tablero[][]=new char[6][6];
	private ArrayList<Coche> coches=new ArrayList<>();
	
	public controlTablero() {
	}
	
	public int crearTablero(String semilla) {
		int resultado=0;
		
		if(semilla.length()!=36) //Comprobar requisito 1 - Tamaño de 36 caracteres
			resultado=1;
		
		if(resultado==0) { //Comprobar requisito 2 - Solo letras las letras 'A-Z' u 'o'
			int cuenta=0;
			for(int i=0;i<tablero.length;i++) {
				for(int j=0;j<tablero[0].length;j++) {
					char letra=semilla.charAt(cuenta);
					tablero[i][j]=letra;
					System.out.print(tablero[i][j]);
					if(!Character.isLetter(letra) && (!Character.isUpperCase(letra) || letra!='o')) {
						resultado=2;
					}
					cuenta++;
				}
				System.out.println();
			}
		}
		
		generarCoches();
		ControlVisual control = new ControlVisual(tablero);
		control.mostrarMatriz(coches);
		
		if(resultado==0) { //Comrpobar requisito 3 - ¿Está el coche rojo?
			int resultadoAuxiliar=resultado;
			resultado = 3;
			for(int i=0;i<coches.size();i++) {
				if (coches.get(i).getNombre()=='A') {
					resultado=resultadoAuxiliar;
					i=coches.size()+1;	
				}
			}
		}
		
		if (resultado==0) { //Comprobar requisito 4 - ¿Está el coche rojo en la tercera fila?
			int resultadoAuxiliar=resultado;
			resultado = 4;
			for(int i=0;i<coches.size();i++) {
				if (coches.get(i).getNombre()=='A') {
					ArrayList<int[]> coordenadas=coches.get(i).getCoordenadas();
					int[] posicion = coordenadas.get(0); //se puede con cualquier coordenada
					int fila=posicion[0]; //Si de verdad está en la tercera fila con comprobarlo en un valor es suficiente (el siguiente punto mira que sea horizontal)
					if(fila==2) {
						resultado=resultadoAuxiliar;
						i=coches.size()+1;
					}
				}
			}
		}
		
		if (resultado==0) { //Comprobar requisito 5  - ¿Está el coche rojo está horizontal? 
			for(int i=0;i<coches.size();i++) {
				if (coches.get(i).getNombre()=='A') {
					boolean legal;
					legal=coches.get(i).definirOrientacion();
					if(legal) {
						if(coches.get(i).getOrientacion())
							resultado=5;
					}
					i=coches.size()+1;
				}
			}
		}
		
		if (resultado==0) { //Comprobar requisito 6  - ¿El coche tiene un tamaño de 2 o 3? 
			for(int i=0;i<coches.size();i++) {
				if(coches.get(i).getCoordenadas().size()>3 || coches.get(i).getCoordenadas().size()<2)
					resultado=6;
			}
		}
		
		if (resultado==0 || resultado==6) { //Comprobar requisito 7  - ¿Hay vehículos con la misma letra duplicados?
			boolean legal;
			for(int i=0;i<coches.size();i++) {
				legal=coches.get(i).definirOrientacion();
				if(!legal) {
					resultado=7;
					i=coches.size()+1;
				}
			}
		}
		
		return resultado;
	}
	
	public void generarCoches() {
		ControlVisual control = new ControlVisual(tablero);
		Color[] listaColores=control.generarColores();
		for(int i=0;i<tablero.length;i++) {
			for(int j=0;j<tablero[0].length;j++) {
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
						Coche c = new Coche(tablero[i][j], listaColores[coches.size()]);
						c.addCoordenada(coordenadas);
						coches.add(c);
					}
				}
			}
		}
	}
	
	public String preguntasTablero(String args[]) {
		String resultadoConsulta="";
		
		switch (args[3]) {
			case "--whereis": 
				if(args.length>4) {
					if(args[4].length()==1) {
						boolean cocheEncontrado=false;
						for(int i=0;i<coches.size();i++) {
							if(coches.get(i).getNombre()==args[4].toCharArray()[0]) {
								resultadoConsulta=coches.get(i).getStringCoordenadas();
								i=coches.size()+1;
								cocheEncontrado=true;
							}
						}
						if(!cocheEncontrado) 
							resultadoConsulta="El vehículo: "+args[4]+" no existe.";
					}
					else
						resultadoConsulta="El vehículo: "+args[4]+" no es válido, recuerda que se representan como un solo caracter en mayúsculas.";	
				}
				else
					resultadoConsulta="Número de parametros insuficiente, --whereis requiere indicar el vehículo a buscar.";
				
				break;
				
			case "--what":
				resultadoConsulta="Formato de las coordenadas incorrecto, el formato para --what es: --what X,Y donde X e Y son las coordenadas a buscar.\nX e Y deben tener un valor 0 y 5 e ir sin parentesis.";
				if(args.length>4) {
					if(args[4].length()>2) {
						String analizarCoordenadas = args[4];
						if(Character.isDigit(analizarCoordenadas.charAt(0)) && analizarCoordenadas.charAt(1)==',' && Character.isDigit(analizarCoordenadas.charAt(2))) {
							int posicionX=Character.getNumericValue(analizarCoordenadas.charAt(0));
							int posicionY=Character.getNumericValue(analizarCoordenadas.charAt(2));
							resultadoConsulta=""+tablero[posicionX][posicionY];
						}
					}	
				}
				else
					resultadoConsulta="Número de parametros insuficiente, --what requiere indicar la celda a examinar.";
				break;
				
			case "--size":
				if(args.length>4) {
					if(args[4].length()==1) {
						boolean cocheEncontrado=false;
						for(int i=0;i<coches.size();i++) {
							if(coches.get(i).getNombre()==args[4].toCharArray()[0]) {
								resultadoConsulta=""+coches.get(i).getCoordenadas().size();
								i=coches.size()+1;
								cocheEncontrado=true;
							}
						}
						if(!cocheEncontrado) 
							resultadoConsulta="El vehículo: "+args[4]+" no existe.";
					}
					else
						resultadoConsulta="El vehículo: "+args[4]+" no es válido, recuerda que se representan como un solo caracter en mayúsculas.";
				}
				else
					resultadoConsulta="Número de parametros insuficiente, --size requiere indicar el vehículo a examinar.";
				break;
				
			case "--howmany":
				resultadoConsulta=""+coches.size();
				break;
			default:
				resultadoConsulta="La opción: "+args[3]+" no está reconocida, prueba con: --whereis --what --size --howmany";
		}
		return resultadoConsulta;
	}
}

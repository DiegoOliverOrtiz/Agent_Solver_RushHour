package rushhour;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

public class Tablero {
	private String semilla="";
	private char matriz[][]=new char[6][6];
	private ArrayList<String> sucesores = new ArrayList<>();
	private ArrayList<Vehiculo> vehiculos=new ArrayList<>();
	
	public Tablero() {
	}
	
	public Tablero(String semilla) {
		this.semilla=semilla;
		int cuenta=0;
		for(int i=0;i<matriz.length;i++) {
			for(int j=0;j<matriz[0].length;j++) {
				matriz[i][j]=semilla.charAt(cuenta);
				cuenta++;
			}
		}
	}
	
	public Tablero(char matriz[][]) {
		this.matriz=matriz;
		for(int i=0;i<matriz.length;i++) {
			for(int j=0;j<matriz[0].length;j++) {
				semilla=semilla+matriz[i][j];
			}
		}
	}
	public String getSemilla() {
		return semilla;
	}
	public void setSemilla(String semilla) {
		this.semilla = semilla;
	}
	public void setSemillaConMatriz() {
		semilla="";
		for(int i=0;i<matriz.length;i++) {
			for(int j=0;j<matriz[0].length;j++) {
				semilla=semilla+matriz[i][j];
			}
		}
	}
	public char[][] getMatriz() {
		return matriz;
	}
	public char getValorMatriz(int x, int y) {
		return matriz[x][y];
	}
	public void setMatriz(char[][] matriz) {
		this.matriz = matriz;
	}
	public void setMatrizConSemilla(String semilla) {
		int cuenta=0;
		for(int i=0;i<matriz.length;i++) {
			for(int j=0;j<matriz[0].length;j++) {
				matriz[i][j]=semilla.charAt(cuenta);
				cuenta++;
			}
		}
	}
	public void setValorMatriz(int x, int y, char nuevoValor) {
		this.matriz[x][y]=nuevoValor;
	}
	
	public ArrayList<String> getSucesores(){
		return sucesores;
	}
	
	public String getSucesoresJuntos() {
		String retorno="";
		if(sucesores.size()<1)
			retorno="No hay sucesores para este tablero";
		else {
			Collections.sort(sucesores);
			for(int i=0;i<sucesores.size();i++) {
				retorno=retorno+sucesores.get(i)+"\n";
			}
		}
		return retorno;
	}
	
	public void setSucesores(ArrayList<String> sucesores) {
		this.sucesores=sucesores;
	}
	
	public void addSucesor(String nuevoSucesor) {
		sucesores.add(nuevoSucesor);
	}
	
	/* generarVehiculos tiene muchas lineas pero es simple
	 * Recorreremos todo el tablero casilla por casilla
	 * si vamos una letra que pueda ser un coche (es decir no tenemos 'o')
	 * vemos si esa letra está asociada ya a algún coche (recorriendo y buscando en toda la lista de coches)
	 * si esa letra está asociada entonces es una nueva coordenada para el coche (la añadimos)
	 * si esa letra no lo está entonces creamos un nuevo coche
	 * */
	public void generarVehiculos() {
		ControlVisual control = new ControlVisual(matriz); //Creamos un control visual para asignar los colores
		Color[] listaColores=control.generarColores(); //Cargamos los colores
		for(int i=0;i<matriz.length;i++) { //Recorremos todo el tablero
			for(int j=0;j<matriz[0].length;j++) {
				if(matriz[i][j]!='o') { //Si no es un hueco en blanco entonces
					int[]coordenadas = {i, j}; //Guardamos las coordenadas actuales en un vector (recordemos que los coches tienen una lista con vectores de coordenadas)
					boolean existeCoche=false; //Asumimos que no es un nuevo coche
					for(int k=0;k<vehiculos.size();k++) { //Buscamos entre todos los coches
						if(vehiculos.get(k) != null){ //En caso de que la lista no esté vacia
							Vehiculo v = vehiculos.get(k); 
							if(v.getNombre()==matriz[i][j]) { //Vemos si el coche está ya en el tablero
								existeCoche=true; //Si lo está así indicamos y añadimos las coordenadas nuevas al coche
								v.addCoordenada(coordenadas);
							}
						}
					}
					if(existeCoche==false) { //Si no está el coche pues lo creamos 
						Vehiculo v = new Vehiculo(matriz[i][j], listaColores[vehiculos.size()]);
						v.addCoordenada(coordenadas);
						vehiculos.add(v);
					}
				}
			}
		}
	}
	
	public ArrayList<Vehiculo> getVehiculos(){
		return vehiculos;
	}
	
	public void definirOrientacionVehiculosTablero() {
		for(int i=0;i<vehiculos.size();i++) { //Primero definimos la orientación de todos los coches
			vehiculos.get(i).definirOrientacion();
		}
	}
}

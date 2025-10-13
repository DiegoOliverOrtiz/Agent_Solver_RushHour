package rushhour;

import java.util.ArrayList;

public class Tablero {
	private String semilla="";
	private char matriz[][]=new char[6][6];
	private ArrayList<String> sucesores = new ArrayList<>();
	
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
}

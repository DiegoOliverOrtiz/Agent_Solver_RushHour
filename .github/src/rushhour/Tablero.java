package rushhour;

public class Tablero {
	private String semilla="";
	private char matriz[][]=new char[6][6];
	
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
				this.matriz[i][j]=semilla.charAt(cuenta);
				cuenta++;
			}
		}
	}
	public void setValorMatriz(int x, int y, char nuevoValor) {
		this.matriz[x][y]=nuevoValor;
	}
}

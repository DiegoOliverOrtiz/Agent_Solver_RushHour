package rushhour;

import java.util.ArrayList;
import java.awt.Color;

/* Descripción: 
 * 	La clase coche se encarga de gestionar los vehículos del tablero
 * 
 * Atributos: 
 * 	nombre: letra que lo representa
 * 	coordenadas: arraylist que contiene un vector, cada vector representa un conjunto de coordenadas (X,Y) de tal forma que 
 * 	cada posición del array list contiene unas coordenadas especificas en forma de vector y cada vector contiene el desglose de esa coordenada´
 * 	orientacion: define si el coche está en horizontal o vertical
 * 	col: determina el color del coche para cuando lo visualicemos
 * 
 * Funciones:
 * 	Constructor, getters y solo setter para las coordenadas (pero solo para añadir no para modificar anteriores)
 * 	Definir orientacion: función que determina si la posición del objeto es legal (no esté duplicado) y define si está vertical u horizontal
 * 
 * */
public class Coche {
	private char nombre;
	private ArrayList<int[]> coordenadas = new ArrayList<>();
	private boolean orientacion; //false es horizontal y true es vertical
	private Color col;
	
	public Coche() {
	}
	
	public Coche(char nombre, Color col) {
		this.nombre=nombre;
		//Como el coche A siempre es el rojo y los colores los generamos aleatoriamente le forzamos en el constructor a ser de color rojo
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
	
	//Además de tener un metodo para devolver las coordenadas como valores, también tenemos este método por si es necesario que sea un String
	public String getStringCoordenadas() {
		String coordsToString="";
		if(coordenadas.size()>0) { //Si tiene algo en el vector de coordenadas lo devuelve
			for(int i=0;i<coordenadas.size();i++) {
				coordsToString=coordsToString+"("+coordenadas.get(i)[0]+","+coordenadas.get(i)[1]+")"; //Expande las coordenadas en formato (X,Y)(A,B)
			}
		}
		else //En caso contrario devuelve un aviso de error
			coordsToString="Sin coordenadas cargadas";
		return coordsToString;
	}
	
	public boolean getOrientacion() {
		return orientacion;
	}
	
	public void addCoordenada(int[] nuevasCoords) {
		//No las sobreescribimos solo añadimos
		coordenadas.add(nuevasCoords);
	}
	
	/* Método para saber si el objeto tiene coordenadas legales (no duplicado), en caso de no estarlo define su orientación
	 * Devuelve false si la posición es ilegal (está duplicado o lo que sea) y true si es legal
	 * */
	public boolean definirOrientacion() {
		boolean legal=true; //Asumimos que el coche tiene unas coordenadas legales
		int[] filas = new int[coordenadas.size()];
		int[] columnas = new int[coordenadas.size()];
		boolean diferentesFilas=false; //Usaremos estas variables para ver si tiene diferentes filas o columnas
		boolean diferentesColumnas=false; //Luego lo explico pero sirve para ver la orientación
		//Desglosamos las coordenadas en filas y columnas
		for(int i=0;i<coordenadas.size();i++) {
			filas[i]=coordenadas.get(i)[0];
			columnas[i]=coordenadas.get(i)[1];
		}

		for (int i = 1; i < filas.length; i++) {
	        if (filas[i] != filas[0]) { //Como sabemos que no tiene que variar podmeos comprobar que se mantenga igual solo comparando on el primer valor
	            diferentesFilas=true;
	        }
	        if (columnas[i] != columnas[0]) {
	            diferentesColumnas=true;
	        }
	    }
		/* Comprobamos si se repite algún valor de las filas o las columnas, en caso de estarlo es ilegal la posición, pues si fuera legal
		 * Habría al menos un valor que se mantendría igual (si es horizontal su Y (columnas) se mantiene igual y si es vertical su X (filas) se mantiene igual)
		 */
		if(diferentesFilas==diferentesColumnas) {
			legal=false; //Al tener X e Y diferente quiere decir que hay un duplicado y por tanto es una posición ilegal
		}
		else if(diferentesFilas==true && diferentesColumnas==false){ //Si son mismas columnas y diferentes filas
			orientacion=true; //Recordemos que true es vertical
		}
		else if(diferentesFilas==false && diferentesColumnas==true){ //Si son mismas filas y diferentes columnas
			orientacion=false; //Recordemos que false es horizontal
		}
		return legal;
	}
}

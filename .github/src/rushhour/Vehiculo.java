package rushhour;

import java.util.ArrayList;
import java.awt.Color;

/* Descripción: 
 * 	La clase vehiculo se encarga de gestionar los vehículos del tablero independientemente de si es un coche o un camión
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
 * 	Mover: actualiza las coordenadas del vehiculo en función de su orientación y del movimiento pasado como parametro
 * 
 * */
public class Vehiculo {
	private char nombre;
	private ArrayList<int[]> coordenadas = new ArrayList<>();
	private boolean orientacion; //false es horizontal y true es vertical
	private Color col;
	
	public Vehiculo() {
	}
	
	public Vehiculo(char nombre, Color col) {
		this.nombre=nombre;
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
	
	/* Metodo para actualizar las coordenadas simulando un movimiento, el vehiculo reacciona diferente si es vertical u horizontal
	 * se comprueba en el primer if, después se encarga de ver si el movimiento es ilegal, al final del método una vez se han
	 * comprobado todos los movimientos si se mantiene como legal se actualizan las coordenadas.
	 * Los parametro de entrada son: el movimiento a realizar (+-X) y el tablero sobre el que moverse, recordemos que este tablero 
	 * viene sin el vehículo a mover para evitar solapamientos, nos aseguramos que siempre sea así porque se invoca desde question --move
	 * */
	public boolean mover(int movimiento, Tablero tableroJuego) {
		boolean legal=true; //Asumimos que el movimiento va a ser legal
		ArrayList<int[]> coordenadasTemporales = new ArrayList<>(); //Lista para tener de forma temporal las coordenadas actualizadas
		//Comprobamos si es horizontal o vertical, recordemos false (!orientacion) es horizontal y true (orientacion) es vertical)
		if(!orientacion) { //Horizontal
			for(int i=0;i<coordenadas.size();i++) { //Para cada cordenada
				if((coordenadas.get(i)[1]+movimiento)<=5&&(coordenadas.get(i)[1]+movimiento)>=0) { //Vemos si la nueva posición se sale del tablero
					int[] nuevasCoords = coordenadas.get(i); //guardamos temporalmente las coordenadas actuales
					nuevasCoords[1]=coordenadas.get(i)[1]+movimiento; //se aplica el movimiento a [1] porque es horizontal, recordemos que cada coordenada es una tupla X,Y
					if(tableroJuego.getValorMatriz(nuevasCoords[0], nuevasCoords[1])=='o') //Comprobamos que sea un hueco y no haya coches
						coordenadasTemporales.add(nuevasCoords); //Si lo es se añaden a las coordenaasTemporales
					else
						legal=false; //Si colisiona el movimiento es ilegal
				}
				else {
					legal=false; //Si se sale es ilegal
				}
			}
			if(legal)
				coordenadas=coordenadasTemporales;
		}
		//Se hace lo mismo para el movimiento vertical, pero teniendo en cuenta las coordenadas a mover
		else if (orientacion){ //Vertical
			for(int i=0;i<coordenadas.size();i++) {
				int invertirMovimiento=movimiento*-1;
				if((coordenadas.get(i)[0]+invertirMovimiento)<=5&&(coordenadas.get(i)[0]+invertirMovimiento)>=0) {
					int[] nuevasCoords = coordenadas.get(i);
					nuevasCoords[0]=coordenadas.get(i)[0]+invertirMovimiento;
					if(tableroJuego.getValorMatriz(nuevasCoords[0], nuevasCoords[1])=='o')
						coordenadasTemporales.add(nuevasCoords);
					else
						legal=false;
				}
				else {
					legal=false;
				}
			}
			if(legal)
				coordenadas=coordenadasTemporales;
		}
		return legal;
	}
}

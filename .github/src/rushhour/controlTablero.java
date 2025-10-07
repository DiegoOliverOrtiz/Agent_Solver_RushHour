package rushhour;

import java.awt.Color;
import java.util.*;
/* Descripción: 
 * 	Clase que gestiona el tablero de juego, lo crea, añade coches y le realiza cuestiones al tablero
 * 
 * Atributos: 
 * 	tablero[][]: guarda el tablero de juego siendo cada casilla un char (letras mayúsculas para un coche, 'o' para un hueco)
 * 	coches: lista que contiene los coches que se vayan añadiendo al juego
 * 
 * Funciones:
 * 	controlTablero: Constructor
 * 	crearTablero: Crea el tablero en función de un string que pasamos como parametro (semilla), se aprovecha para hacer la función verify
 * 	revisando que sea un tablero legal o ilegal, retorna un entero que representa ese estado de validez y en caso de no serlo el error dado
 * 	generarCoches: crea los coches que habrá en el juego en función de cada letra guardada en el tablero
 * 	preguntasTablero: realiza las funciones de la consulta question y retorna su resultado o un mensaje de error en caso de ser incorrecto
 * 
 * */

public class controlTablero {
	private char tablero[][]=new char[6][6];
	private ArrayList<Coche> coches=new ArrayList<>();
	
	public controlTablero() {
	}
	
	/* La función crearTablero es la más densa y larga del programa, en función de una semilla (la cadena de los args por lo general)
	 * devuelve un valor indicando si esa cadena es apta para usarse o si es invalida, si es el caso devuelve un error. La función se encarga
	 * de ir de requisito en requisito de error de uno en uno entrando a comprobar si y solo si no se ha generado antes otro error
	 * 
	 * */
	public int crearTablero(String semilla) {
		int resultado=0; //Asumimos que la cadena es válida e iremos comprobando de uno en uno los posibles errores
		
		//Comprobar requisito 1 - Tamaño de 36 caracteres
		if(semilla.length()!=36) 
			resultado=1;
		
		//Comprobar requisito 2 - Solo letras las letras 'A-Z' u 'o'
		if(resultado==0) { //Entra solo si no ha habido error antes, esto será igual para todos menos para el 7
			int cuenta=0; //Usamos una variable auxiliar para llevar el conteo de caracteres de la semilla
			for(int i=0;i<tablero.length;i++) { //Recorre de una en una todas las posiciones del tablero (cuando entre estarán vacías)
				for(int j=0;j<tablero[0].length;j++) {
					char letra=semilla.charAt(cuenta); //Toma el caracter de la semilla correspondiente
					tablero[i][j]=letra; //Lo añade al tablero
					if(!Character.isLetter(letra) && (!Character.isUpperCase(letra) || letra!='o')) { //Mira si es inválido
						resultado=2; //Damos el error
					}
					cuenta++; //Aumenta la cuenta para seguir el conteo necesario
				}
			}
		}
		
		
		//Comrpobar requisito 3 - ¿Está el coche rojo?
		if(resultado==0) { 
			//Dentro del if para no generar coches ilegales según el requisito 2
			generarCoches(); //Generamos los coches (se explica más adelante el cómo)
			ControlVisual control = new ControlVisual(tablero); //Creamos una instancia del control visual
			control.mostrarMatriz(coches); //Mostramos el tablero para ir viendo errores (o ver si está bien)
			
			int resultadoAuxiliar=resultado; //Guardamos nuestro estado actual
			resultado = 3; //Asumimos que no está el coche rojo
			for(int i=0;i<coches.size();i++) {
				if (coches.get(i).getNombre()=='A') {
					resultado=resultadoAuxiliar; //Lo buscamos entre la lista de coches y si está recuperamos nuestro estado de resultado previo
					i=coches.size()+1; //Salimos del bucle para eficienciar el programa (se hace más veces más adelante)
				}
			}
		}
		
		//Comprobar requisito 4 - ¿Está el coche rojo en la tercera fila?
		if (resultado==0) { 
			int resultadoAuxiliar=resultado; //Igual que antes guardaremos nuestro estado
			resultado = 4; //Y asumieremos que el coche rojo no está en la tercera fila
			for(int i=0;i<coches.size();i++) { //Buscamos el coche rojo entre todos los coches
				if (coches.get(i).getNombre()=='A') {
					ArrayList<int[]> coordenadas=coches.get(i).getCoordenadas(); //Obtenemos su lista de coordenadas
					int[] posicion = coordenadas.get(0); //Cogemos la primera (se puede con cualquier coordenada)
					int fila=posicion[0]; //Si de verdad está en la tercera fila con comprobarlo en un valor es suficiente (el siguiente punto mira que sea horizontal)
					if(fila==2) { //Al igual que antes si vemos que está bien se recupera el estado de resultado anterior
						resultado=resultadoAuxiliar;
						i=coches.size()+1;
					}
				}
			}
		}
		
		//Comprobar requisito 5  - ¿Está el coche rojo está horizontal? 
		if (resultado==0) { 
			for(int i=0;i<coches.size();i++) {
				if (coches.get(i).getNombre()=='A') {
					boolean legal; //Por como hemos hecho nuestro metodo para saber la orientación primero tenemos que ver que sea un coche valido
					legal=coches.get(i).definirOrientacion(); //Lo guardamos
					if(legal) { //Esto solo se aplica si es legal, no nos importa si no lo es pues se mira en el requisito 7
						if(coches.get(i).getOrientacion()) //Obtenemos la orienación por como está definida si es true es vertical
							resultado=5; //Por tanto guardamos el error
					}
					i=coches.size()+1;
				}
			}
		}
		
		if (resultado==0) { //Comprobar requisito 6  - ¿El coche tiene un tamaño de 2 o 3? 
			/*Simplemente recorremos toda la lista de coche y como el tamaño de la lista de coordenadas de cada coche
			 * guarda una coordenada X,Y sabemos que cada elemento de la lista es una posición del coche, por lo tanto 
			 * si el tamaño de la lista es el tamaño del coche*/
			for(int i=0;i<coches.size();i++) { 
				if(coches.get(i).getCoordenadas().size()>3 || coches.get(i).getCoordenadas().size()<2) //Vemos que no sea menor a 2 o mayor a 3
					resultado=6;
			}
		}
		
		/*Comprobar requisito 7  - ¿Hay vehículos con la misma letra duplicados?
		 * Este es el único requisito que nos metemos si hay un error, pues puede haber ambiguedad de porque la da
		 * Un vehículo puede dar el error 6 (por tener 4 posiciones, por ejemplo) pero que sea por estar ilegales
		 * tal y como buscamos ver en este apartado
		 */
		if (resultado==0 || resultado==6) { 
			boolean legal;
			for(int i=0;i<coches.size();i++) { //Primero definimos la orientación de todos ls coches
				legal=coches.get(i).definirOrientacion();
				if(!legal&&coches.get(i).getCoordenadas().size()>1) { //Nuestro método nos devuelve si es legal o no, al no serlo sabemos que tenemos un coche ilegal
					resultado=7;
					i=coches.size()+1;
				}
			}
		}
		
		//Comprobar requisito EXTRA - El vehículo rojo es un coche (longitud 2) 
		if (resultado==0) { 
			for(int i=0;i<coches.size();i++) {
				if (coches.get(i).getNombre()=='A') {
					if(coches.get(i).getCoordenadas().size()>2) { 
							resultado=-1; //Devolvemos un error especial al no estar indicado como acutar en el campues (en el main no se representa así)
					}
					i=coches.size()+1;
				}
			}
		}
		
		return resultado;
	}
	
	/* generarCoches tiene muchas lineas pero es simple
	 * Recorreremos todo el tablero casilla por casilla
	 * si vamos una letra que pueda ser un coche (es decir no tenemos 'o')
	 * vemos si esa letra está asociada ya a algún coche (recorriendo y buscando en toda la lista de coches)
	 * si esa letra está asociada entonces es una nueva coordenada para el coche (la añadimos)
	 * si esa letra no lo está entonces creamos un nuevo coche
	 * */
	public void generarCoches() {
		ControlVisual control = new ControlVisual(tablero); //Creamos un control visual para asignar los colores
		Color[] listaColores=control.generarColores(); //Cargamos los colores
		for(int i=0;i<tablero.length;i++) { //Recorremos todo el tablero
			for(int j=0;j<tablero[0].length;j++) {
				if(tablero[i][j]!='o') { //Si no es un hueco en blanco entonces
					int[]coordenadas = {i, j}; //Guardamos las coordenadas actuales en un vector (recordemos que los coches tienen una lista con vectores de coordenadas)
					boolean existeCoche=false; //Asumimos que no es un nuevo coche
					for(int k=0;k<coches.size();k++) { //Buscamos entre todos los coches
						if(coches.get(k) != null){ //En caso de que la lista no esté vacia
							Coche c=coches.get(k); 
							if(c.getNombre()==tablero[i][j]) { //Vemos si el coche está ya en el tablero
								existeCoche=true; //Si lo está así indicamos y añadimos las coordenadas nuevas al coche
								c.addCoordenada(coordenadas);
							}
						}
					}
					if(existeCoche==false) { //Si no está el coche pues lo creamos 
						Coche c = new Coche(tablero[i][j], listaColores[coches.size()]);
						c.addCoordenada(coordenadas);
						coches.add(c);
					}
				}
			}
		}
	}
	
	
	/* Preguntas tablero consiste en un switch que revisa args[3] (recordemos que este método solo se activa si tenemos más de 3 argumentos,
	 * si hemos indicado en args[0] que es question y en args[1] que es -s y viendo que la cadena en args[2] es legal)
	 * ya en función de args[3] elige una opción u otra
	 * Hay multiples mensajes de error la mayoría sencillos de entender solo se han comentado los que tengan mayor relevancia o complejidad
	 * */
	public String preguntasTablero(String args[]) { //Pasamos todos los argumentos porque algunas preguntas tienen más o menos
		String resultadoConsulta=""; //Creamos el string a devolver (lo modificaremos según sea necesario)
		switch (args[3]) {
			case "--whereis": //Opción --whereis
				if(args.length>4) { //Whereis requiere indicar el coche a buscar así que ha de haber 5 parametros por lo menos
					if(args[4].length()==1) { //Comprobamos que solo nos estén metiendo una letra 
						boolean cocheEncontrado=false; //Asumimos que no lo hemos encontrado
						for(int i=0;i<coches.size();i++) { //Recorremos todos los coches
							if(coches.get(i).getNombre()==args[4].toCharArray()[0]) { //Vemos si coincide con el coche a buscar
								resultadoConsulta=coches.get(i).getStringCoordenadas(); //Si lo encontramos devuelve el string de las coordenadas 
								i=coches.size()+1; //Salimos del bucle y marcamos el coche como encontrado
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
				
			case "--what": //Opción --what
				//Como what tiene muchos casos de error muy similares, para simplificar ponemos un mensaje de error como el mensaje de la consulta por defecto
				resultadoConsulta="Formato de las coordenadas incorrecto, el formato para --what es: --what X,Y donde X e Y son las coordenadas a buscar.\nX e Y deben tener un valor 0 y 5 e ir sin parentesis.";
				if(args.length>4) {
					if(args[4].length()>2) {
						String analizarCoordenadas = args[4]; //Guardamos el parametro las coordenadas para simplificar la búsqueda
						//Comprobación de que sea un formato de coordenadas validas
						if(Character.isDigit(analizarCoordenadas.charAt(0)) && analizarCoordenadas.charAt(1)==',' && Character.isDigit(analizarCoordenadas.charAt(2))) {
							int posicionX=Character.getNumericValue(analizarCoordenadas.charAt(0)); //Guardamos las coordenadas para simplificar 
							int posicionY=Character.getNumericValue(analizarCoordenadas.charAt(2));
							resultadoConsulta=""+tablero[posicionX][posicionY];
						}
					}	
				}
				else
					resultadoConsulta="Número de parametros insuficiente, --what requiere indicar la celda a examinar.";
				break;
				
			case "--size": //Opción --size
				if(args.length>4) {
					if(args[4].length()==1) { //Tamaño uno pues tenemos que buscar un coche que se representa como una letra en concreto
						boolean cocheEncontrado=false;
						for(int i=0;i<coches.size();i++) { //Esto es lo mismo que en cualquiera de los métodos anteriores, recorremos la lista de coches hasta encontrarlo
							if(coches.get(i).getNombre()==args[4].toCharArray()[0]) {
								resultadoConsulta=""+coches.get(i).getCoordenadas().size(); //Guardamos el tamaño si lo encuentra
								i=coches.size()+1;
								cocheEncontrado=true;
							}
						}
						if(!cocheEncontrado) //Haciendo uso de esta variable auxiliar podemos indicar que el coche a buscar no existe
							resultadoConsulta="El vehículo: "+args[4]+" no existe.";
					}
					else
						resultadoConsulta="El vehículo: "+args[4]+" no es válido, recuerda que se representan como un solo caracter en mayúsculas.";
				}
				else
					resultadoConsulta="Número de parametros insuficiente, --size requiere indicar el vehículo a examinar.";
				break;
				
			case "--howmany": //Opción --howmany
				resultadoConsulta=""+coches.size(); //Es la más simple, solo hay que devolver el tamaño de nuestra lista de coches
				break;
			default: //Opción default por si nos introducen algun argumento ilegal
				resultadoConsulta="La opción: "+args[3]+" no está reconocida, prueba con: --whereis --what --size --howmany";
		}
		return resultadoConsulta;
	}
}

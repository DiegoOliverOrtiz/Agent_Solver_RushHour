package rushhour;

import java.util.*;
/* Descripción: 
 * 	Clase que gestiona un tablero de juego haciendo comprobaciones y preguntas 
 * 
 * Funciones:
 * 	ControlTablero: Constructor
 * 	verificarTablero: Dada una semilla (tablero en string) revisa si es un tablero válido o no, retornando un número en función de cada situación
 * 	revisando que sea un tablero legal o ilegal, retorna un entero que representa ese estado de validez y en caso de no serlo el error dado
 * 	preguntasTablero: realiza las funciones de la consulta question y retorna su resultado o un mensaje de error en caso de ser incorrecto
 * 	buscarVehiculo: función auxiliar ya que es necesario en múltiples ocasiones saber donde está un vehiculo y si existe o no
 * 	sucesoresTablero: se encarga de generar y devolver de los sucesores de un tablero 
 * */

public class ControlTablero {
	public ControlTablero() {
	}
	
	/* La función verificarTabler en función de una semilla (la cadena de los args por lo general)o una que le pasemos 
	 * devuelve un valor indicando si esa cadena es apta para usarse o si es invalida, si es el caso devuelve un error. La función se encarga
	 * de ir de requisito de error en requisito de error entrando a comprobar que se valido si y solo si no se ha generado antes otro error
	 * */
	public int verificarTablero(String semilla) {
		Tablero tableroAux=new Tablero(); //Usamos un tablero auxiliar para simplicar el método
		int resultado=0; //Asumimos que la cadena es válida e iremos comprobando de uno en uno los posibles errores
		
		//Comprobar requisito 1 - Tamaño de 36 caracteres
		if(semilla.length()!=36) 
			resultado=1;
		
		//Comprobar requisito 2 - Solo letras las letras 'A-Z' u 'o'
		if(resultado==0) { //Entra solo si no ha habido error antes, esto será igual para todos menos para el 7
			tableroAux.setMatrizConSemilla(semilla);
			tableroAux.setSemilla(semilla);
			int cuenta=0; //Usamos una variable auxiliar para llevar el conteo de caracteres de la semilla
			for(int i=0;i<tableroAux.getMatriz().length;i++) { //Recorre de una en una todas las posiciones del tablero (cuando entre estarán vacías)
				for(int j=0;j<tableroAux.getMatriz()[0].length;j++) {
					char letra=semilla.charAt(cuenta); //Toma el caracter de la semilla correspondiente
					tableroAux.setValorMatriz(i,j,letra); //Lo añade al tablero
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
			tableroAux.generarVehiculos(); //Generamos los coches (se explica más adelante el cómo)
			int resultadoAuxiliar=resultado; //Guardamos nuestro estado actual
			resultado = 3; //Asumimos que no está el coche rojo
			if (buscarVehiculo('A', tableroAux)>-1) { //Recordemos que -1 es que no ha encontrado el vehículo
				resultado=resultadoAuxiliar; //Lo buscamos entre la lista de coches y si está recuperamos nuestro estado de resultado previo
			}
		}
		
		//Comprobar requisito 4 - ¿Está el coche rojo en la tercera fila?
		if (resultado==0) { 
			int resultadoAuxiliar=resultado; //Igual que antes guardaremos nuestro estado
			resultado = 4; //Y asumieremos que el coche rojo no está en la tercera fila
			int posicionCocheA=buscarVehiculo('A', tableroAux);
			if (posicionCocheA>-1) {
				ArrayList<int[]> coordenadas=tableroAux.getVehiculos().get(posicionCocheA).getCoordenadas(); //Obtenemos su lista de coordenadas
				int[] posicion = coordenadas.get(0); //Cogemos la primera (se puede con cualquier coordenada)
				int fila=posicion[0]; //Si de verdad está en la tercera fila con comprobarlo en un valor es suficiente (el siguiente punto mira que sea horizontal)
				if(fila==2) { //Al igual que antes si vemos que está bien se recupera el estado de resultado anterior
					resultado=resultadoAuxiliar;
				}
			}
		}
		
		//Comprobar requisito 5  - ¿Está el coche rojo está horizontal? 
		if (resultado==0) { 
			int posicionCocheA=buscarVehiculo('A', tableroAux);
			if (posicionCocheA>-1) {
				boolean legal; //Por como hemos hecho nuestro metodo para saber la orientación primero tenemos que ver que sea un coche valido
				legal=tableroAux.getVehiculos().get(posicionCocheA).definirOrientacion(); //Lo guardamos
				if(legal) { //Esto solo se aplica si es legal, no nos importa si no lo es pues se mira en el requisito 7
					if(tableroAux.getVehiculos().get(posicionCocheA).getOrientacion()) //Obtenemos la orienación por como está definida si es true es vertical
						resultado=5; //Por tanto guardamos el error
				}
			}
		}
		
		if (resultado==0) { //Comprobar requisito 6  - ¿El coche tiene un tamaño de 2 o 3? 
			/*Simplemente recorremos toda la lista de coche y como el tamaño de la lista de coordenadas de cada coche
			 * guarda una coordenada X,Y sabemos que cada elemento de la lista es una posición del coche, por lo tanto 
			 * si el tamaño de la lista es el tamaño del coche*/
			for(int i=0;i<tableroAux.getVehiculos().size();i++) { 
				if(tableroAux.getVehiculos().get(i).getCoordenadas().size()>3 || tableroAux.getVehiculos().get(i).getCoordenadas().size()<2) //Vemos que no sea menor a 2 o mayor a 3
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
			for(int i=0;i<tableroAux.getVehiculos().size();i++) { //Primero definimos la orientación de todos los coches
				legal=tableroAux.getVehiculos().get(i).definirOrientacion();
				if(!legal&&tableroAux.getVehiculos().get(i).getCoordenadas().size()>1) { //Nuestro método nos devuelve si es legal o no, al no serlo sabemos que tenemos un coche ilegal
					resultado=7;
					i=tableroAux.getVehiculos().size()+1;
				}
			}
		}
		
		//Comprobar requisito EXTRA - El vehículo rojo es un coche (longitud 2) 
		if (resultado==0) { 
			int posicionCocheA=buscarVehiculo('A', tableroAux);
			if (posicionCocheA>-1) {
				if(tableroAux.getVehiculos().get(posicionCocheA).getCoordenadas().size()>2) { 
						resultado=-1; //Devolvemos un error especial al no estar indicado como acutar en el campues (en el main no se representa así)
				}
			}
		}		
		return resultado;
	}
	
	
	/* Preguntas tablero consiste en un switch que revisa args[3] (recordemos que este método solo se activa si tenemos más de 3 argumentos,
	 * si hemos indicado en args[0] que es question y en args[1] que es -s y viendo que la cadena en args[2] es legal)
	 * ya en función de args[3] elige una opción u otra
	 * Hay multiples mensajes de error la mayoría sencillos de entender solo se han comentado los que tengan mayor relevancia o complejidad
	 * */
	public String preguntasTablero(String args[]) { //Pasamos todos los argumentos porque algunas preguntas tienen más o menos
		String resultadoConsulta=""; //Creamos el string a devolver (lo modificaremos según sea necesario)
		if(verificarTablero(args[2])==0) { //Vemos si es legal la cadena sobre la que trabajar
		Tablero tableroAux=new Tablero(args[2]);
		tableroAux.generarVehiculos();
		tableroAux.definirOrientacionVehiculosTablero();
			switch (args[3]) {
				case "--whereis": //Opción --whereis
					if(args.length>4) { //Whereis requiere indicar el coche a buscar así que ha de haber 5 parametros por lo menos
						if(args[4].length()==1) { //Comprobamos que solo nos estén metiendo una letra 
							int posicionVehiculo=buscarVehiculo(args[4].toCharArray()[0], tableroAux); //Buscamos el vehiculo indicado
							if (posicionVehiculo>-1)
								resultadoConsulta=tableroAux.getVehiculos().get(posicionVehiculo).getStringCoordenadas(); //Si lo encontramos devuelve el string de las coordenadas 
							else if(posicionVehiculo==-1) 
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
								resultadoConsulta=""+tableroAux.getValorMatriz(posicionX, posicionY);
							}
						}	
					}
					else
						resultadoConsulta="Número de parametros insuficiente, --what requiere indicar la celda a examinar.";
					break;
				
				case "--size": //Opción --size
					if(args.length>4) {
						if(args[4].length()==1) { //Tamaño uno pues tenemos que buscar un coche que se representa como una letra en concreto
							int posicionVehiculo=buscarVehiculo(args[4].toCharArray()[0], tableroAux);
							if (posicionVehiculo>-1)
									resultadoConsulta=""+tableroAux.getVehiculos().get(posicionVehiculo).getCoordenadas().size(); //Guardamos el tamaño si lo encuentra
							else if(posicionVehiculo==-1) //Haciendo uso de esta variable auxiliar podemos indicar que el coche a buscar no existe
								resultadoConsulta="El vehículo: "+args[4]+" no existe.";
						}
						else
							resultadoConsulta="El vehículo: "+args[4]+" no es válido, recuerda que se representan como un solo caracter en mayúsculas.";
					}
					else
						resultadoConsulta="Número de parametros insuficiente, --size requiere indicar el vehículo a examinar.";
					break;
				
				case "--howmany": //Opción --howmany
					resultadoConsulta=""+tableroAux.getVehiculos().size(); //Es la más simple, solo hay que devolver el tamaño de nuestra lista de coches
					break;
				
				case "--goal": //Goal es simple, dado un tablero cualquiera, busca las coordenadas del vehiculo rojo 'A' y mira si coincide con las de salida
					resultadoConsulta="FALSE"; //Asumimos que no coinciden 
					int posicionCocheA=buscarVehiculo('A', tableroAux);
					if (posicionCocheA>-1) {
						ArrayList<int[]> coordenadas=tableroAux.getVehiculos().get(posicionCocheA).getCoordenadas();
						if(coordenadas.get(0)[0]==2&&coordenadas.get(0)[1]==5||coordenadas.get(1)[0]==2&&coordenadas.get(1)[1]==5)
							resultadoConsulta="TRUE";//Si coincide devolvemos true
					}
					break;
				/* --move puede parecer largo y complejo pero su lógica se resume en realizar ciertas comprobaciones para ver que sea válido
				 * y vamos de poco a poco realizando comprobaciones hasta alterar las coordenadas
				 * */
				case "--move":
					if(args.length>4) {
						String[] cadenaMovimientos=args[4].split(","); //Primero dado que move puede traer una serie de movimientos los separa para irde uno en uno
						for(int i=0;i<cadenaMovimientos.length;i++) { //Para cada movimiento detectado
							if(cadenaMovimientos[i].length()==3) { //Primero vemos que sean 3 caracteres: vehiculo,+/-,numero de casillas a mover
								char vehiculoMover=cadenaMovimientos[i].charAt(0); //vemos que sea un vehículo (representado por cahr)
								int posicionVehiculo=buscarVehiculo(vehiculoMover, tableroAux); //Vemos si existe
								if(posicionVehiculo>-1) { //Si existe seguimos (recordemos que posición vehiculo es la posición en la lista de vehiculos de un tablero, -1 es que no existe)
									ArrayList<int[]> coordenadasOriginales=tableroAux.getVehiculos().get(posicionVehiculo).getCoordenadas(); //guardamos las coordenadas del vehículo
									for(int j=0;j<coordenadasOriginales.size();j++) { //Y vamos coordenada a coordenada borrando el vehículo
										int[] tuplaCoordsOriginales=coordenadasOriginales.get(j); //El fin de esto es luego trabajar sobre este tablero sin que el propio vehículo se solape consigo mismo
										tableroAux.setValorMatriz(tuplaCoordsOriginales[0], tuplaCoordsOriginales[1], 'o');
									}
									String movimientoString=cadenaMovimientos[i].charAt(1)+""+cadenaMovimientos[i].charAt(2); //concatenamos el posible simbolo con el movimiento
									try { //Intentamos convertir el movimiento a un valor numerico 
										int movimiento=Integer.parseInt(movimientoString);
										boolean comprobarMovimiento = tableroAux.getVehiculos().get(posicionVehiculo).mover(movimiento, tableroAux); //Llamamos al metodo del coche para intentar moverlo
										if(comprobarMovimiento) { //Si se ha movido
											ArrayList<int[]> coordenadasNuevas = tableroAux.getVehiculos().get(posicionVehiculo).getCoordenadas(); //Cogemos las nuevas coordenadas
											for(int j=0;j<coordenadasOriginales.size();j++) { //Actualizamos las coordenadas del tablero
												int[] tuplaCoordsNuevas=coordenadasNuevas.get(j);
												tableroAux.setValorMatriz(tuplaCoordsNuevas[0], tuplaCoordsNuevas[1], tableroAux.getVehiculos().get(posicionVehiculo).getNombre());
											}
											tableroAux.setSemillaConMatriz(); //Actualizamos la semilla
											resultadoConsulta=tableroAux.getSemilla(); //El resultado de la consulta se establece como el de la semilla
										}
										else //Mensaje de error si el movimiento no es legal
											resultadoConsulta="El movimiento nº"+i+": "+cadenaMovimientos[i]+" es ilegal, asegurate que el vehiculo no salga del tablero ni se mueva a una posicion ya ocupada.";
									}
									catch (NumberFormatException e) { //Mensaje de error si no se puede convertir a un valor numerico el movimiento
										resultadoConsulta="El movimiento: "+movimientoString+" no es válido de ser: V~X, donde V es el vehículo a mover, ~ el simbolo (+ -) y X las posiciones a mover.";
									}
								}
								else if(posicionVehiculo==-1) { //Mensaje de error si el vehículo no existe 
									resultadoConsulta="El vehículo: "+vehiculoMover+" no existe.";
									i=cadenaMovimientos.length+1;
								}
							}
							else { //Mensaje por si es un formato incorrecto
								resultadoConsulta="El formato de: "+cadenaMovimientos[i]+ " es incorrecto debe ser: V~X, donde V es el vehículo a mover, ~ el simbolo (+ -) y X las posiciones a mover.";
								i=cadenaMovimientos.length+1;
							}
						}
					}
					else //Mensaje por si es un formato incorrecto
						resultadoConsulta="La opción --move debe incluir el movimiento a realizar con el formato: V~X, donde V es el vehículo a mover, ~ el simbolo (+ -) y X las posiciones a mover.";
					break;
				
				default: //Opción default por si nos introducen algun argumento ilegal
					resultadoConsulta="La opción: "+args[3]+" no está reconocida, prueba con: --whereis --what --size --howmany --goal --move";
			}
		}
		else //Si no es una cadena válida 
			resultadoConsulta="La cadena introducida no es válida, compruebala con el comando: verify -s <cadena>";
		
		return resultadoConsulta;
	}
	
	/*Metodo para simplificar la búsqueda de vehículos (se repite mucho a lo largo del código)
	 * devuelve la posición del vehículo en la lista de vehículos de un tablero, de tal forma que si es cualquier valor excepto -1 
	 * dicho coche existe y a mayores sabemos que posición ocupa en dicha lista
	 */
	public int buscarVehiculo(char vehiculoBuscar, Tablero tableroBuscar) {
		int encontrado=-1; //Devuelve -1 si no lo encuentra
		for(int i=0;i<tableroBuscar.getVehiculos().size();i++) { //Esto es lo mismo que en cualquiera de los métodos anteriores, recorremos la lista de coches hasta encontrarlo
			if(tableroBuscar.getVehiculos().get(i).getNombre()==vehiculoBuscar	) {
				encontrado=i; //Devuelve la posición del vehículo en la lista de vehículos
				i=tableroBuscar.getVehiculos().size(); //Salimos del bucle para eficienciar la ejecución
			}
		}
		return encontrado;
	}
	/* El metodo sucesoresTablero genera todos los posibles sucesores de un tablero 
	 * resumiendo toma un tablero, mira si es legal, en caso de serlo genera sus vehículos, les define orientación
	 * genera una cadena de sucesores vacía y posteriormente toma toda la lista de vehículos de dicho tablero y va de uno en uno
	 * sumandole hasta +/- 4 posiciones y viendo si son legales, generando así los sucesores
	 * */
	public String sucesoresTablero(String semilla) {
		String resultadoConsulta="Tablero sin sucesores posibles."; //De primeras se asigna este mensaje por si es un tablero sin sucesores
		if(verificarTablero(semilla)==0) { //Vemos si es un tablero legal
			Tablero tableroAux=new Tablero(semilla); //Creación del tablero, vehículos, cadena de sucesores vacia
			tableroAux.generarVehiculos();
			tableroAux.definirOrientacionVehiculosTablero();
			String cadenaSucesor="";
			int coste=0; //Decalaramos el coste como variable, luego se usará a lo largo dle método
			// Recorremos todos los vehículos
			for(int i=0;i<tableroAux.getVehiculos().size();i++) {
				/*Hacemos un for para las coordenadas positivas, solo llega hasta +4 porque por el propio tamaño del vehículo 4 
				 es lo máximo que podría sin salirse del tablero (suponiendo un vehículo de tamaño minimo = 2)*/
				for(int j=1;j<=4;j++) { //Positivos
					String[] cadenaPreguntarMove=new String[5]; //Generamos un array de strings para invocar a question --move ya que nos sirve
					cadenaPreguntarMove[0]="question"; //Asignamos los parametros que tendría --move 
					cadenaPreguntarMove[1]="-s";
					cadenaPreguntarMove[2]=""+tableroAux.getSemilla();
					cadenaPreguntarMove[3]="--move";
					cadenaPreguntarMove[4]=""+tableroAux.getVehiculos().get(i).getNombre()+"+"+j; //Asignamos el movimiento, en este caso positvo
					String resultadoMovimiento=preguntasTablero(cadenaPreguntarMove); //Realizamos la consulta y guardamos el resultado
					/*Hacemos un verify del resultado, ¿por qué? Porque question --move nos puede devolver muchos tipos de mensaje en función
					 * del error, pero sabemos que si se hace el movimiento correctamente devuelve el string con la cadena del tablero 
					 * actualizada tras el movimiento, por lo tanto si le aplicamos un verify y es válido sabemos que --move ha funcionado*/
					if(verificarTablero(resultadoMovimiento)==0) {
						coste=6-j; //Calculamos el coste del movimiento
						cadenaSucesor="["+tableroAux.getVehiculos().get(i).getNombre()+"+"+j+","+resultadoMovimiento+","+coste+"]";
						tableroAux.addSucesor(cadenaSucesor); //Generamos el sucesor con el formato especificado y lo guardamos en el tablero
					}
					else { //Si el movimiento ha sido ilegal nos salimos del bucle de positivos porque no tiene sentido seguir avanzando ya que sabemos que no se podrá
						j=tableroAux.getMatriz().length+1;
					}
				}
				//Aplicamos exactamente lo mismo pero para los movimientos negativos
				for(int j=1;j<=4;j++) { //Negativos
					String[] cadenaPreguntarMove=new String[5];
					cadenaPreguntarMove[0]="question";
					cadenaPreguntarMove[1]="-s";
					cadenaPreguntarMove[2]=""+tableroAux.getSemilla();
					cadenaPreguntarMove[3]="--move";
					cadenaPreguntarMove[4]=""+tableroAux.getVehiculos().get(i).getNombre()+"-"+j;	
					String resultadoMovimiento=preguntasTablero(cadenaPreguntarMove);
					if(verificarTablero(resultadoMovimiento)==0) {
						coste=6-j;
						cadenaSucesor="["+tableroAux.getVehiculos().get(i).getNombre()+"-"+j+","+resultadoMovimiento+","+coste+"]";
						tableroAux.addSucesor(cadenaSucesor);
					}
					else {
						j=tableroAux.getMatriz().length+1;
					}
				}
			}
			resultadoConsulta=tableroAux.getSucesoresJuntos();
		}
		else //Mensaje de error por si la consulta es invalida
			resultadoConsulta="La cadena introducida no es válida, compruebala con el comando: verify -s <cadena>";
		return resultadoConsulta;
	}
}

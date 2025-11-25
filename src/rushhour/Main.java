package rushhour; //v0.4.1

/* Descripción: 
 * 	Clase principal del programa
 * 
 * Funciones:
 * 	Realiza las llamadas a los metodos necesarias para el correcto funcionamiento del programa
 * 
 * */
public class Main {
	public static void main(String[] args) {
		//Comprobación de argumentos (empiezan después del rushhour.jar)
		if(args.length>=2) { //Por ahora todos nuestros comandos han de tener al menos dos parametros siendo: <comando> y -s
			ControlTablero tablero = new ControlTablero(); //Creamos una instancia de la clase controlTablero sobre la que trabajar
			ControlVisual visual = new ControlVisual();
			boolean activarVisual=false;//El visual solo se mostrará si lo activamos como última opción con el fin de no romper los tests
			if(args[args.length-1].equalsIgnoreCase("--visual"))
				activarVisual=true;
				
			//////
			////// VERIFY
			//////
			if(args[0].equals("verify")&&args[1].equals("-s")&&args.length>2) { //Primero comprobamos si nos han metido el comando verify
				int verify=tablero.verificarTablero(args[2]); //Si tiene verify -s <parametro> ya comprueba el resto en la función de crearTablero
				if(verify!=-1) { //Si da un retorno que no sea el -1 muestra los errores pedidos en el campus
					System.out.println(verify);
					if(activarVisual) //Solo si hemos marcado la opción se verá
						visual.crearVisual(verify+"", args);
				}
				else //Si es el error -1 muestra el siguiente mensaje (se hace así porque este error no está indicado en el campus)
					System.out.println("Tablero correcto, pero el vehículo rojo (A) no es un coche (longitud 2).");
			}
			//////
			////// QUESTION
			//////
			else if(args[0].equals("question")&&args[1].equals("-s")&&args.length>3) { //Si no es la opción verify vemos si es question
				String resultadoConsulta=tablero.preguntasTablero(args);
				System.out.println(resultadoConsulta); //Lo ejecutamos si lo es
				if(activarVisual)
					visual.crearVisual(resultadoConsulta, args);
			}
			//////
			////// SUCCESSORS
			//////
			else if(args[0].equals("successors")&&args[1].equals("-s")) {//Vemos si es successors y si lo es lo ejecuta
				String resultadoConsulta=tablero.sucesoresTablero(args[2]);
				System.out.println(resultadoConsulta);
				if(activarVisual)
					visual.crearVisual(resultadoConsulta, args);
			}
			
			//////
			////// SOLVER
			//////
			else if(args[0].equals("solver")&&args[1].equals("-s")) {
				Solver solucionador = new Solver();
				String resultadoConsulta=solucionador.realizarBusqueda(args);
				System.out.println(resultadoConsulta);
				if(activarVisual)
					visual.crearVisual(resultadoConsulta, args);
			}
			
			//////
			////// ERRORES
			//////
			else //Si ninguna de las opciones es correcta mostramos un error 
				System.out.println("Error en los parámetros, no se reconoce el comando: "+args[0]+" usa java -jar rushhour.jar --help para mirar los comandos.");
		}
		else if(args.length>0) {
			if(args[0].equalsIgnoreCase("--help")||args[0].equalsIgnoreCase("-help")||args[0].equalsIgnoreCase("help")||args[0].equalsIgnoreCase("--man")||args[0].equalsIgnoreCase("-man")||args[0].equalsIgnoreCase("man")) {
				System.out.println(cadenaManual()); //El manual va en un método a parte para no saturar el menú
			}
		}
		else //Mostramos error también si no hay parametros suficientes
			System.out.println("Error en los parámetros, número de parámetros insuficiente usa java -jar rushhour.jar --help para mirar los parametros.");
	}
	
	/* cadenaManual devuelve un manual de ayuda al usuario,
	 * se ha decidido implementar por la gran cantidad de comandos a probar y para
	 * hacer al usuario consciente de la existencia de la opción del visual,
	 * limitada ahora a una activación por línea de comandos para evitar conflictos con los tests automáticos 
	 * */
	public static String cadenaManual() {
		String manual="\n";
		manual=manual+"--------------- BIENVENIDO AL MANUAL DE AYUDA ---------------\n";
		manual=manual+"\n";
		manual=manual+"~~~~~~~~~ ¿QUÉ ES RUSHHOUR? ~~~~~~~~~\n";
		manual=manual+"Rushhour es un rompecabezas de vloques deslizantes donde el objetivo es liberar de un atasco al coche rojo.\n";
		manual=manual+"El juego consiste en un tablero 6x6 donde hay mínimo un coche rojo, en el tablero puede haber otros vehículos.\n";
		manual=manual+"El resto de vehículos pueden ser coches (de tamaño 2) o camiones (de tamaño 3), pueden estar colocados vertical u horizontalmente.\n";
		manual=manual+"El coche rojo debe ir siempre colocado en la tercera fila y estar orientado horizontalmente, pues al final de esta fila está la salida.\n";
		manual=manual+"\n";
		manual=manual+"\n";
		manual=manual+"~~~~~~~~~ REPRESENTACIÓN DE LOS NIVELES EN EL PROGRAMA ~~~~~~~~~\n";
		manual=manual+"Ya que los niveles se representan como cadenas de caractéres, al ser un tablero 6x6 las cadenas deben de ser de 36 caracteres.\n";
		manual=manual+"A lo largo del programa a estas cadenas se les llama también semillas, en referencia a las seeds de los algortimos procedurales.\n";
		manual=manual+"La cadena es el tablero ''desdoblado'' y debe cumplir con las reglas del juego, es decir coche rojo en posición correcta,\n";
		manual=manual+"sin vehículos duplicados, solo coches o camiones (nada de vehículos de tamaño 1 o mayor a 3), etc.\n";
		manual=manual+"Los coches se representarán como letras en mayúsculas, mientras que las casillas en blanco con la letra o minúscila.\n";
		manual=manual+"No se aceptan otros carecteres que no sean esos. Un ejemplo de tablero convertido a semilla es el siguiente:\n";
		manual=manual+"BCCoDo\n";
		manual=manual+"BEFoDG\n";
		manual=manual+"BEFAAG\n";
		manual=manual+"HHHIoG\n";
		manual=manual+"ooJIKK\n";
		manual=manual+"LLJMMo\n";
		manual=manual+"Dando la cadena resultante: BCCoDoBEFoDGBEFAAGHHHIoGooJIKKLLJMMo\n";
		manual=manual+"\n";
		manual=manual+"\n";
		manual=manual+"~~~~~~~~~ COMANDOS Y EJECUCIÓN DEL PROGRAMA ~~~~~~~~~\n";
		manual=manual+"El programa se ejecuta en consola con tal que así: java -jar rushhour.jar <comando> -s <semilla> [<parametros>] [--visual]\n";
		manual=manual+"Donde comando es uno de los siguientes comandos que se verán acontinuación, semilla el string de un tablero válido,\n";
		manual=manual+"parametros los parametros de dicho comando y visual una GUI para ver el tablero, los inputs en corchetes son opcionales.\n";
		manual=manual+"\n";
		manual=manual+"------LISTA COMPLETA DE COMANDOS------\n";
		manual=manual+"*Nota si no se indican parametros el comando no dispone de ellos, a excepción de --visual que se puede usar con todos.\n";
		manual=manual+"--help\n";
		manual=manual+"Función: Muestra el manual, se puede activar con otras opciones como man, -help, --MAN, etc.\n";
		manual=manual+"\n";
		manual=manual+"verify\n";
		manual=manual+"Función: verifica si la cadena para representar un nivel es válida o no.\n";
		manual=manual+"\n";
		manual=manual+"question\n";
		manual=manual+"Función: sirve para realizar preguntas sobre el tablero.\n";
		manual=manual+"Parametros (solo se puede uno a la vez): \n";
		manual=manual+"\t--whereis <vehículo>\n";
		manual=manual+"\tFunción: da las coordenadas de un vehículo determinado, ejemplo: --whereis A\n";
		manual=manual+"\n";
		manual=manual+"\t--what <f,c>\n";
		manual=manual+"\tFunción: da el contenido de una celda dada su fila y columna, ejemplo: --what 5,3\n";
		manual=manual+"\n";
		manual=manual+"\t--size <vehículo>\n";
		manual=manual+"\tFunción: da el tamaño de un vehículo determinado, ejemplo: --size A\n";
		manual=manual+"\n";
		manual=manual+"\t--howmany\n";
		manual=manual+"\tFunción: da el número de vehículo del tablero introducirdo, ejemplo: --howmany\n";
		manual=manual+"\n";
		manual=manual+"\t--goal\n";
		manual=manual+"\tFunción: da si el tablero introducido está en un estado resuelto (TRUE) o no (FALSE), ejemplo: --goal\n";
		manual=manual+"\n";
		manual=manual+"\t--move <movimiento>,[<movimiento>]\n";
		manual=manual+"\tFunción: da el estado final tras realizar uno o varios movimientos sobre dicho tablero, ejemplo: --move A+1 ~ ejemplo 2: --move A-1,A-1\n";
		manual=manual+"\n";
		manual=manual+"successors\n";
		manual=manual+"Función: muestra todos los posibles movimientos para un tablero determinado, junto a su coste,\n";
		manual=manual+"se calcula como: seis menos nº casillas movidas. Ejemplo de uso: successors -s <cadena>\n";
		manual=manual+"\n";
		manual=manual+"solver\n";
		manual=manual+"Función: resuelve automáticamente un tablero en función de la búsqueda que estemos realizando, ejemplo: solver -s <cadena> --strategy <ESTRATEGIA>\n";
		manual=manual+"Parametros: \n";
		manual=manual+"\t--strategy BFS\n";
		manual=manual+"\tFunción: resuelve el tablero por búsqueda en anchura, ejemplo: solver -s <cadena> --strategy BFS\n";
		manual=manual+"\n";
		manual=manual+"\t--strategy DFS --depth <profundidadMaxima>\n";
		manual=manual+"\tFunción: resuelve el tablero por búsqueda en profundidad dada una profundidad máxima, ejemplo: solver -s <cadena> --strategy DFS --depth 10\n";
		manual=manual+"\n";
		manual=manual+"\t--strategy UCS\n";
		manual=manual+"\tFunción: resuelve el tablero por búsqueda de coste uniforme, ejemplo: solver -s <cadena> --strategy UCS\n";
		manual=manual+"\n";
		manual=manual+"\t--strategy GBF --heuristic <heurísticaValida>\n";
		manual=manual+"\tFunción: resuelve el tablero por búsqueda con heurística voraz, ejemplo: solver -s <cadena> --strategy GBF --heuristic 0\n";
		manual=manual+"\n";
		manual=manual+"\t--strategy AStar --heuristic <heuristicaValida>\n";
		manual=manual+"\tFunción: resuelve el tablero por búsqueda con heurística de A-Estrella, ejemplo: solver -s <cadena> --strategy AStar --heuristic 0\n";
		manual=manual+"\n";
		manual=manual+"\tLa heurística debe tener los valores 0, 1 o 2 se calcula de la siguiente forma:\n";
		manual=manual+"\t0: La heurística es igual al nº de casillas a la derecha del coche rojo.\n";
		manual=manual+"\t1: La heurística es igual al nº de vehículos a la derecha del coche rojo.\n";
		manual=manual+"\t2: Se aplica la heurística 0 y 1, siendo el valor total la suma de ambas.\n";
		manual=manual+"\n";
		manual=manual+"\tParametros adicionales: \n";
		manual=manual+"\t\t--stats\n";
		manual=manual+"\t\tFunción: indica las estadísticas de la búsqueda, se puede usar con cualquier búsqueda, ejemplo: solver -s <cadena> --strategy BFS --stats\n";
		manual=manual+"\n";
		manual=manual+"--visual\n";
		manual=manual+"Función: muestra una GUI visual dependiendo del comando usado, este comando se añade de forma opcional al final de cualquier otro comando.\n";
		manual=manual+"Algunos ejemplos: verify -s <cadena> --visual, successors -s <cadena> --visual, solver -s <cadena> --strategy UCS --visual\n";
		manual=manual+"Este comando SIEMPRE debe ir al final de toda la línea de comandos, en caso contrario no se reconocerá.";
		manual=manual+"\n";
		return manual;
	}
}
package rushhour;

import java.util.*;

/* Descripción: 
 * 	La clase Solver se encarga de realizar el algoritmo de búsqueda que intenta encontrar una solución a los niveles de rushhour
 * 
 * Atributos: 
 * 	control: instancia de la clase ControlTablero que se usa para generar sucesores, entre otros
 * 
 * Funciones:
 * 	Constructor
 * 	realizarBusqueda: es el menú de esta clase, sirve para pasarle los parametros y que decida que estartegia buscar, si activar stats entre otros
 * 	resolucion: es el algoritmo de búsqueda como tal, es universal para todos los algoritmos de búsqueda
 * 	devolverSolucion: devuelve el camino solución, busca desde el nodo solución al nodo inicial y lo organiza para dar la salida esperada
 * 	generarNodoConEstrategia: genera un nodo con un valor específico en función de la búsqueda que estemos realizando
 * 	calcularHeuristica: realiza el cálculo para el valor de la heurística en función de la función heuristica seleccionada
 * */
public class Solver {
	private ControlTablero control = new ControlTablero();
	
	public Solver() { //Contructor vacío
	}
	
	/* realizarBusqueda nos sirve de menú, toma todos los args y gestiona errores que pueda haber en ellos y se encarga de organizar la busqueda adecuada, activar stats, etc
	 * Parametros: args: argumentos que nos hayan pasado desde la línea del cmd
	 * Retorno: un string con o la solución o un mensaje de error correspondiente
	 * */	
	public String realizarBusqueda(String[] args){ //Dados unos args elegimos una búsqueda u otra
		String resultado=""; //Cadena que se devolverá
		boolean activarStats=false; //boolean que nos determina si han marcado la opción --stats o no
		String estrategia="No_Valida"; //Guardamos la estrategia como un string 
		int profundidadMaxima=-1; //Inicializadas a -1 como simbolo de que no tenemos un valor válido por parte del usuario
		int heuristica=-1; //
		boolean argsInvalidos=false; //Declaramos esta variable por si nos encontramos con argumentos invalidos
		boolean argsProfValidaDFS=true; //Variable para inficar si tenemos una profundidad invalida para DFS
		boolean argsHeuristicaValida=true; //Variable para inficar si tenemos una heurística inválida para búsquedas informadas
		int verificarTableroInicial=control.verificarTablero(args[2]);  //Pero primero vemos si nos han pasado un tablero legal
		if(verificarTableroInicial==0&&args.length>4) { //Si lo es y tenemos los suficientes argumentos para que pueda estar --strategy y --<ESTRATEGIA> los buscamos
			for(int i=3;i<args.length;i++) { //Empezamos en 3 porque del 0 al 2 son bien conocidos, mientras que el resto puede ir en caulquier orden
				if(args[i].equals("--strategy")&&args.length>i) { //Buscamos strategy
					if(estrategia.equals("No_Valida")) { //Si no hemos encontrado ninguna estrategia
						//Miramos si es alguna de las válidas o si no lo es
						if(args[i+1].equals("BFS")||args[i+1].equals("bfs")||args[i+1].equals("DFS")||args[i+1].equals("dfs")||args[i+1].equals("UCS")||args[i+1].equals("ucs")||args[i+1].equals("GBF")||args[i+1].equals("gbf")||args[i+1].equals("AStar")||args[i+1].equals("ASTAR")) {
							estrategia=args[i+1]; //Si lo es la guardamos
						}
						else { //En caso contrario de que sea una estrategia no reconocida, 
							resultado="Estrategia: "+args[i+1]+" no permitida, prueba con: BFS, DFS, UCS, GBF o AStar";
							argsInvalidos=true;
						}
					}
					else { //Si nos han metido varias estrategias mejor damos un mensaje de error para evitar conflictos
						resultado="Conflicto en la estrategia, dos estrategias introducidas: Estrategia 1="+estrategia+" ~ Estrategia 2="+args[i+1];
						argsInvalidos=true;
					}
				}
				else if(args[i].equals("--depth")&&args.length>i) { //Buscamos depth
					if(profundidadMaxima==-1) { //Si tenemos una profundidad exacta de -1 (valor por defecto, nada introducido)
						try {
							profundidadMaxima=Integer.parseInt(args[i+1]); //Intentamos coger el posible valor de profundidad
							if(profundidadMaxima<0) { //Si es negativo damos error
								resultado="La profundidad introducida es inválida, no puede ser un número negativo.";
								argsProfValidaDFS=false;
							}
						}
						catch (NumberFormatException e){ //Excepción por si nos meten algo que no sea un número
							resultado="Valor incorrecto para --depth, asegurese de que sea: --depth <profundidadComoNumero>";
							argsProfValidaDFS=false;
						}
					}
					else { //Mensaje especial de error por si nos meten varias profundidades
						resultado="Conflicto en la profundidad, dos profundidades introducidas: Profundidad 1="+profundidadMaxima+" ~ Profundidad 2="+args[i+1];
						argsProfValidaDFS=false;
					}
				}
				else if(args[i].equals("--heuristic")&&args.length>i) { //Buscamos heuristic
					if(heuristica==-1) { //Si tenemos una profundidad exacta de -1 (valor por defecto, nada introducido)
						try {
							heuristica=Integer.parseInt(args[i+1]); //Intentamos coger el posible valor de heuristica
							if(heuristica<0||heuristica>2) { //Si es negativo damos error
								resultado="La heuristica introducida es inválida, solo puede ser un valor entre: 0, 1 y 2";
								argsHeuristicaValida=false;
							}
						}
						catch (NumberFormatException e){ //Excepción por si nos meten algo que no sea un número
							resultado="Valor incorrecto para --heuristic, asegurese de que sea: --heuristic <heuristicaComoNumero>";
							argsHeuristicaValida=false;
						}
					}
					else { //Mensaje especial de error por si nos meten varias heuristicas
						resultado="Conflicto en la profundidad, dos profundidades introducidas: Profundidad 1="+profundidadMaxima+" ~ Profundidad 2="+args[i+1];
						argsHeuristicaValida=false;
					}
				}
				else if(args[i].equals("--stats")) { //Buscamos stats
					activarStats=true;
				} //No hay else, si hay parametros paja, los ignoramos
			}
			
			if(argsInvalidos==false&&!estrategia.equals("No_Valida")) { //Si no nos han metido mal los args y tenemos una estrategia valida
				estrategia=estrategia.toLowerCase(); //Ya que permitimos que la estrategia vaya en minusculas lo pasamos todo a minusculas para hacer más sencilla las lógicas
				//Nos aseguramos también que para DFS tengamos bien la profundidad correcta
				if(((estrategia.equals("dfs"))&&argsProfValidaDFS&&profundidadMaxima>-1)||((estrategia.equals("gbf")||estrategia.equals("astar"))&&argsHeuristicaValida&&(heuristica>=0&&heuristica<=2))||(!estrategia.equals("dfs")||!estrategia.equals("gbf")||!estrategia.equals("astar"))) { //Si todo esta correcto realizamos la bysqueda
					resultado=resolucion(args[2], activarStats, estrategia, profundidadMaxima, heuristica);
				}
				else if(resultado.equals("")&&estrategia.equals("dfs")) //Si no tenemos mensaje de error y tenemos DFS todavía en este punto es que no nos han metido profundidad
					resultado="Parametros insuficientes para --strategy DFS, se debe incluir la profundidad como: --strategy DFS --depth <valor-profundidad> [--stats]";
				else if(resultado.equals("")&&(estrategia.equals("gbf")||estrategia.equals("astar"))) //Si no tenemos mensaje de error y tenemos voraz o A* todavía en este punto es que no nos han metido heuristica
					resultado="Parametros insuficientes para --strategy DFS, se debe incluir la profundidad como: --strategy DFS --depth <valor-profundidad> [--stats]";
			}	
			else if(resultado.equals("")) //Si no tenemos mensaje de error todavía en este punto damos un mensaje genérico
				resultado="Parametros incorrectos, el formato de solver es: solver -s <semilla> --strategy <ESTRATEGIA> [--stats]";
		}
		else //Mensaje de error por si nos han metido mal la cedena del tablero
			resultado="La cadena introducida (semilla) no es válida, compruebala con el comando: verify -s <cadena>";
		return resultado;
	}

	/* resolución hace el algoritmo de búsqueda para encontrar nuestra solución, el algoritmo es genérico por lo que sirve para cualquier estrategia
	 * Parametros-> 
	 * semilla: es el string inicial que representa nuestro primer tablero y por tanto nodo, activarStats: boolean que nos indica si al final añadiremos las estadisticas o no
	 * estrategia: string que nos indica la estrategia a realizar, profundidadPermitida: profundidad maxima para DFS
	 * Retorno: un string con o la solución ordenada y los stats si se han activado
	 * */	
	private String resolucion(String semilla, boolean activarStats, String estrategia, int profundidadPermitida, int heuristicaUtilizar) {
		//Definicion de variables
		String solucion= "";
		boolean solucionEncontrada=false; //Variable para ver si hemos encontrado la solución
		double tiempoEjecucion=0; //Variablespara las stats
		int nodosGenerados=0;
		int nodosPodados=0;
		int nodosExpandidos=0;
		int profundidadMaxima=0;
		PriorityQueue<Nodo> frontera = new PriorityQueue<>(); //Cola con prioridad para la frontera
		ArrayList<Nodo> listaCerrada = new ArrayList<>(); //Lista para los nodos que hemos explorado
		Map<String, Integer> mejorValorPorSemilla = new HashMap<>(); //Usamos un hashmap para guardar mejores valores y hacer eficiente la poda
		//Los hashmaps guardan objetos por lo tanto no podemos usar un int convencional y debemos usar Integer
		
		//Primer nodo de todos
		Tablero tableroInicial = new Tablero(semilla); //Creamos un tablero a partir de nuestra semilla inicial
		tableroInicial.generarVehiculos(); //Lo inicializamos con todos sus vehiculos y orientaciones
		tableroInicial.definirOrientacionVehiculosTablero();
		int id=0; //Declaramos id (será usado más adelante), creamos el nodo y lo añadimos a la frontera
		int heuristica=0;
		if(estrategia.equals("gbf")||estrategia.equals("astar")) //Solo aplicamos la heuristica si usamos una búsqueda informada
			heuristica=calcularHeuristica(heuristicaUtilizar, tableroInicial);
		frontera.add(generarNodoConEstrategia(estrategia, id,"none", "___",tableroInicial,0,0,heuristica)); //Sin pasarle el valor, lo resuelve el metodo generarNodoConEstrategia
		nodosGenerados++; //Incrementamos las stats de los nodosGenerados
		
		Nodo nodoSolucion=new Nodo();
		double tiempoInicio = System.nanoTime(); //El tiempo que tarda el algoritmo se toma antes del bucle según las instrucciones
		while(solucionEncontrada==false&&frontera.size()>0) { //El búcle se realiza mientras no haya solución y sigamos teniendo nodos para analizar
			Nodo nodoAnalizar = frontera.poll(); //Toma el primer nodo de la frontera (al ser cola con prioridad irán en el orden correspondiente de cada tipo de búsqueda)
			boolean podar = false; //variable para ver si podamos, inicializada a false (asumimos que no podamos)
			String semillaNodo = nodoAnalizar.getEstado().getSemilla();  //Tomamos la semilla del nodo actual
			int valorNodo = Math.abs(nodoAnalizar.getValor()); //Tomamos su valor en valor absoluto porque para DFS está en negativo
			Integer mejorValor = mejorValorPorSemilla.get(semillaNodo); //Tomamos el valor guardado a la semilla, ese valor que guardamos será el mejor 
			if (mejorValor != null) { //Aquí también la importancia de que fuera Integer y no int, necesitamos ver si ya hemos visto esa semilla o no
			    if (valorNodo >= mejorValor) //Si el valor del nodo es mayor o igual, lo podamos, es peor
			        podar = true;
			    else //En caso de ser mejor lo añadimos al hashmap
			        mejorValorPorSemilla.put(semillaNodo, valorNodo);
			} 
			else //Si no existe ese nodo en el hashmap lo añade
			    mejorValorPorSemilla.put(semillaNodo, valorNodo);

			if(!podar) { //Si no hemos podado el nodo
				Tablero tableroNodo = nodoAnalizar.getEstado(); //Creamos un tablero a partir del nodo
				//VER SI ES SOLUCION
				String[] goal = new String[4]; //Gestionamos los parametros para ver si es objetivos
				goal[0]="question";
				goal[1]="-s";
				goal[2]=tableroNodo.getSemilla();
				goal[3]="--goal";
				if(control.preguntasTablero(goal).equals("TRUE")) { //Aprovechamos que tenemos creado goal en question, así que lo invocamos y miramos si es TRUE
					solucionEncontrada=true; //Si lo es marcamos la solución como encontrada
					nodoSolucion=nodoAnalizar; //Lo añadimos a la lista cerrada para luego poder realizar el camino hasta el nodo inicial
				}
				else if(estrategia.equals("dfs")&&nodoAnalizar.getProfundidad()>=profundidadPermitida) { //Si la estrategia es dfs vemos si podamos por produndidad
					nodosPodados++; //Si lo hacemos actualizamos el contador
				}
				else { //Si no es objetivo ni se ha podado procedemos a expandir
					listaCerrada.add(nodoAnalizar); //Añadimos el nodo a la lista cerrada (donde están los nodos explorados)
					nodosExpandidos++; //Actualizamos el contado de expandidos
					String sucesores = control.sucesoresTablero(tableroNodo.getSemilla()); //generamos los sucesores ayudandonos del metodo creado para eso
					String[] separados = sucesores.split("\n"); //separamos la cadena (recordemos que successors nos devuelve una sola cadena con todos los sucesores)
					for(int i=0;i<separados.length;i++) { //Para cada elemento (sucesor)
						separados[i]=separados[i].substring(1, separados[i].length() - 1); //Quitamos los corchetes
						String[] sucesoresSeparados=separados[i].split(","); //Separamos los elementos (Movimiento, cadena resultante, coste)
						Tablero tableroSucesor = new Tablero(sucesoresSeparados[1]); //Generamos un tablero con la cadena resultante
						tableroSucesor.generarVehiculos(); //Definimos sus vehiculos y orientaciones
						tableroSucesor.definirOrientacionVehiculosTablero();
						heuristica=0; //Para la heuristica por ahora es 0, en un futuro llamará a una función que le asigne un valor
						if(estrategia.equals("gbf")||estrategia.equals("astar")) //Solo aplicamos la heuristica si usamos una búsqueda informada
							heuristica=calcularHeuristica(heuristicaUtilizar, tableroSucesor);
						id++; //Actualizamos el id
						if(nodoAnalizar.getProfundidad()+1>profundidadMaxima) //Y actualizamos la profundidad maxima alcanzada si es mayor que la guardada hasta el momento
							profundidadMaxima++;
						//Generamos el nuevo nodo en función de la estrategia con la función y lo guardamos en la frontera, se ordenará solo gracias a ser una cola con prioridad
						frontera.add(generarNodoConEstrategia(estrategia,id,nodoAnalizar.getId()+"", sucesoresSeparados[0],tableroSucesor,nodoAnalizar.getCosto()+Integer.parseInt(sucesoresSeparados[2]),nodoAnalizar.getProfundidad()+1,heuristica));
						nodosGenerados++; //Actualizamos el número de nodos generados
					}
				}
			}
			else
				nodosPodados++; //En caso de que podemos actualizamos el contador 
		}
		tiempoEjecucion = (System.nanoTime()-tiempoInicio)/1000; //Finalizamos el conteo de tiempo y dividimos entre 1000 para que sea microsegundos
		if(solucionEncontrada) { //Si hemos encontrado una solución
			solucion=devolverSolucion(listaCerrada, nodoSolucion); //Llamamos a devolver solución para que nos busque el camino y lo organice
			if(activarStats) { //Y si ha activado las stats las añadimos al final
				solucion=solucion+"\n"+"ET: "+tiempoEjecucion+"\n"+"TN: "+nodosGenerados+"\n"+"EN: "+nodosExpandidos+"\n"+"CN: "+nodosPodados+"\n"+"DF: "+profundidadMaxima+"\n";
			}
		}
		//En caso de no tener solución damos un mensaje de advertencia
		else if(!solucionEncontrada) 
			solucion="El tablero no tiene solución para la profundidad: "+profundidadPermitida; //Mensaje especial para profundidad para que pruebe otros valores
		else 
			solucion="El tablero no tiene solución.";
		return solucion;
	}
	
	/* devolverSolucion nos devuelve de forma ordenada el camino de la solución, para conseguirlo toma el último nodo (solución)
	 * toma su padre y realiza un for de atrás a adelante buscando nodos cuyo padre coincida (actualizando los padres) hasta terminar el for
	 * Parametros: listaCerrada que contiene de forma asegurada: el nodo inicial, nodos intermedios cuya exploración daba en una potencial solución, 
	 * 			   nodoSolucion es el nodo solución de nuestro tablero
	 * Retorno: un string con todo los ndososolucion
	 * */	
	private String devolverSolucion(ArrayList<Nodo> listaCerrada, Nodo nodoSolucion) {
		String solucion= ""; //Solución vacia 
		solucion=nodoSolucion.toString()+"\n"; //Tomamos el último nodo y lo ponemos en la solución con un salto de línea
		String padre=nodoSolucion.getPadre(); //Variable para guardar los padre
		for(int i=0;i<listaCerrada.size();i++) { //Recorremos toda la lista cerrada 
			int posicionAnalizar = (listaCerrada.size()-1)-i; //Tomamos los nodos de atrás a adelante para poder así encontrar a su padre (su padre estará en la lista antes que él)
			Nodo nodoAnalizar = listaCerrada.get(posicionAnalizar); //Tomamos el nodo en la posición correcta
			if(!padre.equals("none")) { //Si el padre que estamos buscando no es none (nos evita buscar un padre inexistente al nodo inicial y eviar conflictos con parseInt)
				int idPadre=Integer.parseInt(padre); //Convertimos el padre a int
				if(nodoAnalizar.getId()==idPadre) { //Vemos si el id del nodo coincide con el del padre
					padre=nodoAnalizar.getPadre(); //Actualizamos el padre
					solucion=solucion+nodoAnalizar.toString()+"\n"; //Añadimos el nodo a la solucion con un salto de línea
				}
			}
		}
		String[] invertir=solucion.split("\n"); //Separamos todos los nodos solución que hemos metido, por saltos de línea (de ahí que los hayamos puesto)
		solucion=""; //Reiniciamos solución
		for(int i=0;i<invertir.length;i++) { //Recorremos todos los nodos solución
			int posicionInvertir=(invertir.length-1)-i; //Y los vamos tomando en orden inverso (recordemos que estarán al revés por como hemos buscado padres)
			solucion=solucion+invertir[posicionInvertir]+"\n"; //Concatenamos la solución y metemos salto de línea (el split los borra)
		}
		return solucion;
	}
	
	/* calcularHeuristica se encarga dde calcular el valor de la heurística para un nodo, en función de la función heuristica seleccionada:
	 * FUNCIONES:
	 * 0: cantidad de casillas a la derecha del coche rojo
	 * 1: cantidad de vehículos a la derecha del coche rojo
	 * 2: suma de las heurísticas 1 y 2
	 * Primero el método toma la posición más a la derecha del coche rojo, es útil para ambos métodos
	 * Y si es heurística 0 o 2 resta 5 - esa posición dando así el nº de casillas a la derecha
	 * Y si es heurística 1 o 2 aprovecha la función what implementada en el hito 1 para ver que hay en la casilla de la derecha
	 * si es un espacio en blanco "o" lo ignora y si es un vehículo (no "o") suma 1, no tiene en cuenta duplicados porque si hay
	 * un duplicado es porque tenemos un coche en horizontal en la fila del coche rojo y a su derecha y por tanto el tablero
	 * es irresoluble dando igual la heuristica
	 * */
	private int calcularHeuristica(int heuristicaUtilizar, Tablero tableroAnalizar) {
		int heuristica=0;
		//Nos interesa saber para ambas heuristicas donde "termina el coche A"
		int posicionCoheAenLista=control.buscarVehiculo('A', tableroAnalizar); //Tomamos la posición en la lista del coche A
		ArrayList<int[]> coordenadasCocheA=tableroAnalizar.getVehiculos().get(posicionCoheAenLista).getCoordenadas(); //Tomamos sus coordenadas
		int columna=coordenadasCocheA.get(1)[1]; //Tomamos la columna del extremo de más a la derecha
		
		if(heuristicaUtilizar==0||heuristicaUtilizar==2) //Si la heurística es 0 o 2
			heuristica=heuristica+(5-columna); //Vemos a cuanto está de la salida
		if(heuristicaUtilizar==1||heuristicaUtilizar==2) { //Si la heurística es 1 o 2, if separados para que si es 2 la heurisitca a utilizar acceda a ambos ifs
			int empezar=columna+1; //Empezamos a buscar en la columna posterior al final del coche rojo
			String[] what = new String[5]; //Gestionamos los parametros para ver si es objetivos
			what[0]="question";
			what[1]="-s";
			what[2]=tableroAnalizar.getSemilla();
			what[3]="--what";
			for(int i=empezar;i<=5;i++) { //Vemos todas las posiciones a la derecha dl coche rojo
				what[4]="2,"+i; //Coordenadas donde buscaremos, 2 es fijo porque es la fila del coche
				if(!control.preguntasTablero(what).equals("o")) //Realizamos la consulta y miramos si no es un hueco vacío
					heuristica++; //Si lo es aumentamos el contado en uno
			}
		}
		return heuristica;
	}
	
	/* generarNodoConEstrategia nos devuelve un nodo con el valor concreto en función de la estrategia introducida
	 * Parametros: estrategia que estemos usando y todos los valores necesarios para generar un nodo
	 * Retorno: nodo generado con la estrategia correcto
	 * */
	private Nodo generarNodoConEstrategia(String estrategia, int id, String idPadre, String accion, Tablero tableroSucesor, int coste, int profundidad, int heuristica) {
		int valor=0; //Declaramos el valor
		switch (estrategia) { //Hacemos un switch con la estrategia para definir el valor
			case "bfs": //En minusculas porque lo ponemos en minusculas todo antes 
				valor=profundidad; //En BFS será la profundidad
				break;
			case "dfs":
				valor=profundidad*-1; //En DFS será -profundidad
				break;
			case "ucs":
				valor=coste;//En UCS será el coste
				break;
			case "gbf":
				valor=heuristica; //En voraz será la heurística directamente
				break;
			case "astar":
				valor=heuristica+coste; //En a-estrella será la suma del coste y la heurística
				break;
		}
		Nodo nodoGenerado = new Nodo(id,idPadre, accion, tableroSucesor, coste, profundidad, heuristica, valor); //Generamos el nodo y lo devolvemos
		return nodoGenerado;
	}
}
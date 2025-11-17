package rushhour;

import java.util.*; //

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
 * 	
 * 
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
		int profundidadMaxima=-1; //Inicializada a -1 como simbolo de que no tenemos un valor válido por parte del usuario
		boolean argsInvalidos=false; //Declaramos esta variable por si nos encontramos con argumentos invalidos
		boolean argsProfValidaDFS=true; //Variable para inficar si tenemos una profundidad invalida para DFS
		int verificarTableroInicial=control.verificarTablero(args[2]);  //Pero primero vemos si nos han pasado un tablero legal
		if(verificarTableroInicial==0&&args.length>4) { //Si lo es y tenemos los suficientes argumentos para que pueda estar --strategy y --<ESTRATEGIA> los buscamos
			for(int i=3;i<args.length;i++) { //Empezamos en 3 porque del 0 al 2 son bien conocidos, mientras que el resto puede ir en caulquier orden
				if(args[i].equals("--strategy")&&args.length>i) { //Buscamos strategy
					if(estrategia.equals("No_Valida")) { //Si no hemos encontrado ninguna estrategia
						//Miramos si es alguna de las válidas o si no lo es
						if(args[i+1].equals("BFS")||args[i+1].equals("bfs")||args[i+1].equals("DFS")||args[i+1].equals("dfs")||args[i+1].equals("UCS")||args[i+1].equals("ucs")) {
							estrategia=args[i+1]; //Si lo es la guardamos
						}
						else { //En caso contrario de que sea una estrategia no reconocida, 
							resultado="Estrategia: "+args[i+1]+" no permitida, prueba con: BFS, DFS o UCS";
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
				else if(args[i].equals("--stats")) { //Buscamos stats
					activarStats=true;
				} //No hay else, si hay parametros paja, los ignoramos
			}
			
			if(argsInvalidos==false&&!estrategia.equals("No_Valida")) { //Si no nos han metido mal los args y tenemos una estrategia valida
				estrategia=estrategia.toLowerCase(); //Ya que permitimos que la estrategia vaya en minusculas lo pasamos todo a minusculas para hacer más sencilla las lógicas
				//Nos aseguramos también que para DFS tengamos bien la profundidad correcta
				if(((estrategia.equals("dfs"))&&argsProfValidaDFS&&profundidadMaxima>-1)||(!estrategia.equals("dfs"))) { //Si todo esta correcto realizamos la bysqueda
					resultado=resolucion(args[2], activarStats, estrategia, profundidadMaxima);
				}
				else if(resultado.equals("")) //Si no tenemos mensaje de error todavía en este punto es que no nos han metido profundidad
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
	 * Parametros-> semilla: es el string inicial que representa nuestro primer tablero y por tanto nodo, activarStats: boolean que nos indica si al final añadiremos las estadisticas o no
	 * estrategia: string que nos indica la estrategia a realizar, profundidadPermitida: profundidad maxima para DFS
	 * Retorno: un string con o la solución ordenada y los stats si se han activado
	 * */	
	public String resolucion(String semilla, boolean activarStats, String estrategia, int profundidadPermitida) {
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
		
		//Primer nodo de todos
		Tablero tableroInicial = new Tablero(semilla); //Creamos un tablero a partir de nuestra semilla inicial
		tableroInicial.generarVehiculos(); //Lo inicializamos con todos sus vehiculos y orientaciones
		tableroInicial.definirOrientacionVehiculosTablero();
		int id=0; //Declaramos id (será usado más adelante), creamos el nodo y lo añadimos a la frontera
		frontera.add(generarNodoConEstrategia(estrategia, id,"none", "___",tableroInicial,0,0,0)); //Sin pasarle el valor, lo resuelve el metodo generarNodoConEstrategia
		nodosGenerados++; //Incrementamos las stats de los nodosGenerados
		double tiempoInicio = System.nanoTime(); //El tiempo que tarda el algoritmo se toma antes del bucle según las instrucciones
		while(solucionEncontrada==false&&frontera.size()>0) { //El búcle se realiza mientras no haya solución y sigamos teniendo nodos para analizar
			Nodo nodoAnalizar = frontera.poll(); //Toma el primer nodo de la frontera (al ser cola con prioridad irán en el orden correspondiente de cada tipo de búsqueda)
			boolean podar = false; //variable para ver si podamos, inicializada a false (asumimos que no podamos)
			for(int i=0;i<listaCerrada.size();i++) { //Para todos los elementos que hemos analizado
				//Vemos si tenemos un nodo mejor que el que estamos analizando
				if(nodoAnalizar.getEstado().getSemilla().equals(listaCerrada.get(i).getEstado().getSemilla())&&(Math.abs(nodoAnalizar.getValor())>=Math.abs(listaCerrada.get(i).getValor()))) { //Muy muy importante el valor absoluto porque dfs nos lo manda en negativo
					podar=true; //Si es peor lo podamos y salimos del búcle, no necesitamos buscar más porque con uno peor ya sabemos que hemos explorado un camino mejor
					i=listaCerrada.size()+1;
				}
				else if(nodoAnalizar.getEstado().getSemilla().equals(listaCerrada.get(i).getEstado().getSemilla())&&(Math.abs(nodoAnalizar.getValor())<Math.abs(listaCerrada.get(i).getValor())))
					listaCerrada.remove(i); //Si encuentra un nodo peor elimina el nodo peor para optimizar el bucle, sigue en el búcle porque puede que haya un nodo mejor (y haya que podar) o incluso haya más nodos peores que podamos eliminar para optimizar
				//Nos podemos permitir eliminarlos porque son nodos que no van a llegar a la solución nunca
			}
			
			if(!podar) {
				Tablero tableroNodo = nodoAnalizar.getEstado();
				//VER SI ES SOLUCION
				String[] goal = new String[4];
				goal[0]="question";
				goal[1]="-s";
				goal[2]=tableroNodo.getSemilla();
				goal[3]="--goal";
				if(control.preguntasTablero(goal).equals("TRUE")) {
					solucionEncontrada=true;
					listaCerrada.add(nodoAnalizar);
				}
				else if(estrategia.equals("dfs")&&nodoAnalizar.getProfundidad()>=profundidadPermitida) {
					nodosPodados++;
				}
				else {
					listaCerrada.add(nodoAnalizar);
					nodosExpandidos++;
					String sucesores = control.sucesoresTablero(tableroNodo.getSemilla());
					String[] separados = sucesores.split("\n");
					for(int i=0;i<separados.length;i++) {
						separados[i]=separados[i].substring(1, separados[i].length() - 1);
						String[] sucesoresSeparados=separados[i].split(",");
						Tablero tableroSucesor = new Tablero(sucesoresSeparados[1]);
						tableroSucesor.generarVehiculos();
						tableroSucesor.definirOrientacionVehiculosTablero();
						int heuristica=0;
						id++;
						if(nodoAnalizar.getProfundidad()+1>profundidadMaxima)
							profundidadMaxima++;
						frontera.add(generarNodoConEstrategia(estrategia,id,nodoAnalizar.getId()+"", sucesoresSeparados[0],tableroSucesor,nodoAnalizar.getCosto()+Integer.parseInt(sucesoresSeparados[2]),nodoAnalizar.getProfundidad()+1,heuristica));
						nodosGenerados++;
					}
				}
			}
			else
				nodosPodados++;
		}
		tiempoEjecucion = (System.nanoTime()-tiempoInicio)/1000; //Entre 1000 para que sea microsegundos
		if(solucionEncontrada) {
			solucion=devolverSolucion(listaCerrada);
			if(activarStats) {
				solucion=solucion+"\n"+"ET: "+tiempoEjecucion+"\n"+"TN: "+nodosGenerados+"\n"+"EN: "+nodosExpandidos+"\n"+"CN: "+nodosPodados+"\n"+"DF: "+profundidadMaxima+"\n";
			}
		}
		else
			solucion="El tablero no tiene solución.";
		return solucion;
	}
	
	/* devolverSolucion nos devuelve de forma ordenada el camino de la solución, para conseguirlo toma el último nodo (solución)
	 * toma su padre y realiza un for de atrás a adelante buscando nodos cuyo padre coincida (actualizando los padres) hasta terminar el for
	 * Parametros: listaCerrada que contiene de forma asegurada: el nodo inicial, nodos intermedios cuya exploración daba en una potencial solución, nodo final
	 * Retorno: un string con todo los ndososolucion
	 * */	
	public String devolverSolucion(ArrayList<Nodo> listaCerrada) {
		String solucion= ""; //Solución vacia
		Nodo nodoFinal = listaCerrada.get(listaCerrada.size()-1); //Tomamos el último nodo
		solucion=nodoFinal.toString()+"\n"; //Lo ponemos en la solución con un salto de línea
		String padre=nodoFinal.getPadre(); //Variable para guardar los padre
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
	
	/* generarNodoConEstrategia nos devuelve un nodo con el valor concreto en función de la estrategia introducida
	 * Parametros: estrategia que estemos usando y todos los valores necesarios para generar un nodo
	 * Retorno: nodo generado con la estrategia correcto
	 * */
	public Nodo generarNodoConEstrategia(String estrategia, int id, String idPadre, String accion, Tablero tableroSucesor, int coste, int profundidad, int heuristica) {
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
		}
		Nodo nodoGenerado = new Nodo(id,idPadre, accion, tableroSucesor, coste, profundidad, heuristica, valor); //Generamos el nodo y lo devolvemos
		return nodoGenerado;
	}
}
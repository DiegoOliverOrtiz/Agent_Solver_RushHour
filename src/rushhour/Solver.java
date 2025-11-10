package rushhour;

import java.util.*;
import java.io.*;

public class Solver {
	private ControlTablero control = new ControlTablero();
	public Solver() {
	}
	
	public String realizarBusqueda(String[] args){
		String resultado="";
		boolean activarStats=false;
		int verificarTableroInicial=control.verificarTablero(args[2]);
		if(verificarTableroInicial==0) {
			if(args.length>4) {
				if(args[3].equals("--strategy")) {
					switch (args[4]) {
						case "BFS":
							if (args.length>5) {
								if(args[5].equals("--stats"))
									activarStats=true;
							}
							resultado=bfs(args[2], activarStats);
							break;
						case "DFS":
							if(args.length>5) {
								if(args[5].equals("--depth")&&args.length>6) {
									try {
										int profundidad=Integer.parseInt(args[6]);
										if (args.length>7) {
											if(args[7].equals("--stats"))
												activarStats=true;
										}
										resultado=dfs(args[2], activarStats, profundidad);
									}
									catch (NumberFormatException e){
										resultado="Valor incorrecto para --depth, asegurese de que sea un número.";
									}
								}
								else
									resultado="Formato incorrecto para DFS, debe ser: solver -s <semilla> --strategy DFS --depth <valor-profundidad>";
							}
							else
								resultado="Parametros insuficientes para --strategy DFS, se debe incluir la profundidad como: --strategy DFS --depth <valor-profundidad>";
							break;
						case "UCS":
							if (args.length>5) {
								if(args[5].equals("--stats"))
									activarStats=true;
							}
							resultado=ucs(args[2], activarStats);
							break;
						default:
							resultado="Estrategia no permitida, prueba con: BFS, DFS o UCS";
					}
				}
				else
					resultado="Parametros incorrectos, el formato de solver es: solver -s <semilla> --strategy ESTRATEGIA";
			}
			else
				resultado="Parametros incorrectos, el formato de solver es: solver -s <semilla> --strategy ESTRATEGIA";
		}
		else
			resultado="La cadena introducida (semilla) no es válida, compruebala con el comando: verify -s <cadena>";
		return resultado;
	}
	public String bfs(String semilla, boolean activarStats) {
		//Definicion de variables
		String solucion= "";
		boolean solucionEncontrada=false;
		double tiempoEjecucion=0;
		int nodosGenerados=0;
		int nodosPodados=0;
		int nodosExpandidos=0;
		int profundidadMaxima=0;
		Queue<Nodo> frontera = new LinkedList<>();
		ArrayList<Nodo> listaCerrada = new ArrayList<>();
		Tablero tableroInicial = new Tablero(semilla);
		tableroInicial.generarVehiculos();
		tableroInicial.definirOrientacionVehiculosTablero();
		int id=0;
		Nodo nodoInicial=new Nodo(id,"none", "___",tableroInicial,0,0,0,0);
		frontera.add(nodoInicial);
		nodosGenerados++;
		double tiempoInicio = System.nanoTime();
		while(solucionEncontrada==false&&frontera.size()>0) {
			Nodo nodoAnalizar = frontera.poll();
			boolean podar =false;
			for(int i=0;i<listaCerrada.size();i++) {
				if(nodoAnalizar.getEstado().getSemilla().equals(listaCerrada.get(i).getEstado().getSemilla()))
					podar=true;
			}
			if(!podar) {
				Tablero tableroNodo = nodoAnalizar.getEstado();
				listaCerrada.add(nodoAnalizar);
				//VER SI ES SOLUCION
				String[] goal = new String[4];
				goal[0]="question";
				goal[1]="-s";
				goal[2]=tableroNodo.getSemilla();
				goal[3]="--goal";
				if(control.preguntasTablero(goal).equals("TRUE")) {
					solucionEncontrada=true;
				}
				else {
					nodosExpandidos++;
					String sucesores = control.sucesoresTablero(tableroNodo.getSemilla());
					String[] separados = sucesores.split("\n");
					for(int i=0;i<separados.length;i++) {
						separados[i]=separados[i].substring(1, separados[i].length() - 1);
						String[] sucesoresSeparados=separados[i].split(",");
						Tablero tableroSucesor = new Tablero(sucesoresSeparados[1]);
						tableroSucesor.generarVehiculos();
						tableroSucesor.definirOrientacionVehiculosTablero();
						int idPadre=nodoAnalizar.getId();
						int coste = nodoAnalizar.getCosto()+Integer.parseInt(sucesoresSeparados[2]);
						id++;
						if(nodoAnalizar.getProfundidad()+1>profundidadMaxima)
							profundidadMaxima++;
						Nodo nodoSucesor=new Nodo(id,idPadre+"", sucesoresSeparados[0],tableroSucesor,coste,nodoAnalizar.getProfundidad()+1,0,nodoAnalizar.getProfundidad()+1);
						frontera.add(nodoSucesor);
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
	public String dfs(String semilla, boolean activarStats, int profundidadPermitida) {
		//Definicion de variables
		String solucion= "";
		boolean solucionEncontrada=false;
		double tiempoEjecucion=0;
		int nodosGenerados=0;
		int nodosPodados=0;
		int nodosExpandidos=0;
		int profundidadMaxima=0;
		Stack<Nodo> frontera = new Stack<>();
		ArrayList<Nodo> listaCerrada = new ArrayList<>();
		Tablero tableroInicial = new Tablero(semilla);
		tableroInicial.generarVehiculos();
		tableroInicial.definirOrientacionVehiculosTablero();
		int id=0;
		Nodo nodoInicial=new Nodo(id,"none", "___",tableroInicial,0,0,0,0);
		frontera.add(nodoInicial);
		nodosGenerados++;
		double tiempoInicio = System.nanoTime();
		while(solucionEncontrada==false&&frontera.size()>0) {
			Nodo nodoAnalizar = frontera.pop();
			boolean podar =false;
			for(int i=0;i<listaCerrada.size();i++) {
				if(nodoAnalizar.getEstado().getSemilla().equals(listaCerrada.get(i).getEstado().getSemilla())&&nodoAnalizar.getValor()<=listaCerrada.get(i).getValor()) {
					podar=true;
					i=listaCerrada.size();
				}
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
				else if(nodoAnalizar.getProfundidad()>=profundidadPermitida) {
					nodosPodados++;
				}
				else {
					listaCerrada.add(nodoAnalizar);
					nodosExpandidos++;
					String sucesores = control.sucesoresTablero(tableroNodo.getSemilla());
					String[] separados = sucesores.split("\n");
					Stack<Nodo> fronteraAdd = new Stack<>();
					for(int i=0;i<separados.length;i++) {
						separados[i]=separados[i].substring(1, separados[i].length() - 1);
						String[] sucesoresSeparados=separados[i].split(",");
						Tablero tableroSucesor = new Tablero(sucesoresSeparados[1]);
						tableroSucesor.generarVehiculos();
						tableroSucesor.definirOrientacionVehiculosTablero();
						int idPadre=nodoAnalizar.getId();
						int coste = nodoAnalizar.getCosto()+Integer.parseInt(sucesoresSeparados[2]);
						id++;
						if(nodoAnalizar.getProfundidad()+1>profundidadMaxima)
							profundidadMaxima++;
						Nodo nodoSucesor=new Nodo(id,idPadre+"", sucesoresSeparados[0],tableroSucesor,coste,nodoAnalizar.getProfundidad()+1,0,(nodoAnalizar.getProfundidad()+1)*-1);
						fronteraAdd.add(nodoSucesor);
						nodosGenerados++;
					}
					while(fronteraAdd.size()>0) {
						frontera.add(fronteraAdd.pop());
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
	public String resolucion(String semilla, boolean activarStats, String estrategia, int profundidadPermitida) {
		//Definicion de variables
		String solucion= "";
		boolean solucionEncontrada=false;
		double tiempoEjecucion=0;
		int nodosGenerados=0;
		int nodosPodados=0;
		int nodosExpandidos=0;
		int profundidadMaxima=0;
		//Cola para BFS
		Queue<Nodo> fronteraBFS = new LinkedList<>();
		//Pila para DFS
		Stack<Nodo> fronteraDFS = new Stack<>();
		//Cola con prioridad para UCS y busquedas informadas
		PriorityQueue<Nodo> cola = new PriorityQueue<>();
		//Lista cerrada
		ArrayList<Nodo> listaCerrada = new ArrayList<>();
		
		//Primer nodo de todos
		Tablero tableroInicial = new Tablero(semilla);
		tableroInicial.generarVehiculos();
		tableroInicial.definirOrientacionVehiculosTablero();
		int id=0;
		Nodo nodoInicial=new Nodo(id,"none", "___",tableroInicial,0,0,0,0);
		switch(estrategia) {
			case "BFS":
				fronteraBFS.add(nodoInicial);
				break;
			case "DFS":
				fronteraDFS.add(nodoInicial);
				break;
			case "UCS":
				break;
			default:
				break;
		}
		
		nodosGenerados++;
		double tiempoInicio = System.nanoTime();
		while(solucionEncontrada==false&&frontera.size()>0) {
			Nodo nodoAnalizar = frontera.pop();
			boolean podar =false;
			for(int i=0;i<listaCerrada.size();i++) {
				if(nodoAnalizar.getEstado().getSemilla().equals(listaCerrada.get(i).getEstado().getSemilla())&&nodoAnalizar.getValor()<=listaCerrada.get(i).getValor()) {
					podar=true;
					i=listaCerrada.size();
				}
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
				else if(nodoAnalizar.getProfundidad()>=profundidadPermitida) {
					nodosPodados++;
				}
				else {
					listaCerrada.add(nodoAnalizar);
					nodosExpandidos++;
					String sucesores = control.sucesoresTablero(tableroNodo.getSemilla());
					String[] separados = sucesores.split("\n");
					Stack<Nodo> fronteraAdd = new Stack<>();
					for(int i=0;i<separados.length;i++) {
						separados[i]=separados[i].substring(1, separados[i].length() - 1);
						String[] sucesoresSeparados=separados[i].split(",");
						Tablero tableroSucesor = new Tablero(sucesoresSeparados[1]);
						tableroSucesor.generarVehiculos();
						tableroSucesor.definirOrientacionVehiculosTablero();
						int idPadre=nodoAnalizar.getId();
						int coste = nodoAnalizar.getCosto()+Integer.parseInt(sucesoresSeparados[2]);
						id++;
						if(nodoAnalizar.getProfundidad()+1>profundidadMaxima)
							profundidadMaxima++;
						Nodo nodoSucesor=new Nodo(id,idPadre+"", sucesoresSeparados[0],tableroSucesor,coste,nodoAnalizar.getProfundidad()+1,0,(nodoAnalizar.getProfundidad()+1)*-1);
						fronteraAdd.add(nodoSucesor);
						nodosGenerados++;
					}
					int tamanoAnadir=fronteraAdd.size();
					for(int i=0;i<tamanoAnadir;i++) {
						frontera.add(fronteraAdd.pop());
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
	
	public String devolverSolucion(ArrayList<Nodo> listaCerrada) {
		String solucion= "";
		Nodo nodoFinal = listaCerrada.get(listaCerrada.size()-1);
		solucion=nodoFinal.toString()+"\n";
		String padre=nodoFinal.getPadre();
		for(int i=0;i<listaCerrada.size();i++) {
			int posicionAnalizar = (listaCerrada.size()-1)-i;
			Nodo nodoAnalizar = listaCerrada.get(posicionAnalizar);
			if(!padre.equals("none")) {
				int idPadre=Integer.parseInt(padre);
				if(nodoAnalizar.getId()==idPadre) {
					padre=nodoAnalizar.getPadre();
					solucion=solucion+nodoAnalizar.toString()+"\n";
				}
			}
		}
		String[] invertir=solucion.split("\n");
		solucion="";
		for(int i=0;i<invertir.length;i++) {
			int posicionInvertir=(invertir.length-1)-i;
			solucion=solucion+invertir[posicionInvertir]+"\n";
		}
		return solucion;
	}
}

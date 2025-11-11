package rushhour;

import java.util.*;
import java.io.*;

public class Solver {
	private ControlTablero control = new ControlTablero();
	public Solver() {
	}
	
	public String realizarBusqueda(String[] args){ //Seguimos en obras ¿Dónde está tu chaleco reflectante?
		String resultado="";
		boolean activarStats=false;
		String estrategia="No_Valida";
		int profundidadMaxima=-1; //Inicializada a -1 como simbolo de que no tenemos un valor válido por parte del usuario
		boolean argsInvalidos=false;
		int verificarTableroInicial=control.verificarTablero(args[2]);
		if(verificarTableroInicial==0&&args.length>4) {
			for(int i=3;i<args.length;i++) {
				if(args[i].equals("--strategy")&&args.length>i) { //Buscamos strategy
					if(estrategia.equals("No_Valida")) {
						switch (args[i+1]) {
							case "BFS", "bfs":
								estrategia=args[i+1];
								break;
							case "DFS", "dfs":
								estrategia=args[i+1];
								break;
							case "UCS", "ucs":
								estrategia=args[i+1];
								break;
							default:
								resultado="Estrategia: "+args[i+1]+" no permitida, prueba con: BFS, DFS o UCS";
								argsInvalidos=true;
								break;
						}
					}
					else {
						resultado="Conflicto en la estrategia, dos estrategias introducidas: Estrategia 1="+estrategia+" ~ Estrategia 2="+args[i+1];
						argsInvalidos=true;
					}
				}
				else if(args[i].equals("--depth")&&args.length>i) { //Buscamos depth
					if(profundidadMaxima==-1) {
						try {
							profundidadMaxima=Integer.parseInt(args[i+1]);
							if(profundidadMaxima<0) {
								resultado="La profundidad introducida es inválida, no puede ser un número negativo.";
								argsInvalidos=true;
							}
						}
						catch (NumberFormatException e){
							resultado="Valor incorrecto para --depth, asegurese de que sea: --depth <profundidadComoNumero>";
							argsInvalidos=true;
						}
					}
					else {
						resultado="Conflicto en la profundidad, dos profundidades introducidas: Profundidad 1="+profundidadMaxima+" ~ Profundidad 2="+args[i+1];
						argsInvalidos=true;
					}
				}
				else if(args[i].equals("--stats")) { //Buscamos stats
					activarStats=true;
				} //No hay else, si hay parametros paja, los ignoramos
			}
			if(argsInvalidos==false&&!estrategia.equals("No_Valida")) {
				if((estrategia.equals("DFS")||estrategia.equals("DFS"))&&profundidadMaxima>-1||((!estrategia.equals("DFS")||!estrategia.equals("DFS")))) {
					resultado=resolucion(args[2], activarStats, estrategia, profundidadMaxima);
				}
				else
					resultado="Parametros insuficientes para --strategy DFS, se debe incluir la profundidad como: --strategy DFS --depth <valor-profundidad> [--stats]";
			}
			else
				resultado="Parametros incorrectos, el formato de solver es: solver -s <semilla> --strategy <ESTRATEGIA> [--stats]";
		}
		else
			resultado="La cadena introducida (semilla) no es válida, compruebala con el comando: verify -s <cadena>";
		return resultado;
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
		Frontera frontera = new Frontera(estrategia);
		ArrayList<Nodo> listaCerrada = new ArrayList<>();
		//Primer nodo de todos
		Tablero tableroInicial = new Tablero(semilla);
		tableroInicial.generarVehiculos();
		tableroInicial.definirOrientacionVehiculosTablero();
		int id=0;
		frontera.addNodo(id,"none", "___",tableroInicial,0,0,0); //Sin pasarle el valor, lo resurlve frontera
		if(estrategia.equals("DFS"))
			frontera.addFronteraDFS();
		nodosGenerados++;
		double tiempoInicio = System.nanoTime();
		while(solucionEncontrada==false&&frontera.getSize()>0) {
			Nodo nodoAnalizar = frontera.sacarNodo();
			boolean podar =false;
			for(int i=0;i<listaCerrada.size();i++) {
				if(nodoAnalizar.getEstado().getSemilla().equals(listaCerrada.get(i).getEstado().getSemilla())&&frontera.estrategiaPoda(nodoAnalizar.getValor(), listaCerrada.get(i).getValor())) {
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
				else if(estrategia.equals("DFS")&&nodoAnalizar.getProfundidad()>=profundidadPermitida) {
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
						int idPadre=nodoAnalizar.getId();
						int coste = nodoAnalizar.getCosto()+Integer.parseInt(sucesoresSeparados[2]);
						int heuristica=0;
						id++;
						if(nodoAnalizar.getProfundidad()+1>profundidadMaxima)
							profundidadMaxima++;
						frontera.addNodo(id,idPadre+"", sucesoresSeparados[0],tableroSucesor,coste,nodoAnalizar.getProfundidad()+1,heuristica);
						nodosGenerados++;
					}
					if(estrategia.equals("DFS"))
						frontera.addFronteraDFS();
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

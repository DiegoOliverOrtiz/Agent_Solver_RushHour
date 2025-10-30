package rushhour;

import java.util.*;

public class Solver {
	
	public Solver() {}
	
	public String bfs(String semilla) {
		//Definicion de variables
		String solucion= "";
		boolean solucionEncontrada=false;
		double tiempoEjecucion=0;
		int nodosGenerados=0;
		int nodosPodados=0;
		int nodosExpandidos=0;
		int profundidadMaxima=0;
		Queue<String> listaCerrada = new LinkedList<>();
		ArrayList<String> listaCerradaAnalizados = new ArrayList<>();
		
		Tablero tableroInicial = new Tablero(semilla);
		tableroInicial.generarVehiculos();
		tableroInicial.definirOrientacionVehiculosTablero();
		Nodo nodoInicial=new Nodo();
		
		
		solucion=devolverSolucion(listaCerradaAnalizados);
		return solucion;
	}
	public String dfs(String semilla) {
		String solucion= "";
		boolean solucionEncontrada=false;
		double tiempoEjecucion=0;
		int nodosGenerados=0;
		int nodosPodados=0;
		int nodosExpandidos=0;
		int profundidadMaxima=0;
		Stack<Integer> listaCerrada = new Stack<>();
		ArrayList<String> listaCerradaAnalizados = new ArrayList<>();
		
		solucion=devolverSolucion(listaCerradaAnalizados);
		return solucion;
	}
	public String ucs(String semilla) {
		String solucion= "";
		boolean solucionEncontrada=false;
		double tiempoEjecucion=0;
		int nodosGenerados=0;
		int nodosPodados=0;
		int nodosExpandidos=0;
		int profundidadMaxima=0;
		ArrayList<String> listaCerradaAnalizados = new ArrayList<>();
		solucion=devolverSolucion(listaCerradaAnalizados);
		return solucion;
	}
	
	public String devolverSolucion(ArrayList<String> listaCerradaAnalizados) {
		String solucion= "";
		return solucion;
	}
}

package rushhour;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/*
 * TO DO: MIRAR COMO PODEMOS IMPLEMENTAR DFS CON COLAS CON PRIORIDAD PARA VER SI ES MÁS EFICIENTE
 * EN CASO DE SERLO ELIMINAR LA CLASE FRONTERA, NO TENDRÍA SENTIDO USARLA
 */
public class Frontera {
	//Cola para BFS
	private Queue<Nodo> fronteraBFS = new LinkedList<>();
	//Pila para DFS
	private Stack<Nodo> fronteraDFS = new Stack<>();
	private Stack<Nodo> fronteraAuxDFS = new Stack<>(); //Pila auxiliar para invertir y meter en orden correcto
	//Cola con prioridad para UCS y busquedas informadas
	private PriorityQueue<Nodo> fronteraValor = new PriorityQueue<>();
	
	private String estrategia;
	
	public Frontera(String estrategia) {
		this.estrategia=estrategia;
	}
	
	public void addNodo(int id, String idPadre, String accion, Tablero tableroSucesor, int coste, int profundidad, int heuristica) {
		switch (estrategia) {
			case "BFS":
				Nodo nodoSucesorBFS=new Nodo(id,idPadre, accion, tableroSucesor, coste, profundidad, 0, profundidad);
				fronteraBFS.add(nodoSucesorBFS);
				break;
			case "DFS":
				Nodo nodoSucesorDFS=new Nodo(id,idPadre, accion, tableroSucesor, coste, profundidad, 0, profundidad*-1);
				fronteraAuxDFS.add(nodoSucesorDFS);
				break;
			case "UCS":
				Nodo nodoSucesorUCS=new Nodo(id,idPadre, accion, tableroSucesor, coste, profundidad, 0, coste);
				fronteraValor.add(nodoSucesorUCS);
				break;
		}
	}
	
	public void addFronteraDFS() {
		while(fronteraAuxDFS.size()>0) {
			fronteraDFS.add(fronteraAuxDFS.pop());
		}
	}
	
	public Nodo sacarNodo() {
		Nodo nodoSacar = new Nodo();	
		switch (estrategia) {
			case "BFS":
				nodoSacar = fronteraBFS.poll();
				break;
			case "DFS":
				nodoSacar = fronteraDFS.pop();
				break;
			case "UCS":
				nodoSacar=fronteraValor.poll();
				break;
		}
		return nodoSacar;
	}
	
	public int getSize() {
		int tamano=0;
		switch (estrategia) {
			case "BFS":
				tamano=fronteraBFS.size();
				break;
			case "DFS":
				tamano=fronteraDFS.size();
				break;
			case "UCS":
				tamano=fronteraValor.size();
				break;
		}
		return tamano;
	}
	
	public boolean estrategiaPoda(int valorNodoAnalizar, int valorNodoLC) {
		boolean AnalizarTienePeorValor=false;
		switch (estrategia) {
		case "BFS":
			if(valorNodoAnalizar>=valorNodoLC)
				AnalizarTienePeorValor=true;
			break;
		case "DFS":
			if(valorNodoAnalizar<=valorNodoLC)
				AnalizarTienePeorValor=true;
			break;
		case "UCS":
			if(valorNodoAnalizar>=valorNodoLC)
				AnalizarTienePeorValor=true;
			break;
		
		}
		return AnalizarTienePeorValor;
	}
}

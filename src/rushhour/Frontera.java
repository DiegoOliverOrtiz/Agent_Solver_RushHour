package rushhour;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

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
	
	public void addNodo(Nodo nodoAdd) {
		switch (estrategia) {
			case "BFS":
				fronteraBFS.add(nodoAdd);
				break;
			case "DFS":
				fronteraAuxDFS.add(nodoAdd); //Primero tiene que ir a la auxiliar
				break;
			case "UCS":
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
				break;
		}
		return nodoSacar;
	}
	
	public Nodo crearNodoConEstrategia(int id, String idPadre, String accion, Tablero tableroSucesor, int coste, int profundidad, int heuristica) {
		Nodo nodoCrear=new Nodo();
		switch (estrategia) {
		case "BFS":
			Nodo nodoSucesorBFS=new Nodo(id,idPadre, accion, tableroSucesor, coste, profundidad, 0, profundidad);
			nodoCrear=nodoSucesorBFS;
			break;
		case "DFS":
			Nodo nodoSucesorDFS=new Nodo(id,idPadre, accion, tableroSucesor, coste, profundidad, 0, profundidad*-1);
			nodoCrear=nodoSucesorDFS;
			break;
		case "UCS":
			break;
		}
		return nodoCrear;
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
		boolean AnalizarConPeorValor=false;
		switch (estrategia) {
		case "BFS":
			if(valorNodoAnalizar>=valorNodoLC)
				AnalizarConPeorValor=true;
			break;
		case "DFS":
			if(valorNodoAnalizar<=valorNodoLC)
				AnalizarConPeorValor=true;
			break;
		case "UCS":
			break;
		
		}
		return AnalizarConPeorValor;
	}
}

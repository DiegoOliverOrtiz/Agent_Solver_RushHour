package rushhour;

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
	private PriorityQueue<Nodo> cola = new PriorityQueue<>();
	
	String estrategia;
	
	public Frontera(String estrategia) {
		this.estrategia=estrategia;
	}
	
	public void addFrontera(Nodo nodoAdd) {
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
	public Nodo sacarFrontera() {
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
}

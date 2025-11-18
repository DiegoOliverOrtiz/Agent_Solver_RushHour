package rushhour;

/* Descripción: 
 * 	La clase Nodo representa los nodos que se usan para los algoritmos de búsqueda 
 * 
 * Atributos: 
 * 	id: ID del nodo
 * 	padre: ID del padre del nodo
 * 	accion: es la acción que se toma para 
 * 	estado: es el tablero de dicho nodo
 * 	costo: coste para llegar a este nodo
 * 	profundidad: profundidad a la que se encuentra el nodo
 * 	heuristica: heuristica del nodo
 * 	valor: valor en función del método que empleemos
 * 
 * Funciones:
 * 	Constructor, getters y setter para cada valor.
 * 	compareTo: comparador para ver que nodo debe ir en frente de la cola con prioridad de la frontera
 * 	toString: representa todos los valores en una única cadena
 * 
 * */
public class Nodo implements Comparable<Nodo>{ //Tenemos que poner el Comparable para que funcione compareTo
	private int id;
	private String padre;
	private String accion;
	private Tablero estado;
	private int costo;
	private int profundidad;
	private int heuristica;
	private int valor;
	
	public Nodo() { //Constructor vacío para ciertos casos	
	}
	
	public Nodo(int id, String padre, String accion, Tablero estado, int costo, int profundidad, int heuristica, int valor) {
		this.id = id;
		this.padre = padre;
		this.accion = accion;
		this.estado = estado;
		this.costo = costo;
		this.profundidad = profundidad;
		this.heuristica = heuristica;
		this.valor = valor;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPadre() {
		return padre;
	}

	public void setPadre(String padre) {
		this.padre = padre;
	}

	@Override
	public String toString() { //Concatena todos los valores, para el estado toma la cadena que representa el tablero (semilla)
		return "["+id + "," + padre + "," + accion + "," + estado.getSemilla() + "," + costo + "," + profundidad + "," + heuristica + "," + valor + "]";
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public Tablero getEstado() {
		return estado;
	}

	public void setEstado(Tablero estado) {
		this.estado = estado;
	}

	public int getCosto() {
		return costo;
	}

	public void setCosto(int costo) {
		this.costo = costo;
	}

	public int getProfundidad() {
		return profundidad;
	}

	public void setProfundidad(int profundidad) {
		this.profundidad = profundidad;
	}

	public int getHeuristica() {
		return heuristica;
	}

	public void setHeuristica(int heuristica) {
		this.heuristica = heuristica;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	public int compareTo(Nodo otro) { //compareTo para compara valores
		int compararValores = Integer.compare(this.valor, otro.valor); //Primero toma el valor
		if(compararValores==0) { //En caso de empate desempata por valor de ID
			compararValores= Integer.compare(this.id, otro.id);
		}
		return compararValores;
	}
	
}

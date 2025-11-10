package rushhour;

public class Nodo {
	private int id;
	private String padre;
	private String accion;
	private Tablero estado;
	private int costo;
	private int profundidad;
	private int heuristica;
	private int valor;
	
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
	public String toString() {
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

	public int compareTo(Nodo otro) { //compareTo para UCS y en un futuro búsquedas informadas
		return Integer.compare(this.valor, otro.valor);
	}
	
}

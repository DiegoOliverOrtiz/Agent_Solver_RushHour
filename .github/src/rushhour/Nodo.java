package rushhour;

public class Nodo {
	int id;
	int padre;
	String accion;
	Tablero estado;
	int costo;
	int profundidad;
	int heuristica;
	int valor;
	
	public Nodo(int id, int padre, String accion, Tablero estado, int costo, int profundidad, int heuristica, int valor) {
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

	public int getPadre() {
		return padre;
	}

	public void setPadre(int padre) {
		this.padre = padre;
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

	
}

package rushhour;

import java.util.*;

public class Solver {
	ControlTablero control = new ControlTablero();
	public Solver() {
	}
	
	public String bfs(String semilla) {
		//Definicion de variables
		String solucion= "";
		boolean solucionEncontrada=false;
		double tiempoEjecucion=0;
		int nodosGenerados=0;
		int nodosPodados=0;
		int nodosExpandidos=0;
		int profundidadMaxima=0;
		Queue<Nodo> listaCerrada = new LinkedList<>();
		ArrayList<Nodo> listaCerradaVisitados = new ArrayList<>();
		Tablero tableroInicial = new Tablero(semilla);
		tableroInicial.generarVehiculos();
		tableroInicial.definirOrientacionVehiculosTablero();
		int id=0;
		Nodo nodoInicial=new Nodo(id,"none", "___",tableroInicial,0,0,0,0);
		listaCerrada.add(nodoInicial);
		double tiempoInicio = System.nanoTime();
		while(solucionEncontrada==false&&listaCerrada.size()>0) {
			Nodo nodoAnalizar = listaCerrada.poll();
			boolean podar =false;
			for(int i=0;i<listaCerradaVisitados.size();i++) {
				if(nodoAnalizar.getEstado().getSemilla().equals(listaCerradaVisitados.get(i).getEstado().getSemilla()))
					podar=true;
			}
			if(!podar) {
				listaCerradaVisitados.add(nodoAnalizar);
				Tablero tableroNodo = nodoAnalizar.getEstado();
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
						Nodo nodoSucesor=new Nodo(id,idPadre+"", sucesoresSeparados[0],tableroSucesor,coste,nodoAnalizar.getProfundidad()+1,0,nodoAnalizar.getProfundidad());
						listaCerrada.add(nodoSucesor);
					}
				}
			}
		}
		tiempoEjecucion = (System.nanoTime()-tiempoInicio)/1000; //Entre 1000 para que sea microsegundos
		if(solucionEncontrada)
			solucion=devolverSolucion(listaCerradaVisitados);
		else
			solucion="El tablero no tiene solución.";
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
		Stack<Nodo> listaCerrada = new Stack<>();
		ArrayList<Nodo> listaCerradaVisitados = new ArrayList<>();
		
		solucion=devolverSolucion(listaCerradaVisitados);
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
		ArrayList<Nodo> listaCerradaVisitados = new ArrayList<>();
		solucion=devolverSolucion(listaCerradaVisitados);
		return solucion;
	}
	
	public String devolverSolucion(ArrayList<Nodo> listaCerradaAnalizados) {
		String solucion= "";
		Nodo nodoFinal = listaCerradaAnalizados.get(listaCerradaAnalizados.size()-1);
		solucion=nodoFinal.toString()+"\n";
		String padre=nodoFinal.getPadre();
		for(int i=0;i<listaCerradaAnalizados.size();i++) {
			int posicionAnalizar = (listaCerradaAnalizados.size()-1)-i;
			Nodo nodoAnalizar = listaCerradaAnalizados.get(posicionAnalizar);
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
			solucion=invertir+invertir[posicionInvertir]+"\n";
		}
		return solucion;
	}
}

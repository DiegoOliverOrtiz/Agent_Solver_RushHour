package rushhour;

import java.awt.*;
import java.util.*;
import javax.swing.*;

 /* METODO EN DESHUSO ACTUALMENTE, PENDIENTE DE ACTUALIZAR/DESECHAR EN FUNCIÓN DE SI PUEDO O NO PARA LOS SIGUIENTES RETOS */
 
/* Descripción: 
 * 	Clase para ver mediante una GUI el tablero del juego (para depurar)
 * 
 * Atributos: 
 * 	tablero[][]: guarda la información del tablero para representarla
 * 
 * Funciones:
 * 	mostrarMatriz: Representa visualmente la matriz con los colores asignados a cada coche
 * 	generarColores: genera un array de colores aleatorios para asignar a cada coche
 * 
 * */
public class ControlVisual {
	//Al constructor le pasamos solo el tablero de nuestro juego
	private Color[] listaColores = new Color[36]; //36 por si nos meten coches de uno (por verify)
	int iteracion=0; //Iteración para navegar entre los posibles estados
	
	public ControlVisual() {
		for(int  i=0;i<listaColores.length;i++) { //Para cada color genera un número entre el 0 y el 255 (valor máximo del rgb)
			Random random = new Random();
        	int r = random.nextInt(255);
        	int g = random.nextInt(255);
        	int b = random.nextInt(255);
			Color c = new Color(r, g, b);
			//Comprobamos que el color no sea rojo (coche A, no lo debemos confundir. O blanco para tampoco confundir)
			if((Color.RED.getRed()==r&&Color.RED.getGreen()==g&&Color.RED.getBlue()==b) || (Color.WHITE.getRed()==r&&Color.WHITE.getGreen()==g&&Color.WHITE.getBlue()==b))
				c=Color.BLUE; //Si es igual forzamos el color a azul 
			listaColores[i]=c; //Añadimos y retornamos los colores
		}
	}

	/* crearVisual se encarga de ver si lo que le pasamos a la clase es dibujable (puede que nos llegue un mensaje de error)
	 * luuego en función del método que el usuario haya elegido (verify, successors...) los representará de una forma u otra
	 * los metodos que solo generan una salida, como verify o question --goal por ejemplo solo representarán la semilla que pasemos
	 * como parametro de entrada de la función a usar, mientras que métodso que dan múltiples soluciones como solver o successors
	 * mostrarán todos los tableros que existen como salida (todos los sucesores o todo el camino solución del solver)
	 * 
	 * Parametros de entrada:
	 * 	resultado: es el resultado del método original al que llamemos (0 o 1 o un númeor si fuera verify, la cadena de nodos solución si fuera solver...)
	 * 	args: args del programa original
	 * */
	public void crearVisual(String resultado, String[] args) {
		try {
			ArrayList<Tablero> tableros = new ArrayList<>(); //Lista de tableros, serán todos los tableros que representaremos
			boolean dibujable=false; //Variable que nos confirma si es un tablero dibujable u otra cosa
			//////
			////// VERIFY
			//////
			if(args[0].equals("verify")) { //Primero comprobamos si nos han metido el comando verify
				int resultadoInt = Integer.parseInt(resultado);
				if(resultadoInt>1||resultadoInt==0) { //Mira si el resultado lo podemos dibujar (se pueden todos menos un string ilegal)
					Tablero tableroAux = new Tablero(args[2]); //Crea un tablero, lo añade a la lista y lo marca como dibujable
					tableroAux.generarVehiculos();
					tableroAux.definirOrientacionVehiculosTablero();
					tableros.add(tableroAux);
					dibujable=true;
				}
					
			}
			//////
			////// QUESTION
			//////
			else if(args[0].equals("question")) {//Si no es la opción verify vemos si es question
				if((args[3].equals("--whereis")&&args.length>4)||(args[3].equals("--what")&&args.length>4)||(args[3].equals("--size")&&args.length>4)||args[3].equals("--howmany")||args[3].equals("--goal")) {
					ControlTablero controlAux = new ControlTablero();
					int valido=controlAux.verificarTablero(args[2]);
					if(valido==0) {
						Tablero tableroAux = new Tablero(args[2]);
						tableroAux.generarVehiculos();
						tableroAux.definirOrientacionVehiculosTablero();
						tableros.add(tableroAux);
						dibujable=true;
					}		
				}
				else if(args[3].equals("--move")&&args.length>4) {
					ControlTablero controlAux = new ControlTablero();
					int validoInput=controlAux.verificarTablero(args[2]);
					int validoResultado=controlAux.verificarTablero(resultado);
					if(validoInput==0&&validoResultado==0) {
						Tablero tableroAux = new Tablero(args[2]);
						tableroAux.generarVehiculos();
						tableroAux.definirOrientacionVehiculosTablero();
						tableros.add(tableroAux);
						Tablero tableroAux2 = new Tablero(resultado);
						tableroAux2.generarVehiculos();
						tableroAux2.definirOrientacionVehiculosTablero();
						tableros.add(tableroAux2);
						dibujable=true;
					}
				}
			}
			//////
			////// SUCCESSORS Y SOLVER
			//////
			else if((args[0].equals("successors")&&args[1].equals("-s")&&args.length>2)||(args[0].equals("solver")&&args[1].equals("-s")&&args.length>3)) {
				ControlTablero controlAux = new ControlTablero();
				if(controlAux.verificarTablero(args[2])==0) {
					String[] separados = resultado.split("\n");
					Tablero tableroAux = new Tablero(args[2]);
					tableroAux.generarVehiculos();
					tableroAux.definirOrientacionVehiculosTablero();
					tableros.add(tableroAux);
					int tamanoSeparados=0;
					if(args.length==6) {
						if(args[5].equals("--stats"))
							tamanoSeparados=separados.length-6;
						else
							tamanoSeparados=separados.length;
					}
					else if(args.length==7) {
						if(args[6].equals("stats"))
							tamanoSeparados=separados.length-6;
						else
							tamanoSeparados=separados.length;
					}
					for(int i=0;i<tamanoSeparados;i++) {
						separados[i]=separados[i].substring(1, separados[i].length() - 1);
					}
					if(args[0].equals("successors")) {
						for(int i=0;i<separados.length;i++) {
							String[] sucesoresSeparados=separados[i].split(",");
							if(sucesoresSeparados.length==3) {
								Tablero tableroAux2 = new Tablero(sucesoresSeparados[1]);
								tableroAux2.generarVehiculos();
								tableroAux2.definirOrientacionVehiculosTablero();
								tableros.add(tableroAux2);
								dibujable=true;
							}
						}
					}
					else if(args[0].equals("solver")) {
						for(int i=1;i<separados.length;i++) {
							String[] solverSeparados=separados[i].split(",");
							if(solverSeparados.length==8) {
								Tablero tableroAux2 = new Tablero(solverSeparados[3]);
								tableroAux2.generarVehiculos();
								tableroAux2.definirOrientacionVehiculosTablero();
								tableros.add(tableroAux2);
								dibujable=true;
							}
						}
					}
					
				} //Vemos si es successors y si lo es lo ejecuta
				
				if(dibujable)
					mostrarTablero(tableros, args);
			}
		}
		catch(Exception e) {
			System.out.println("Error al dibujar el tablero.");
		}
	}
	
	private void mostrarTablero(ArrayList<Tablero> tableros, String[] args) {
		JFrame frame = new JFrame("Tablero de juego."); //Creamos el frame para mostrar el contenido visual
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Indicamos que se saldrá del frame al cerrarlo 
        frame.setSize(800, 600); //Indicamos el tamaño de nuestro frame
        JPanel panelPrincipal = new JPanel(new BorderLayout()); //Añadimos nuestro frame principal
        // Etiqueta para mostrar el metodo solicitado
        String argumentos="Estados de: ";
        for(int i=0;i<args.length;i++) {
        	argumentos=argumentos+args[i]+" ";
        }
        JLabel etiqueta = new JLabel(argumentos, SwingConstants.CENTER);
        etiqueta.setFont(new Font("Arial", Font.PLAIN, 14));
        etiqueta.setVerticalAlignment(SwingConstants.TOP);
        panelPrincipal.add(etiqueta, BorderLayout.NORTH);
        JPanel panelVehiculos = new JPanel(new GridLayout(6, 6, 10, 10));
        generarPanelVehiculos(tableros.get(iteracion), panelVehiculos);
        panelPrincipal.add(panelVehiculos); //Una vez hechas todas las iteraciones añadimos el panel con los coches
        
        if(tableros.size()>=2) { //Solo si recibimos al menos dos tableros podremos movernos entre ellos
	        // Panel para las flechas de navegación
	        JPanel panelNavegacion = new JPanel(new FlowLayout());
	        JButton botonAtras = new JButton("<");
	        JButton botonSiguiente = new JButton(">");
	        JButton botonPrimero = new JButton("<<");
	        JButton botonUltimo = new JButton(">>");
	        // Etiqueta para mostrar la "iteracion"
	        JLabel etiquetaIt = new JLabel("Estado: 0", SwingConstants.CENTER);
	        etiquetaIt.setFont(new Font("Arial", Font.PLAIN, 14));
	        etiquetaIt.setVerticalAlignment(SwingConstants.TOP);
	        panelNavegacion.add(botonPrimero);
	        panelNavegacion.add(botonAtras);
	        panelNavegacion.add(etiquetaIt, BorderLayout.NORTH);
	        panelNavegacion.add(botonSiguiente);
	        panelNavegacion.add(botonUltimo);
	        panelPrincipal.add(panelNavegacion, BorderLayout.SOUTH);
	        
	        //Funciones de los botones
	        botonAtras.addActionListener(e -> {
	            if (iteracion > 0) {
	                iteracion--;
	                generarPanelVehiculos(tableros.get(iteracion), panelVehiculos);
	                int tamano=tableros.size()-1;
	                etiquetaIt.setText("Estado: "+iteracion+"/"+tamano);
	               
	            }
	        });
	        botonSiguiente.addActionListener(e -> {
	            if (iteracion < tableros.size()-1) {
	            	iteracion++;
	                generarPanelVehiculos(tableros.get(iteracion), panelVehiculos);
	                int tamano=tableros.size()-1;
	                etiquetaIt.setText("Estado: "+iteracion+"/"+tamano);
	            }
	        });
	        botonPrimero.addActionListener(e -> {
	            if (iteracion > 0) {
	                iteracion=0;
	                generarPanelVehiculos(tableros.get(iteracion), panelVehiculos);
	                int tamano=tableros.size()-1;
	                etiquetaIt.setText("Estado: "+iteracion+"/"+tamano);
	            }
	        });
	        botonUltimo.addActionListener(e -> {
	            if (iteracion < tableros.size()-1) {
	                iteracion=tableros.size()-1;
	                generarPanelVehiculos(tableros.get(iteracion), panelVehiculos);
	                int tamano=tableros.size()-1;
	                etiquetaIt.setText("Estado: "+iteracion+"/"+tamano);
	            }
	        });
        }
        frame.add(panelPrincipal); //Añadimos al panel principal
        frame.setVisible(true); //Lo hacemos visible
        
	}
	
	private void generarPanelVehiculos(Tablero tablero, JPanel panelVehiculos) {
		panelVehiculos.removeAll();
		ArrayList<Vehiculo> vehiculos = tablero.getVehiculos(); 
    	char[] nombresVehiculos =new char[vehiculos.size()];
    	for(int i=0;i<vehiculos.size();i++) {
    		nombresVehiculos[i]=vehiculos.get(i).getNombre();
    	}
    	Arrays.sort(nombresVehiculos);
    	for(int i=0;i<nombresVehiculos.length;i++) {
    		for(int j=0;j<vehiculos.size();j++) {
    			if(nombresVehiculos[i]==vehiculos.get(j).getNombre())
    				vehiculos.get(j).setColorVehiculo(listaColores[i]);
    		}
    	}
		for (int i = 0; i < tablero.getMatriz().length; i++) { //Bucle para recorrer la matriz que representa nuestro tablero
            for (int j = 0; j < tablero.getMatriz()[0].length; j++) {
            	String etiqueta=""; //Por defecto asumimos que no hay coche (hueco en blanco así que sin etiqueta y color blanco)
            	Color colorCoche=Color.WHITE;
            	Color colorFuente=Color.WHITE;
            	for (int k=0;k<vehiculos.size();k++) {//Buscamos si entre todos los coches alguno coincide en nombre con...
            		if(vehiculos.get(k).getNombre()==tablero.getValorMatriz(i, j)) { //...El de la posición del tablero actual, en caso afirmativo rompemos el bucle y guardamos sus datos
            			etiqueta=etiqueta+vehiculos.get(k).getNombre();
            			colorCoche=vehiculos.get(k).getColorVehiculo();
            			if(vehiculos.get(k).getNombre()=='A') { //condiciones especificas para el coche rojo
            				colorFuente=Color.BLACK;
            				colorCoche=Color.RED;
            			}
            			k=vehiculos.size()+1;
            		}
            	}
                if((i==2&&j==5)&&tablero.getValorMatriz(i, j)=='o') { //Para ver donde está la meta
            		etiqueta=etiqueta+"META";
            		colorFuente=Color.RED;
            	}
                JLabel celda = new JLabel(etiqueta, SwingConstants.CENTER); //Creamos una etiqueta para representar cada celda de la matriz
                celda.setOpaque(true); //Lo poenmos opaco para que se vea el color
                celda.setBackground(colorCoche); //Y ponemos el color de fondo
                celda.setForeground(colorFuente); //El color de la etiqueta siempre a blanco
                celda.setFont(new Font("Arial", Font.BOLD, 18)); //La fuente
                panelVehiculos.add(celda);   //Por último añadimos la celda creada al panel de los coches 
            }
        }
		panelVehiculos.revalidate();
	    panelVehiculos.repaint();
	}
	
}


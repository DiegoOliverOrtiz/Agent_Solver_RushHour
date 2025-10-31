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
	private Color[] listaColores = new Color[10];
	int iteracion=0;
	
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
	/* mostrarMatriz se encarga de dibujar el tablero mostrando una serie de etiquetas cuadradas que representarán a los coches
	 * su funcionamiento es simple, crea un frame donde ir metiendo los elementos visuales, primero el panel principal y dentro
	 * de este el panel coches dividido en 6x6 huecos libres para ir metiendo etiquetas de colores que representen lo que hay en cada  celda
	 * para ello recorremos todo el tablero (pasado como parametro) suponiendo que encontraremos un hueco vacio ('o'), buscamos si en esa
	 * casilla hay un coche (gracias a tenerlos guardados y pasados como parametro del metodo), en caso de haberlo guarda en unas
	 * variables el color y nombre de dicho coche. Finaliza guardando el color y etiquetas necesarios para representar esa celda y ya tras
	 * salir del bucle hace el frame visible
	 * */
	
	public void crearVisual(String resultado, String[] args) {
		ArrayList<Tablero> tableros = new ArrayList<>();
		boolean dibujable=false;
		//////
		////// VERIFY
		//////
		if(args[0].equals("verify")) { //Primero comprobamos si nos han metido el comando verify
			int resultadoInt = Integer.parseInt(resultado);
			if(resultadoInt>1||resultadoInt==0) {
				Tablero tableroAux = new Tablero(args[2]);
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
            	if((i==5&&j==2)&&tablero.getValorMatriz(i, j)=='o')
            		etiqueta=etiqueta+"META";
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


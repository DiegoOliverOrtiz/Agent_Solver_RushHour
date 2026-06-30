package rushhour;

import java.awt.*;
import java.util.*;
import javax.swing.*;

 /* METODO EN DESHUSO ACTUALMENTE, PENDIENTE DE ACTUALIZAR/DESECHAR EN FUNCIÓN DE SI PUEDO O NO PARA LOS SIGUIENTES RETOS */
 
/* Descripción: 
 * 	Clase para ver mediante una GUI el tablero del juego (para depurar)
 * 
 * Atributos: 
 * 	Color[] listaColores: lista con 36 colores para representar posibles vehículos, tiene 36 porque se puede usar con verify y ahí nos pueden meter 36 vehículos
 * 	iteracion: variable que cuenta en que estado de los que le pasemos nos encontramos, sirve para navegar a través de estos
 * 
 * Funciones:
 * 	ControlVisual: constructor que se encarga de generar los colores aleatorios a usar
 * 	crearVisual: se encarga de ver si lo que le hemos pasado al objeto de esta clase es dibujable o no, en caso de no serlo controla el error
 * 	en caso de serlo lo dibuja, si se trata de successors, move o solver muestra varias pantallas para ver varios posibles estados
 * 	mostrarTablero: se encarga del generador del frame donde irán los elementos, los crea y los añade, se encarga además de realizar
 * 	las llamadas al metodo que dibuja en sí los vehiculos con el estado concreto a dibujar
 * 	generarPanelVehiculos: modifica el panel 6x6 creado en mostrar tablero vaciandolo y rellenandolo de etiquetas con colores y nombres
 * 	que representan lo que hay en cada celda, realiza también las llamadas a función que repintan el tablero.
 * 
 * */
public class ControlVisual {
	//Al constructor le pasamos solo el tablero de nuestro juego
	private Color[] listaColores = new Color[36]; //36 por si nos meten coches de uno (por verify)
	private int iteracion=0; //Iteración para navegar entre los posibles estados
	
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
	 * luego en función del método que el usuario haya elegido (verify, successors...) los representará de una forma u otra
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
				//Revisa los posibles comandos de question con algunas comprobaciones de longitud
				if((args[3].equals("--whereis")&&args.length>4)||(args[3].equals("--what")&&args.length>4)||(args[3].equals("--size")&&args.length>4)||args[3].equals("--howmany")||args[3].equals("--goal")) {
					ControlTablero controlAux = new ControlTablero();
					int valido=controlAux.verificarTablero(args[2]);
					if(valido==0) { //Verificamos si es un tablero válido y si lo es lo dibujamos
						Tablero tableroAux = new Tablero(args[2]);
						tableroAux.generarVehiculos();
						tableroAux.definirOrientacionVehiculosTablero();
						tableros.add(tableroAux);
						dibujable=true;
					}		
				}
				else if(args[3].equals("--move")&&args.length>4) { //Parecido para move
					ControlTablero controlAux = new ControlTablero();
					int validoInput=controlAux.verificarTablero(args[2]); //Verificamos si la semilla original es valida
					int validoResultado=controlAux.verificarTablero(resultado); //Verificamos que la semilla resultante es válida
					if(validoInput==0&&validoResultado==0) { //Si ambas lo son
						Tablero tableroAux = new Tablero(args[2]); //Se genera y añade un tablero para la semilla original
						tableroAux.generarVehiculos();
						tableroAux.definirOrientacionVehiculosTablero();
						tableros.add(tableroAux);
						Tablero tableroAux2 = new Tablero(resultado); //Se genera y añade un tablero para el resultado con los coches movidos
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
				ControlTablero controlAux = new ControlTablero(); //Nuevo control
				if(controlAux.verificarTablero(args[2])==0) { //Verificamos si la cadena original es válida
					String[] separados = resultado.split("\n"); //Separa el output según los saltos de línea
					Tablero tableroAux = new Tablero(args[2]); //Crea un tablero para el inpput original
					tableroAux.generarVehiculos();
					tableroAux.definirOrientacionVehiculosTablero();
					tableros.add(tableroAux); //Lo define y añade
					int tamanoSeparados=0; //Valor para descartar stats si se han realizado en el solver
					for(int i=0;i<args.length;i++) { //Buscamos si nos han pedido stats
						if(args[i].equals("--stats")) {
							tamanoSeparados=separados.length-6; //Si hay stats ignoramos las 6 últimas líneas
							i=args.length+1;
						}
						else
							tamanoSeparados=separados.length;
					}
					for(int i=0;i<tamanoSeparados;i++) { //Quitamos le primer y último caracter de todos (es el corchete entre el que van)
						separados[i]=separados[i].substring(1, separados[i].length() - 1);
					}
					if(args[0].equals("successors")) { //Si es sucesores
						for(int i=0;i<separados.length;i++) { //Para cada sucesore
							String[] sucesoresSeparados=separados[i].split(","); //Para cada uno los separa
							if(sucesoresSeparados.length==3) { //Mira si esun tamaño válido
								Tablero tableroAux2 = new Tablero(sucesoresSeparados[1]); //Toma el valor que corresponde a la semilla lo genera como tablero y añade para dibujar
								tableroAux2.generarVehiculos(); //Genera y define un tablero y lo añade a la lista de los que se deben dibujar
								tableroAux2.definirOrientacionVehiculosTablero();
								tableros.add(tableroAux2);
								dibujable=true;
							}
						}
					}
					else if(args[0].equals("solver")) { //Si es solver hace lo mismo que sucesores pero varía los valores por las posiciones y tamaño del contenido
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
					
				} 
			}
			//Al final del todo vemos si lo puede dibujar (hemos pasado contenido legal y lo dibuja)
			if(dibujable)
				mostrarTablero(tableros, args);
		}
		catch(Exception e) { //Excepción por si algo sale mal o no he controlado algo
			System.out.println("Error al dibujar el tablero.");
		}
	}
	
	/* Mostrar tablero se encarga de la visualización del tablero y la adición de los botones para las visualizaciones que lo requieran
	 * resumiendo su funcionamiento: genera un frame, le da propiedades y mete dentro un panel con la instrucción metida, y otro panel
	 * dentro donde se verán los coches, en caso de que haya dos estados o más añade botones para poder navegar a través de los paneles
	 * finalmente hace todo visible
	 * Parametros de entrada:
	 * 	tableros: lista de tableros que tendremos que representar
	 * 	args: argumentos del programa para mostrarlos en la label
	 * */
	private void mostrarTablero(ArrayList<Tablero> tableros, String[] args) {
		JFrame frame = new JFrame("Tablero de juego."); //Creamos el frame para mostrar el contenido visual
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Indicamos que se saldrá del frame al cerrarlo 
        frame.setSize(800, 600); //Indicamos el tamaño de nuestro frame
        JPanel panelPrincipal = new JPanel(new BorderLayout()); //Añadimos nuestro frame principal
        // Etiqueta para mostrar el metodo solicitado
        String argumentos="Estados de: "; //String para la etiqueta concatenando los args para mostrarlos todos
        for(int i=0;i<args.length;i++) {
        	argumentos=argumentos+args[i]+" ";
        }
        JLabel etiqueta = new JLabel(argumentos, SwingConstants.CENTER); //Añadimos la etiqueta para que el usuario vea que representa el tablero
        etiqueta.setFont(new Font("Arial", Font.PLAIN, 14)); //Le damos fuente y tamaño
        etiqueta.setVerticalAlignment(SwingConstants.TOP); //Le ponemos la alineación
        panelPrincipal.add(etiqueta, BorderLayout.NORTH); //Lo añadimos al frame arriba del todo
        JPanel panelVehiculos = new JPanel(new GridLayout(6, 6, 10, 10)); //Generamos el panel donde irán los vehículos indicandole que tiene que tener 6 filas y 6 columnas
        generarPanelVehiculos(tableros.get(iteracion), panelVehiculos); //Generamos el primer tablero
        panelPrincipal.add(panelVehiculos); //Añadimos al representación de dicho tablero
        if(tableros.size()>=2) { //Solo si recibimos al menos dos tableros podremos movernos entre ellos
	        // Panel para las flechas de navegación
	        JPanel panelNavegacion = new JPanel(new FlowLayout()); //Añadimos un panel de navegación
	        //Declaramos 4 botones
	        JButton botonAtras = new JButton("<"); //Ir atrás un estado
	        JButton botonSiguiente = new JButton(">"); //Ir adelante un estado
	        JButton botonPrimero = new JButton("<<"); //Ir al primer estado
	        JButton botonUltimo = new JButton(">>"); //Ir al último estado
	        // Etiqueta para mostrar la "iteracion"
	        int numEstados=tableros.size()-1;
	        JLabel etiquetaIt = new JLabel("Estado: 0/"+numEstados, SwingConstants.CENTER); //Ponemos la etiqueta donde se nos indicará en que estado estamos, ponemos que de primeras es la 0
	        etiquetaIt.setFont(new Font("Arial", Font.PLAIN, 14)); //Le indicamos la fuente
	        etiquetaIt.setVerticalAlignment(SwingConstants.TOP); //Alineación de la fuente
	        panelNavegacion.add(botonPrimero); //Añadimos los botones en el orden que queremos que aparezcan en nuestra interfaz
	        panelNavegacion.add(botonAtras);
	        panelNavegacion.add(etiquetaIt, BorderLayout.NORTH);
	        panelNavegacion.add(botonSiguiente);
	        panelNavegacion.add(botonUltimo);
	        panelPrincipal.add(panelNavegacion, BorderLayout.SOUTH); //Añadimos el panel de navegación
	        
	        //Funciones de los botones
	        botonAtras.addActionListener(e -> { //Son action listeners
	            if (iteracion > 0) { //Comprobantes para no salirnos del tamaño del tablero
	                iteracion--; //Si es ir uno atrás restamos uno a la iteracion
	                generarPanelVehiculos(tableros.get(iteracion), panelVehiculos); //Modificamos el panel de vehículos (más adelante se explica como)
	                int tamano=tableros.size()-1; //Cambiamos la etiqueta que se muestra
	                etiquetaIt.setText("Estado: "+iteracion+"/"+tamano);
	               
	            }
	        });
	        botonSiguiente.addActionListener(e -> { //Lo mismo con los demás botones
	            if (iteracion < tableros.size()-1) {
	            	iteracion++;
	                generarPanelVehiculos(tableros.get(iteracion), panelVehiculos);
	                int tamano=tableros.size()-1;
	                etiquetaIt.setText("Estado: "+iteracion+"/"+tamano);
	            }
	        });
	        botonPrimero.addActionListener(e -> { //El de primero fuerza la iteración a ser la primera
	            if (iteracion > 0) {
	                iteracion=0;
	                generarPanelVehiculos(tableros.get(iteracion), panelVehiculos);
	                int tamano=tableros.size()-1;
	                etiquetaIt.setText("Estado: "+iteracion+"/"+tamano);
	            }
	        });
	        botonUltimo.addActionListener(e -> { //El de último fuerza la iteración a ser la última
	            if (iteracion < tableros.size()-1) {
	                iteracion=tableros.size()-1;
	                generarPanelVehiculos(tableros.get(iteracion), panelVehiculos);
	                int tamano=tableros.size()-1;
	                etiquetaIt.setText("Estado: "+iteracion+"/"+tamano);
	            }
	        });
        }
        frame.add(panelPrincipal); //Añadimos al panel principal al frame
        frame.setVisible(true); //Lo hacemos visible
        
	}
	
	/* generarPanelVehiculos más que generar se encarga de modificar, ¿por qué? Pues porque le pasamos la referencia a panelVehiculos
	 * cuando pasamos un objeto a un método realmente no estamos pasando el objeto como tal, solo la referencia a dicho objeto ¿Y esto
	 * de qué nos sirve? Pues nos es bastante útil porque al ser la referencia podemos cambiar los parametros de panelVehiculos desde aquí
	 * lo que hará que se cambie para todo (al final llamamos al mismo objeto) y por tanto al modificar algo aquií se mostrará en el visual.
	 * Respecto al funcionamiento se resume en: elimina todo el contenido del panel, le asigna a todos los vehículos su color correspondiente
	 * recorre ahora toda la matriz creando etiquetas de colores que servirán para representar si una casilla la ocupa un coche o nada y por
	 * último añade la nueva etiqueta al frame
	 * 
	 * Parametros:
	 * 	tablero: tablero a representar
	 * 	panelVehiculos: el panel donde irán representados los coches
	 * */
	private void generarPanelVehiculos(Tablero tablero, JPanel panelVehiculos) {
		panelVehiculos.removeAll(); //Elimina todo lo que pueda tener el frame (mantiene su estructura)
		ArrayList<Vehiculo> vehiculos = tablero.getVehiculos(); //Guarda todos los vehiculos del tablero
    	char[] nombresVehiculos =new char[vehiculos.size()]; //Generamos un char del tamaño de los vehículos para guardar sus nombres
    	//Esto que vamos a hacer ahora es con el fin de que siempre se cargue el mismo color a todos los vehículos sin tener que definirlo manualmente
    	for(int i=0;i<vehiculos.size();i++) { //Guardamos los nombres
    		nombresVehiculos[i]=vehiculos.get(i).getNombre();
    	}
    	Arrays.sort(nombresVehiculos); //Los organizamos alfabéticamente
    	for(int i=0;i<nombresVehiculos.length;i++) { //Recorremos los nombres
    		for(int j=0;j<vehiculos.size();j++) { //Buscamos el vehículo que corresponda
    			if(nombresVehiculos[i]==vehiculos.get(j).getNombre())
    				vehiculos.get(j).setColorVehiculo(listaColores[i]); //Si coinciden le asignamos el color correspondiente
    			//Se hace así porque al ordenarlos alfabeticamente mantienen siempre el mismo orden estén donde estén en el tablero
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
            			if(vehiculos.get(k).getNombre()=='A') { //Condiciones especificas para el coche rojo
            				colorFuente=Color.BLACK;
            				colorCoche=Color.RED;
            			}
            			k=vehiculos.size()+1;
            		}
            	}
                if((i==2&&j==5)&&tablero.getValorMatriz(i, j)=='o') { //Para ver donde está la meta
            		etiqueta=etiqueta+"META"; //Le ponemos una etiqueta manualmente
            		colorFuente=Color.RED;
            	}
                JButton celda = new JButton(etiqueta); //Creamos una etiqueta para representar cada celda de la matriz
                celda.setOpaque(true); //Lo poenmos opaco para que se vea el color
                celda.setBackground(colorCoche); //Y ponemos el color de fondo
                celda.setForeground(colorFuente); //El color de la etiqueta siempre a blanco
                celda.setFont(new Font("Arial", Font.BOLD, 18)); //La fuente
                panelVehiculos.add(celda);   //Por último añadimos la celda creada al panel de los coches 
            }
        }
		panelVehiculos.revalidate(); //Esto sirve para repintarlo
	    panelVehiculos.repaint();
	}
	
}


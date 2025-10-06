package rushhour;

import java.awt.*;
import java.util.*;
import javax.swing.*;

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
	private char tablero[][]=new char[6][6];
	//Al constructor le pasamos solo el tablero de nuestro juego
	public ControlVisual(char[][] tablero) {
		this.tablero = tablero;
	}
	/* mostrarMatriz se encarga de dibujar el tablero mostrando una serie de etiquetas cuadradas que representarán a los coches
	 * su funcionamiento es simple, crea un frame donde ir metiendo los elementos visuales, primero el panel principal y dentro
	 * de este el panel coches dividido en 6x6 huecos libres para ir metiendo etiquetas de colores que representen lo que hay en cada  celda
	 * para ello recorremos todo el tablero (pasado como parametro) suponiendo que encontraremos un hueco vacio ('o'), buscamos si en esa
	 * casilla hay un coche (gracias a tenerlos guardados y pasados como parametro del metodo), en caso de haberlo guarda en unas
	 * variables el color y nombre de dicho coche. Finaliza guardando el color y etiquetas necesarios para representar esa celda y ya tras
	 * salir del bucle hace el frame visible
	 * */
	public void mostrarMatriz(ArrayList<Coche> coches) { //Le pasamos el listado de todos los coches existentes
		JFrame frame = new JFrame("Tablero de juego."); //Creamos el frame para mostrar el contenido visual
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Indicamos que se saldrá del frame al cerrarlo 
        frame.setSize(800, 600); //Indicamos el tamaño de nuestro frame
        JPanel panelPrincipal = new JPanel(new BorderLayout()); //Añadimos nuestro frame principal
        JPanel panelCoches = new JPanel(new GridLayout(6, 6, 10, 10)); //Añadimos nuestro frame donde irá nuestra representación visual de los coches
        for (int i = 0; i < tablero.length; i++) { //Bucle para recorrer la matriz que representa nuestro tablero
            for (int j = 0; j < tablero[0].length; j++) {
            	String etiqueta=""; //Por defecto asumimos que nohay coche (hueco en blanco así que sin etiqueta y color blanco)
            	Color colorCoche=Color.WHITE;
            	for (int k=0;k<coches.size();k++) { //Buscamos si entre todos los coches alguno coincide en nombre con...
            		if(coches.get(k).getNombre()==tablero[i][j]) { //...El de la posición del tablero actual, en caso afirmativo rompemos el bucle y guardamos sus datos
            			etiqueta=etiqueta+coches.get(k).getNombre();
            			colorCoche=coches.get(k).getColor();
            			k=coches.size()+1;
            		}
            	}
                JLabel celda = new JLabel(etiqueta, SwingConstants.CENTER); //Creamos una etiqueta para representar cada celda de la matriz
                celda.setOpaque(true); //Lo poenmos opaco para que se vea el color
                celda.setBackground(colorCoche); //Y ponemos el color de fondo
                celda.setForeground(Color.WHITE); //El color de la etiqueta siempre a blanco
                celda.setFont(new Font("Arial", Font.BOLD, 18)); //La fuente
                panelCoches.add(celda);   //Por último añadimos la celda creada al panel de los coches 
            }
        }
        panelPrincipal.add(panelCoches); //Una vez hechas todas las iteraciones añadimos el panel con los coches
        frame.add(panelPrincipal); //Añadimos al panel principal
        frame.setVisible(true); //Lo hacemos visible
        
	}
	/* Metodo para generar los colores aleatoriamente
	 * Genera tres numeros aleatorio para dar valor al RGB del color y tras comprobar que sea valido lo añade al listado
	 * 
	 * */
	public Color[] generarColores(){
		Color[] listaC = new Color[16]; //Genera un array de 16 colores (uno por cada coche máximo posible)
		for(int  i=0;i<listaC.length;i++) { //Para cada color genera un número entre el 0 y el 255 (valor máximo del rgb)
			Random random = new Random();
        	int r = random.nextInt(255);
        	int g = random.nextInt(255);
        	int b = random.nextInt(255);
			Color c = new Color(r, g, b);
			//Comprobamos que el color no sea rojo (coche A, no lo debemos confundir. O blanco para tampoco confundir)
			if((Color.RED.getRed()==r&&Color.RED.getGreen()==g&&Color.RED.getBlue()==b) || (Color.WHITE.getRed()==r&&Color.WHITE.getGreen()==g&&Color.WHITE.getBlue()==b))
				c=Color.BLUE; //Si es igual forzamos el color a azul 
			listaC[i]=c; //Añadimos y retornamos los colores
		}
		return listaC;
	}
	
	
}


package rushhour;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/* Descripción: 
 * 	Clase para ver mediante una GUI el tablero del juego (para depurar)
 * 
 * Atributos: 
 * 	tablero[][] representa el tablero
 * 
 * Funciones:
 * 	mostrarMatriz: Representa visualmente la matriz con los colores asignados a cada coche
 * 
 * */
public class ControlVisual {
	private char tablero[][]=new char[6][6];
	
	public ControlVisual(char[][] tablero) {
		this.tablero = tablero;
	}

	public void mostrarMatriz(ArrayList<Coche> coches) {
		JFrame frame = new JFrame("Mostrar matrices.");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelBloques = new JPanel(new GridLayout(6, 6, 10, 10));
        int filas = tablero.length;
        int columnas = tablero[0].length;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
            	String etiqueta="";
            	Color colorCoche=Color.WHITE;
            	for (int k=0;k<coches.size();k++) {
            		if(coches.get(k).getNombre()==tablero[i][j]) {
            			etiqueta=etiqueta+coches.get(k).getNombre();
            			colorCoche=coches.get(k).getColor();
            			k=coches.size()+1;
            		}
            	}
                JLabel celda = new JLabel(etiqueta, SwingConstants.CENTER);
                celda.setOpaque(true); // necesario para que se vea el color de fondo
                celda.setBackground(colorCoche);
                celda.setForeground(Color.WHITE); // color de la letra
                celda.setFont(new Font("Arial", Font.BOLD, 18));
                panelBloques.add(celda);  
            }
        }
        panelPrincipal.add(panelBloques);
        frame.add(panelPrincipal);
        frame.setVisible(true);
        
	}
	public Color[] generarColores(){
		Color[] listaC = new Color[16];
		for(int  i=0;i<listaC.length;i++) {
			Random random = new Random();
        	int r = random.nextInt(255);
        	int g = random.nextInt(255);
        	int b = random.nextInt(255);
			Color c = new Color(r, g, b);
			if((Color.RED.getRed()==r&&Color.RED.getGreen()==g&&Color.RED.getBlue()==b) || (Color.WHITE.getRed()==r&&Color.WHITE.getGreen()==g&&Color.WHITE.getBlue()==b))
				c=Color.BLUE;
			listaC[i]=c;
		}
		return listaC;
	}
	
	
}


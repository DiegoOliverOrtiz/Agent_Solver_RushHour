package rushhour;

public class Main {
	public static void main(String[] args) {
		if(args.length>=2) {
			controlTablero tablero = new controlTablero();
			if(args[0].equals("verify")&&args[1].equals("-s")&&args.length>2) {
				int verify=tablero.crearTablero(args[2]);
				if(verify!=-1)
					System.out.println(verify);
				else
					System.out.println("Tablero correcto, pero el vehículo rojo (A) no es un coche (longitud 2).");
			}
			else if(args[0].equals("question")&&args[1].equals("-s")&&args.length>3) {
				if(tablero.crearTablero(args[2])==0)
					System.out.println(tablero.preguntasTablero(args));
				else
					System.out.println("La cadena introducida no es válida, compruebala con el comando: verify -s <cadena>");
			}
			else
				System.out.println("Error en los parámetros, no se reconoce la entrada.");
		}
		else
			System.out.println("Error en los parámetros, número de parámetros insuficiente.");
	}
}
package rushhour;

public class Main {
	public static void main(String[] args) {
		controlTablero tablero = new controlTablero();
		if(args[0].equals("verify")&&args[1].equals("-s"))
			System.out.println(tablero.crearTablero(args[2]));
	}
}

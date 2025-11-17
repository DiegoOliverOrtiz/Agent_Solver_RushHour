package rushhour; //v0.3.3

/* Descripción: 
 * 	Clase principal del programa
 * 
 * Funciones:
 * 	Realiza las llamadas a los metodos necesarias para el correcto funcionamiento del programa
 * 
 * */
public class Main {
	public static void main(String[] args) {
		//Comprobación de argumentos (empiezan después del rushhour.jar)
		if(args.length>=2) { //Por ahora todos nuestros comandos han de tener al menos dos parametros siendo: <comando> y -s
			ControlTablero tablero = new ControlTablero(); //Creamos una instancia de la clase controlTablero sobre la que trabajar
			ControlVisual visual = new ControlVisual();
			//////
			////// VERIFY
			//////
			if(args[0].equals("verify")&&args[1].equals("-s")&&args.length>2) { //Primero comprobamos si nos han metido el comando verify
				int verify=tablero.verificarTablero(args[2]); //Si tiene verify -s <parametro> ya comprueba el resto en la función de crearTablero
				if(verify!=-1) { //Si da un retorno que no sea el -1 muestra los errores pedidos en el campus
					System.out.println(verify);
					visual.crearVisual(verify+"", args);
				}
				else //Si es el error -1 muestra el siguiente mensaje (se hace así porque este error no está indicado en el campus)
					System.out.println("Tablero correcto, pero el vehículo rojo (A) no es un coche (longitud 2).");
			}
			//////
			////// QUESTION
			//////
			else if(args[0].equals("question")&&args[1].equals("-s")&&args.length>3) { //Si no es la opción verify vemos si es question
				String resultadoConsulta=tablero.preguntasTablero(args);
				System.out.println(resultadoConsulta); //Lo ejecutamos si lo es
				visual.crearVisual(resultadoConsulta, args);
			}
			//////
			////// SUCCESSORS
			//////
			else if(args[0].equals("successors")&&args[1].equals("-s")) {//Vemos si es successors y si lo es lo ejecuta
					String resultadoConsulta=tablero.sucesoresTablero(args[2]);
					System.out.println(resultadoConsulta);
					visual.crearVisual(resultadoConsulta, args);
			}
			
			//////
			////// SOLVER
			//////
			else if(args[0].equals("solver")&&args[1].equals("-s")) {
				Solver solucionador = new Solver();
				String resultadoConsulta=solucionador.realizarBusqueda(args);
				System.out.println(resultadoConsulta);
				visual.crearVisual(resultadoConsulta, args);
			}
			
			//////
			////// ERRORES
			//////
			else //Si ninguna de las opciones es correcta mostramos un error 
				System.out.println("Error en los parámetros, no se reconoce la entrada.");
		}
		else //Mostramos error también si no hay parametros suficientes
			System.out.println("Error en los parámetros, número de parámetros insuficiente.");
	}
}
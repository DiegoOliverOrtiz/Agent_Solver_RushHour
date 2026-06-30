Autor: Diego Oliver Ortiz
Correo de contacto: diegooliverortizeyt@gmail.com

¿Qué es Rush Hour?:
Rush Hour es un juego de mesa tipo rompecabezas creado por ThinkFun. Está diseñado para un solo jugador y simula un atasco de tráfico en una cuadrícula. El objetivo es liberar un coche rojo que está atrapado entre otros vehículos, moviéndolos dentro de un espacio limitado.

¿Cómo se juega Rush Hour?
-Tablero: Es una cuadrícula de 6x6 casillas.
-Vehículos: Hay coches (2 casillas de largo) y camiones (3 casillas de largo), tanto coches como camiones pueden ser colocados horizontal o verticalmente.
-Coche rojo: Es el vehículo que debes sacar del atasco. Siempre empieza en una fila central y debe salir por el borde derecho del tablero.
-Movimientos: Sólo puedes mover los vehículos hacia adelante o hacia atrás en la dirección en la que están orientados (no se pueden girar).
-Objetivo: Mover los vehículos de forma estratégica para despejar el camino del coche rojo hacia la salida.

Sobre el programa:
La ejecución del programa se realiza en terminal sobre el archivo .jar ya compilado de esta forma: java -jar rushhour.jar <parámetros>
Se recomienda leer el manual de la aplicación ya que ahí se detalla como representar un nivel o todos los comandos disponibles.
Se incluye adicionalmente una GUI activable poniendo siempre al FINAL de cada línea de comandos el comando: --visual

Ejemplos de ejecución:
-Ejemplo de acceso al manual:
java -jar rushhour.jar -man

-Ejemplo del agente resolviendo un nivel, además muestra estadísticas y el visual:
java -jar rushhour.jar solver -s BCCoDoBEFoDGBEFAAGHHHIoGooJIKKLLJMMo --strategy AStar --heuristic 0 --stats --visual



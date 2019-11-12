package Gestion;

import java.sql.*;
import java.util.Scanner;

public class UtilidadesComunes {

    /**
     * Lee y valida el ID de un partido.
     * @return Devuelve asociado al nombre el ID del partido seleccionado.
     */
    public static int leerIDpartido(){
        int partido = 0;
        Scanner teclado = new Scanner(System.in);

        do{
            verPartidosDisponiblesParaApostar(); //Mostramos los partidos disponibles.
            System.out.println("Elige un partido de entre los mostrados.");
            partido = teclado.nextInt();
        }while(partido < 0);

        return partido;
    }
    /*
    * Muestra todos los partidos a los que se puede apostar. Esto es, los partido que se encuentran en estado abierto.
    * */
    public static void verPartidosDisponiblesParaApostar(){
        // Carga el driver
        try {

            // Carga la clase del driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Define the data source for the driver
            String sourceURL = "jdbc:sqlserver://localhost";
            String usuario = "manue";
            String password = "qq";
            String miSelect = "SELECT ID, fechaPartido FROM Partidos WHERE isAbierto = 1";

            // Crear una connexion con el DriverManager
            Connection connexionBaseDatos = DriverManager.getConnection(sourceURL, usuario, password);
            Statement sentencia = connexionBaseDatos.createStatement();
            ResultSet partidos = sentencia.executeQuery(miSelect);

            // Mostrar los datos del ResultSet
            while (partidos.next())
                System.out.println(partidos.getInt("ID")+ " -> " + partidos.getTimestamp("Fecha"));

            // Cerrar conexion
            connexionBaseDatos.close();
        }catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe);
        }catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }
}

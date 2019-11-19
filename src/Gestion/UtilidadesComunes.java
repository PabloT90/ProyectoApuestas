package Gestion;

import java.sql.*;
import java.util.Scanner;

public class UtilidadesComunes {

    /**
     * Lee y valida el ID de un partido.
     * @return Devuelve asociado al nombre el ID del partido seleccionado.
     */
    public static int leerIDpartido(){
        int partido;
        Scanner teclado = new Scanner(System.in);

        do{
            verPartidosDisponiblesParaApostar(); //Mostramos los partidos disponibles.
            System.out.println("Elige un partido de entre los mostrados.");
            partido = teclado.nextInt();
        }while(!partidoEncontrado(partido));

        return partido;
    }
    /*
    * Muestra todos los partidos a los que se puede apostar. Esto es, los partido que se encuentran en estado abierto.
    * */
    public static void verPartidosDisponiblesParaApostar() {
        // Carga el driver
        try {
            // Carga la clase del driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Define the data source for the driver
            String sourceURL = "jdbc:sqlserver://localhost";
            String usuario = "pablo";
            String password = "qq";
            String miSelect = "SELECT ID, fechaPartido FROM Partidos WHERE isAbierto = 1";

            // Crear una connexion con el DriverManager
            Connection connexionBaseDatos = DriverManager.getConnection(sourceURL, usuario, password);
            Statement sentencia = connexionBaseDatos.createStatement();
            ResultSet partidos = sentencia.executeQuery(miSelect);

            // Mostrar los datos del ResultSet
            while (partidos.next()) {
                System.out.println(partidos.getString("ID") + " -> " + partidos.getTimestamp("fechaPartido"));
            }

            // Cerrar conexion
            connexionBaseDatos.close();
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe);
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }

    /**
     * Permite saber si existe un partido con el ID recibido como parametro
     * @param idPartido ID del partido que queremos buscar.
     * @return True en caso de existir, false en caso contrario.
     */
    public static boolean partidoEncontrado(int idPartido){
        boolean ret = false;

            //Buscamos el partido
            //Hacemos un SELECT con ese ID y si devuelve una fila es que existe.
            try {
                // Carga la clase del driver
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                // Define the data source for the driver
                String sourceURL = "jdbc:sqlserver://localhost";
                String usuario = "pablo";
                String password = "qq";
                String miSelect = "SELECT id FROM Partidos where id = " +idPartido;

                // Crear una connexion con el DriverManager
                Connection connexionBaseDatos = DriverManager.getConnection(sourceURL, usuario, password);
                Statement sentencia = connexionBaseDatos.createStatement();
                ResultSet partidos = sentencia.executeQuery(miSelect);

                // Mostrar los datos del ResultSet
                if(partidos.next()){ //Si tiene una fila
                    ret = true;
                }
                // Cerrar conexion
                connexionBaseDatos.close();
            } catch (ClassNotFoundException cnfe) {
                System.err.println(cnfe);
            } catch (SQLException sqle) {
                System.err.println(sqle);
            }

        return ret;
    }

    public static boolean existenPartidosAbiertos(){
        boolean ret = false;

        //Buscamos el partido
        //Hacemos un SELECT con ese ID y si devuelve una fila es que existe.
        try {
            // Carga la clase del driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Define the data source for the driver
            String sourceURL = "jdbc:sqlserver://localhost";
            String usuario = "pablo";
            String password = "qq";
            String miSelect = "SELECT * FROM Partidos where isAbierto = 1";

            // Crear una connexion con el DriverManager
            Connection connexionBaseDatos = DriverManager.getConnection(sourceURL, usuario, password);
            Statement sentencia = connexionBaseDatos.createStatement();
            ResultSet partidos = sentencia.executeQuery(miSelect);

            // Mostrar los datos del ResultSet
            if(partidos.next()){ //Si tiene una fila
                ret = true;
            }
            // Cerrar conexion
            connexionBaseDatos.close();
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe);
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
        return ret;
    }

    public static boolean partidoAbierto(int id){
        boolean ret = false;
        //Hacemos un SELECT con ese ID y si devuelve una fila es que existe.
        try {
            // Carga la clase del driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Define the data source for the driver
            String sourceURL = "jdbc:sqlserver://localhost";
            String usuario = "pablo";
            String password = "qq";
            String miSelect = "SELECT * FROM Partidos where isAbierto = 1 AND ID="+id;

            // Crear una connexion con el DriverManager
            Connection connexionBaseDatos = DriverManager.getConnection(sourceURL, usuario, password);
            Statement sentencia = connexionBaseDatos.createStatement();
            ResultSet partidos = sentencia.executeQuery(miSelect);

            // Mostrar los datos del ResultSet
            if(partidos.next()){ //Si tiene una fila
                ret = true;
            }
            // Cerrar conexion
            connexionBaseDatos.close();
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe);
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
        return ret;
    }
}

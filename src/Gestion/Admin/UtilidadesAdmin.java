package Gestion.Admin;

import Gestion.UtilidadesComunes;

import java.sql.*;
import java.util.Scanner;

public class UtilidadesAdmin {
    /**
     * Lee y valida si el usuario quiere ejecutar una aplicacion.
     */
    public static char leerValidarEjecutar(){
        Scanner teclado = new Scanner(System.in);
        char ejecutar;

        do{
            System.out.println("Quieres ejecutar?");
            ejecutar = Character.toLowerCase(teclado.next().charAt(0));
        }while(ejecutar != 's' && ejecutar != 'n');

        return ejecutar;
    }

    /**
     * Muestra un menu y obtiene una respuesta valida para la opcion seleccionada.
     * @return asociado al nombre devuelve un entero con la opcion del menu.
     */
    public static int MostrarMenuLeerValidarOpcion(){
        int opcion = 0;
        Scanner teclado = new Scanner(System.in);
        do{
            System.out.println("1) Crear partido.");
            System.out.println("2) Abrir partido.");
            System.out.println("3) Cerrar partido.");
            System.out.println("4) Consultar apuestas de un partido.");
            System.out.println("5) Pagar apuestas.");
            System.out.println("0) Salir.");

            opcion = teclado.nextInt();
        }while(opcion < 0 || opcion > 5);

        return opcion;
    }

    public boolean crearPartido(){
        boolean ret = true;

        return ret;
    }

    /*
    Cierra el partido que tenga el id que le pasas
     */
    public static boolean cerrarPartido(int idPartido){
        boolean ret = true;
        int resul;

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Define the data source for the driver
            String sourceURL = "jdbc:sqlserver://localhost";
            String usuario = "pablo";
            String password = "qq";
            String miUpdate = "UPDATE Partidos SET isAbierto = 0 WHERE id =" + idPartido;

            // Crear una connexion con el DriverManager
            Connection connexionBaseDatos = DriverManager.getConnection(sourceURL, usuario, password);
            Statement sentencia = connexionBaseDatos.createStatement();
            resul = sentencia.executeUpdate(miUpdate);

            if (resul == 0){
                ret = false;
            }
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return  ret;
    }

}

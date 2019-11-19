package Gestion.User;

import java.sql.*;
import java.util.Scanner;

public class UtilidadesUser {

    /**
     * Muestra un menu y obtiene una respuesta valida para la opcion seleccionada.
     * @return asociado al nombre devuelve un entero con la opcion del menu.
     */
    public static int MostrarMenuLeerValidarOpcion(){
        int opcion;
        Scanner teclado = new Scanner(System.in);
        do{
            System.out.println("1) Apostar a un partido.");
            System.out.println("2) Ver partidos disponibles.");
            System.out.println("3) Comprobar resultado de una apuesta anterior.");
            System.out.println("4) Realizar ingreso.");
            System.out.println("5) Retirar dinero.");
            System.out.println("6) Ver movimientos.");
            System.out.println("0) Salir.");

            opcion = teclado.nextInt();
        }while(opcion < 0 || opcion > 6);
        return opcion;
    }

    //TODO leer el correo, el ingreso
    public static int ingresarDinero(int cantidad){
        int resultado = 0;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Define the data source for the driver
            String sourceURL = "jdbc:sqlserver://localhost";
            String usuario = "pablo";
            String password = "qq";
            String miSentencia = "EXEC dbo.ingresoACuenta ("+")";

            // Crear una connexion con el DriverManager
            Connection connexionBaseDatos = DriverManager.getConnection(sourceURL, usuario, password);
            Statement sentencia = connexionBaseDatos.createStatement();
            sentencia.executeUpdate(miSentencia);
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return resultado;
    }
}

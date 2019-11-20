package Gestion.User;

import Conexion.clsConexion;

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

    /**
     * Ingresa una cantidad de dinero a un usuario determinado
     * @param cantidad Cantidad de dinero a ingresar.
     * @param correo Correo de la persona.
     * @return entero con el numero de error producido.
     */
    public static int ingresarDinero(int cantidad, String correo){
        int resultado = 0;
        clsConexion conexion = new clsConexion();

        try {
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conexion.abrirConexion();
            CallableStatement cstmt = conexion.getConnexionBaseDatos().prepareCall("{call ingresoACuenta(?,?)}"); //Aqui llamamos al procedimiento que queramos.
            cstmt.setString(1, correo);
            cstmt.setInt(2,cantidad);
            conexion.cerrarConexion();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return resultado;
    }

    /**
     * Leer y valida la cantidad de dinero que queremos ingresar.
     * @return cantidad a ingresar.
     */
    public static int LeerValidarDinero(){
        int cantidad;
        Scanner teclado = new Scanner(System.in);

        do{
            System.out.println("Cantidad de dinero a ingresar:");
            cantidad = teclado.nextInt();
        }while(cantidad < 0);

        return cantidad;
    }

    /**
     * Leer un correo electronico
     * @return Correo de la persona.
     */
    public static String LeerCorreo(){
        String correo;
        Scanner teclado = new Scanner(System.in);

        do{
            System.out.println("Cantidad de dinero a ingresar:");
            correo = teclado.nextLine();
        }while(correo == "");

        return correo;
    }
}

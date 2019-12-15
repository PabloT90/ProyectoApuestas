package Gestion.User;

import Conexion.clsConexion;
import Gestion.UtilidadesComunes;

import java.sql.*;
import java.util.Calendar;
import java.util.Scanner;

public class UtilidadesUser {

    /**
     * Muestra un menu y obtiene una respuesta valida para la opcion seleccionada.
     * @return asociado al nombre devuelve un entero con la opcion del menu.
     */
    public int MostrarMenuLeerValidarOpcion(){
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
    public int ingresarDinero(int cantidad, String correo){
        int resultado = 0;
        clsConexion conexion = new clsConexion();

        try {
            conexion.abrirConexion();
            CallableStatement cstmt = conexion.getConnexionBaseDatos().prepareCall("{call ingresoACuenta(?,?,?)}"); //Aqui llamamos al procedimiento que queramos.
            cstmt.setString(1, correo);
            cstmt.setInt(2,cantidad);
            cstmt.setInt(3, cantidad);
            resultado = cstmt.executeUpdate();
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
    public int LeerValidarDinero(){
        int cantidad;
        Scanner teclado = new Scanner(System.in);

        do{
            System.out.println("Cantidad de dinero:");
            cantidad = teclado.nextInt();
        }while(cantidad < 0);

        return cantidad;
    }

    /**
     * Lee un correo electronico y se asegura que no esta vacio.
     * @return Correo de la persona.
     */
    public String LeerCorreo(){ //Se podria mejorar con regex.
        String correo;
        Scanner teclado = new Scanner(System.in);

        do{
            System.out.println("Correo de la cuenta de usuario:");
            correo = teclado.nextLine();
        }while(correo == ""); //Tiene que haber algo escrito.

        return correo;
    }

    /**
     * Lee una contraseña y se asegura que no esta vacio.
     * @return Contraseña de la persona.
     */
    public String LeerPassword(){
        String pass;
        Scanner teclado = new Scanner(System.in);

        do{
            System.out.println("Contrasenha:");
            pass = teclado.nextLine();
        }while(pass == "");

        return pass;
    }

    /**
     * Retira una cantidad de dinero a un usuario determinado
     * @param cantidad Cantidad de dinero a ingresar.
     * @param correo Correo de la persona.
     * @return entero con el numero de error producido.
     */
    public int retirarDinero(int cantidad, String correo){
        int resultado = 0;
        clsConexion conexion = new clsConexion();

        try {
            conexion.abrirConexion();
            CallableStatement cstmt = conexion.getConnexionBaseDatos().prepareCall("{call retirarCapitalCuenta(?,?,?)}"); //Aqui llamamos al procedimiento que queramos.
            cstmt.setString(1, correo);
            cstmt.setInt(2,cantidad);
            cstmt.setInt(3, 0);
            resultado = cstmt.executeUpdate();
            conexion.cerrarConexion();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return resultado;
    }

    /**
     * Muestra los movimientos que ha hecho un usuario concreto.
     * @param usuario Correo del usuario a consultar.
     */
    public void mostrarMovimientos(String usuario){
        try{
            clsConexion miconexion = new clsConexion();
            String miSelect = "SELECT saldo, fechaYHora FROM Cuentas WHERE CorreoUsuario=?";
            // Crear una connexion con el DriverManager
            miconexion.abrirConexion();
            Connection connexionBaseDatos = miconexion.getConnexionBaseDatos();
            PreparedStatement sentencia = connexionBaseDatos.prepareStatement(miSelect);
            sentencia.setString(1,usuario);
            ResultSet partidos = sentencia.executeQuery();

            // Mostrar los datos del ResultSet
            while (partidos.next()) {
                System.out.println("Saldo: " + partidos.getString("Saldo") + " || Fecha: " + partidos.getTimestamp("fechaYHora"));
            }

            // Cerrar conexion
            connexionBaseDatos.close();
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }

    /**
     * Muestra las apuestas que ha realizado un usuario y su resultado.
     * @param usuario Correo del usuario.
     */
    public void MostrarApuestasAnteriores(String usuario){
        String isGanada;
        try{
            clsConexion miconexion = new clsConexion();
            //String miSelect = "SELECT * FROM Apuestas WHERE CorreoUsuario=" + "'" + usuario + "'";
            String miSelect = "SELECT * FROM Apuestas WHERE CorreoUsuario=?";
            //Crear una connexion con el DriverManager
            miconexion.abrirConexion();
            Connection connexionBaseDatos = miconexion.getConnexionBaseDatos();
            PreparedStatement sentencia = connexionBaseDatos.prepareStatement(miSelect);
            sentencia.setString(1,usuario);
            ResultSet partidos = sentencia.executeQuery();

            // Mostrar los datos del ResultSet
            while (partidos.next()) {
                if(partidos.getBoolean("IsGanador")){
                    isGanada = " Ganada.";
                }else{
                    isGanada = " Perdida.";
                }
                System.out.println("Cuota: " + partidos.getString("Cuota") + " || Cantidad apostada: " + partidos.getBigDecimal("DineroApostado") + " || Resultado: " + isGanada);
            }

            // Cerrar conexion
            connexionBaseDatos.close();
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }

    /*
    * Interfaz
    * Nombre: apostarAPartido
    * Comentario: Este método nos permite realizar una apuesta a un partido válido,
    * es decir, a un partido existente que se encuentre abierto. El usuario podrá
    * en cualquiera de los pasos de creación de la apuesta cancelar la misma. Estye estructura
    * se ha pasado a un método para que la lectura del código sea más clara y ordenada.
    * Este método es útilizado por la clase MainUser.
    * Cabecera: public void apostarAPartido(String correoUsuario)
    * Entrada:
    *   -String correoUsuario
    * Postcondiciones: Si el usuario ha completado el último paso para la creación de la apuesta,
    * este se inserta en la base de datos de la aplicación.
    * */
    public void apostarAPartido(String correoUsuario){
        UtilidadesComunes uComunes = new UtilidadesComunes();
        int idPartido, tipoApuesta, golesLocales = 0, golesVisitantes = 0, goles = 0;
        double capitalApuesta = 0.0;
        Date fechaActual = null;
        String equipo = " ";

        if (uComunes.existenPartidosAbiertos()) { //Si existe algun partido abierto
            if (uComunes.obtenerCapitalMaximoUsuario(correoUsuario) > 0) {
                // //Si el partido elegido es correcto, es decir, está abierto
                if ((idPartido = uComunes.leerIDpartido()) != 0) {
                    //leerYValidarTipoApuesta*
                    if ((tipoApuesta = uComunes.leerYValidarTipoApuesta()) != 0) {
                        //LeerCaracteristicasApuesta
                        switch (tipoApuesta) {
                            case 1:
                                //LeerYValidarGolesLocalesYVisitantes
                                golesLocales = uComunes.leerYValidarGolesLocales();
                                golesVisitantes = uComunes.leerYValidarGolesVisitante();
                                break;
                            case 2:
                                //LeerYValidarEquipoYGoles
                                equipo = uComunes.leerYValidarEquipo();
                                goles = uComunes.leerYValidarGoles();
                                break;

                            case 3:
                                //leerYValidarEquipo()
                                equipo = uComunes.leerYValidarEquipo();
                                break;
                        }

                        //leerYValidarCantidadAApostar*
                        capitalApuesta = uComunes.leerYValidarCantidadAApostar(correoUsuario);
                        //RealizarApuesta
                        fechaActual = uComunes.convertUtilToSql(Calendar.getInstance().getTime());
                        switch (tipoApuesta) {
                            case 1:
                                //realizarApuestaTipo1*
                                uComunes.realizarApuestaTipo1(fechaActual, capitalApuesta, correoUsuario, idPartido, golesLocales, golesVisitantes);
                                break;

                            case 2:
                                //realizarApuestaTipo2*
                                uComunes.realizarApuestaTipo2(fechaActual, capitalApuesta, correoUsuario, idPartido, equipo, goles);
                                break;

                            case 3:
                                //realizarApuestaTipo3*
                                uComunes.realizarApuestaTipo3(fechaActual, capitalApuesta, correoUsuario, idPartido, equipo);
                                break;
                        }
                        System.out.println("Apuesta realizada con exito.");
                    }else{
                        System.out.println("No se ha podido apostar.");
                    }
                }else{
                    System.out.println("El ususario no tiene capital para apostar.");
                }
            }else{
                System.out.println("No hay partidos abiertos para apostar.");
            }
        }
    }
}
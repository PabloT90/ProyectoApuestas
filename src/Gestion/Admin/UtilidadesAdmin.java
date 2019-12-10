package Gestion.Admin;

import Conexion.clsConexion;
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

    /**
     * Abre el partido del mismo id que el que se le pasa
     * @param idPartido el id del partio que se quiere abrir
     * @return un boolean que nos dice si se ha ejecutado bien
     */
    public static boolean abrirPartido(int idPartido){

        boolean ret = true;
        int resultado;

        try {
            clsConexion miConexion = new clsConexion();
            String miUpdate = "UPDATE Partidos SET isAbierto = 1 WHERE id =" + idPartido; //Supongo que 1 es abierto

            // Crear una connexion con el DriverManager
            miConexion.abrirConexion();
            Connection connexionBaseDatos = miConexion.getConnexionBaseDatos();
            Statement sentencia = connexionBaseDatos.createStatement();
            resultado = sentencia.executeUpdate(miUpdate);

            if (resultado == 0){
                ret = false;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return ret;

    }

    /***
     *  Cierra el partido que tenga el id que le pasas
     * @param idPartido el id del partio que se quiere cerrar
     * @return un boolean que nos dice si se ha ejecutado bien
     */
    public static boolean cerrarPartido(int idPartido){
        boolean ret = true;
        int resul;

        try {
            clsConexion miConexion = new clsConexion();
            String miUpdate = "UPDATE Partidos SET isAbierto = 0 WHERE id =" + idPartido; //Supongo que 0 es cerrado

            // Crear una connexion con el DriverManager
            miConexion.abrirConexion();
            Connection connexionBaseDatos = miConexion.getConnexionBaseDatos();
            Statement sentencia = connexionBaseDatos.createStatement();
            resul = sentencia.executeUpdate(miUpdate);

            if (resul == 0){
                ret = false;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return  ret;
    }

    /**
     * Este metodo nos muestra todos los partidos en los que no se pueda apostar  en otras palabras que estene cerrados
     */
   public static void verPartidosCerrados(){

       try {
           clsConexion miconexion = new clsConexion();
           String miSelect = "SELECT ID, fechaPartido FROM Partidos WHERE isAbierto = 0";

           // Crear una connexion con el DriverManager
           miconexion.abrirConexion();
           Connection connexionBaseDatos = miconexion.getConnexionBaseDatos();
           Statement sentencia = connexionBaseDatos.createStatement();
           ResultSet partidos = sentencia.executeQuery(miSelect);

           // Mostrar los datos del ResultSet
           while (partidos.next()) {
               System.out.println(partidos.getString("ID") + " -> " + partidos.getTimestamp("fechaPartido"));
           }

           // Cerrar conexion
           connexionBaseDatos.close();
       } catch (SQLException sqle) {
           System.err.println(sqle);
       }

   }

    /**
     * Lee y valida el ID de un partido.
     * @return Devuelve asociado al nombre el ID del partido seleccionado.
     */
    public static int leerIDpartido(){
        int partido;
        Scanner teclado = new Scanner(System.in);

        do{
            verPartidosCerrados(); //Mostramos los partidos disponibles.
            System.out.println("Elige un partido de entre los mostrados.");
            partido = teclado.nextInt();
        }while(!partidoEncontrado(partido));

        return partido;
    }

    public static boolean partidoEncontrado(int idPartido){ //TODO: cambiar para que tenga en cuenta ademas los partidos abiertos.
        boolean ret = false;

        //Buscamos el partido
        //Hacemos un SELECT con ese ID y si devuelve una fila es que existe.
        try {
            clsConexion miConexion = new clsConexion();
            String miSelect = "SELECT id FROM Partidos where id = " +idPartido;

            // Crear una connexion con el DriverManager
            miConexion.abrirConexion();
            Connection connexionBaseDatos = miConexion.getConnexionBaseDatos();
            Statement sentencia = connexionBaseDatos.createStatement();
            ResultSet partidos = sentencia.executeQuery(miSelect);

            // Mostrar los datos del ResultSet
            if(partidos.next()){ //Si tiene una fila
                ret = true;
            }
            // Cerrar conexion
            connexionBaseDatos.close();
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }

        return ret;
    }

    /**
     * Este metodo nos permitira consultar todas las apuestas y cuanto dinero se ha apostado a cada posible resultado
     * @param idPartido el ide del partido que queremos consultar
     */
    public void consultarApuestasPartido(int idPartido){

        int maxGolesLocales = 0, maxGolesVisitantes;
        ResultSet resultado;
        try {

            //apuestas tipo 1
            clsConexion miconexion = new clsConexion();
            miconexion.abrirConexion();
            Connection connexionBaseDatos = miconexion.getConnexionBaseDatos();
            Statement sentencia = connexionBaseDatos.createStatement();
            String conseguirIDApuesta = "SELECT ID FROM Apuesta WHERE IDPartido = "+ idPartido + "AND Tipo = 1";
            ResultSet ApuestaID = sentencia.executeQuery(conseguirIDApuesta);
            String conseguirGolesLocales = "Select MAX(NumGolesLocal) AS [NumGolesLocal]from ApuestaTipo1 WHERE id = " + ApuestaID.getInt("ID");
            String conseguirGolesVisitantes = "SELECT MAX(numGolesVisitante) AS [numGolesVisitante]from ApuestaTipo1 WHERE id = "+ ApuestaID.getInt("ID");
            ResultSet golesL = sentencia.executeQuery(conseguirGolesLocales);
            ResultSet golesV = sentencia.executeQuery(conseguirGolesVisitantes);
            CallableStatement callStatementApuesta1 = miconexion.getConnexionBaseDatos().prepareCall("{call consultarApuestasTipo1(?,?,?)}");

            maxGolesLocales = golesL.getInt("NumGolesLocal");
            maxGolesVisitantes = golesV.getInt("numGolesVisitante");

            //TODO reprobar todo ahora con nueva columna cambiar insert por nueva columna MATADME
            for (int i = 0; i< maxGolesLocales; i++){

                for (int x = 0; i< maxGolesVisitantes; x++){

                    callStatementApuesta1.setInt(1,idPartido);
                    callStatementApuesta1.setInt(2,i);
                    callStatementApuesta1.setInt(3,x);
                    resultado = callStatementApuesta1.executeQuery();

                    String dineros = "SELECT SUM(DineroApostado) AS [Dineros]FROM Apuestas WHERE IDPartido = " + resultado.getInt("id") ;

                    ResultSet dineroPartido = sentencia.executeQuery(dineros);

                    if (resultado.getString("id") == null){
                        System.out.println("Goles locales: "+resultado.getString("NumGolesLocal")+" Goles visitante: "+ resultado.getString("numGolesVisitante")+ " Dinero apostado: "+ dineroPartido.getInt("Dineros"));
                    }
                }
            }

            //apuestas tipo 2
            //TODO creo que le tengo que hacer un rework a todo
            CallableStatement callStatementApuesta2 = miconexion.getConnexionBaseDatos().prepareCall("{call consultarApuestasTipo2(?,?,?)}");
            String conseguirApuesta2 = "SELECT ID FROM Apuestas WHERE IDPartido = " + idPartido+ "AND Tipo = 2";
            ResultSet partidos2 = sentencia.executeQuery(conseguirApuesta2);
            String conseguirgoles2 = "SELECT MAX(goles) AS [Goles] from ApuestaTipo2 WHERE id = "+ partidos2.getInt("ID");
            ResultSet goles = sentencia.executeQuery(conseguirgoles2);
            int maxgoles2 = goles.getInt("Goles");
            ResultSet resultado2;
            for (int i = 0; i < maxgoles2; i++){

                callStatementApuesta2.setInt(1,idPartido);
                callStatementApuesta2.setString(2, "Visitante");
                callStatementApuesta2.setInt(3,i);
                resultado2 = callStatementApuesta2.executeQuery();

            }


        }
        catch (SQLException r){
            r.printStackTrace();
        }
        //apuestas tipo 3

    }

}

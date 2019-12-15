package Gestion.Admin;

import Conexion.clsConexion;
import Gestion.UtilidadesComunes;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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


    /**
    * Interfaz
    * Nombre: insertarPartido
    * Comentario: Este método nos permite insertar un nuevo partido en la base de datos.
    * Cabecera: public static boolean crearPartido(double maximoTipo1, double maximoTipo2, double maximoTipo3, Date fechaPartido)
    * Entrada:
    *   -double maximoTipo1
    *   -double maximoTipo2
    *   -double maximoTipo3
    *   -Date fechaPartido
    * Salida:
    *   -boolean insertado
    * Postcondiciones: Este método nos devuelve un valor booleano asociado al nombre, true si se ha
    * conseguido insertar el nuevo partido en la base de datos o false en caso contrario.
    * */
    public static boolean insertarPartido(double maximoTipo1, double maximoTipo2, double maximoTipo3, Date fechaPartido){
        boolean insertado = true;

        clsConexion conexion = new clsConexion();

        try {
            conexion.abrirConexion();
            CallableStatement cstmt = conexion.getConnexionBaseDatos().prepareCall("{call insertarPartido(?,?,?,?)}");
            cstmt.setDouble(1, maximoTipo1);
            cstmt.setDouble(2, maximoTipo2);
            cstmt.setDouble(3, maximoTipo3);
            cstmt.setDate(4, fechaPartido);
            if (cstmt.executeUpdate() <= 0){//Si no se ha conseguido insertar ninguna fila
                insertado = false;
            }
            conexion.cerrarConexion();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return insertado;
    }

    /**
    * Interfaz
    * Nombre: leerYValidarMaximoApuestaTipo1
    * Comentario: Este método nos permite leer y validar el capital máximo
    * para una apuesta del tipo1
    * Cabecera: public static double leerYValidarMaximoApuestaTipo1()
    * Salida:
    *   -double maximoApuesta
    * Postcondiciones: El método devuelve un número entero asociado al nombre,
    * que es el máximo posible de capital que se puede apostar al tipo 1.
    * */
    public static double leerYValidarMaximoApuestaTipo1(){
        Scanner teclado = new Scanner(System.in);
        double maximoApuesta = 0.0;

        do{
            System.out.println("Indica el maximo para apuestas del tipo 1 en este partido (Debe ser mayor que 0).");
            maximoApuesta = teclado.nextDouble();
        }while (maximoApuesta <= 0);

        return maximoApuesta;
    }

    /**
     * Interfaz
     * Nombre: leerYValidarMaximoApuestaTipo2
     * Comentario: Este método nos permite leer y validar el capital máximo
     * para una apuesta del tipo2
     * Cabecera: public static double leerYValidarMaximoApuestaTipo2()
     * Salida:
     *   -double maximoApuesta
     * Postcondiciones: El método devuelve un número entero asociado al nombre,
     * que es el máximo posible de capital que se puede apostar al tipo 2.
     * */
    public static double leerYValidarMaximoApuestaTipo2(){
        Scanner teclado = new Scanner(System.in);
        double maximoApuesta = 0.0;

        do{
            System.out.println("Indica el maximo para apuestas del tipo 2 en este partido (Debe ser mayor que 0).");
            maximoApuesta = teclado.nextDouble();
        }while (maximoApuesta <= 0);

        return maximoApuesta;
    }

    /**
    * Interfaz
    * Nombre: leerYValidarFechaPartido
    * Comentario: Este método nos permite leer y validar una fecha para un partido.
    * Cabecera: public static Date leerYValidarFechaPartido()
    * Salida:
    *   -Date fechaValida
    * Postcondiciones: El método devuelve un tipo Date asociado al nombre,
    * que es la fecha válida.
    * */
    public static Date leerYValidarFechaPartido(){
        Scanner teclado = new Scanner(System.in);
        int anno = 0, mes = 0, dia = 0;
        boolean correcto = false;

        do{
            System.out.println("Introduce el anno del partido.");
            anno = teclado.nextInt();
            System.out.println("Introduce el mes del partido.");
            mes = teclado.nextInt();
            System.out.println("Introduce el dia del partido.");
            dia = teclado.nextInt();

            if(!fechaValida(anno, mes, dia)){
                System.out.println("La fecha "+anno+"/"+mes+"/"+dia+" no es correcta.");
            }
        }while (!fechaValida(anno, mes, dia));

        return new Date(dia, mes, anno);
    }

    /**
    * Interfaz
    * Nombre: fechaValida
    * Comentario: Este método nos permite verificar si una fecha es válida.
    * Cabecera: public static boolean fechaValida(int anno, int mes, int dia)
    * Entrada:
    *   -int anno
    *   -int mes
    *   -int dia
    * Salida:
    *   -boolean fechaValida
    * Postcondiciones: Este método nos devuelve un valor booleano asociado al nombre,
    * true si la fecha es válida o false en caso contrario.
    * */
    public static boolean fechaValida(int anno, int mes, int dia){
        boolean fechaValida = false;

        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");//Formato
            formatoFecha.setLenient(false);
            formatoFecha.parse(dia+"/"+mes+"/"+anno);//Intentamos parsearla, si falla nos salta una excepción
            fechaValida = true;
        } catch (ParseException e) {
            fechaValida = false;
        }

        return fechaValida;
    }

    /**
     * Interfaz
     * Nombre: leerYValidarMaximoApuestaTipo3
     * Comentario: Este método nos permite leer y validar el capital máximo
     * para una apuesta del tipo3
     * Cabecera: public static double leerYValidarMaximoApuestaTipo3()
     * Salida:
     *   -double maximoApuesta
     * Postcondiciones: El método devuelve un número entero asociado al nombre,
     * que es el máximo posible de capital que se puede apostar al tipo 3.
     * */
    public static double leerYValidarMaximoApuestaTipo3(){
        Scanner teclado = new Scanner(System.in);
        double maximoApuesta = 0.0;

        do{
            System.out.println("Indica el maximo para apuestas del tipo 3 en este partido (Debe ser mayor que 0).");
            maximoApuesta = teclado.nextDouble();
        }while (maximoApuesta <= 0);

        return maximoApuesta;
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
    public static void consultarApuestasPartido(int idPartido){

        int maxGolesLocales = 0, maxGolesVisitantes;
        ResultSet resultado;
        clsConexion miconexion = new clsConexion();
        int tipo = 1;

        try {
            //apuestas tipo 1
            //Conexiones
            miconexion.abrirConexion();
            Connection connexionBaseDatos = miconexion.getConnexionBaseDatos();
            Statement sentencia = connexionBaseDatos.createStatement();

            //Para conseguir el id de apuesta
            String conseguirIDApuesta = "SELECT ID FROM Apuestas WHERE IDPartido = "+ idPartido + "AND Tipo = " + tipo;
            ResultSet ApuestaID = sentencia.executeQuery(conseguirIDApuesta);

            if (ApuestaID.next()) {
                //Guardo el id y cierro el otro Resulset
                int idApuesta = ApuestaID.getInt("ID");
                //Que en ningun momento lanza su trigger
                ApuestaID.close();
                //Consigo los goles de esa apuesta
                //Los goles del equipo local
                String tgoles = "NumGolesLocal";
                String conseguirGoles = "Select MAX("+tgoles+") AS [NumGoles] from ApuestaTipo1 WHERE id = " + idApuesta;
                ResultSet golesL = sentencia.executeQuery(conseguirGoles);
                maxGolesLocales = golesL.getInt("NumGoles");
                //String conseguirGolesVisitantes = "SELECT MAX(numGolesVisitante) AS [numGoles] from ApuestaTipo1 WHERE id = " + ApuestaID.getInt("ID");
                //Los goles del visitante
                golesL.close();
                tgoles = "numGolesVisitante";
                ResultSet golesV = sentencia.executeQuery(conseguirGoles);
                maxGolesVisitantes = golesV.getInt("NumGoles");
                golesV.close();

                String dineros = "SELECT SUM(DineroApostado) AS [Dineros] FROM Apuestas WHERE IDPartido = " + idPartido + " AND Tipo = " +tipo;
                ResultSet dineroPartido = sentencia.executeQuery(dineros);
                int dinero = dineroPartido.getInt("Dineros");
                dineroPartido.close();

                //Llamo a la primera funcion
                CallableStatement callStatementApuesta1 = miconexion.getConnexionBaseDatos().prepareCall("{call consultarApuestasTipo1(?,?,?)}");

                //Asi vamos comprobando cada posible apuesta
                for (int i = 0; i < maxGolesLocales; i++) {

                    for (int x = 0; i < maxGolesVisitantes; x++) {

                        callStatementApuesta1.setInt(1, idPartido);
                        callStatementApuesta1.setInt(2, i);
                        callStatementApuesta1.setInt(3, x);
                        resultado = callStatementApuesta1.executeQuery();

                        if (resultado.next()) {
                                if (resultado.getString("id") != null) {
                                    System.out.println("Goles locales: " + resultado.getString("NumGolesLocal") + " Goles visitante: " + resultado.getString("numGolesVisitante") + " Dinero apostado: " + dinero);
                                }
                        }
                    }
                }
            }

            //apuestas tipo 2
            //Llamo a la segunda funcion
            CallableStatement callStatementApuesta2 = miconexion.getConnexionBaseDatos().prepareCall("{call consultarApuestasTipo2(?,?,?)}");
            //Consigo el id de la apuesta2
            tipo++;
            ApuestaID = sentencia.executeQuery(conseguirIDApuesta);

            //Consigo los goles
            if (ApuestaID.next()) {
                String conseguirgoles2 = "SELECT MAX(goles) AS [Goles] from ApuestaTipo2 WHERE id = " + ApuestaID.getInt("ID");
                ResultSet goles = sentencia.executeQuery(conseguirgoles2);
                if (goles.next()) {
                    int maxgoles2 = goles.getInt("Goles");

                    //Visitante
                    for (int i = 0; i < maxgoles2; i++) {

                        callStatementApuesta2.setInt(1, idPartido);
                        callStatementApuesta2.setString(2, "Visitante");
                        callStatementApuesta2.setInt(3, i);
                        resultado = callStatementApuesta2.executeQuery();
                        if (resultado.next()) {
                            String dineros = "SELECT SUM(DineroApostado) AS [Dineros] FROM Apuestas WHERE IDPartido = " + idPartido + " AND Tipo = "+tipo;
                            ResultSet dineroPartido = sentencia.executeQuery(dineros);
                            if (resultado.getString("id") != null) {

                                System.out.println("Goles: " + resultado.getString("goles") + " Equipo ganador " + resultado.getString("equipo") + " Dinero partido: " + dineroPartido.getInt("Dineros"));
                            }
                        }
                    }

                    //Local
                    for (int i = 0; i < maxgoles2; i++) {

                        callStatementApuesta2.setInt(1, idPartido);
                        callStatementApuesta2.setString(2, "Local");
                        callStatementApuesta2.setInt(3, i);
                        resultado = callStatementApuesta2.executeQuery();
                        if (resultado.next()) {
                            String dineros = "SELECT SUM(DineroApostado) AS [Dineros] FROM Apuestas WHERE IDPartido = " + resultado.getInt("id") + " AND Tipo = 2";

                            ResultSet dineroPartido = sentencia.executeQuery(dineros);
                            if (dineroPartido.next()) {
                                if (resultado.getString("id") != null) {
                                    System.out.println("Goles: " + resultado.getString("goles") + " Equipo ganador " + resultado.getString("equipo") + " Dinero partido: " + dineroPartido.getInt("Dineros"));
                                }
                            }
                        }
                    }
                }
            }

            //apuestas tipo 3
            //Local
            CallableStatement callStatementApuesta3 = miconexion.getConnexionBaseDatos().prepareCall("{call consultarApuestasTipo3(?,?)}");
            tipo++;
            //ResultSet Apuesta3 = sentencia.executeQuery(conseguirIDApuesta);
            ResultSet resultado3L;
            callStatementApuesta3.setInt(1,idPartido);
            callStatementApuesta3.setString(2,"Local");
            resultado3L = callStatementApuesta3.executeQuery();
            if (resultado3L.next()) {
                String dineros3L = "SELECT SUM(DineroApostado) AS [Dineros] FROM Apuestas WHERE IDPartido = " + resultado3L.getInt("id") + " AND Tipo = 3";
                ResultSet dineroPartido3L = sentencia.executeQuery(dineros3L);

                if (dineroPartido3L.next()) {

                    if (resultado3L.getString("id") == null) {
                        System.out.println("Equipo ganandor" + resultado3L.getString("ganador") + " dinero " + dineroPartido3L.getInt("Dineros"));
                    }
                }
            }

            //Visitante
            CallableStatement callStatementApuesta3V = miconexion.getConnexionBaseDatos().prepareCall("{call consultarApuestasTipo3(?,?)}");
            ResultSet resultado3V;
            callStatementApuesta3.setInt(1,idPartido);
            callStatementApuesta3.setString(2,"Visitante");
            resultado3V = callStatementApuesta3.executeQuery();
            if (resultado3V.next()) {
                String dineros3V = "SELECT SUM(DineroApostado) AS [Dineros] FROM Apuestas WHERE IDPartido = " + resultado3V.getInt("id") + " AND Tipo = 3";
                ResultSet dineroPartido3V = sentencia.executeQuery(dineros3V);

                if (dineroPartido3V.next()) {

                    if (resultado3L.getString("id") == null) {
                        System.out.println("Equipo ganandor" + resultado3V.getString("ganador") + " dinero " + dineroPartido3V.getInt("Dineros"));
                    }
                }
            }

        }
        catch (SQLException r) {
            r.printStackTrace();
        }
        finally {
        }

    }

    /*
    * Interfaz
    * Nombre: leerYValidarPartidoConApuestasSinContabilizar
    * Comentario: Este método nos permite obtener la id de un partido con apuestas
    * sin contabilizar.
    * Cabecera: public static int leerYValidarPartidoConApuestasSinContabilizar()
    * Salida:
    *   -int idPartido
    * Precondiciones:
    *   -Deben exitir en la base de datos partidos con apuestas sin finalizar.
    * Postcondiciones: El método devuelve un número entero asociado al nombre,
    * que es el id del partido con apuestas sin finalizar.
    * */
    public static int leerYValidarPartidoConApuestasSinContabilizar(){
        int idPartido = 0;
        Scanner teclado = new Scanner(System.in);

        do{
            System.out.println("Elige un partido: ");
            UtilidadesComunes.mostrarPartidosConApuestasNoContabilizadas();
            idPartido = teclado.nextInt();
        }while (!UtilidadesComunes.partidoConApuestasSinContabilizar(idPartido));

        return idPartido;
    }

    /*
     * Interfaz
     * Nombre: pagarApuestasPartido
     * Comentario: Este método nos permite pagar las apuestas sin contabilizar de un partido.
     * Cabecera: public static void pagarApuestasPartido(int idPartido)
     * Entrada:
     *   -int idPartido
     * Postcondiciones: El método paga las apuestas victoriosas que se encontraban sin contabilizar
     * de un partido, si el partido no existe o si no contenía apuestas sin contabilizar, el método
     * no realiza ninguna modificación.
     * */
    public static void pagarApuestasPartido(int idPartido){
        clsConexion conexion = new clsConexion();

        try {
            conexion.abrirConexion();
            CallableStatement cstmt = conexion.getConnexionBaseDatos().prepareCall("{call contabilizarApuestasNoContabilizadas(?)}");
            cstmt.setInt(1, idPartido);
            cstmt.executeUpdate();
            conexion.cerrarConexion();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}

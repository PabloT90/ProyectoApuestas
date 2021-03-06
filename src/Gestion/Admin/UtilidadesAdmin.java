package Gestion.Admin;

import Conexion.clsConexion;
import Gestion.UtilidadesComunes;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class UtilidadesAdmin {

    /**
     * Interfaz
     * Nombre: MostrarMenuLeerValidarOpcion
     * Comentario: Muestra un menu y obtiene una respuesta valida para la opcion seleccionada.
     * Cabecera: public int MostrarMenuLeerValidarOpcion()
     * Salida:
     *  @return asociado al nombre devuelve un entero con la opcion del menu.
     */
    public int MostrarMenuLeerValidarOpcion(){
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
    * Cabecera: public boolean crearPartido(double maximoTipo1, double maximoTipo2, double maximoTipo3, Date fechaPartido)
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
    public boolean insertarPartido(double maximoTipo1, double maximoTipo2, double maximoTipo3, Date fechaPartido){
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
    * Cabecera: public double leerYValidarMaximoApuestaTipo1()
    * Salida:
    *   -double maximoApuesta
    * Postcondiciones: El método devuelve un número entero asociado al nombre,
    * que es el máximo posible de capital que se puede apostar al tipo 1.
    * */
    public double leerYValidarMaximoApuestaTipo1(){
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
     * Cabecera: public double leerYValidarMaximoApuestaTipo2()
     * Salida:
     *   -double maximoApuesta
     * Postcondiciones: El método devuelve un número entero asociado al nombre,
     * que es el máximo posible de capital que se puede apostar al tipo 2.
     * */
    public double leerYValidarMaximoApuestaTipo2(){
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
    * Cabecera: public Date leerYValidarFechaPartido()
    * Salida:
    *   -Date fechaValida
    * Postcondiciones: El método devuelve un tipo Date asociado al nombre,
    * que es la fecha válida.
    * */
    public Date leerYValidarFechaPartido(){
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
    * Cabecera: public boolean fechaValida(int anno, int mes, int dia)
    * Entrada:
    *   -int anno
    *   -int mes
    *   -int dia
    * Salida:
    *   -boolean fechaValida
    * Postcondiciones: Este método nos devuelve un valor booleano asociado al nombre,
    * true si la fecha es válida o false en caso contrario.
    * */
    public boolean fechaValida(int anno, int mes, int dia){
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
     * Cabecera: public double leerYValidarMaximoApuestaTipo3()
     * Salida:
     *   -double maximoApuesta
     * Postcondiciones: El método devuelve un número entero asociado al nombre,
     * que es el máximo posible de capital que se puede apostar al tipo 3.
     * */
    public double leerYValidarMaximoApuestaTipo3(){
        Scanner teclado = new Scanner(System.in);
        double maximoApuesta = 0.0;

        do{
            System.out.println("Indica el maximo para apuestas del tipo 3 en este partido (Debe ser mayor que 0).");
            maximoApuesta = teclado.nextDouble();
        }while (maximoApuesta <= 0);

        return maximoApuesta;
    }


    /**
     * Interfaz
     * Nombre: abrirPartido
     * Comentario: Abre el partido del mismo id que el que se le pasa
     * Cabecera: public boolean abrirPartido(int idPartido)
     * Entrada:
     *  @param idPartido el id del partio que se quiere abrir
     * Salida:
     *  @return un boolean que nos dice si se ha ejecutado bien
     */
    public boolean abrirPartido(int idPartido){

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

    /**
     * Interfaz
     * Nombre: cerrarPartido
     * Comentario: Cierra el partido que tenga el id que le pasas
     * Cabecera: public boolean cerrarPartido(int idPartido)
     * Entrada:
     *  @param idPartido el id del partio que se quiere cerrar
     * Salida:
     *  @return un boolean que nos dice si se ha ejecutado bien
     */
    public boolean cerrarPartido(int idPartido){
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
     * Interfaz
     * Nombre: verPartidosCerrados
     * Comentario: Este metodo nos muestra todos los partidos.
     * Cabecera: public void verPartidos()
     * Postcondiciones: El método muestra por pantalla los partidos que se encuentren cerrados.
     */
   public void verPartidosnoAbiertos(){

       try {
           clsConexion miconexion = new clsConexion();
           String miSelect = "SELECT ID, fechaPartido FROM Partidos WHERE IsAbierto = 0";

           // Crear una connexion con el DriverManager
           miconexion.abrirConexion();
           Connection connexionBaseDatos = miconexion.getConnexionBaseDatos();
           Statement sentencia = connexionBaseDatos.createStatement();
           ResultSet partidos = sentencia.executeQuery(miSelect);

           // Mostrar los datos del ResultSet
           while (partidos.next()) {
               System.out.println(partidos.getString("ID") + " Fecha del Partido: -> " + partidos.getTimestamp("fechaPartido"));
           }

           // Cerrar conexion
           connexionBaseDatos.close();
       } catch (SQLException sqle) {
           System.err.println(sqle);
       }

   }

    /**
     * Interfaz
     * Nombre: leerIDpartido
     * Comentario: Lee y valida el ID de un partido.
     * Cabecera: public int leerIDpartido()
     * Salida:
     *  @return Devuelve asociado al nombre el ID del partido seleccionado.
     */
    public int leerIDpartido(){
        int partido;
        Scanner teclado = new Scanner(System.in);

        do{
            verPartidosnoAbiertos(); //Mostramos los partidos disponibles.
            System.out.println("Elige un partido de entre los mostrados.");
            partido = teclado.nextInt();
        }while(!partidoEncontradoYCerrado(partido));

        return partido;
    }

    /**
    * Interfaz
    * Nombre: partidoEncontrado
    * Comentario: Este método nos permite verificar si existe un partido con
    * una id específica ne la base de datos y ademas esta cerrado..
    * Cabecera: public boolean partidoEncontradoYCerrado(int idPartido)
    * Entrada:
    *   -int idPartido
    * Salida:
    *   -boolean ret
    * Postcondiciones: El método devuelve un valor booleano asociado al nombre,
    * true si el partido existe en la base de datos o false en caso contrario.
    * */
    public boolean partidoEncontradoYCerrado(int idPartido){
        boolean ret = false;

        //Buscamos el partido
        //Hacemos un SELECT con ese ID y si devuelve una fila es que existe.
        try {
            clsConexion miConexion = new clsConexion();
            String miSelect = "SELECT id FROM Partidos where id = ? AND IsAbierto = 0";

            // Crear una connexion con el DriverManager
            miConexion.abrirConexion();
            Connection connexionBaseDatos = miConexion.getConnexionBaseDatos();
            PreparedStatement sentencia = connexionBaseDatos.prepareStatement(miSelect);
            sentencia.setInt(1, idPartido);
            ResultSet partidos = sentencia.executeQuery();

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
     * Interfaz
     * Nombre: consultarApuestasPartido
     * Comentario: Este metodo nos permitira consultar todas las apuestas y
     * cuanto dinero se ha apostado a cada posible resultado
     * Cabecera: public void consultarApuestasPartido(int idPartido)
     * Entrada:
     *  @param idPartido el ide del partido que queremos consultar
     * Postcondiciones: La función muestra por pantalla los datos de todas las
     * apuestas de un partido específico. Si no existe un partido con esa id en
     * la base de datos, no se muestra nada.
     */
    public void consultarApuestasPartido(int idPartido){

        ResultSet resultado;
        clsConexion miconexion = new clsConexion();
        int tipo = 1, golesL = 0, golesV = 0;
        Connection connexionBaseDatos;

        try {
            //apuestas tipo 1
            //Conexiones
            miconexion.abrirConexion();
            connexionBaseDatos = miconexion.getConnexionBaseDatos();
            Statement sentencia = connexionBaseDatos.createStatement();

            System.out.println("--Apuestas Tipo 1--");
            //Llamo a la primera funcion
            CallableStatement callStatementApuesta1 = miconexion.getConnexionBaseDatos().prepareCall("{call consultarApuestaTipo1(?)}");
            callStatementApuesta1.setInt(1, idPartido);
            resultado = callStatementApuesta1.executeQuery();

            while (resultado.next()) {
                if (resultado.getString("NumGolesLocal") != null) {
                    System.out.println("Goles locales: " + resultado.getInt("NumGolesLocal") + " Goles visitante: " + resultado.getInt("numGolesVisitante") + " Dinero apostado: " + resultado.getInt("DineroApostado"));
                }
            }

            System.out.println("--Apuestas Tipo 2--");
            //APUESTAS tipo 2
            //Llamo a la segunda funcion
            CallableStatement callStatementApuesta2 = miconexion.getConnexionBaseDatos().prepareCall("{call consultarApuestaTipo2(?)}");
            //Visitante
            callStatementApuesta2.setInt(1, idPartido);
            resultado = callStatementApuesta2.executeQuery();
            if (resultado.next()) {
                if (resultado.getString("DineroApostado") != null) {
                    System.out.println("Goles: " + resultado.getInt("goles") + " Equipo ganador " + resultado.getString("equipo") + " Dinero partido: " + resultado.getInt("DineroApostado"));
                }
            }

            System.out.println("--Apuestas Tipo 3--");
            //apuestas tipo 3
            //Local
            CallableStatement callStatementApuesta3 = miconexion.getConnexionBaseDatos().prepareCall("{call consultarApuestaTipo3(?)}");
            tipo++;
            callStatementApuesta3.setInt(1,idPartido);
            ResultSet resultado3L = callStatementApuesta3.executeQuery();
            if (resultado3L.next()) {
                if (resultado3L.getString("DineroApostado") != null) {
                    System.out.println("Equipo ganandor" + resultado3L.getString("Ganador") + " dinero " + resultado3L.getInt("DineroApostado"));
                }
            }
        }
        catch (SQLException r) {
            r.printStackTrace();
        }
        finally {

        }

    }

    /**
    * Interfaz
    * Nombre: leerYValidarPartidoConApuestasSinContabilizar
    * Comentario: Este método nos permite obtener la id de un partido con apuestas
    * sin contabilizar.
    * Cabecera: public int leerYValidarPartidoConApuestasSinContabilizar()
    * Salida:
    *   -int idPartido
    * Precondiciones:
    *   -Deben exitir en la base de datos partidos con apuestas sin finalizar.
    * Postcondiciones: El método devuelve un número entero asociado al nombre,
    * que es el id del partido con apuestas sin finalizar.
    * */
    public int leerYValidarPartidoConApuestasSinContabilizar(){
        UtilidadesComunes uComunes = new UtilidadesComunes();
        int idPartido = 0;
        Scanner teclado = new Scanner(System.in);

        do{
            System.out.println("Elige un partido: ");
            uComunes.mostrarPartidosConApuestasNoContabilizadas();
            idPartido = teclado.nextInt();
        }while (!uComunes.partidoConApuestasSinContabilizar(idPartido));

        return idPartido;
    }

    /**
     * Interfaz
     * Nombre: pagarApuestasPartido
     * Comentario: Este método nos permite pagar las apuestas sin contabilizar de un partido.
     * Cabecera: public void pagarApuestasPartido(int idPartido)
     * Entrada:
     *   -int idPartido
     * Postcondiciones: El método paga las apuestas victoriosas que se encontraban sin contabilizar
     * de un partido, si el partido no existe o si no contenía apuestas sin contabilizar, el método
     * no realiza ninguna modificación.
     * */
    public void pagarApuestasPartido(int idPartido){
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

package Gestion;

import Conexion.clsConexion;
import Gestion.User.UtilidadesUser;

import java.sql.*;
import java.util.Calendar;
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
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Define the data source for the driver
            //String sourceURL = "jdbc:sqlserver://localhost";
            //String usuario = "pepito";
            //String password = "qq";
            clsConexion miconexion = new clsConexion();
            String miSelect = "SELECT ID, fechaPartido FROM Partidos WHERE isAbierto = 1";

            miconexion.abrirConexion();
            Connection connexionBaseDatos = miconexion.getConnexionBaseDatos();
            Statement sentencia = connexionBaseDatos.createStatement();
            ResultSet partidos = sentencia.executeQuery(miSelect);

            // Mostrar los datos del ResultSet
            while (partidos.next()) {
                System.out.println("Partido: " + partidos.getString("ID") + " Abierto desde: " + partidos.getTimestamp("fechaPartido"));
            }

            // Cerrar conexion
            connexionBaseDatos.close();
        }catch (SQLException sqle) {
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
                //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                // Define the data source for the driver
                //String sourceURL = "jdbc:sqlserver://localhost";
                //String usuario = "pepito";
                //String password = "qq";
                clsConexion miconexion = new clsConexion();
                String miSelect = "SELECT id FROM Partidos where id = " +idPartido+" AND isAbierto = 1";

                miconexion.abrirConexion();
                Connection connexionBaseDatos = miconexion.getConnexionBaseDatos();
                Statement sentencia = connexionBaseDatos.createStatement();
                ResultSet partidos = sentencia.executeQuery(miSelect);

                // Mostrar los datos del ResultSet
                if(partidos.next()){ //Si tiene una fila
                    ret = true;
                }
                // Cerrar conexion
                connexionBaseDatos.close();
            }catch (SQLException sqle) {
                System.err.println(sqle);
            }

        return ret;
    }

    /**
     * Permite saber si existe algun partido abierto
     * @return True en caso de existir alguno, false en caso contrario.
     */
    public static boolean existenPartidosAbiertos(){
        boolean ret = false;

        //Buscamos el partido
        //Hacemos un SELECT con ese ID y si devuelve una fila es que existe.
        try {
            // Carga la clase del driver
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Define the data source for the driver
            //String sourceURL = "jdbc:sqlserver://localhost";
            //String usuario = "pepito";
            //String password = "qq";
            clsConexion miconexion = new clsConexion();
            String miSelect = "SELECT * FROM Partidos where isAbierto = 1";

            // Crear una connexion con el DriverManager
            miconexion.abrirConexion();
            Connection connexionBaseDatos = miconexion.getConnexionBaseDatos();
            Statement sentencia = connexionBaseDatos.createStatement();
            ResultSet partidos = sentencia.executeQuery(miSelect);

            // Mostrar los datos del ResultSet
            if(partidos.next()){ //Si tiene una fila
                ret = true;
            }
            // Cerrar conexion
            connexionBaseDatos.close();
        }catch (SQLException sqle) {
            System.err.println(sqle);
        }
        return ret;
    }

    /**
     * Nos indica si un partido en concreto se encuentra abierto para apostar
     * @param id ID del partido
     * @return True si se encuentra abierto, false en caso contrario.
     */
    public static boolean partidoAbierto(int id){
        boolean ret = false;
        //Hacemos un SELECT con ese ID y si devuelve una fila es que existe.
        try {
            // Carga la clase del driver
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Define the data source for the driver
            //String sourceURL = "jdbc:sqlserver://localhost";
            //String usuario = "pepito";
            //String password = "qq";
            clsConexion miconexion =  new clsConexion();
            String miSelect = "SELECT * FROM Partidos where isAbierto = 1 AND ID="+id;

            // Crear una connexion con el DriverManager
            miconexion.abrirConexion();
            Connection connexionBaseDatos = miconexion.getConnexionBaseDatos();
            Statement sentencia = connexionBaseDatos.createStatement();
            ResultSet partidos = sentencia.executeQuery(miSelect);

            // Mostrar los datos del ResultSet
            if(partidos.next()){ //Si tiene una fila
                ret = true;
            }
            // Cerrar conexion
            connexionBaseDatos.close();
        }catch (SQLException sqle) {
            System.err.println(sqle);
        }
        return ret;
    }

    /**
     * Muestra un mensaje del estado que ha devuelto la operacion seleccionada.
     * @param codMensaje Codigo del mensaje.
     */
    public static void mostrarMensajeOperacion(int codMensaje){
        switch (codMensaje){
            case 0:
                System.out.println("Se ha retirado correctamente.");
                break;
            case -1:
                System.out.println("El correo introducido no es correcto.");
                break;
            case -3:
                System.out.println("No tienes tanto dinero para retirar.");
                break;
            default:
                System.out.println("Dinero ingresado correctamente.");
                break;
        }
    }

    /**
     * Permite saber si una cuenta especifica existe en la base de datos
     * @param user Correo del usuario.
     * @param pass Contraseña del usuario
     * @return True si existe, false en caso contrario.
     */
    public static boolean existeCuenta(String user, String pass){
        boolean ret = false;

        try {
            // Carga la clase del driver
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            clsConexion miConexion = new clsConexion();
            // Define the data source for the driver
            /*String sourceURL = "jdbc:sqlserver://localhost";
            String usuario = "pablo";
            String password = "qq";*/
            String miSelect = "SELECT correo FROM Usuarios where contraseña = " +"'"+pass+"'"+" AND correo = "+"'" + user+"'" ;

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
        //} catch (ClassNotFoundException cnfe) {
            //System.err.println(cnfe);
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }

        return ret;
    }

    /**
    * Interfaz
    * Nombre: leerYValidarTipoApuesta
    * Comentario: Este método nos permite leer y validar el tipo de una apuesta.
    * Cabecera: public static int leerYValidarTipoApuesta()
     * Salida:
     *  -int tipoApuesta
     * Postcondiciones: El método devuelve un número asociado al nombre, que es el
     * número de un tipo de apuesta o 0 si no se ha elegido ninguna.
    * */
    public static int leerYValidarTipoApuesta(){
        int tipoApuesta = 0;
        Scanner teclado = new Scanner(System.in);

        do{
            mostrarMenuTiposApuesta();
            tipoApuesta = teclado.nextInt();
        }while(tipoApuesta < 0 || tipoApuesta > 3);

        return tipoApuesta;
    }

    /**
    * Interfaz
    * Nombre: mostrarMenuTiposApuesta
    * Comentario: Este método nos permite mostrar un menú de tipos de apuesta por pantalla.
    * Cabecera: public void mostrarMenuTiposApuesta()
    * Postcondiciones: Se muestra un menú por pantalla.
    * */
    private static void mostrarMenuTiposApuesta(){
        System.out.println();
        System.out.println("Elige una de los siguientes tipos de apuesta:");
        System.out.println();
        System.out.println("0 --> Cancelar apuesta.");
        System.out.println("1 --> Apuesta tipo 1");
        System.out.println("2 --> Apuesta tipo 2");
        System.out.println("3 --> Apuesta tipo 3");
        System.out.println();
    }

    /**
     * Interfaz
     * Nombre: leerYValidarGolesLocales
     * Comentario: Este método nso permite leer y validar un número de goles de un equipo local.
     * Cabecera: public static int leerYValidarGolesLocales()
     * Salida:
     *   -int goles
     * Postcondiciones: El método devuelve un número entero asociado al nombre,
     * que son el número de goles.
     * */
    public static int leerYValidarGolesLocales(){
        int goles = 0;
        Scanner teclado = new Scanner(System.in);

        do{
            System.out.println("Elige el número de goles del equipo local.");
            System.out.println("El número de goles debe ser entre 0 y 100.");
            goles = teclado.nextInt();
        }while(goles < 0 || goles > 100);

        return goles;
    }

    /**
     * Interfaz
     * Nombre: leerYValidarGolesVisitante
     * Comentario: Este método nso permite leer y validar un número de goles de un equipo local.
     * Cabecera: public static int leerYValidarGolesVisitante()
     * Salida:
     *   -int goles
     * Postcondiciones: El método devuelve un número entero asociado al nombre,
     * que son el número de goles.
     * */
    public static int leerYValidarGolesVisitante(){
        int goles = 0;
        Scanner teclado = new Scanner(System.in);

        do{
            System.out.println("Elige el número de goles del equipo visitante.");
            System.out.println("El número de goles debe ser entre 0 y 100.");
            goles = teclado.nextInt();
        }while(goles < 0 || goles > 100);

        return goles;
    }

    /**
    * Interfaz
    * Nombre: leerYValidarEquipo
    * Comentario: Este método nos permite leer y validar un equipo.
    * Cabecera: public static String leerYValidarEquipo()
    * Salida:
    *   -String equipo
    * Postcondiciones: El método devuelve una cadena asociada al nombre, que
    * indica si el equipo es local o visitante.
    * */
    public static String leerYValidarEquipo(){
        String equipo = "";
        Scanner teclado = new Scanner(System.in);

        do {
            System.out.println("Elige 'local' o 'visitante'");
            equipo = (teclado.next()).toLowerCase();
        }while(!equipo.equals("local") && !equipo.equals("visitante"));
        return equipo;
    }

    /**
    * Interfaz
    * Nombre: leerYValidarGoles
    * Comentario: Este método nso permite leer y validar un número de goles de un equipo.
    * Cabecera: public static int leerYValidarGoles()
    * Salida:
    *   -int goles
    * Postcondiciones: El método devuelve un número entero asociado al nombre,
    * que son el número de goles.
    * */
    public static int leerYValidarGoles(){
        int goles = 0;
        Scanner teclado = new Scanner(System.in);

        do{
            System.out.println("Elige el número de goles del equipo.");
            System.out.println("El número de goles debe ser entre 0 y 100.");
            goles = teclado.nextInt();
        }while(goles < 0 || goles > 100);

        return goles;
    }

    /**
    * Interfaz
    * Nombre: leerYValidarCantidadAApostar
    * Comentario: Este método nos permite obtener una cantidad válida de capital para
    * realizar una apuesta.
    * Cabecera: public static double leerYValidarCantidadAApostar(String correo)
    * Entrada:
    *   -String correo
    * Salida:
    *   -double capital
    * Postcondiciones: El método devuelve un número real asociado al nombre, que es el
    * capital válido.
    * */
    public static double leerYValidarCantidadAApostar(String correo){
        double capital = 0.0, capitalMaximoUsuario = obtenerCapitalMaximoUsuario(correo);
        Scanner teclado = new Scanner(System.in);

        do{
            System.out.print("Tu capital actual: "+capitalMaximoUsuario);
            System.out.print("Indica el capital a apostar, debe ser mayor que 0.");
            capital = teclado.nextDouble();
        }while (capital <= 0 || capital > capitalMaximoUsuario);

        return  capital;
    }

    /**
    * Interfaz
    * Nombre: obtenerCapitalMaximoUsuario
    * Comentario: Este método nos permite obtener el capital máximo en la cuenta
    * de un usurio de la base de datos.
    * Cabecera: public static double obtenerCapitalMaximoUsuario(String correo)
    * Entrada:
    *   -String correo
    * Salida:
    *   -double capitalMaximo
    * Postcondiciones: El método devuelve un número real asociado al nombre, que es el
    * capital máximo del usuario o -1 si no se ha encontrado el usuario en la base de datos.
    * */
    public static double obtenerCapitalMaximoUsuario(String correo){
        double capitalMaximo = -1.0;

        //Hacemos un SELECT con ese ID y si devuelve una fila es que existe.
        try {
            // Carga la clase del driver
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Define the data source for the driver
            //String sourceURL = "jdbc:sqlserver://localhost";
            //String usuario = "pepito";
            //String password = "qq";
            clsConexion miconexion = new clsConexion();
            String miSelect = "SELECT saldoActual FROM Usuarios where correo = '"+correo+"'";

            miconexion.abrirConexion();
            Connection connexionBaseDatos = miconexion.getConnexionBaseDatos();
            Statement sentencia = connexionBaseDatos.createStatement();
            ResultSet usuarios = sentencia.executeQuery(miSelect);

            // Mostrar los datos del ResultSet
            if(usuarios.next()){ //Si tiene una fila
                capitalMaximo = usuarios.getDouble("saldoActual");
            }
            // Cerrar conexion
            connexionBaseDatos.close();
        }catch (SQLException sqle) {
            System.err.println(sqle);
        }

        return capitalMaximo;
    }

    /**
    * Interfaz
    * Nombre: realizarApuestaTipo1
    * Comentario: Este método nos permite realizar una apuesta del tipo 1 en la base de datos.
    * Cabecera: public void realizarApuestaTipo1(Date fechaHora, double capitalAApostar, String correo, int idPartido, int numGolesLocal, int numGolesVisitante)
    * Entrada:
    *   -Date fechaHora
    *   -double capitalAApostar
    *   -String correo
    *   -int idPartido
    *   -int numGolesLocal
    *   -int numGolesVisitante
    * Precondiciones:
    *   -capitalAApostar debe ser mayor que 0.
    *   -idPartido debe ser mayor que 0.
    *   -numGolesLocal y numGolesVisitante deben ser un número entre 0 y 100.
    * Postcondiciones: El método devuelve un número asociado al nombre, 1 si se ha conseguido
    * insertar la nueva apuesta o 0 en caso contrario.
    * */
    public static int realizarApuestaTipo1(Date fechaHora, double capitalAApostar, String correo, int idPartido, int numGolesLocal, int numGolesVisitante){
        clsConexion conexion = new clsConexion();
        int resultado = 0, error = 0;

        try {
            conexion.abrirConexion();
            CallableStatement cstmt = conexion.getConnexionBaseDatos().prepareCall("{call realizarApuestaTipo1(?,?,?,?,?,?,?)}"); //Aqui llamamos al procedimiento que queramos.
            cstmt.setString(1, correo);
            cstmt.setDouble(2,capitalAApostar);
            cstmt.setString(3, correo);
            cstmt.setInt(4, idPartido);
            cstmt.setInt(5, numGolesLocal);
            cstmt.setInt(6, numGolesVisitante);
            cstmt.setInt(7, error);//Por si acaso
            resultado = cstmt.executeUpdate();  //Filas afectadas
            conexion.cerrarConexion();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return resultado;
    }

    /**
     * Interfaz
     * Nombre: realizarApuestaTipo2
     * Comentario: Este método nos permite realizar una apuesta del tipo 1 en la base de datos.
     * Cabecera: public void realizarApuestaTipo2(Date fechaHora, double capitalAApostar, String correo, int idPartido, String equipo, int goles)
     * Entrada:
     *   -Date fechaHora
     *   -double capitalAApostar
     *   -String correo
     *   -int idPartido
     *   -String equipo
     *   -int goles
     * Precondiciones:
     *   -capitalAApostar debe ser mayor que 0.
     *   -idPartido debe ser mayor que 0.
     *   -goles debe ser un número entre 0 y 100.
     *   -equipo debe ser igual a 'local' o 'visitante'
     * Postcondiciones: El método devuelve un número asociado al nombre, 1 si se ha conseguido
     * insertar la nueva apuesta o 0 en caso contrario.
     * */
    public static int realizarApuestaTipo2(Date fechaHora, double capitalAApostar, String correo, int idPartido, String equipo, int goles){
        clsConexion conexion = new clsConexion();
        int resultado = 0, error = 0;

        try {
            conexion.abrirConexion();
            CallableStatement cstmt = conexion.getConnexionBaseDatos().prepareCall("{call realizarApuestaTipo2(?,?,?,?,?,?,?)}"); //Aqui llamamos al procedimiento que queramos.
            cstmt.setString(1, correo);
            cstmt.setDouble(2,capitalAApostar);
            cstmt.setString(3, correo);
            cstmt.setInt(4, idPartido);
            cstmt.setString(5, equipo);
            cstmt.setInt(6, goles);
            cstmt.setInt(7, error);//Por si acaso
            resultado = cstmt.executeUpdate();  //Filas afectadas
            conexion.cerrarConexion();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return resultado;
    }

    /**
     * Interfaz
     * Nombre: realizarApuestaTipo3
     * Comentario: Este método nos permite realizar una apuesta del tipo 1 en la base de datos.
     * Cabecera: public void realizarApuestaTipo3(Date fechaHora, double capitalAApostar, String correo, int idPartido, String equipo)
     * Entrada:
     *   -Date fechaHora
     *   -double capitalAApostar
     *   -String correo
     *   -int idPartido
     *   -String equipo
     * Precondiciones:
     *   -capitalAApostar debe ser mayor que 0.
     *   -idPartido debe ser mayor que 0.
     *   -equipo debe ser igual a 'local' o 'visitante'
     * Postcondiciones: El método devuelve un número asociado al nombre, 1 si se ha conseguido
     * insertar la nueva apuesta o 0 en caso contrario.
     * */
    public static int realizarApuestaTipo3(Date fechaHora, double capitalAApostar, String correo, int idPartido, String equipo){
        clsConexion conexion = new clsConexion();
        int resultado = 0, error = 0;

        try {
            conexion.abrirConexion();
            CallableStatement cstmt = conexion.getConnexionBaseDatos().prepareCall("{call realizarApuestaTipo3(?,?,?,?,?,?)}"); //Aqui llamamos al procedimiento que queramos.
            cstmt.setString(1, correo);
            cstmt.setDouble(2,capitalAApostar);
            cstmt.setString(3, correo);
            cstmt.setInt(4, idPartido);
            cstmt.setString(5, equipo);
            cstmt.setInt(6, error);//Por si acaso
            resultado = cstmt.executeUpdate();  //Filas afectadas
            conexion.cerrarConexion();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return resultado;
    }

    /*
    * Interfaz
    * Nombre: convertUtilToSql
    * Comentario: Este método nos permite convertir un tipo java.util.Date
    * a un tipo java.sql.Date.
    * Cabecera: private static java.sql.Date convertUtilToSql(java.util.Date uDate)
    * Entrada:
    *   -java.util.Date uDate
    * Salida:
    *   -java.sql.Date sDate
    * Postcondiciones: El método devuelve un tipo java.util.Date asociado al nombre,
    * que es la conversión del tipo java.util.Date.
    * */
    public static java.sql.Date convertUtilToSql(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }
}

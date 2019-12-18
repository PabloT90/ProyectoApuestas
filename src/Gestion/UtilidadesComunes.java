package Gestion;

import Conexion.clsConexion;
import Gestion.User.UtilidadesUser;

import java.sql.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Scanner;

public class UtilidadesComunes {

    /**
     * Interfaz
     * Nombre: leerIDpartido
     * Comentario: Lee y valida el ID de un partido.
     * Cabecera: public int leerIDpartido()
     * Salida:
     *  -int partido
     * Postcondiciones: Devuelve asociado al nombre el ID del partido seleccionado.
     */
    public int leerIDpartido(){
        int partido;
        Scanner teclado = new Scanner(System.in);

        do{
            verPartidosDisponiblesParaApostar(); //Mostramos los partidos disponibles.
            System.out.println("Elige un partido de entre los mostrados.");
            partido = teclado.nextInt();
        }while(!partidoEncontradoAbierto(partido));

        return partido;
    }
    /**
    * Interfaz
    * Nombre: verPartidosDisponiblesParaApostar
    * Comentario: Muestra todos los partidos a los que se puede apostar. Esto es,
    * los partido que se encuentran en estado abierto.
    * Cabecera: public void verPartidosDisponiblesParaApostar()
    * Postcondiciones: La función muestra por pantalla los partidos disponibles.
    * */
    public void verPartidosDisponiblesParaApostar() {
        // Carga el driver
        try {
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
     * Interfaz
     * Nombre: partidoEncontrado
     * Comentario: Permite saber si existe un partido con el ID recibido como parametro
     * Cabecera: public boolean partidoEncontrado(int idPartido)
     * Entrada:
     *  @param idPartido ID del partido que queremos buscar.
     * Salida:
     *  @return True en caso de existir, false en caso contrario.
     */
    public boolean partidoEncontradoAbierto(int idPartido){
        boolean ret = false;

            //Buscamos el partido
            //Hacemos un SELECT con ese ID y si devuelve una fila es que existe.
            try {
                clsConexion miconexion = new clsConexion();
                //String miSelect = "SELECT id FROM Partidos where id = " +idPartido+" AND isAbierto = 1";
                String miSelect = "SELECT id FROM Partidos where id = ? AND isAbierto = 1" ;
                miconexion.abrirConexion();
                Connection connexionBaseDatos = miconexion.getConnexionBaseDatos();
                PreparedStatement sentencia = connexionBaseDatos.prepareStatement(miSelect);
                sentencia.setInt(1,idPartido);
                ResultSet partidos = sentencia.executeQuery();

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
     * Interfaz
     * Nombre: existenPartidosAbiertos
     * Comentario: Permite saber si existe algun partido abierto
     * Cabecera: public boolean existenPartidosAbiertos()
     * Salida:
     *  @return True en caso de existir alguno, false en caso contrario.
     */
    public boolean existenPartidosAbiertos(){
        boolean ret = false;
        //Buscamos el partido
        //Hacemos un SELECT con ese ID y si devuelve una fila es que existe.
        try {
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
     * Interfaz
     * Nombre: partidoAbierto
     * Comentario: Nos indica si un partido en concreto se encuentra abierto para apostar.
     * Entrada:
     *  @param id ID del partido
     * Salida:
     *  @return True si se encuentra abierto, false en caso contrario.
     */
    public boolean partidoAbierto(int id){
        boolean ret = false;
        //Hacemos un SELECT con ese ID y si devuelve una fila es que existe.
        try {
            clsConexion miconexion =  new clsConexion();
            //String miSelect = "SELECT * FROM Partidos where isAbierto = 1 AND ID="+id;
            String miSelect = "SELECT * FROM Partidos where isAbierto = 1 AND ID=?";
            // Crear una connexion con el DriverManager
            miconexion.abrirConexion();
            Connection connexionBaseDatos = miconexion.getConnexionBaseDatos();
            PreparedStatement sentencia = connexionBaseDatos.prepareStatement(miSelect);
            sentencia.setInt(1,id);
            ResultSet partidos = sentencia.executeQuery();

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
     * Interfaz
     * Nombre: mostrarMensajeOperacion
     * Comentario: Muestra un mensaje del estado que ha devuelto la operacion seleccionada.
     * Cabecera: public static void mostrarMensajeOperacion(int codMensaje)
     * Entrada:
     *  @param codMensaje Codigo del mensaje.
     * Postcondiciones: La función muestra por pantalla un mensaje según una operación seleccionada.
     */
    public void mostrarMensajeOperacion(int codMensaje){
        switch (codMensaje){
            case 0: case 1:
                System.out.println("Operacion realizada correctamente.");
                break;
            case -1:
                System.out.println("La cantidad introducida no es correcta");
                break;
            case -2:
                System.out.println("No tienes tanto dinero para retirar.");
                break;

        }
    }

    /**
     * Interfaz
     * Nombre: existeCuenta
     * Comentario: Permite saber si una cuenta especifica existe en la base de datos
     * Cabecera: public boolean existeCuenta(String user, String pass)
     * Entrada:
     *  @param user Correo del usuario.
     *  @param pass Contraseña del usuario
     * Salida:
     *  @return True si existe, false en caso contrario.
     */
    public boolean existeCuenta(String user, String pass){
        boolean ret = false;

        try {
            // Carga la clase del driver
            clsConexion miConexion = new clsConexion();
            String miSelect = "SELECT correo FROM Usuarios WHERE contraseña = ? AND correo = ?" ;
            // Crear una connexion con el DriverManager
            miConexion.abrirConexion();
            Connection connexionBaseDatos = miConexion.getConnexionBaseDatos();
            PreparedStatement sentencia = connexionBaseDatos.prepareStatement(miSelect);
            sentencia.setString(2,user);
            sentencia.setString(1,pass);
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
    * Nombre: leerYValidarTipoApuesta
    * Comentario: Este método nos permite leer y validar el tipo de una apuesta.
    * Cabecera: public int leerYValidarTipoApuesta()
    * Salida:
    *  -int tipoApuesta
    * Postcondiciones: El método devuelve un número asociado al nombre, que es el
    * número de un tipo de apuesta o 0 si no se ha elegido ninguna.
    * */
    public int leerYValidarTipoApuesta(){
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
    private void mostrarMenuTiposApuesta(){
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
     * Cabecera: public int leerYValidarGolesLocales()
     * Salida:
     *   -int goles
     * Postcondiciones: El método devuelve un número entero asociado al nombre,
     * que son el número de goles.
     * */
    public int leerYValidarGolesLocales(){
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
     * Cabecera: public int leerYValidarGolesVisitante()
     * Salida:
     *   -int goles
     * Postcondiciones: El método devuelve un número entero asociado al nombre,
     * que son el número de goles.
     * */
    public int leerYValidarGolesVisitante(){
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
    * Cabecera: public String leerYValidarEquipo()
    * Salida:
    *   -String equipo
    * Postcondiciones: El método devuelve una cadena asociada al nombre, que
    * indica si el equipo es local o visitante.
    * */
    public String leerYValidarEquipo(){
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
     * Nombre: leerValidarEjecutar
     * Comentario: Lee y valida si el usuario quiere ejecutar una aplicacion.
     * Cabecera: public char leerValidarEjecutar()
     * Salida:
     *  -char ejecutar
     * Postcondiciones: La función devuelve un carácter asociado al nombre,
     * que es una ejecución válida para la aplicación.
     */
    public char leerValidarEjecutar(){
        Scanner teclado = new Scanner(System.in);
        char ejecutar;

        do{
            System.out.println("Quieres ejecutar?");
            ejecutar = Character.toLowerCase(teclado.next().charAt(0));
        }while(ejecutar != 's' && ejecutar != 'n');

        return ejecutar;
    }

    /**
    * Interfaz
    * Nombre: leerYValidarGoles
    * Comentario: Este método nso permite leer y validar un número de goles de un equipo.
    * Cabecera: public int leerYValidarGoles()
    * Salida:
    *   -int goles
    * Postcondiciones: El método devuelve un número entero asociado al nombre,
    * que son el número de goles.
    * */
    public int leerYValidarGoles(){
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
    * Cabecera: public double leerYValidarCantidadAApostar(String correo)
    * Entrada:
    *   -String correo
    * Salida:
    *   -double capital
    * Postcondiciones: El método devuelve un número real asociado al nombre, que es el
    * capital válido.
    * */
    public double leerYValidarCantidadAApostar(String correo){
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
    * Cabecera: public double obtenerCapitalMaximoUsuario(String correo)
    * Entrada:
    *   -String correo
    * Salida:
    *   -double capitalMaximo
    * Postcondiciones: El método devuelve un número real asociado al nombre, que es el
    * capital máximo del usuario o -1 si no se ha encontrado el usuario en la base de datos.
    * */
    public double obtenerCapitalMaximoUsuario(String correo){
        double capitalMaximo = -1.0;
        try {
            clsConexion miconexion = new clsConexion();
            String miSelect = "SELECT saldoActual FROM Usuarios where correo = ?";

            miconexion.abrirConexion();
            Connection connexionBaseDatos = miconexion.getConnexionBaseDatos();
            PreparedStatement sentencia = connexionBaseDatos.prepareStatement(miSelect);
            sentencia.setString(1,correo);
            ResultSet usuarios = sentencia.executeQuery();

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
    public int realizarApuestaTipo1(Date fechaHora, double capitalAApostar, String correo, int idPartido, int numGolesLocal, int numGolesVisitante){
        clsConexion conexion = new clsConexion();
        int resultado = 0, error = 0;

        try {
            conexion.abrirConexion();
            CallableStatement cstmt = conexion.getConnexionBaseDatos().prepareCall("{call realizarApuestaTipo1(?,?,?,?,?,?)}"); //Aqui llamamos al procedimiento que queramos.
            cstmt.setDouble(1,capitalAApostar);
            cstmt.setString(2, correo);
            cstmt.setInt(3, idPartido);
            cstmt.setInt(4, numGolesLocal);
            cstmt.setInt(5, numGolesVisitante);
            cstmt.setInt(6, error);//Por si acaso*/
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
    public int realizarApuestaTipo2(Date fechaHora, double capitalAApostar, String correo, int idPartido, String equipo, int goles){
        clsConexion conexion = new clsConexion();
        int resultado = 0, error = 0;

        try {
            conexion.abrirConexion();
            CallableStatement cstmt = conexion.getConnexionBaseDatos().prepareCall("{call realizarApuestaTipo2(?,?,?,?,?,?)}"); //Aqui llamamos al procedimiento que queramos.
            cstmt.setDouble(1,capitalAApostar);
            cstmt.setString(2, correo);
            cstmt.setInt(3, idPartido);
            cstmt.setString(4, equipo);
            cstmt.setInt(5, goles);
            cstmt.setInt(6, error);
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
    public int realizarApuestaTipo3(Date fechaHora, double capitalAApostar, String correo, int idPartido, String equipo){
        clsConexion conexion = new clsConexion();
        int resultado = 0, error = 0;

        try {
            conexion.abrirConexion();
            CallableStatement cstmt = conexion.getConnexionBaseDatos().prepareCall("{call realizarApuestaTipo3(?,?,?,?,?)}");
            cstmt.setDouble(1, capitalAApostar);
            cstmt.setString(2, correo);
            cstmt.setInt(3, idPartido);
            cstmt.setString(4, equipo);
            cstmt.setInt(5, error);//Por si acaso*/
            resultado = cstmt.executeUpdate();  //Filas afectadas
            System.out.println(error);
            conexion.cerrarConexion();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return resultado;
    }

    /**
    * Interfaz
    * Nombre: convertUtilTimeStamp
    * Comentario: Este método nos permite convertir un tipo java.util.Date
    * a un tipo java.sql.Timestamp.
    * Cabecera: private java.sql.Date convertUtilTimeStamp(java.util.Date uDate)
    * Entrada:
    *   -java.util.Date uDate
    * Salida:
    *   -java.sql.Timestamp ts
    * Postcondiciones: El método devuelve un tipo java.sql.Timestamp asociado al nombre,
    * que es la conversión del tipo java.util.Date.
    * */
    public java.sql.Timestamp convertUtilTimeStamp(java.util.Date uDate) {
        Timestamp ts = new Timestamp(uDate.getTime());
        return ts;
    }

    /**
     * Interfaz
     * Nombre: convertUtilToSql
     * Comentario: Este método nos permite convertir un tipo java.util.Date
     * a un tipo java.sql.Date.
     * Cabecera: private java.sql.Date convertUtilToSql(java.util.Date uDate)
     * Entrada:
     *   -java.util.Date uDate
     * Salida:
     *   -java.sql.Date sDate
     * Postcondiciones: El método devuelve un tipo java.util.Date asociado al nombre,
     * que es la conversión del tipo java.util.Date.
     * */
    public java.sql.Date convertUtilToSql(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }

    /**
    * Interfaz
    * Nombre: mostrarPartidosConApuestasNoContabilizadas
    * Comentario: Este función nos permite mostrar los partidos con apuestas
    * no contabilizadas.
    * Cabecera: public void mostrarPartidosConApuestasNoContabilizadas()
    * Postcondiciones: Este método nos permite mostrar por pantalla los partidos
    * con apuestas no contabilizaadas.
    * */
    public void mostrarPartidosConApuestasNoContabilizadas(){
        clsConexion conexion = new clsConexion();
        ResultSet resultado;

        try {
            conexion.abrirConexion();
            CallableStatement callStatement = conexion.getConnexionBaseDatos().prepareCall("{call partidosConApuestasSinContabilizar2}");
            resultado = callStatement.executeQuery();

            while (resultado.next()){
                System.out.println("Id partido: "+resultado.getInt("IDpartido"));//Preguntar a pablo esta parte por si acaso
            }
            conexion.cerrarConexion();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Interfaz
     * Nombre: existenPartidosConApuestasSinContabilizar
     * Comentario: Este método nos permite verificar si existen partidos con
     * apuestas sin contabilizar.
     * Cabecera: public boolean existenPartidosConApuestasSinContabilizar()
     * Salida:
     *  -boolean ret
     * Postcondiciones: El método devuelve un valor booleano asociado al nombre,
     * true si existen partidos con apuestas sin contabilizar o false en caso contrario.
     */
    public boolean existenPartidosConApuestasSinContabilizar(){
        boolean ret = false;
        clsConexion conexion = new clsConexion();

        try {
            conexion.abrirConexion();
            CallableStatement callStatement = conexion.getConnexionBaseDatos().prepareCall("{call partidosConApuestasSinContabilizar2}");
            //callStatement.registerOutParameter(1, Types.JAVA_OBJECT);
            ResultSet resultado = callStatement.executeQuery();

            if (resultado.next()){//Si tiene alguna fila, significa que existe un partido con apuestas sin contabilizar
                ret = true;
            }
            conexion.cerrarConexion();
        }catch (SQLException sqle) {
            System.err.println(sqle);
        }
        return ret;
    }

    /**
    * Interfaz
    * Nombre: partidoConApuestasSinContabilizar
    * Comentario: Este método nos permite verificar si un partido tiene apuestas sin contabilizar.
    * Cabecera: public boolean partidoConApuestasSinContabilizar(int idPartido)
    * Entrada:
    *   -int idPartido
    * Salida:
    *   -boolean resultado
    * Precondiciones:
    *   -El partido debe existir en la base de datos
    * Postcondiciones: El método devuleve un valor booleano asociado al nombre,
    * true si el partido contiene apuestas sin contabilizar o false en caso contrario.
    * */
    public boolean partidoConApuestasSinContabilizar(int idPartido){
        boolean resultado = false;

        clsConexion conexion = new clsConexion();
        ResultSet filas;
        try {
            conexion.abrirConexion();
            CallableStatement callStatement = conexion.getConnexionBaseDatos().prepareCall("{call apuestasSinContabilizarDeUnPartido2(?)}");
            callStatement.setInt(1,idPartido);
            filas = callStatement.executeQuery();

            if (filas.next()){//Si tiene alguna fila, significa que el partido tiene apuestas sin contabilizar
                resultado = true;
            }
            conexion.cerrarConexion();
        }catch (SQLException sqle) {
            System.err.println(sqle);
        }

        return resultado;
    }

    /**
     * Interfaz
     * Nombre: verPartidos
     * Comentario: Muestra todos los partidos.
     * Cabecera: public void verPartidos()
     * Postcondiciones: La función muestra por pantalla todos los partidos
     * */
    public void verPartidos() {
        // Carga el driver
        try {
            clsConexion miconexion = new clsConexion();
            String miSelect = "SELECT ID, fechaPartido FROM Partidos";

            miconexion.abrirConexion();
            Connection connexionBaseDatos = miconexion.getConnexionBaseDatos();
            Statement sentencia = connexionBaseDatos.createStatement();
            ResultSet partidos = sentencia.executeQuery(miSelect);

            // Mostrar los datos del ResultSet
            while (partidos.next()) {
                System.out.println("Partido: " + partidos.getString("ID") + " Fecha: " + partidos.getTimestamp("fechaPartido"));
            }

            // Cerrar conexion
            connexionBaseDatos.close();
        }catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }

    /**
     * Interfaz
     * Nombre: leerIDpartido
     * Comentario: Lee y valida el ID de un partido.
     * Cabecera: public int leerIDpartido2()
     * Salida:
     *  -int partido
     * Postcondiciones: Devuelve asociado al nombre el ID del partido seleccionado.
     */
    public int leerIDpartido2(){
        int partido;
        Scanner teclado = new Scanner(System.in);

        do{
            verPartidos(); //Mostramos los partidos disponibles.
            System.out.println("Elige un partido de entre los mostrados.");
            partido = teclado.nextInt();
        }while(!partidoEncontrado(partido));

        return partido;
    }

    /**
     * Interfaz
     * Nombre: partidoEncontrado
     * Comentario: Permite saber si existe un partido con el ID recibido como parametro
     * Cabecera: public boolean partidoEncontrado(int idPartido)
     * Entrada:
     *  @param idPartido ID del partido que queremos buscar.
     * Salida:
     *  @return True en caso de existir, false en caso contrario.
     */
    public boolean partidoEncontrado(int idPartido){
        boolean ret = false;

        //Buscamos el partido
        //Hacemos un SELECT con ese ID y si devuelve una fila es que existe.
        try {
            clsConexion miconexion = new clsConexion();
            //String miSelect = "SELECT id FROM Partidos where id = " +idPartido+" AND isAbierto = 1";
            String miSelect = "SELECT id FROM Partidos where id = ?" ;
            miconexion.abrirConexion();
            Connection connexionBaseDatos = miconexion.getConnexionBaseDatos();
            PreparedStatement sentencia = connexionBaseDatos.prepareStatement(miSelect);
            sentencia.setInt(1,idPartido);
            ResultSet partidos = sentencia.executeQuery();

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
}

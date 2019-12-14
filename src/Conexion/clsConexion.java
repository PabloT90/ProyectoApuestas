package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class clsConexion {
    private String sourceURL;
    private String usuario;
    private String password;
    private Connection connexionBaseDatos;

    public clsConexion(){
        sourceURL="jdbc:sqlserver://localhost";
        //sourceURL="jdbc:sqlserver://KROOM";
        usuario="pepito"; //Cambiar a pablo
        password="qq";
    }
    public clsConexion(String sourceURL, String usuario, String password){
        this.sourceURL=sourceURL;
        this.password=password;
        this.usuario=usuario;
    }
    public void abrirConexion() throws SQLException {
        connexionBaseDatos = DriverManager.getConnection(sourceURL,
                usuario, password);
    }
    public Connection getConnexionBaseDatos(){
        return connexionBaseDatos;
    }
    public void cerrarConexion() throws SQLException {
        connexionBaseDatos.close();
    }
}

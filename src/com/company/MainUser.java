package com.company;
import Gestion.Admin.UtilidadesAdmin;
import Gestion.User.UtilidadesUser;
import Gestion.UtilidadesComunes;

/*
PG N0 MainUser
INICIO
si quiere ejecutar*
	LeerCredenciales*
	si no existeCuenta*
		MostrarMensajeErrorCredenciales
	sino
		mientras no quiera salir
			segun opcionMenu
				caso 1: ApostarPartidos*
				caso 2: VerPartidosDisponibles
				caso 3: ComprobarApuestasAnteriores*
				caso 4: RealizarIngreso
				caso 5: RetirarDinero
				caso 6: VerMovimientos*
			fin_segun
		fin_mientras
	fin_si
fin_si
FIN

Entradas:
    -int opcionMenu
    -int idPartido
    -String correo
    -String pass
    -double capitalApuesta
    -int tipoApuesta
    -int golesLocales
    -int golesVisitantes
    -string equipo
    -int goles
    -int cantidad

Restricciones:
    -opcionMenu solo podra valer 1,2,3,4,5,6 o 0
    -el idPartido tendra que ser mayor que 0
    -el capitalApuesta debe de ser mayor que 0
    -el tipoApuesta solo podra ser 1,2 o 3
    -los golesLocales deben de estar entre 0 y 100
    -los golesVisitante deben de estar entre 0 y 100
    -El equipo solo podra ser 'local' o 'visitante'
    -los goles deben de estar entre 0 y 100
    -la cantidad debe de ser mayor que 0

Salidas:
    -Mensajes de comunicacion con el usuario(errores, informativos...)
    -int CodDevuelto
* */
public class MainUser {
    public static void main(String[] args) {
        UtilidadesComunes uComunes = new UtilidadesComunes();
        UtilidadesAdmin uAdmin = new UtilidadesAdmin();
        UtilidadesUser uUser = new UtilidadesUser();
        int opcionMenu = 0, codDevuelto;
        double capitalApuesta;
        String usuario, pass;

        if(uComunes.leerValidarEjecutar() == 's') {
            //AccederCuentaUsuario
            usuario = uUser.LeerCorreo();
            pass = uUser.LeerPassword();

            if (!uComunes.existeCuenta(usuario, pass)) {
                System.out.println("Credenciales incorrectas para el usuario: " + usuario);
            } else {
                System.out.println("Bienvenido: " + usuario + ". Que quieres hacer?");
                while ((opcionMenu = uUser.MostrarMenuLeerValidarOpcion()) != 0) {
                    switch (opcionMenu) {
                        case 1: //Apostar partidos
                            uUser.apostarAPartido(usuario);
                            break;
                        case 2://Ver partidos disponibles
                            if(uComunes.existenPartidosAbiertos()){
                                uComunes.verPartidosDisponiblesParaApostar();
                            }else{
                                System.out.println("No hay partidos disponibles para apostar.");
                            }
                            break;
                        case 3: //Comprobar resultados de apuestas anteriores
                            uUser.MostrarApuestasAnteriores(usuario);
                            break;
                        case 4: //Realizar ingreso
                            codDevuelto = uUser.ingresarDinero(uUser.LeerValidarDinero(), usuario);
                            uComunes.mostrarMensajeOperacion(codDevuelto);
                            break;
                        case 5: //Retirar dinero
                            codDevuelto = uUser.retirarDinero(uUser.LeerValidarDinero(), usuario);
                            uComunes.mostrarMensajeOperacion(codDevuelto);
                            break;
                        case 6: //Ver movimientos.
                            uUser.mostrarMovimientos(usuario);
                            break;
                    }
                }
            }
        }

    }
}
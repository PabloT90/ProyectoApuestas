package com.company;
import Gestion.Admin.UtilidadesAdmin;
import Gestion.User.UtilidadesUser;
import Gestion.UtilidadesComunes;

import java.sql.Date;
import java.util.Calendar;

/*
Nivel: 0
si quiere ejecutar
    IngresarCuentaUsuario
    mientras no quiera salir (opcionMenu != 7)
        segun(opcionMenu)
            caso 1: ApostarPartido
            caso 2: VerPartidosDisponibles
            caso 3: ComprobarResultadoApuestaAnterior
            caso 4: RealizarIngreso
            caso 5: RetirarDinero
            caso 6: VerMovimientos
        fin_segun
    fin_mientras
fin_si

Nivel: 1 ApostarPartido
Inicio
Si existen partidos abiertos
    leerYValidarIdPartidoAbierto*
    Si idPartidoAbierto != 0
        leerYValidarTipoApuesta*
        Si tipoApuesta != 0
            LeerCaracteristicasApuesta
            leerYValidarCantidadAApostar*
            RealizarApuesta
        Fin_si
    Fin_si
Sino
    MostrarMensajeError
Fin_si
Fin

Nivel: 2

LeerCaracteristicasApuesta
Inicio
    Segun (tipoApuesta)
        para tipoApuesta == 1
            LeerYValidarGolesLocalesYVisitantes

        para tipoApuesta == 2
            LeerYValidarEquipoYGoles

        para tipoApuesta == 3
            leerYValidarGanador*

    Fin_segun
Fin
* */
public class MainUser {
    public static void main(String[] args) {
        int opcionMenu = 0, codDevuelto, idPartido, tipoApuesta, golesLocales = 0, golesVisitantes = 0, goles = 0;
        double capitalApuesta;
        String usuario, pass, equipo = "";
        Date fechaActual;

        if(UtilidadesAdmin.leerValidarEjecutar() == 's') {
            //AccederCuentaUsuario
            usuario = UtilidadesUser.LeerCorreo();
            pass = UtilidadesUser.LeerPassword();

            if (!UtilidadesComunes.existeCuenta(usuario, pass)) {
                System.out.println("Credenciales incorrectas para el usuario: " + usuario);
            } else {
                System.out.println("Bienvenido: " + usuario + ". Que quieres hacer?");
                while ((opcionMenu = UtilidadesUser.MostrarMenuLeerValidarOpcion()) != 0) {
                    switch (opcionMenu) {
                        case 1: //Apostar partidos
                            if (UtilidadesComunes.existenPartidosAbiertos()) { //Si existe algun partido abierto
                                if(UtilidadesComunes.obtenerCapitalMaximoUsuario(usuario) > 0){
                                    // //Si el partido elegido es correcto, es decir, está abierto
                                    if ((idPartido = UtilidadesComunes.leerIDpartido()) != 0) { //Aqui faltaria comprobar que hay algun partido abierto.
                                        //leerYValidarTipoApuesta*
                                        if((tipoApuesta = UtilidadesComunes.leerYValidarTipoApuesta()) != 0){
                                            //LeerCaracteristicasApuesta
                                            switch (tipoApuesta){
                                                case 1:
                                                    //LeerYValidarGolesLocalesYVisitantes
                                                    golesLocales = UtilidadesComunes.leerYValidarGolesLocales();
                                                    golesVisitantes = UtilidadesComunes.leerYValidarGolesVisitante();
                                                    break;
                                                case 2:
                                                    //LeerYValidarEquipoYGoles
                                                    equipo = UtilidadesComunes.leerYValidarEquipo();
                                                    goles = UtilidadesComunes.leerYValidarGoles();
                                                    break;

                                                case 3:
                                                    //leerYValidarEquipo()
                                                    equipo = UtilidadesComunes.leerYValidarEquipo();
                                                    break;
                                            }

                                            //leerYValidarCantidadAApostar*
                                            capitalApuesta = UtilidadesComunes.leerYValidarCantidadAApostar(usuario);
                                            //RealizarApuesta       //Yo esto lo haria en un modulo.
                                            //fechaActual = (Date) (Calendar.getInstance()).getTime();//Obtenemos la fecha actual
                                            fechaActual = UtilidadesComunes.convertUtilToSql(Calendar.getInstance().getTime());
                                            switch (tipoApuesta){
                                                case 1:
                                                    //realizarApuestaTipo1*
                                                    UtilidadesComunes.realizarApuestaTipo1(fechaActual, capitalApuesta, usuario, idPartido, golesLocales, golesVisitantes);
                                                    break;

                                                case 2:
                                                    //realizarApuestaTipo2*
                                                    UtilidadesComunes.realizarApuestaTipo2(fechaActual, capitalApuesta, usuario, idPartido, equipo, goles);
                                                    break;

                                                case 3:
                                                    //realizarApuestaTipo3*
                                                    UtilidadesComunes.realizarApuestaTipo3(fechaActual, capitalApuesta, usuario, idPartido, equipo);
                                                    break;
                                            }
                                        }
                                    }
                                }else{
                                    System.out.println("El ususario no tiene capital para apostar.");
                                }
                            } else {
                                System.out.println("No hay partidos abiertos para apostar.");
                            }
                            break;
                        case 2://Ver partidos disponibles
                            if(UtilidadesComunes.existenPartidosAbiertos()){
                                UtilidadesComunes.verPartidosDisponiblesParaApostar();
                            }else{
                                System.out.println("No hay partidos disponibles para apostar.");
                            }
                            break;
                        case 3: //Comprobar resultados de apuestas anteriores
                            UtilidadesUser.MostrarApuestasAnteriores(usuario);
                            break;
                        case 4: //Realizar ingreso
                            codDevuelto = UtilidadesUser.ingresarDinero(UtilidadesUser.LeerValidarDinero(), usuario);
                            UtilidadesComunes.mostrarMensajeOperacion(codDevuelto);
                            break;
                        case 5: //Retirar dinero
                            codDevuelto = UtilidadesUser.retirarDinero(UtilidadesUser.LeerValidarDinero(), usuario);
                            UtilidadesComunes.mostrarMensajeOperacion(codDevuelto);
                            break;
                        case 6: //Ver movimientos.
                            UtilidadesUser.mostrarMovimientos(usuario);
                            break;
                    }
                }
            }
        }

    }
}
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
        int opcionMenu = 0, codDevuelto;
        double capitalApuesta;
        String usuario, pass;

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
                            UtilidadesUser.apostarAPartido(usuario);
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
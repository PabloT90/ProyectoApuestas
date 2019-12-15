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
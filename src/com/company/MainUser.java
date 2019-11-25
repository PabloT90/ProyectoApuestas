package com.company;
import Gestion.Admin.UtilidadesAdmin;
import Gestion.User.UtilidadesUser;
import Gestion.UtilidadesComunes;
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
        LeerYValidarTipoApuesta
        LeerYValidarCaracteristicasApuesta
        LeerYValidarCantidadApuesta
        RealizarApuesta
    Fin_si
Sino
    MostrarMensajeError
Fin_si
Fin

Nivel: 2 leerYValidarIdPartidoAbierto*
Inicio
     mostrarPartidosAbiertos*
     leerIdPartidoAbiertos*  //Debe permitir leer un 0
Fin

LeerYValidarCaracteristicasApuesta
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
        String usuario, pass;

        if(UtilidadesAdmin.leerValidarEjecutar() == 's') {
            //AccederCuentaUsuario
            usuario = UtilidadesUser.LeerCorreo();
            pass = UtilidadesUser.LeerPassword();

            if (!UtilidadesComunes.existeCuenta(usuario, pass)) {
                System.out.println("Bienvenido: " + usuario);
            } else {
                while ((opcionMenu = UtilidadesUser.MostrarMenuLeerValidarOpcion()) != 0) {
                    switch (opcionMenu) {
                        case 1: //Apostar partidos
                            if (UtilidadesComunes.existenPartidosAbiertos()) { //Si existe algun partido abierto
                                if (UtilidadesComunes.partidoAbierto(UtilidadesComunes.leerIDpartido())) { //Si el partido elegido es correcto, es decir, está abierto
                                    //Apostar al partido elegido
                                } else {
                                    System.out.println("El partido seleccionado no esta disponible para apostar");
                                }
                            } else {
                                System.out.println("No hay partidos abiertos para apostar.");
                            }
                            break;
                        case 2://Ver partidos disponibles
                            UtilidadesComunes.verPartidosDisponiblesParaApostar();
                            break;
                        case 3: //Comprobar resultados de apuestas anteriores

                            break;
                        case 4: //Realizar ingreso
                            codDevuelto = UtilidadesUser.ingresarDinero(UtilidadesUser.LeerValidarDinero(), UtilidadesUser.LeerCorreo());
                            UtilidadesComunes.mostrarMensajeOperacion(codDevuelto);
                            break;
                        case 5: //Retirar dinero
                            codDevuelto = UtilidadesUser.retirarDinero(UtilidadesUser.LeerValidarDinero(), UtilidadesUser.LeerCorreo());
                            UtilidadesComunes.mostrarMensajeOperacion(codDevuelto);
                            break;
                        case 6: //Ver movimientos.

                            break;
                    }
                }
            }
        }

    }
}
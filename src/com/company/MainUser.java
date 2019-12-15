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
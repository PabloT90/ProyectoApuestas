package com.company;
import Gestion.Admin.UtilidadesAdmin;
import Gestion.User.UtilidadesUser;
import Gestion.UtilidadesComunes;

/*
si quiere ejecutar
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
public class Main {
    public static void main(String[] args) {
        int opcionMenu = 0;

        if(UtilidadesAdmin.leerValidarEjecutar() == 's'){
            while((opcionMenu = UtilidadesUser.MostrarMenuLeerValidarOpcion()) != 0){
                switch(opcionMenu){
                    case 1: //Apostar partidos
                        if(true) { //Este tiene que ser si hay algun partido al que apostar.
                            UtilidadesComunes.leerIDpartido();
                            System.out.println("Habia alguno");
                        }
                        break;
                    case 2://Ver partidos disponibles
                        UtilidadesComunes.verPartidosDisponiblesParaApostar();
                        break;
                    case 3: //Comprobar resultados de apuestas anteriores

                        break;
                    case 4: //Realizar ingreso

                        break;
                    case 5: //Retirar dinero

                        break;
                    case 6: //Ver movimientos.

                        break;
                }
            }
        }

    }
}
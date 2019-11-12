package com.company;
import Gestion.Admin.Utilidades;
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

        if(Utilidades.leerValidarEjecutar() == 's'){
            while((opcionMenu = UtilidadesUser.MostrarMenuLeerValidarOpcion()) != 0){
                switch(opcionMenu){
                    case 1: //Apostar partidos
                        
                        break;
                    case 2://Ver partidos disponibles
                        UtilidadesComunes.verPartidosDisponiblesParaApostar();
                        break;
                    case 3: //Co probar resultados de apuestas anteriores

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
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
                        if(UtilidadesComunes.existenPartidosAbiertos()) { //Si existe algun partido abierto
                            if(UtilidadesComunes.partidoAbierto(UtilidadesComunes.leerIDpartido())){ //Si el partido elegido es correcto, es decir, está abierto
                                //Apostar al partido elegido
                            }else{
                                System.out.println("El partido seleccionado no esta disponible para apostar");
                            }
                        }else{
                            System.out.println("No hay partidos abiertos para apostar.");
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
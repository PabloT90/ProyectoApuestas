package com.company;

import Gestion.Admin.UtilidadesAdmin;
import Gestion.UtilidadesComunes;

/*
si quiere ejecutar
    mientras no quiera salir (opcionMenu != 6)
        segun(opcionMenu)
            caso 1: CrearPartido
            caso 2: AbrirPartido
            caso 3: CerrarPartido
            caso 4: ConsultarApuestasPartido
            caso 5: PagarApuestas
        fin_segun
    fin_mientras
fin_si
 */
public class MainAdministradores {
    public static void main(String[]args){
        int opcionMenu = 0, idPartido = 0;

        if(UtilidadesAdmin.leerValidarEjecutar() == 's') {
            while ((opcionMenu = UtilidadesAdmin.MostrarMenuLeerValidarOpcion()) != 0) {
                switch (opcionMenu) {
                    case 1: //Crear partido

                        break;
                    case 2: //Abrir partido

                        break;
                    case 3: //Cerrar partido
                        idPartido = UtilidadesComunes.leerIDpartido();

                        if (!UtilidadesAdmin.cerrarPartido(idPartido)){
                            System.out.println("Ha ocurrido un error");
                        }

                        break;
                    case 4: //Consultar apuestas partido

                        break;
                    case 5: //Pagar apuestas

                        break;
                }

            }
        }

    }
}
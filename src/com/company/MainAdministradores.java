package com.company;

import Gestion.Admin.Utilidades;
import java.util.Scanner;

/*
mientras quiera ejecutar
    MostrarMenu, leeryValidarOpcionMenu
        segun(opcionMenu)
            caso 1: CrearPartido
            caso 2: AbrirPartido
            caso 3: CerrarPartido
            caso 4: ConsultarApuestasPartido
            caso 5: PagarApuestas
        fin_segun
fin_mientras
 */
public class MainAdministradores {
    public static void main(String[]args){

        while(Utilidades.leerValidarEjecutar() == 's'){
            switch(Utilidades.MostrarMenuLeerValidarOpcion()){
                case 1: //Crear partido

                    break;
                case 2: //Abrir partido

                    break;
                case 3: //Cerrar partido

                    break;
                case 4: //Consultar apuestas partido

                    break;
                case 5: //Pagar apuestas

                    break;
            }

        }

    }
}
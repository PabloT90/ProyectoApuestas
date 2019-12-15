package com.company;

import Gestion.Admin.UtilidadesAdmin;
import Gestion.UtilidadesComunes;

import java.time.LocalDate;
import java.util.Date;

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
        double maximoApuesta1, maximoApuesta2, maximoApuesta3;
        Date fechaPartido;

        if(UtilidadesAdmin.leerValidarEjecutar() == 's') {
            while ((opcionMenu = UtilidadesAdmin.MostrarMenuLeerValidarOpcion()) != 0) {
                switch (opcionMenu) {
                    case 1: //Crear partido
                        maximoApuesta1 = UtilidadesAdmin.leerYValidarMaximoApuestaTipo1();
                        maximoApuesta2 = UtilidadesAdmin.leerYValidarMaximoApuestaTipo2();
                        maximoApuesta3 = UtilidadesAdmin.leerYValidarMaximoApuestaTipo3();
                        fechaPartido = UtilidadesAdmin.leerYValidarFechaPartido();

                        UtilidadesAdmin.insertarPartido(maximoApuesta1, maximoApuesta2, maximoApuesta3, UtilidadesComunes.convertUtilToSql(fechaPartido));
                        break;
                    case 2: //Abrir partido

                        idPartido = UtilidadesAdmin.leerIDpartido();

                        if (UtilidadesAdmin.abrirPartido(idPartido)){
                            System.out.println("Se ha abierto correctamente.");
                        }else{
                            System.out.println("No se ha podido abrir.");
                        }

                        break;
                    case 3: //Cerrar partido
                        idPartido = UtilidadesComunes.leerIDpartido();

                        if (UtilidadesAdmin.cerrarPartido(idPartido)){
                            System.out.println("Cerrado correctamente.");
                        }else{
                            System.out.println("No se ha podido cerrar correctamente.");
                        }

                        break;
                    case 4: //Consultar apuestas partido

                        idPartido = UtilidadesComunes.leerIDpartido();

                        UtilidadesAdmin.consultarApuestasPartido(idPartido);

                        break;
                    case 5: //Pagar apuestas
                        if(UtilidadesComunes.existenPartidosConApuestasSinContabilizar()){
                            idPartido = UtilidadesAdmin.leerYValidarPartidoConApuestasSinContabilizar();
                            UtilidadesAdmin.pagarApuestasPartido(idPartido);
                        }else{
                            System.out.println("No existen partidos con apuestas cin sontabilizar.");
                        }
                        break;
                }

            }
        }

    }
}
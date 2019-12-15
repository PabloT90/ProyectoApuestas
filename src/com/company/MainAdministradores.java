package com.company;

import Gestion.Admin.UtilidadesAdmin;
import Gestion.User.UtilidadesUser;
import Gestion.UtilidadesComunes;
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
        UtilidadesComunes uComunes = new UtilidadesComunes();
        UtilidadesAdmin uAdmin = new UtilidadesAdmin();
        UtilidadesUser uUser = new UtilidadesUser();

        int opcionMenu = 0, idPartido = 0;
        double maximoApuesta1, maximoApuesta2, maximoApuesta3;
        Date fechaPartido;

        if(uComunes.leerValidarEjecutar() == 's') {
            while ((opcionMenu = uAdmin.MostrarMenuLeerValidarOpcion()) != 0) {
                switch (opcionMenu) {
                    case 1: //Crear partido
                        maximoApuesta1 = uAdmin.leerYValidarMaximoApuestaTipo1();
                        maximoApuesta2 = uAdmin.leerYValidarMaximoApuestaTipo2();
                        maximoApuesta3 = uAdmin.leerYValidarMaximoApuestaTipo3();
                        fechaPartido = uAdmin.leerYValidarFechaPartido();

                        if(uAdmin.insertarPartido(maximoApuesta1, maximoApuesta2, maximoApuesta3, uComunes.convertUtilToSql(fechaPartido))){
                            System.out.println("Partido insertado.");
                        }else{
                            System.out.println("No se ha podido insertar.");
                        }
                        break;
                    case 2: //Abrir partido
                        idPartido = uAdmin.leerIDpartido();

                        if (uAdmin.abrirPartido(idPartido)){
                            System.out.println("Se ha abierto correctamente.");
                        }else{
                            System.out.println("No se ha podido abrir.");
                        }

                        break;
                    case 3: //Cerrar partido
                        //Comprobar si hay algun partido abierto
                        if(uComunes.existenPartidosAbiertos()){
                            idPartido = uComunes.leerIDpartido();
                            if (uAdmin.cerrarPartido(idPartido)){
                                System.out.println("Cerrado correctamente.");
                            }else{
                                System.out.println("No se ha podido cerrar correctamente.");
                            }
                        }else{
                            System.out.println("No hay partidos abiertos.");
                        }
                        break;
                    case 4: //Consultar apuestas partido
                        idPartido = uComunes.leerIDpartido2();
                        uAdmin.consultarApuestasPartido(idPartido);
                        break;
                    case 5: //Pagar apuestas
                        if(uComunes.existenPartidosConApuestasSinContabilizar()){
                            idPartido = uAdmin.leerYValidarPartidoConApuestasSinContabilizar();
                            uAdmin.pagarApuestasPartido(idPartido);
                        }else{
                            System.out.println("No existen partidos con apuestas sin contabilizar.");
                        }
                        break;
                }

            }
        }

    }
}
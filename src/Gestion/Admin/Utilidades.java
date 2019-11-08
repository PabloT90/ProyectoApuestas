package Gestion.Admin;

import java.util.Scanner;

public class Utilidades {
    /**
     * Lee y valida si el usuario quiere ejecutar una aplicacion.
     */
    public static char leerValidarEjecutar(){
        Scanner teclado = new Scanner(System.in);
        char ejecutar;

        do{
            System.out.println("Quieres ejecutar?");
            ejecutar = Character.toLowerCase(teclado.next().charAt(0));
        }while(ejecutar != 's' && ejecutar != 'n');

        return ejecutar;
    }
}

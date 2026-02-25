package jmsb.utils;

import java.util.Scanner;

public class Input {
    static {
        sc = new Scanner(System.in);
    }

    private static Scanner sc;

    public static int pedirNumero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje + ": ");
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes introducir un número entero válido.");
            }
        }
    }

    public static String pedirTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje + ": ");
            String texto = sc.nextLine().trim();
            if (!texto.isEmpty()) return texto;
            System.out.println("Error: El texto no puede estar vacío.");
        }
    }

    public static String pedirTextoNulleable(String mensaje) {
        while (true) {
            System.out.print(mensaje + ": ");
            String texto = sc.nextLine().trim();
            if (!texto.isEmpty()) return texto;
            return null;
        }
    }

    public static Integer pedirNumeroNulleable(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (o presiona Enter para omitir): ");
            String entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) return null; // Ahora sí permite salir
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Error: Introduce un número válido o deja vacío.");
            }
        }
    }

    public static Double pedirNumeroDecimalNulleable(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (o presiona Enter para omitir): ");
            String entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) return null;
            try {
                return Double.parseDouble(entrada.replace(',', '.')); // Soporte para comas
            } catch (NumberFormatException e) {
                System.out.println("Error: Introduce un decimal válido.");
            }
        }
    }

    public static Boolean pedirBooleanoEstricto(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (true/false): ");
            String entrada = sc.nextLine().trim().toLowerCase();
            if (entrada.equals("true")) return true;
            if (entrada.equals("false")) return false;
            System.out.println("Error: Debes escribir 'true' o 'false'.");
        }
    }

    public static Boolean pedirBooleanoEstrictoNulleable(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (true/false) (o presiona Enter para omitir) : ");
            String entrada = sc.nextLine().trim().toLowerCase();
            if (entrada.isEmpty())return null;
            if (entrada.equals("true")) return true;
            if (entrada.equals("false")) return false;
            System.out.println("Error: Debes escribir 'true' o 'false'.");
        }
    }
}

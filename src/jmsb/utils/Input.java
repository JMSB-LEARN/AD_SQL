package jmsb.utils;

import bbdd.GestionDBCliente;
import bbdd.GestionDBFabricante;
import bbdd.GestionDBProducto;
import model.DetalleCompra;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class UnrecognizedIDException extends Exception {
}

class InsuficientStorageException extends Exception {
}

public class Input {
    private static Scanner sc;

    private static GestionDBCliente gestionDBCliente;
    private static GestionDBProducto gestionDBProducto;
    private static GestionDBFabricante gestionDBFabricante;

    static {
        sc = new Scanner(System.in);
        gestionDBCliente = GestionDBCliente.getInstancia();
        gestionDBProducto = GestionDBProducto.getInstancia();
        gestionDBFabricante=GestionDBFabricante.getInstancia();
    }

    //Inputs Numericos

    public static Integer pedirInt(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje + ": ");
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes introducir un número entero válido.");
            }
        }
    }

    public static Integer pedirIntNulleable(String mensaje) {
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

    public static double pedirNumeroDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim();
            try {
                return Double.parseDouble(entrada.replace(',', '.')); // Soporte para comas
            } catch (NumberFormatException e) {
                System.out.println("Error: Introduce un decimal válido.");
            }
        }
    }


    //Inputs IDs

    public static int pedirIdProducto() {
        boolean idValido = false;
        int idAValidar = -1;
        while (!idValido) {
            System.out.println("Elige un Producto.");
            gestionDBProducto.mostrarProductos();
            System.out.print(":");
            try {
                idAValidar = Integer.parseInt(sc.nextLine());
                if (comprobarIdProducto(idAValidar))
                    idValido = true;
                else throw new UnrecognizedIDException();
            } catch (NumberFormatException e) {
                System.out.println("Numro no valido");
            } catch (UnrecognizedIDException e) {
                System.out.println("Id no reconocido");
            }
        }
        return idAValidar;
    }

    private static Integer pedirIdProductoNulleable() {
        boolean idValido = false;
        Integer idAValidar = -1;
        while (!idValido) {
            System.out.println("Elige un Producto.");
            gestionDBProducto.mostrarProductos();
            System.out.print(":");
            try {
                idAValidar = pedirIntNulleable("Que producto queires");
                if(idAValidar==null){
                    return null;
                }
                if (comprobarIdProducto(idAValidar))
                    idValido = true;
                else throw new UnrecognizedIDException();
            } catch (NumberFormatException e) {
                System.out.println("Numro no valido");
            } catch (UnrecognizedIDException e) {
                System.out.println("Id no reconocido");
            }
        }
        return idAValidar;
    }

    public static int pedirIdFabricante() {
        boolean idValido = false;
        int idAValidar = -1;
        while (!idValido) {
            System.out.println("Elige un fabricante.");
            gestionDBFabricante.mostrarFabricantes();
            System.out.print(":");
            try {
                idAValidar = Integer.parseInt(sc.nextLine());
                if (comprobarIdFabricante(idAValidar))
                    idValido = true;
                else throw new UnrecognizedIDException();
            } catch (NumberFormatException e) {
                System.out.println("Numro no valido");
            } catch (UnrecognizedIDException e) {
                System.out.println("Id no reconocido");
            }
        }
        return idAValidar;
    }

    public static int pedirIdCliente() {
        boolean idValido = false;
        int idAValidar = -1;
        while (!idValido) {
            System.out.println("Elige un cliente.");
            gestionDBCliente.mostrarClientes();
            System.out.print(":");
            try {
                idAValidar = Integer.parseInt(sc.nextLine());
                if (comprobarIdCliente(idAValidar))
                    idValido = true;
                else throw new UnrecognizedIDException();
            } catch (NumberFormatException e) {
                System.out.println("Numro no valido");
            } catch (UnrecognizedIDException e) {
                System.out.println("Id no reconocido");
            }
        }
        return idAValidar;
    }

    //Inputs Varios

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
            if (entrada.isEmpty()) return null;
            if (entrada.equals("true")) return true;
            if (entrada.equals("false")) return false;
            System.out.println("Error: Debes escribir 'true' o 'false'.");
        }
    }
    //inputs Complejos

    public static List<DetalleCompra> pedirListaDetallesCompra() {
        List<DetalleCompra> detallesCompra = new ArrayList<>();
        boolean finalizarCompra = false;
        while (!finalizarCompra) {
            try {
                DetalleCompra detalleCompra = pedirDetalleCompra();
                if (detalleCompra == null)
                    finalizarCompra = true;
                else
                    detallesCompra.add(detalleCompra);
            } catch (InsuficientStorageException e) {
                System.out.println("No hay inventaio Suficiente");
            }
        }
        return detallesCompra;
    }

    private static DetalleCompra pedirDetalleCompra() throws InsuficientStorageException {
        Integer idProducto = pedirIdProducto(), cantidad = pedirInt("Cantidad a comprar");
        if (idProducto == null)
            if (!gestionDBProducto.revisarInventarioSuficiente(idProducto, cantidad)) {
                throw new InsuficientStorageException();
            }
        return new DetalleCompra(idProducto, cantidad);
    }

    //Comprobadores

    public static boolean comprobarIdCliente(int id) {
        return gestionDBCliente.comprobarIdCliente(id);
    }

    private static boolean comprobarIdProducto(int id) {
        return gestionDBProducto.comprobarIdProducto(id);
    }

    private static boolean comprobarIdFabricante(int id) {return gestionDBFabricante.comprobarIdFabricante(id);}


}

import bbdd.GestionDBTienda;

import java.util.Scanner;

public class Main {

    private static Scanner sc;

    private static int pedirNumero() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Debes introducir un numero");
            }
        }
    }

    private static String pedirTexto(String mensaje) {
        while (true) {
            System.out.println(mensaje);
            try {
                String texto = sc.nextLine();
                if (texto.isBlank()) throw new Exception();
                return texto;
            } catch (Exception e) {
                System.out.println("No puede estar vacio");
            }
        }
    }

    private static void annadirProducto() {
        if (GestionDBTienda.getInstancia().annadirProducto(sc)) {
            System.out.println("Producto añadido correctamente");
        }
    }


    private static void mostrarProductos() {
        GestionDBTienda.getInstancia().mostrarProductos();
    }

    private static void mostrarProductos(String fabricante) {
        GestionDBTienda.getInstancia().mostrarProductosFabricante(fabricante);
    }

    private static void mostrarFabricantes() {
        GestionDBTienda.getInstancia().mostrarFabricantes();
    }

    private static void modificarProducto() {
        System.out.println("Elige el producto a modificar:");
        GestionDBTienda.getInstancia().mostrarProductos();

        System.out.println("Introduce el id del producto a modificar: ");
        int id = sc.nextInt();

        if (GestionDBTienda.getInstancia().modificarProducto(sc, id)) {
            System.out.println("\nMostramos para comprobar");
            GestionDBTienda.getInstancia().mostrarProductos();
        }
    }

    private static void modificarFabricante() {
        System.out.println("Elige el fabricante a modificar:");
        GestionDBTienda.getInstancia().mostrarFabricantes();

        System.out.println("Introduce el id del fabricante a modificar: ");
        int id = sc.nextInt();

        if (GestionDBTienda.getInstancia().modificarFabricante(sc, id)) {
            System.out.println("\nMostramos para comprobar");
            GestionDBTienda.getInstancia().mostrarFabricantes();
        }
    }

    private static void eliminarProducto() {
        System.out.println("Elige el producto a eliminar:");
        GestionDBTienda.getInstancia().mostrarProductos();

        System.out.println("Introduce el id del producto a eliminar: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (GestionDBTienda.getInstancia().eliminarProducto(id)) {
            System.out.println("\nMostramos para comprobar");
            GestionDBTienda.getInstancia().mostrarProductos();
        }
    }

    private static void eliminarFabricante() {

        System.out.println("Elige el fabricante a eliminar:");
        GestionDBTienda.getInstancia().mostrarFabricantes();

        System.out.println("Introduce el id del fabricante a eliminar: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (GestionDBTienda.getInstancia().eliminarFabricante(id)) {
            System.out.println("\nMostramos para comprobar");
            GestionDBTienda.getInstancia().mostrarFabricantes();
        }
    }


    public static void main(String[] args) {
        sc = new Scanner(System.in);
        mostrarMenu();
    }


    private static void mostrarMenuFabricantes() {
        while (true) {
            System.out.println("1.Mostrar todos los Fabricantes\n" + "2.Añadir Fabricante\n" + "3.Modificar los datos de un Fabricante.\n" + "4.Eliminar un Fabricante.\n" + "5.Volver");
            switch (pedirNumero()) {
                case 1:
                    mostrarFabricantes();
                    break;
                case 2:
                    annadirFabricante();
                    break;
                case 3:
                    modificarFabricante();
                    break;
                case 4:
                    eliminarFabricante();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Error, opcion no reconocida");
            }
        }
    }

    private static void annadirFabricante() {
        GestionDBTienda.getInstancia().annadirFabricante(sc);
    }

    private static void mostrarMenuProductos() {
        while (true) {
            System.out.println("1.Mostrar todos los productos de la tienda\n" + "2.Mostrar los productos del fabricante\n" + "3.Añadir producto\n" + "4.Modificar los datos de un producto.\n" + "5.Eliminar un producto.\n" + "6.Volver");
            switch (pedirNumero()) {
                case 1:
                    mostrarProductos();
                    break;
                case 2:
                    mostrarProductos(pedirTexto("Introduce el nombre del fabricante a buscar"));
                    break;
                case 3:
                    annadirProducto();
                    break;
                case 4:
                    modificarProducto();
                    break;
                case 5:
                    eliminarProducto();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Error, opcion no reconocida");
            }
        }
    }

    private static void mostrarMenu() {
        while (true) {
            System.out.println("1.Acciones de Productos\n" + "2.Acciones de Fabricantes\n" + "3.Acciones de Clientes\n" + "4.Acciones de Compras\n" + "5. Salir");
            switch (pedirNumero()) {
                case 1:
                    mostrarMenuProductos();
                    break;
                case 2:
                    mostrarMenuFabricantes();
                    break;
                case 3:
                    mostarMenuClientes();
                case 4:
                    mostrarMenuCompras();
                case 5:
                    return;
                default:
                    System.out.println("Error, opcion no reconocida");
            }
        }
    }

    private static void mostrarMenuCompras() {
        while (true) {
            System.out.println("1.Realizar Compra\n" + "2.Eliminar Compra\n" + "3.Volver");
            switch (pedirNumero()) {
                case 1:
                    realizarCompra();
                    break;
                case 2:
                    eliminarCompra();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Error, opcion no reconocida");
            }
        }
    }

    private static void eliminarCompra() {
    }

    private static void realizarCompra() {
        GestionDBTienda.getInstancia().realizarCompra(sc);
    }

    private static void mostarMenuClientes() {
        while (true) {
            System.out.println("1.Añadir Cliente\n" + "2.Modificar Cliente\n" + "3.Eliminar Cliente.\n" + "4.Mostrar Clientes.\n" + "5.Volver");
            switch (pedirNumero()) {
                case 1:
                    annadirCliente();
                    break;
                case 2:
                    modificarCliente();
                    break;
                case 3:
                    eliminarCliente();
                    break;
                case 4:
                    mostrarClientes();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Error, opcion no reconocida");
            }
        }
    }

    private static void annadirCliente() {
        GestionDBTienda.getInstancia().annadirCliente(sc);
    }

    private static void modificarCliente() {
        GestionDBTienda.getInstancia().modificarCliente(sc);
    }

    private static void eliminarCliente() {
        GestionDBTienda.getInstancia().eliminarCliente(sc);
    }

    private static void mostrarClientes() {
        GestionDBTienda.getInstancia().mostrarClientes();
    }
}
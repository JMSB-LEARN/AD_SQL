import jmsb.utils.Input;
import model.Producto;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        mostrarMenu();
    }

    private static void mostrarMenu() {
        while (true) {
            switch (Input.pedirNumero("1.Acciones de Productos\n" + "2.Acciones de Fabricantes\n" + "3.Acciones de Clientes\n" + "4.Acciones de Compras\n" + "5. Salir")) {
                case 1:
                    mostrarMenuProductos();
                    break;
                case 2:
                    mostrarMenuFabricantes();
                    break;
                case 3:
                    mostarMenuClientes();
                    break;
                case 4:
                    mostrarMenuCompras();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Error, opcion no reconocida");
            }
        }
    }

    private static void mostarMenuClientes() {
        while (true) {
            switch (Input.pedirNumero("1.Añadir Cliente\n" + "2.Modificar Cliente\n" + "3.Eliminar Cliente.\n" + "4.Mostrar Clientes.\n" + "5.Volver")) {
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
        System.out.print("Nombre del cliente: ");
        System.out.print("Email del cliente: ");
        System.out.print("Telefono del cliente: ");
        System.out.print("Direccion del cliente: ");
        GestionDBConnection.getInstancia().annadirCliente();
    }

    private static void modificarCliente() {

        mostrarClientes();
        System.out.println("¿Cual es el id del cliente a modificar?");
        System.out.print("Nuevo nombre del cliente: ");
        System.out.print("Nuevo email del cliente: ");
        System.out.print("Nuevo telefono del cliente: ");
        System.out.print("Nuevo direccion del cliente: ");
        System.out.print("Nuevo cliente activo: \nyes\nno");
        GestionDBConnection.getInstancia().modificarCliente();
    }

    private static void eliminarCliente() {
        GestionDBConnection.getInstancia().eliminarCliente();
    }

    private static void mostrarClientes() {
        GestionDBConnection.getInstancia().mostrarClientes();
    }


    private static void mostrarMenuFabricantes() {
        while (true) {
            switch (Input.pedirNumero("1.Mostrar todos los Fabricantes\n" + "2.Añadir Fabricante\n" + "3.Modificar los datos de un Fabricante.\n" + "4.Eliminar un Fabricante.\n" + "5.Volver")) {
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

    private static int pedirIdFabricante(String idDelFabricante) {
        GestionDBConnection.getInstancia().mostrarFabricantes();
    }

    private static void mostrarFabricantes() {
        GestionDBConnection.getInstancia().mostrarFabricantes();
    }

    private static void modificarFabricante() {
        System.out.println("Elige el fabricante a modificar:");
        GestionDBConnection.getInstancia().mostrarFabricantes();

        System.out.println("Introduce el id del fabricante a modificar: ");
        int id = sc.nextInt();

        if (GestionDBConnection.getInstancia().modificarFabricante(sc, id)) {
            System.out.println("\nMostramos para comprobar");
            GestionDBConnection.getInstancia().mostrarFabricantes();
        }
    }

    private static void eliminarFabricante() {

        System.out.println("Elige el fabricante a eliminar:");
        GestionDBConnection.getInstancia().mostrarFabricantes();

        System.out.println("Introduce el id del fabricante a eliminar: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (GestionDBConnection.getInstancia().eliminarFabricante(id)) {
            System.out.println("\nMostramos para comprobar");
            GestionDBConnection.getInstancia().mostrarFabricantes();
        }
    }

    private static void annadirFabricante() {
        GestionDBConnection.getInstancia().annadirFabricante(sc);
    }



    private static void mostrarMenuProductos() {
        while (true) {
            switch (Input.pedirNumero("1.Mostrar todos los productos de la tienda\n" + "2.Mostrar los productos del fabricante\n" + "3.Añadir producto\n" + "4.Modificar los datos de un producto.\n" + "5.Eliminar un producto.\n" + "6.Volver")) {
                case 1:
                    mostrarProductos();
                    break;
                case 2:
                    mostrarProductos(Input.pedirTexto("Introduce el nombre del fabricante a buscar"));
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

    private static void annadirProducto() {

        System.out.print("Nombre del producto: ");
        if (GestionDBConnection.getInstancia().annadirProducto(new Producto(Input.pedirTexto("Nombre del producto"),Input.pedirNumeroDecimal("Precio del producto"),pedirIdFabricante("Id del fabricante")))) {
            System.out.println("Producto añadido correctamente");
        }
    }

    private static void mostrarProductos() {
        GestionDBConnection.getInstancia().mostrarProductos();
    }

    private static void mostrarProductos(String fabricante) {
        GestionDBConnection.getInstancia().mostrarProductosFabricante(fabricante);
    }

    private static void modificarProducto() {
        System.out.println("Elige el producto a modificar:");
        GestionDBConnection.getInstancia().mostrarProductos();

        System.out.println("Introduce el id del producto a modificar: ");
        int id = sc.nextInt();

        if (GestionDBConnection.getInstancia().modificarProducto(sc, id)) {
            System.out.println("\nMostramos para comprobar");
            GestionDBConnection.getInstancia().mostrarProductos();
        }
    }

    private static void eliminarProducto() {
        System.out.println("Elige el producto a eliminar:");
        GestionDBConnection.getInstancia().mostrarProductos();

        System.out.println("Introduce el id del producto a eliminar: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (GestionDBConnection.getInstancia().eliminarProducto(id)) {
            System.out.println("\nMostramos para comprobar");
            GestionDBConnection.getInstancia().mostrarProductos();
        }
    }



    private static void mostrarMenuCompras() {
        while (true) {
            switch (Input.pedirNumero("1.Realizar Compra\n" + "2.Eliminar Compra\n" + "3.Volver")) {
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
        GestionDBConnection.getInstancia().realizarCompra(sc);
    }
}
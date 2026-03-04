import bbdd.*;
import jmsb.utils.Input;
import model.Cliente;
import model.Compra;
import model.Fabricante;
import model.Producto;

import java.time.LocalDateTime;


public class Main {
    private static GestionDBCliente gestionDBCliente;
    private static GestionDBCompra gestionDBCompra;
    private static GestionDBProducto gestionDBProducto;
    private static GestionDBFabricante gestionDBFabricante;
    private static GestionDBDetallesCompra gestionDBDetallesCompra;

    public static void main(String[] args) {
        initGestion();
        mostrarMenu();
    }

    private static void initGestion() {
        gestionDBCliente=GestionDBCliente.getInstancia();
        gestionDBCompra=GestionDBCompra.getInstancia();
        gestionDBFabricante=GestionDBFabricante.getInstancia();
        gestionDBProducto=GestionDBProducto.getInstancia();
    }

    private static void mostrarMenu() {
        while (true) {
            switch (Input.pedirInt("1.Acciones de Productos\n" + "2.Acciones de Fabricantes\n" + "3.Acciones de Clientes\n" + "4.Acciones de Compras\n" + "5. Salir")) {
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
            switch (Input.pedirInt("1.Añadir Cliente\n" + "2.Modificar Cliente\n" + "3.Eliminar Cliente.\n" + "4.Mostrar Clientes.\n" + "5.Volver")) {
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
        gestionDBCliente.annadirCliente(new Cliente(Input.pedirTexto("Nombre del cliente: "),Input.pedirTexto("Email del cliente: "),Input.pedirTexto("Telefono del cliente: "),Input.pedirTexto("Direccion del cliente: ")));
    }

    private static void modificarCliente() {
        mostrarClientes();
        gestionDBCliente.modificarCliente(new Cliente(Input.pedirInt("¿Cual es el id del cliente a modificar?"),Input.pedirTextoNulleable("Nombre del cliente: "),Input.pedirTextoNulleable("Email del cliente: "),Input.pedirTextoNulleable("Telefono del cliente: "),Input.pedirTextoNulleable("Direccion del cliente: "),Input.pedirBooleanoEstrictoNulleable("¿Esta activo?: ")));
    }

    private static void eliminarCliente() {
        mostrarClientes();
        gestionDBCliente.eliminarCliente(Input.pedirInt("¿Cual es el id del cliente a eliminar?"));
    }

    private static void mostrarClientes() {
        gestionDBCliente.mostrarClientes();
    }


    private static void mostrarMenuFabricantes() {
        while (true) {
            switch (Input.pedirInt("1.Mostrar todos los Fabricantes\n" + "2.Añadir Fabricante\n" + "3.Modificar los datos de un Fabricante.\n" + "4.Eliminar un Fabricante.\n" + "5.Volver")) {
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

    private static void mostrarFabricantes() {
        gestionDBFabricante.mostrarFabricantes();
    }

    private static void modificarFabricante() {
        System.out.println("Elige el fabricante a modificar:");
        gestionDBFabricante.mostrarFabricantes();

        int id = Input.pedirIdFabricante();

        System.out.println("Introduce el nuevo nombre:");
        if (gestionDBFabricante.modificarFabricante(new Fabricante(Input.pedirIdFabricante(),Input.pedirTextoNulleable("Introduce el nuevo nombre:")))) {
            gestionDBFabricante.mostrarFabricantes();
        }
    }

    private static void eliminarFabricante() {

        System.out.println("Elige el fabricante a eliminar:");
        gestionDBFabricante.mostrarFabricantes();

        System.out.println("Introduce el id del fabricante a eliminar: ");
        int id = Input.pedirIdFabricante();

        if (gestionDBFabricante.eliminarFabricante(id)) {
            System.out.println("\nMostramos para comprobar");
            gestionDBFabricante.getInstancia().mostrarFabricantes();
        }
    }

    private static void annadirFabricante() {
        gestionDBFabricante.annadirFabricante(new Fabricante(Input.pedirTexto("Nombre del fabricante: ")));
    }



    private static void mostrarMenuProductos() {
        while (true) {
            switch (Input.pedirInt("1.Mostrar todos los productos de la tienda\n" + "2.Mostrar los productos del fabricante\n" + "3.Añadir producto\n" + "4.Modificar los datos de un producto.\n" + "5.Eliminar un producto.\n" + "6.Volver")) {
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
        System.out.print("Precio: ");
        System.out.print("Nombre del producto: ");
        if (gestionDBProducto.annadirProducto(new Producto(Input.pedirTexto("Nombre del producto"),Input.pedirNumeroDecimal("Precio del producto"),Input.pedirIdFabricante()))) {
            System.out.println("Producto añadido correctamente");
        }
    }

    private static void mostrarProductos() {
        gestionDBProducto.mostrarProductos();
    }

    private static void mostrarProductos(String fabricante) {
        gestionDBProducto.mostrarProductosFabricante(fabricante);
    }

    private static void modificarProducto() {
        System.out.println("Elige el producto a modificar:");
        gestionDBProducto.mostrarProductos();

        System.out.println("Introduce el id del producto a modificar: ");
        int id = Input.pedirIdProducto();

        System.out.println("Introduce el nuevo nombre:");
        System.out.println("Introduce el nuevo precio:");
        if (gestionDBProducto.modificarProducto(new Producto(Input.pedirIdProducto(),Input.pedirTextoNulleable("Nuevo nombre del producto"),Input.pedirNumeroDecimal("Nuevo precio del producto")))) {

            System.out.println("\nMostramos para comprobar");
            gestionDBProducto.mostrarProducto(id);
        }
    }

    private static void eliminarProducto() {
        System.out.println("Elige el producto a eliminar:");
        gestionDBProducto.mostrarProductos();

        System.out.println("Introduce el id del producto a eliminar: ");
        int id = Input.pedirIdProducto();

        if (gestionDBProducto.eliminarProducto(id)) {
            System.out.println("\nMostramos para comprobar");
            gestionDBProducto.mostrarProductos();
        }
    }



    private static void mostrarMenuCompras() {
        while (true) {
            switch (Input.pedirInt("1.Realizar Compra\n" + "2.Eliminar Compra\n" + "3.Volver")) {
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

    private static boolean realizarCompra() {
        System.out.println("¿Que cliente ha efectuado la compra?");
        int idCliente=Input.pedirIdCliente();
        if(!gestionDBCliente.isClienteActivo(idCliente)){
            System.out.println("No es cliente activo");
            return false;
        }
        return gestionDBCompra.realizarCompra(new Compra(idCliente, LocalDateTime.now()),Input.pedirListaDetallesCompra());
    }
}
//        System.out.print("Nombre del cliente: ");
//        System.out.print("Email del cliente: ");
//        System.out.print("Telefono del cliente: ");
//        System.out.print("Direccion del cliente: ");
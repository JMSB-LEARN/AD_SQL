package bbdd;


import model.Fabricante;
import model.Cliente;
import model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestionDBTienda {

    private static GestionDBTienda instancia;
    private Connection connection;

    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "tienda";
    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
    private static final String DB_USER = "root";
    private static final String DB_PASS = "959546724";

    //Base

    private GestionDBTienda() {
        try {
            System.out.print("Cargando Driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("OK!");

            System.out.print("Conectando a la base de datos...");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("OK!");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error al cargar el driver.");
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Error al establecer la conexión: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static GestionDBTienda getInstancia() {
        if (instancia == null) {
            instancia = new GestionDBTienda();
        }
        return instancia;
    }

    //Producto

    public List<Producto> obtenerProductos() {
        List<Producto> productos = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM producto p JOIN fabricante f ON p.id_fabricante = f.id");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int idFabricante = rs.getInt("id_fabricante");
                String nombreFabricante = rs.getString("f.nombre");

                Producto p = new Producto(id, nombre, precio, idFabricante, nombreFabricante);
                productos.add(p);
            }
        } catch (SQLException ex) {
            System.out.println("Error al leer los productos");
            return null;
        }
        return productos;
    }

    public boolean annadirProducto(Scanner sc) {
        String sql = "INSERT INTO producto (nombre, precio, id_fabricante) VALUES (?, ?, ?)";
        String sqlNameCheck = "Select nombre from producto where nombre=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql); PreparedStatement providerChk = connection.prepareStatement("select id from fabricante where id=?")) {

            System.out.print("Nombre del producto: ");
            String name = sc.nextLine();
            try (PreparedStatement stmtNameCheck = connection.prepareStatement(sqlNameCheck)) {
                stmtNameCheck.setString(1, name);

                try (ResultSet rs = stmtNameCheck.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Producto ya existente");
                        return false;
                    }
                }
            }
            stmt.setString(1, name);
            System.out.print("Precio: ");
            double precio = Double.parseDouble(sc.nextLine());
            stmt.setDouble(2, precio);

            Boolean idValido = false;
            int idFab = 0;
            while (!idValido) {
                mostrarFabricantes();
                System.out.print("ID Fabricante: ");
                idFab = Integer.parseInt(sc.nextLine());
                providerChk.setInt(1, idFab);
                if ((providerChk.executeQuery()).next()) {
                    idValido = true;
                } else {
                    System.out.println("Id no reconocido, vuelva a intentarlo");
                }
            }
            stmt.setInt(3, idFab);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (NumberFormatException e) {
            System.err.println("Error: El precio o el ID deben ser números válidos.");
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ocurrió un error inesperado.");
            e.printStackTrace();
        }
        return false;
    }

    public void mostrarProductos() {
        List<Producto> productos = obtenerProductos();

        for (Producto p : productos) {
            System.out.println(p);
        }
    }

    private Producto buscarProducto(int id) {
        Producto p = null;
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT *" + " FROM producto p" + " WHERE id = ?");

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");

                p = new Producto(id, nombre, precio);
            }
        } catch (SQLException ex) {
            System.out.println("Error al leer el producto");
            ex.printStackTrace();
        }

        return p;
    }

    public boolean modificarProducto(Scanner sc, int id) {
        Producto p = buscarProducto(id);
        if (p == null) {
            System.out.println("No se ha encontrado el producto indicado.");
            return false;
        }

        System.out.println("Introduce el nuevo nombre:");
        p.setNombre(sc.nextLine());

        System.out.println("Introduce el nuevo precio:");
        p.setPrecio(sc.nextDouble());

        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE producto" + " SET nombre = ?, precio = ?" + " WHERE id = ?");

            stmt.setString(1, p.getNombre());
            stmt.setDouble(2, p.getPrecio());
            stmt.setInt(3, p.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al modificar el producto en la BBDD.");
            e.printStackTrace();
            return false;
        }

        System.out.println("Se ha actualizado la información del producto.");
        return true;
    }

    public boolean eliminarProducto(int id) {
        Producto p = buscarProducto(id);
        if (p == null) {
            System.out.println("No se ha encontrado el producto indicado.");
            return false;
        }

        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM producto" + " WHERE id = ?");

            stmt.setInt(1, p.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar el producto de la BBDD.");
            e.printStackTrace();
            return false;
        }

        System.out.println("Se ha eliminado la información del producto.");
        return true;
    }

    //Fabricante

    public List<Fabricante> obtenerFabricantes() {
        List<Fabricante> fabricantes = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM fabricante");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                Fabricante f = new Fabricante(id, nombre);
                fabricantes.add(f);
            }
        } catch (SQLException ex) {
            System.out.println("Error al leer los fabricantes");
            return null;
        }
        return fabricantes;
    }

    public boolean annadirFabricante(Scanner sc) {
        String sql = "INSERT INTO fabricante (nombre) VALUES (?,)";

        try (PreparedStatement stmt = connection.prepareStatement(sql); PreparedStatement providerChk = connection.prepareStatement("select id from fabricante where id=?")) {

            System.out.print("Nombre del fabricante: ");
            stmt.setString(1, sc.nextLine());
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (NumberFormatException e) {
            System.err.println("Error: El nombre debe de ser valido;.");
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ocurrió un error inesperado.");
            e.printStackTrace();
        }
        return false;
    }

    public void mostrarFabricantes() {
        List<Fabricante> fabricante = obtenerFabricantes();

        for (Fabricante f : fabricante) {
            System.out.println(f);
        }
    }

    public void mostrarProductosFabricante(String nombreFabricante) {
        List<Producto> productos = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT *" + " FROM producto p JOIN fabricante f ON p.id_fabricante = f.id" + " WHERE f.nombre = ?");

            stmt.setString(1, nombreFabricante);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int idFabricante = rs.getInt("id_fabricante");
                String nombreFab = rs.getString("f.nombre");

                Producto p = new Producto(id, nombre, precio, idFabricante, nombreFab);
                productos.add(p);
            }
        } catch (SQLException ex) {
            System.out.println("Error al leer los productos");
            ex.printStackTrace();
        }

        if (productos.isEmpty()) {
            System.out.println("No hay productos asociados al fabricante indicado.");
        } else {
            for (Producto p : productos) {
                System.out.println(p);
            }
        }
    }

    private Fabricante buscarFabricante(int id) {
        Fabricante f = null;
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT *" + " FROM fabricante f" + " WHERE id = ?");

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");

                f = new Fabricante(id, nombre);
            }
        } catch (SQLException ex) {
            System.out.println("Error al leer el fabricante");
            ex.printStackTrace();
        }

        return f;
    }

    public boolean modificarFabricante(Scanner sc, int id) {
        Fabricante f = buscarFabricante(id);
        if (f == null) {
            System.out.println("No se ha encontrado el fabricante indicado.");
            return false;
        }

        System.out.println("Introduce el nuevo nombre:");
        f.setNombre(sc.nextLine());

        sc.nextLine();

        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE fabricante" + " SET nombre = ?" + " WHERE id = ?");

            stmt.setString(1, f.getNombre());
            stmt.setInt(2, f.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al modificar el fabricante en la BBDD.");
            e.printStackTrace();
            return false;
        }

        System.out.println("Se ha actualizado la información del fabricante.");
        return true;
    }

    public boolean eliminarFabricante(int id) {
        Fabricante f = buscarFabricante(id);
        if (f == null) {
            System.out.println("No se ha encontrado el fabricante indicado.");
            return false;
        }

        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM fabricante" + " WHERE id = ?");

            stmt.setInt(1, f.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar el fabricante de la BBDD.");
            e.printStackTrace();
            return false;
        }

        System.out.println("Se ha eliminado la información del fabricante.");
        return true;
    }

    //Cliente

    public List<Cliente> obtenerClientes() {
        List<Cliente> clientes = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM cliente"); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id_cliente");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String telefono = rs.getString("telefono");
                String direccion = rs.getString("direccion");
                boolean activo = rs.getBoolean("activo");

                Cliente c = new Cliente(id, nombre, email, telefono, direccion, activo);
                clientes.add(c);
            }
        } catch (SQLException ex) {
            System.out.println("Error al leer los clientes");
            return null;
        }
        return clientes;
    }

    public void mostrarClientes() {
        List<Cliente> clientes = obtenerClientes();

        for (Cliente c : clientes) {
            System.out.println(c);
        }
    }

    public void annadirCliente(Scanner sc) {
        String sql = "INSERT INTO Cliente (nombre,email,telefono,direction) VALUES (?,?,?,?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql); PreparedStatement providerChk = connection.prepareStatement("select id from fabricante where id=?")) {

            System.out.print("Nombre del cliente: ");
            stmt.setString(1, sc.nextLine());
            System.out.print("Email del cliente: ");
            stmt.setString(2, sc.nextLine());
            System.out.print("Telefono del cliente: ");
            stmt.setString(3, sc.nextLine());
            System.out.print("Direccion del cliente: ");
            stmt.setString(4, sc.nextLine());
            stmt.setBoolean(5, true);
            stmt.executeUpdate();
        } catch (NumberFormatException e) {
            System.err.println("Error: El nombre debe de ser valido;.");
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ocurrió un error inesperado.");
            e.printStackTrace();
        }
    }

    public boolean modificarCliente(Scanner sc) {
        String sql = "UPDATE Cliente SET nombre=?, email=?,telefono=?,direction=?,activo=? where id_cliente=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql); PreparedStatement providerChk = connection.prepareStatement("select id from fabricante where id=?")) {
            mostrarClientes();
            System.out.println("¿Cual es el id del cliente a modificar?");
            stmt.setInt(Integer.parseInt(sc.nextLine()), 6);

            System.out.print("Nuevo nombre del cliente: ");
            stmt.setString(1, sc.nextLine());

            System.out.print("Nuevo email del cliente: ");
            stmt.setString(2, sc.nextLine());

            System.out.print("Nuevo telefono del cliente: ");
            stmt.setString(3, sc.nextLine());

            System.out.print("Nuevo direccion del cliente: ");
            stmt.setString(4, sc.nextLine());

            System.out.print("Nuevo cliente activo: \nyes\nno");
            stmt.setBoolean(5, Boolean.parseBoolean(sc.nextLine()));
            return (stmt.executeUpdate()) > 0;

        } catch (NumberFormatException e) {
            System.err.println("Error: El nombre debe de ser valido;.");
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ocurrió un error inesperado.");
            e.printStackTrace();
        }
        return false;
    }

    public void eliminarCliente(Scanner sc) {


        String sql = "DELET from Cliente where id_cliente=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql); PreparedStatement providerChk = connection.prepareStatement("select id from fabricante where id=?")) {
            mostrarClientes();
            System.out.println("¿Cual es el id del cliente a eliminar?");
            stmt.setInt(Integer.parseInt(sc.nextLine()), 1);


        } catch (NumberFormatException e) {
            System.err.println("Error: El nombre debe de ser valido;.");
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ocurrió un error inesperado.");
            e.printStackTrace();
        }
    }

    public void realizarCompra(Scanner sc) {

        String sqlClientStatus = "Select activo from clientes where id_cliente = ?";
        String sqlCompra = "INSERT INTO compra (id_cliente) VALUES (?)";
        String sqlCompraDetalles = "INSERT INTO compra (id_cliente) VALUES (?)";

        try (PreparedStatement stmtClientStatus = connection.prepareStatement(sqlClientStatus); PreparedStatement stmtCompra = connection.prepareStatement(sqlCompra); PreparedStatement stmtCompraDetalles = connection.prepareStatement(sqlCompraDetalles)) {
            mostrarClientes();
            System.out.println("¿Que cliente ha efectuado la compra?");
            stmtClientStatus.setInt(Integer.parseInt(sc.nextLine()), 1);
            //AQUI hacer resultset

            System.out.print("Nombre del cliente: ");
            stmtCompra.setString(1, sc.nextLine());
            System.out.print("Email del cliente: ");
            stmtCompra.setString(2, sc.nextLine());
            System.out.print("Telefono del cliente: ");
            stmtCompra.setString(3, sc.nextLine());
            System.out.print("Direccion del cliente: ");
            stmtCompra.setString(4, sc.nextLine());
            stmtCompra.setBoolean(5, true);
            stmtCompra.executeUpdate();
        } catch (NumberFormatException e) {
            System.err.println("Error: El nombre debe de ser valido;.");
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ocurrió un error inesperado.");
            e.printStackTrace();
        }
    }
}

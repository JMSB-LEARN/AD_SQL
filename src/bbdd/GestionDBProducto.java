package bbdd;

import model.Cliente;
import model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestionDBProducto {
    private static Connection connection;
    private static GestionDBProducto instancia;

    public static GestionDBProducto getInstancia() {
        if (instancia == null) {
            instancia = new GestionDBProducto();
        }
        connection=GestionDBConnection.getInstancia().getConnection();
        return instancia;
    }

    public List<Producto> obtenerProductos() {
        List<Producto> productos = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM producto p JOIN fabricante f ON p.id_fabricante = f.id");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int idFabricante = rs.getInt("id_fabricante");

                Producto p = new Producto(nombre, precio, idFabricante);
                productos.add(p);
            }
        } catch (SQLException ex) {
            System.out.println("Error al leer los productos");
            return null;
        }
        return productos;
    }

    public boolean annadirProducto(Producto producto) {
        String sql = "INSERT INTO producto (nombre, precio, id_fabricante) VALUES (?, ?, ?)";
        String sqlNameCheck = "Select Count(nombre) from producto where nombre=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql); PreparedStatement providerChk = connection.prepareStatement("select id from fabricante where id=?")) {
            String name = producto.getNombre();
            try (PreparedStatement stmtNameCheck = connection.prepareStatement(sqlNameCheck)) {
                stmtNameCheck.setString(1, name);

                try (ResultSet rs = stmtNameCheck.executeQuery()) {
                    if (rs.getInt(1)>0) {
                        System.out.println("Producto ya existente");
                        return false;
                    }
                }
            }
            stmt.setString(1, name);
            System.out.print("Precio: ");
            double precio = producto.getPrecio();
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

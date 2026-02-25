package bbdd;

import model.Fabricante;
import model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestionDBFabricante {
    private static Connection connection;
    private static GestionDBFabricante instancia;

    public static GestionDBFabricante getInstancia() {
        if (instancia == null) {
            instancia = new GestionDBFabricante();
        }
        connection=GestionDBConnection.getInstancia().getConnection();
        return instancia;
    }

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

    public List<Fabricante> mostrarFabricantes() {
        List<Fabricante> fabricante = obtenerFabricantes();

        for (Fabricante f : fabricante) {
            System.out.println(f);
        }
        return fabricante;
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

}

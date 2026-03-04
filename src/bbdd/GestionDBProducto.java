package bbdd;

import model.Cliente;
import model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestionDBProducto {
    private static Connection connection;
    private static GestionDBProducto instancia;

    public static GestionDBProducto getInstancia() {
        if (instancia == null) {
            instancia = new GestionDBProducto();
        }
        connection = GestionDBConnection.getInstancia().getConnection();
        return instancia;
    }

    public List<Producto> obtenerProductos() {
        List<Producto> productos = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM producto p JOIN fabricante f ON p.id_fabricante = f.id");) {
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

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setInt(3, producto.getIdFabricante());
            return stmt.executeUpdate() > 0;
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
        try (PreparedStatement stmt = connection.prepareStatement("SELECT *" + " FROM producto p" + " WHERE id = ?");) {

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

    public boolean modificarProducto(Producto datosNuevosProducto) {
        boolean modificar = false;
        List<Object> elementosAModificar = new ArrayList<>();
        int index = 0;
        if (datosNuevosProducto == null) {
            System.out.println("No se ha encontrado el producto indicado.");
            return false;
        }

        StringBuilder sqlUpdate = new StringBuilder("UPDATE producto SET ");
        if (datosNuevosProducto.getNombre() != null) {
            modificar = true;
            sqlUpdate.append("nombre = ?, ");
            elementosAModificar.add(datosNuevosProducto.getNombre());
        }
        if (datosNuevosProducto.getPrecio() != null) {
            modificar = true;
            sqlUpdate.append("precio = ?, ");
            elementosAModificar.add(datosNuevosProducto.getPrecio());
        }
        if (!modificar) {
            System.out.println("No se modificara nada");
            return false;
        }
        sqlUpdate.setLength(sqlUpdate.length() - 2);

        sqlUpdate.append(" WHERE id = ?");
        elementosAModificar.add(datosNuevosProducto.getId());

        try (PreparedStatement stmt = connection.prepareStatement(sqlUpdate.toString());) {
            for (Object o : elementosAModificar) {
                stmt.setObject(++index, o);
            }
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

        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM producto" + " WHERE id = ?");) {

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

    public boolean comprobarIdProducto(int id) {
        String sql = "select count(id_producto) from producto where id =?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            if (stmt.executeQuery().getInt(1) == 1) return true;
            return false;
        } catch (SQLException e) {
            return false;
        }
    }


    public boolean revisarInventarioSuficiente(int idProducto, int cantidad) {
        String sql = "select cantidad from producto where id =?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            if (stmt.executeQuery().getInt(1) >= cantidad) return true;
            return false;
        } catch (SQLException e) {
            System.out.println("Fallo desconocido");
            return false;
        }
    }

    public Double getPrecioActual(int idProducto) {
        String sql = "select precio from producto where id =?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    public void mostrarProductosFabricante(String nombreFabricante) {
        List<Producto> productos = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT *" + " FROM producto p JOIN fabricante f ON p.id_fabricante = f.id" + " WHERE f.nombre = ?");) {


            stmt.setString(1, nombreFabricante);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int idFabricante = rs.getInt("id_fabricante");
                Producto p = new Producto(id, nombre, precio, idFabricante);
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

    public void mostrarProducto(int id) {

    }
}

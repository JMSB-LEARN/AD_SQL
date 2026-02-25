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

public class GestionDBCliente {
    private Connection connection;
    private GestionDBCliente instancia;

    public GestionDBCliente getInstancia() {
        if (instancia == null) {
            instancia = new GestionDBCliente();
        }
        connection=GestionDBConnection.getInstancia().getConnection();
        return instancia;
    }

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

    public void annadirCliente(Cliente cliente) {
        String sql = "INSERT INTO Cliente (nombre,email,telefono,direction) VALUES (?,?,?,?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql); PreparedStatement providerChk = connection.prepareStatement("select id from fabricante where id=?")) {

            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefono());
            stmt.setString(4, cliente.getDireccion());
            stmt.setBoolean(5, true);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ocurrió un error inesperado.");
            e.printStackTrace();
        }
    }

    public boolean modificarCliente(Cliente nuevosDatosCliente) {

        StringBuilder sql = new StringBuilder("UPDATE Cliente SET ");
        List<Object> parametros = new ArrayList<>();

        if (nuevosDatosCliente.getNombre() != null) {
            sql.append("nombre = ?, ");
            parametros.add(nuevosDatosCliente.getNombre());
        }
        if (nuevosDatosCliente.getEmail() != null) {
            sql.append("email = ?, ");
            parametros.add(nuevosDatosCliente.getEmail());
        }
        if (nuevosDatosCliente.getTelefono() != null) {
            sql.append("telefono = ?, ");
            parametros.add(nuevosDatosCliente.getTelefono());
        }

        if (nuevosDatosCliente.getDireccion() != null) {
            sql.append("direccion = ?, ");
            parametros.add(nuevosDatosCliente.getDireccion());
        }

        if (nuevosDatosCliente.isActive() != null) {
            sql.append("activo = ?, ");
            parametros.add(nuevosDatosCliente.isActive());
        }

        if (parametros.isEmpty()) {
            System.out.println("No hay datos para actualizar.");
            return false;
        }
        sql.setLength(sql.length() - 2);
        sql.append(" WHERE id_cliente = ?");
        parametros.add(nuevosDatosCliente.getIdCliente());

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
            return false;
        }
    }

    public void eliminarCliente() {


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

}

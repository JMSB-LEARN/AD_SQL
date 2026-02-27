package bbdd;

import model.Compra;
import model.DetalleCompra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class GestionDBCompra {
    private static Connection connection;
    private static GestionDBCompra instancia;

    public static GestionDBCompra getInstancia() {
        if (instancia == null) {
            instancia = new GestionDBCompra();
        }
        connection=GestionDBConnection.getInstancia().getConnection();
        return instancia;
    }
    public void realizarCompra(Compra compra, List<DetalleCompra> detallesCompra) {

        String sqlCompra = "INSERT INTO compra (id_cliente) VALUES (?)";
        String sqlCompraDetalles = "INSERT INTO compra (id_cliente) VALUES (?)";

        try ( PreparedStatement stmtCompra = connection.prepareStatement(sqlCompra); PreparedStatement stmtCompraDetalles = connection.prepareStatement(sqlCompraDetalles)) {
            GestionDBCliente.getInstancia().mostrarClientes();
            stmtCompra.setString(1, sc.nextLine());
            stmtCompra.setString(2, sc.nextLine());
            stmtCompra.setString(3, sc.nextLine());
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

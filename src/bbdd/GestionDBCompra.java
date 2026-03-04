package bbdd;

import model.Compra;
import model.DetalleCompra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GestionDBCompra {
    public static class NoDetailsOnCheckoutExceptiopn extends Exception{}
    private static Connection connection;
    private static GestionDBCompra instancia;

    public static GestionDBCompra getInstancia() {
        if (instancia == null) {
            instancia = new GestionDBCompra();
        }
        connection=GestionDBConnection.getInstancia().getConnection();
        return instancia;
    }
    public boolean realizarCompra(Compra compra, List<DetalleCompra> detallesCompra) {
        // 1. Validar entrada antes de abrir transacciones

        if (detallesCompra == null || detallesCompra.isEmpty()) {
            System.err.println("Error: No hay detalles en la compra.");
            return false;
        }

        String sqlCompra = "INSERT INTO compra (id_cliente, fecha_transaccion) VALUES (?, ?)";
        String sqlDetalle = "INSERT INTO compra_Detalle (id_compra, id_producto, cantidad) VALUES (?, ?, ?)";

        try {
            connection.setAutoCommit(false); // Iniciamos transacción

            // 2. Insertar Compra y recuperar el ID generado automáticamente
            try (PreparedStatement stmtInsertIntroCompra = connection.prepareStatement(sqlCompra, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmtInsertIntroCompra.setInt(1, compra.getIdCliente());
                stmtInsertIntroCompra.setObject(2, compra.getFechaCompra());
                stmtInsertIntroCompra.executeUpdate();

                // Obtener el ID
                ResultSet rs = stmtInsertIntroCompra.getGeneratedKeys();
                int idCompra;
                if (rs.next()) {
                    idCompra = rs.getInt(1);
                } else {
                    throw new SQLException("Error al obtener el ID de la compra.");
                }

                // 3. Insertar detalles usando Batch para mejorar el rendimiento
                try (PreparedStatement stmtInsertIntroDetallesCompra = connection.prepareStatement(sqlDetalle)) {
                    for (DetalleCompra dc : detallesCompra) {
                        stmtInsertIntroDetallesCompra.setInt(1, idCompra);
                        stmtInsertIntroDetallesCompra.setInt(2, dc.getIdProducto());
                        stmtInsertIntroDetallesCompra.setInt(3, dc.getCantidad());
                        stmtInsertIntroDetallesCompra.addBatch(); // Añadir a la tanda
                    }
                    stmtInsertIntroDetallesCompra.executeBatch(); // Ejecutar todos los detalles de golpe
                }

                connection.commit(); // Todo bien, commit
                return true;
            }

        } catch (SQLException e) {
            // 4. Si algo falla, tollback
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Error en la transacción: " + e.getMessage());
        } finally {
            // 5. Restaurar el autoCommit
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}

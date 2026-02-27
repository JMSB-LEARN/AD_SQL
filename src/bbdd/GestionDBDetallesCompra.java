package bbdd;

import model.Compra;
import model.DetalleCompra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class GestionDBDetallesCompra {
    private static Connection connection;
    private static GestionDBDetallesCompra instancia;

    public static GestionDBDetallesCompra getInstancia() {
        if (instancia == null) {
            instancia = new GestionDBDetallesCompra();
        }
        connection=GestionDBConnection.getInstancia().getConnection();
        return instancia;
    }


}

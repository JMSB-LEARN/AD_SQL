package bbdd;

import java.sql.Connection;

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


}

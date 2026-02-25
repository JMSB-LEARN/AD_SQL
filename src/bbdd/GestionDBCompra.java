package bbdd;

import java.sql.Connection;

public class GestionDBCompra {
    private Connection connection;
    private GestionDBCompra instancia;

    public GestionDBCompra getInstancia() {
        if (instancia == null) {
            instancia = new GestionDBCompra();
        }
        connection=GestionDBConnection.getInstancia().getConnection();
        return instancia;
    }


}

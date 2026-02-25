package bbdd;

import java.sql.*;

class GestionDBConnection {

    private static GestionDBConnection instancia;
    private Connection connection;

    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "tienda";
    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
    private static final String DB_USER = "root";
    private static final String DB_PASS = "959546724";


    private GestionDBConnection() {
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

    public static GestionDBConnection getInstancia() {
        if (instancia == null) {
            instancia = new GestionDBConnection();
        }
        return instancia;
    }

    public Connection getConnection() {
        return connection;
    }
}

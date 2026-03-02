package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class Conexao {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String URL = "jdbc:postgresql://"
            + dotenv.get("DB_HOST") + ":"
            + dotenv.get("DB_PORT") + "/"
            + dotenv.get("DB_NAME") + "?sslmode=require";
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    public Connection conectar() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            assert URL != null;
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;

        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", ex);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver do banco de dados não encontrado", e);
        }
    }

    public boolean desconectar(Connection conn) {
        try{
            if(conn != null && !conn.isClosed()){
                conn.close();
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return false;
    }


}

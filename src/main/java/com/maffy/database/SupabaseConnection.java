package com.maffy.database;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;

public class SupabaseConnection {
    private static Connection connection = null;
    
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Cargar configuración
                Properties props = new Properties();
                InputStream input = SupabaseConnection.class.getClassLoader()
                    .getResourceAsStream("config.properties");
                
                if (input == null) {
                    System.out.println("❌ No se encontró config.properties");
                    return null;
                }
                
                props.load(input);
                
                // Establecer conexión JDBC
                String jdbcUrl = props.getProperty("database.url");
                String user = props.getProperty("database.user");
                String password = props.getProperty("database.password");
                
                connection = DriverManager.getConnection(jdbcUrl, user, password);
                System.out.println("✅ Conexión a Supabase establecida");
                
            } catch (Exception e) {
                System.out.println("❌ Error conectando a Supabase: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔌 Conexión cerrada");
            }
        } catch (SQLException e) {
            System.out.println("Error cerrando conexión: " + e.getMessage());
        }
    }
}
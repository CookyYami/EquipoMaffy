package com.maffy.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:postgresql://db.enumdszoalsfsbuohqro.supabase.co:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "U22210613@utp";

    public static Connection conectar() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("[OK] Conexión exitosa a Supabase!");
        } catch (SQLException e) {
            System.out.println("[ERROR] Error al conectar a la base de datos:");
            e.printStackTrace();
        }
        return con;
    }

    public static void main(String[] args) {
        conectar(); // Prueba de conexión
    }
}

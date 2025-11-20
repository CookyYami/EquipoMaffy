package com.maffy.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_FILE = "C:\\Users\\USUARIO\\Documents\\EquipoMaffy\\app_debug.log";
    private static FileWriter writer = null;

    static {
        try {
            writer = new FileWriter(LOG_FILE, true); // append mode
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void info(String message) {
        log("INFO", message);
    }

    public static void debug(String message) {
        log("DEBUG", message);
    }

    public static void error(String message) {
        log("ERROR", message);
    }

    public static void warn(String message) {
        log("WARN", message);
    }

    public static void exception(String message, Exception ex) {
        log("EXCEPTION", message + " -> " + ex.getMessage());
        if (ex.getCause() != null) {
            log("EXCEPTION", "Cause: " + ex.getCause().toString());
        }
    }

    private static void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        String logMsg = String.format("[%s] %s - %s", timestamp, level, message);
        System.out.println(logMsg);
        try {
            if (writer != null) {
                writer.write(logMsg + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearLog() {
        try {
            writer = new FileWriter(LOG_FILE, false); // overwrite mode
            writer.write("=== LOG INICIADO ===\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

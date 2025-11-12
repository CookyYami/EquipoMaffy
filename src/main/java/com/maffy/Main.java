package com.maffy;

import com.maffy.services.ProductoService;
import com.maffy.models.Producto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static ProductoService productoService = new ProductoService();
    
    public static void main(String[] args) {
        System.out.println("🎂 BIENVENIDO A PASTELERÍA MAFFY 🎂");
        System.out.println("Sistema de Control de Ventas\n");
        
        mostrarMenuPrincipal();
    }
    
    private static void mostrarMenuPrincipal() {
        while (true) {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. 📦 Gestionar Productos");
            System.out.println("2. 💰 Registrar Venta");
            System.out.println("3. 📊 Ver Reportes");
            System.out.println("4. ❌ Salir");
            System.out.print("Selecciona una opción: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            
            switch (opcion) {
                case 1:
                    gestionarProductos();
                    break;
                case 2:
                    registrarVenta();
                    break;
                case 3:
                    verReportes();
                    break;
                case 4:
                    System.out.println("¡Hasta pronto! 👋");
                    return;
                default:
                    System.out.println("❌ Opción inválida");
            }
        }
    }
    
    private static void gestionarProductos() {
        System.out.println("\n--- GESTIÓN DE PRODUCTOS ---");
        System.out.println("1. Ver todos los productos");
        System.out.println("2. Agregar nuevo producto");
        System.out.println("3. Volver al menú principal");
        System.out.print("Selecciona: ");
        
        int opcion = scanner.nextInt();
        scanner.nextLine();
        
        switch (opcion) {
            case 1:
                verTodosProductos();
                break;
            case 2:
                agregarNuevoProducto();
                break;
            case 3:
                return;
            default:
                System.out.println("Opción inválida");
        }
    }
    
    private static void verTodosProductos() {
        System.out.println("\n--- LISTA DE PRODUCTOS ---");
        List<Producto> productos = productoService.obtenerTodosProductos();
        
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
        } else {
            for (Producto producto : productos) {
                System.out.println(producto);
            }
        }
    }
    
    private static void agregarNuevoProducto() {
        System.out.println("\n--- AGREGAR NUEVO PRODUCTO ---");
        
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();
        
        System.out.print("Precio: ");
        BigDecimal precio = scanner.nextBigDecimal();
        
        System.out.print("Stock: ");
        int stock = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Categoría: ");
        String categoria = scanner.nextLine();
        
        Producto nuevoProducto = new Producto(nombre, descripcion, precio, stock, categoria);
        
        if (productoService.agregarProducto(nuevoProducto)) {
            System.out.println("✅ Producto agregado exitosamente!");
        } else {
            System.out.println("❌ Error al agregar producto");
        }
    }
    
    private static void registrarVenta() {
        System.out.println("\n--- REGISTRAR VENTA ---");
        System.out.println("Función en desarrollo... 🚧");
    }
    
    private static void verReportes() {
        System.out.println("\n--- VER REPORTES ---");
        System.out.println("Función en desarrollo... 🚧");
    }
}
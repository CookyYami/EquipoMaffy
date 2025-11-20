package tools;

import com.maffy.services.ProductoService;
import com.maffy.models.Producto;
import java.util.List;

public class DbTestProductos {
    public static void main(String[] args) {
        ProductoService svc = new ProductoService();
        System.out.println("== Probando obtenerTodosProductos() ==");
        try {
            List<Producto> productos = svc.obtenerTodosProductos();
            System.out.println("Productos encontrados: " + productos.size());
            for (Producto p : productos) {
                System.out.println(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

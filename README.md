# EquipoMaffy

Este proyecto es la aplicación de consola para la Pastelería Maffy. Aquí encontrarás instrucciones para conectar la aplicación a una base de datos Supabase (Postgres) y verificar la conexión.

**Archivos relevantes:**
- `src/main/resources/config.properties` — plantilla de configuración (no subir credenciales reales)
- `src/main/java/com/maffy/database/SupabaseConnection.java` — gestiona la conexión JDBC (acepta variables de entorno `DB_URL`, `DB_USER`, `DB_PASSWORD`)
- `src/main/java/com/maffy/database/DBTest.java` — clase de prueba que imprime filas de `productos` y `ventas`

## Configurar conexión a Supabase

1. En el panel de Supabase de tu proyecto: Settings → Database → Connection string (o Credentials).
2. Copia la URL JDBC, usuario y contraseña.

Recomendado: usar variables de entorno en lugar de guardar credenciales en `config.properties`.

### PowerShell (temporal, en la sesión actual)
```powershell
$env:DB_URL = 'jdbc:postgresql://db.tu-proyecto.supabase.co:5432/postgres'
$env:DB_USER = 'postgres'
$env:DB_PASSWORD = 'TuPasswordSecreto'
```

La aplicación añadirá `?sslmode=require` automáticamente si la URL no lo contiene.

## Ejecutar la prueba de conexión

1. Asegúrate de tener Java 11+ instalado.
2. Instala Maven y asegúrate de que `mvn` está en el PATH (ver `mvn -v`).
3. En PowerShell, en la raíz del proyecto:
```powershell
mvn -q compile
mvn exec:java -Dexec.mainClass=com.maffy.database.DBTest
```

Salida esperada: filas de ejemplo de `productos` y `ventas` o mensajes de error descriptivos si faltan permisos/credenciales.

## Archivos de ejemplo
- `.env.example` — plantilla con variables de entorno (no contiene valores reales).

Si quieres, puedo añadir scripts para automatizar la ejecución o añadir `mvnw` (Maven Wrapper) al proyecto para no depender de una instalación global de Maven. Pídemelo y lo preparo.
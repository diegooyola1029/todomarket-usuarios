# todomarket-usuarios

Repositorio del microservicio de Usuarios para el tercer corte de Diseño de Soluciones. Administra usuarios en MongoDB. Para las solicitudes de productos, el cliente `InventarioClient` llama por HTTP a la API del repositorio `todomarket-productos`.

## Ejecutar

Con Java 17 o posterior, Maven, MongoDB y Productos disponibles, ejecuta desde esta carpeta:

```powershell
mvn spring-boot:run
```

La API local escucha en `8184`. [Swagger](http://localhost:8184/swagger-ui/index.html) documenta Usuarios y las rutas de Productos que se reenvían a Inventario.

| Variable | Uso |
| --- | --- |
| `PORT` | Puerto local 8184 por defecto. |
| `MONGODB_URI` | URI MongoDB. Por defecto apunta a la base local `todomarket` en el puerto 27017. |
| `INVENTORY_URL` | Origen HTTP de Productos. El valor local es `http://localhost:8183`. |
| `INVENTORY_TOKEN` | Credencial de Productos cuando ese servicio activa `SERVICE_TOKEN`. |
| `SERVICE_TOKEN` | Credencial del servicio en la nube. TodoMarket la envía como `USUARIOS_TOKEN`. |

Usuarios se compila y se publica desde este repositorio. Tanto [Productos](https://github.com/diegooyola1029/todomarket-productos) como [TodoMarket](https://github.com/diegooyola1029/TodoMarket) tienen su propio ciclo de ejecución.

# TodoMarket - Microservicio de usuarios

API REST independiente para crear y administrar usuarios, sus datos de contacto, roles y estado. Construido con Java 17 y Spring Boot.

## Requisitos

- JDK 17 o superior
- Apache Maven 3.6.3 o superior

## Ejecutar

Desde la raíz de este repositorio:

```powershell
mvn spring-boot:run
```

La API de usuarios queda disponible en `http://localhost:8184`.

## Endpoints

Base: `http://localhost:8184/api/usuarios`

- `GET /api/usuarios` - listar usuarios
- `GET /api/usuarios/{id}` - buscar por ID
- `POST /api/usuarios` - crear; el servicio asigna el ID
- `PUT /api/usuarios/{id}` - actualizar
- `DELETE /api/usuarios/{id}` - eliminar

La API habilita CORS para el frontend local en `http://localhost:4200`.

## Relación con el microservicio de productos

Este servicio conserva endpoints de productos como proxy para los clientes existentes de TodoMarket. Para que esas rutas funcionen, inicia también el repositorio [todomarket-productos](https://github.com/diegooyola1029/todomarket-productos) en `http://localhost:8183`. La URL se puede cambiar mediante la propiedad `todomarket.inventory.url`.

La API de usuarios y la interfaz Angular pueden funcionar de forma independiente del proxy de productos.

## Datos

El repositorio de datos actual es en memoria. Al detener o reiniciar el servicio se vuelven a cargar los datos de ejemplo y se reinicia la secuencia de IDs.

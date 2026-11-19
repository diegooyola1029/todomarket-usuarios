package minimercado.api.producto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import minimercado.api.ApiError;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Productos · proxy", description = "Operaciones reenviadas por HTTP a Inventario.")
@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {
    private final InventarioClient inventario;

    public ProductoRestController(InventarioClient inventario) { this.inventario = inventario; }

    @Operation(summary = "Listar productos", description = "Devuelve el listado de registros ordenado por ID. El backend reenvía la operación a Inventario.")
    @ApiResponse(responseCode = "200", description = "Consulta correcta.", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "503", description = "El servicio de inventario o su base de datos no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El microservicio de inventario no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @GetMapping
    public List<ProductoResponse> listar() { return inventario.listar(); }

    @Operation(summary = "Consultar por ID", description = "Devuelve un registro por el identificador numérico de la ruta. El backend reenvía la operación a Inventario.")
    @ApiResponse(responseCode = "200", description = "Consulta correcta.", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Identificador o datos inválidos.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El identificador o el cuerpo de la solicitud tiene un formato inválido.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "404", description = "No se encontró el registro.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"Producto no encontrado.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "503", description = "El servicio de inventario o su base de datos no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El microservicio de inventario no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @GetMapping("/{id}")
    public ProductoResponse buscar(@Parameter(description = "ID numérico del registro", example = "42") @PathVariable int id) { return inventario.buscar(id); }

    @Operation(summary = "Crear", description = "Asigna un ID y devuelve el registro creado. El encabezado Location indica cómo consultarlo. El backend reenvía la operación a Inventario.")
    @ApiResponse(responseCode = "201", description = "Registro creado.", useReturnTypeSchema = true,
            headers = @Header(name = "Location", description = "Ruta de consulta del registro."))
    @ApiResponse(responseCode = "400", description = "Identificador o datos inválidos.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El identificador o el cuerpo de la solicitud tiene un formato inválido.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "409", description = "Los datos deben ser únicos.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"Ya existe un producto con ese nombre.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "503", description = "El servicio de inventario o su base de datos no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El microservicio de inventario no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@RequestBody ProductoRequest solicitud) {
        ProductoResponse creado = inventario.crear(solicitud);
        return ResponseEntity.created(URI.create("/api/productos/" + creado.idProducto())).body(creado);
    }

    @Operation(summary = "Actualizar", description = "Sustituye los datos editables. Si el cuerpo incluye un ID, debe coincidir con la ruta. El backend reenvía la operación a Inventario.")
    @ApiResponse(responseCode = "200", description = "Registro actualizado.", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Identificador o datos inválidos.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El identificador o el cuerpo de la solicitud tiene un formato inválido.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "404", description = "No se encontró el registro.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"Producto no encontrado.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "409", description = "Los datos deben ser únicos.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"Ya existe un producto con ese nombre.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "503", description = "El servicio de inventario o su base de datos no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El microservicio de inventario no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @PutMapping("/{id}")
    public ProductoResponse actualizar(@Parameter(description = "ID numérico del registro", example = "42") @PathVariable int id, @RequestBody ProductoRequest solicitud) {
        return inventario.actualizar(id, solicitud);
    }

    @Operation(summary = "Eliminar", description = "Borra el registro identificado por la ruta y responde con el cuerpo vacío. El backend reenvía la operación a Inventario.")
    @ApiResponse(responseCode = "204", description = "Registro eliminado.", content = @Content)
    @ApiResponse(responseCode = "400", description = "Identificador o datos inválidos.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El identificador o el cuerpo de la solicitud tiene un formato inválido.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "404", description = "No se encontró el registro.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"Producto no encontrado.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "503", description = "El servicio de inventario o su base de datos no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El microservicio de inventario no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID numérico del registro", example = "42") @PathVariable int id) {
        inventario.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar productos con stock bajo", description = "Devuelve productos con existencias positivas iguales o inferiores al mínimo. El backend reenvía la operación a Inventario.")
    @ApiResponse(responseCode = "200", description = "Consulta correcta.", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "503", description = "El servicio de inventario o su base de datos no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El microservicio de inventario no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @GetMapping("/stock-bajo")
    public List<ProductoResponse> stockBajo() { return inventario.stockBajo(); }

    @Operation(summary = "Listar productos agotados", description = "Devuelve productos sin existencias. El backend reenvía la operación a Inventario.")
    @ApiResponse(responseCode = "200", description = "Consulta correcta.", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "503", description = "El servicio de inventario o su base de datos no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El microservicio de inventario no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @GetMapping("/agotados")
    public List<ProductoResponse> agotados() { return inventario.agotados(); }

    @Operation(summary = "Listar productos vencidos", description = "Devuelve productos cuya fecha de vencimiento es anterior a hoy. El backend reenvía la operación a Inventario.")
    @ApiResponse(responseCode = "200", description = "Consulta correcta.", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "503", description = "El servicio de inventario o su base de datos no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El microservicio de inventario no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @GetMapping("/vencidos")
    public List<ProductoResponse> vencidos() { return inventario.vencidos(); }

    @Operation(summary = "Listar productos próximos a vencer", description = "Devuelve productos que vencen entre hoy y los próximos 30 días, inclusive. El backend reenvía la operación a Inventario.")
    @ApiResponse(responseCode = "200", description = "Consulta correcta.", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "503", description = "El servicio de inventario o su base de datos no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El microservicio de inventario no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @GetMapping("/proximos-a-vencer")
    public List<ProductoResponse> proximosAVencer() { return inventario.proximosAVencer(); }

    @Operation(summary = "Consultar alertas de inventario", description = "Devuelve mensajes de agotamiento, stock bajo y vencimiento próximo calculados a partir de MongoDB. El backend reenvía la operación a Inventario.")
    @ApiResponse(responseCode = "200", description = "Consulta correcta.", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "503", description = "El servicio de inventario o su base de datos no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El microservicio de inventario no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @GetMapping("/alertas")
    public List<String> alertas() { return inventario.alertas(); }
}

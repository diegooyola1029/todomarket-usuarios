package minimercado.api.usuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.List;
import minimercado.api.ApiError;
import minimercado.modelo.Usuario;
import minimercado.servicio.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Usuarios", description = "Gestión de usuarios del supermercado.")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {
    private final UsuarioService service;

    public UsuarioRestController(UsuarioService service) { this.service = service; }

    @Operation(summary = "Listar usuarios", description = "Devuelve el listado de registros ordenado por ID.")
    @ApiResponse(responseCode = "200", description = "Consulta correcta.", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "503", description = "MongoDB no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"MongoDB no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @GetMapping
    public List<UsuarioResponse> listar() { return service.listar().stream().map(UsuarioResponse::desde).toList(); }

    @Operation(summary = "Consultar por ID", description = "Devuelve un registro por el identificador numérico de la ruta.")
    @ApiResponse(responseCode = "200", description = "Consulta correcta.", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Identificador o datos inválidos.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El identificador o el cuerpo de la solicitud tiene un formato inválido.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "404", description = "No se encontró el registro.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"Usuario no encontrado.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "503", description = "MongoDB no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"MongoDB no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @GetMapping("/{id}")
    public UsuarioResponse buscar(@Parameter(description = "ID numérico del registro", example = "42") @PathVariable int id) { return UsuarioResponse.desde(service.buscar(id)); }

    @Operation(summary = "Crear", description = "Asigna un ID y devuelve el registro creado. El encabezado Location indica cómo consultarlo.")
    @ApiResponse(responseCode = "201", description = "Registro creado.", useReturnTypeSchema = true,
            headers = @Header(name = "Location", description = "Ruta de consulta del registro."))
    @ApiResponse(responseCode = "400", description = "Identificador o datos inválidos.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El identificador o el cuerpo de la solicitud tiene un formato inválido.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "409", description = "Los datos deben ser únicos.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"Ya existe ese nombre de usuario.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "503", description = "MongoDB no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"MongoDB no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@RequestBody UsuarioRequest solicitud) {
        Usuario creado = service.crear(solicitud.aUsuario(1));
        return ResponseEntity.created(URI.create("/api/usuarios/" + creado.getIdPersona()))
                .body(UsuarioResponse.desde(creado));
    }

    @Operation(summary = "Actualizar", description = "Sustituye los datos editables. Si el cuerpo incluye un ID, debe coincidir con la ruta.")
    @ApiResponse(responseCode = "200", description = "Registro actualizado.", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Identificador o datos inválidos.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El identificador o el cuerpo de la solicitud tiene un formato inválido.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "404", description = "No se encontró el registro.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"Usuario no encontrado.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "409", description = "Los datos deben ser únicos.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"Ya existe ese nombre de usuario.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "503", description = "MongoDB no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"MongoDB no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @PutMapping("/{id}")
    public UsuarioResponse actualizar(@Parameter(description = "ID numérico del registro", example = "42") @PathVariable int id, @RequestBody UsuarioRequest solicitud) {
        if (solicitud.idPersona() != 0 && id != solicitud.idPersona()) {
            throw new IllegalArgumentException("El ID de la ruta y del usuario deben coincidir.");
        }
        return UsuarioResponse.desde(service.actualizar(solicitud.aUsuario(id)));
    }

    @Operation(summary = "Eliminar", description = "Borra el registro identificado por la ruta y responde con el cuerpo vacío.")
    @ApiResponse(responseCode = "204", description = "Registro eliminado.", content = @Content)
    @ApiResponse(responseCode = "400", description = "Identificador o datos inválidos.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"El identificador o el cuerpo de la solicitud tiene un formato inválido.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "404", description = "No se encontró el registro.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"Usuario no encontrado.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @ApiResponse(responseCode = "503", description = "MongoDB no está disponible.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value =
                            "{\"message\":\"MongoDB no está disponible.\",\"timestamp\":\"2026-11-18T14:30:00\"}")))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID numérico del registro", example = "42") @PathVariable int id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

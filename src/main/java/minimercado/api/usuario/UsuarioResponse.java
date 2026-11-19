package minimercado.api.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import minimercado.modelo.Usuario;

public record UsuarioResponse(
        @Schema(description = "ID numérico del usuario.", example = "25")
        int idPersona,
        @Schema(description = "Nombre único del usuario.", example = "maria.demo")
        String nombreUsuario,
        @Schema(description = "Documento de identificación.", example = "TM-DEMO-25")
        String documento,
        @Schema(description = "Teléfono opcional.", example = "3000000025")
        String telefono,
        @Schema(description = "Correo opcional.", example = "maria.demo@example.test")
        String correo,
        @Schema(description = "Rol funcional, guardado dentro del usuario.")
        RolResponse rol,
        @Schema(description = "Estado activo o inactivo.", example = "true")
        boolean estado,
        @Schema(description = "Fecha en que se creó el usuario.", example = "2026-11-18", type = "string", format = "date")
        LocalDate fechaCreacion) {

    public static UsuarioResponse desde(Usuario usuario) {
        var rol = usuario.getRol();
        return new UsuarioResponse(usuario.getIdPersona(), usuario.getNombreUsuario(), usuario.getDocumento(),
                usuario.getTelefono(), usuario.getCorreo(),
                new RolResponse(rol.getIdRol(), rol.getNombreRol(), rol.getDescripcion()),
                usuario.isEstado(), usuario.getFechaCreacion());
    }

    public Usuario aUsuario() {
        var rolUsuario = new minimercado.modelo.Rol(rol.idRol(), rol.nombreRol(), rol.descripcion());
        return new Usuario(idPersona, nombreUsuario, "", documento, telefono, correo, rolUsuario, estado);
    }

    public record RolResponse(
        @Schema(description = "Identificador del rol.", example = "3") int idRol,
        @Schema(description = "Nombre del rol.", example = "Cliente") String nombreRol,
        @Schema(description = "Descripción del rol.", example = "Cliente del minimercado") String descripcion) { }
}

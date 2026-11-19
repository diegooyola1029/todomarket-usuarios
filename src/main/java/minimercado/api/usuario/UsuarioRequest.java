package minimercado.api.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

import minimercado.modelo.Rol;
import minimercado.modelo.Usuario;

public record UsuarioRequest(
        @Schema(description = "ID numérico del usuario. En POST lo asigna el servicio.", example = "25")
        int idPersona,
        @Schema(description = "Nombre único del usuario.", example = "maria.demo", requiredMode = Schema.RequiredMode.REQUIRED)
        String nombreUsuario,
        @Schema(description = "Campo de escritura. En PUT, vacío conserva el valor.", example = "ClaveDemo2026", accessMode = Schema.AccessMode.WRITE_ONLY, format = "password")
        String contrasena,
        @Schema(description = "Documento de identificación.", example = "TM-DEMO-25", requiredMode = Schema.RequiredMode.REQUIRED)
        String documento,
        @Schema(description = "Teléfono opcional.", example = "3000000025")
        String telefono,
        @Schema(description = "Correo opcional.", example = "maria.demo@example.test")
        String correo,
        @Schema(description = "Rol funcional, guardado dentro del usuario.", requiredMode = Schema.RequiredMode.REQUIRED)
        RolRequest rol,
        @Schema(description = "Estado activo o inactivo.", example = "true")
        boolean estado) {

    public Usuario aUsuario() {
        return aUsuario(idPersona);
    }

    public Usuario aUsuario(int id) {
        Rol rolUsuario = rol == null ? null : new Rol(rol.idRol(), rol.nombreRol(), rol.descripcion());
        return new Usuario(id, nombreUsuario, contrasena, documento, telefono, correo, rolUsuario, estado);
    }

    public static UsuarioRequest desde(Usuario usuario) {
        Rol rolUsuario = usuario.getRol();
        RolRequest rolDto = new RolRequest(rolUsuario.getIdRol(), rolUsuario.getNombreRol(), rolUsuario.getDescripcion());
        return new UsuarioRequest(usuario.getIdPersona(), usuario.getNombreUsuario(), usuario.getContrasena(),
                usuario.getDocumento(), usuario.getTelefono(), usuario.getCorreo(), rolDto, usuario.isEstado());
    }

    public record RolRequest(
        @Schema(description = "Identificador del rol.", example = "3") int idRol,
        @Schema(description = "Nombre del rol.", example = "Cliente", requiredMode = Schema.RequiredMode.REQUIRED) String nombreRol,
        @Schema(description = "Descripción del rol.", example = "Cliente del minimercado") String descripcion) { }
}

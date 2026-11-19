package minimercado.repositorio;

import java.time.LocalDate;
import java.util.Locale;
import minimercado.modelo.Rol;
import minimercado.modelo.Usuario;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("usuarios")
record UsuarioDocumento(
        @Id int id,
        String nombreUsuario,
        @Indexed(unique = true) String nombreNormalizado,
        String contrasenaHash,
        @Indexed(unique = true) String documento,
        @Indexed(unique = true, sparse = true) String telefono,
        String correo,
        @Indexed(unique = true, sparse = true) String correoNormalizado,
        RolDocumento rol,
        boolean estado,
        LocalDate fechaCreacion) {

    static UsuarioDocumento desde(Usuario usuario) {
        Rol rol = usuario.getRol();
        String correo = opcional(usuario.getCorreo());
        return new UsuarioDocumento(usuario.getIdPersona(), usuario.getNombreUsuario(),
                usuario.getNombreUsuario().toLowerCase(Locale.ROOT), opcional(usuario.getContrasena()),
                usuario.getDocumento(), opcional(usuario.getTelefono()), correo,
                correo == null ? null : correo.toLowerCase(Locale.ROOT),
                new RolDocumento(rol.getIdRol(), rol.getNombreRol(), rol.getDescripcion()),
                usuario.isEstado(), usuario.getFechaCreacion());
    }

    Usuario aUsuario() {
        return new Usuario(id, nombreUsuario, contrasenaHash, documento, telefono, correo,
                new Rol(rol.idRol(), rol.nombreRol(), rol.descripcion()), estado, fechaCreacion);
    }

    private static String opcional(String valor) {
        return valor.isBlank() ? null : valor;
    }

    record RolDocumento(int idRol, String nombreRol, String descripcion) { }
}

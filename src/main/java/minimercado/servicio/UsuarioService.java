package minimercado.servicio;

import java.nio.charset.StandardCharsets;
import java.util.List;
import minimercado.modelo.Usuario;
import minimercado.repositorio.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder contrasenas;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder contrasenas) {
        this.repository = repository;
        this.contrasenas = contrasenas;
    }

    public Usuario crear(Usuario usuario) {
        String hash = protegerContrasena(usuario.getContrasena());
        Usuario creado = new Usuario(repository.siguienteId(), usuario.getNombreUsuario(), hash,
                usuario.getDocumento(), usuario.getTelefono(), usuario.getCorreo(), usuario.getRol(),
                usuario.isEstado());
        repository.guardar(creado);
        return creado;
    }

    public List<Usuario> listar() { return repository.listar(); }

    public Usuario buscar(int id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
    }

    public Usuario actualizar(Usuario datos) {
        Usuario cambios = new Usuario(datos.getIdPersona(), datos.getNombreUsuario(),
                protegerContrasena(datos.getContrasena()), datos.getDocumento(), datos.getTelefono(),
                datos.getCorreo(), datos.getRol(), datos.isEstado());
        return repository.actualizar(cambios);
    }

    public void eliminar(int id) {
        if (!repository.eliminar(id)) throw new IllegalArgumentException("Usuario no encontrado.");
    }

    private String protegerContrasena(String clave) {
        if (clave.isBlank()) return "";
        if (clave.length() < 4 || clave.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 4 caracteres y hasta 72 bytes UTF-8.");
        }
        return contrasenas.encode(clave);
    }
}

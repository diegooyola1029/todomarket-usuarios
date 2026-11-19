package minimercado.repositorio;

import java.util.List;
import java.util.Optional;
import minimercado.modelo.Usuario;

public interface UsuarioRepository {
    int siguienteId();
    void guardar(Usuario usuario);
    List<Usuario> listar();
    Optional<Usuario> buscarPorId(int id);
    Usuario actualizar(Usuario usuario);
    boolean eliminar(int id);
}

package minimercado.repositorio;

import java.util.List;
import java.util.Optional;
import minimercado.modelo.Usuario;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
class MongoUsuarioRepository implements UsuarioRepository {
    private final MongoTemplate mongo;

    MongoUsuarioRepository(MongoTemplate mongo) {
        this.mongo = mongo;
    }

    @Override
    public int siguienteId() {
        Secuencia secuencia = mongo.findAndModify(
                porId("usuarios"), new Update().inc("valor", 1),
                FindAndModifyOptions.options().upsert(true).returnNew(true), Secuencia.class);
        return Math.toIntExact(secuencia.valor());
    }

    @Override
    public void guardar(Usuario usuario) {
        mongo.insert(UsuarioDocumento.desde(usuario));
    }

    @Override
    public List<Usuario> listar() {
        return mongo.find(new Query().with(Sort.by("_id")), UsuarioDocumento.class)
                .stream().map(UsuarioDocumento::aUsuario).toList();
    }

    @Override
    public Optional<Usuario> buscarPorId(int id) {
        return Optional.ofNullable(mongo.findById(id, UsuarioDocumento.class))
                .map(UsuarioDocumento::aUsuario);
    }

    @Override
    public Usuario actualizar(Usuario usuario) {
        UsuarioDocumento datos = UsuarioDocumento.desde(usuario);
        Update cambios = new Update()
                .set("nombreUsuario", datos.nombreUsuario())
                .set("nombreNormalizado", datos.nombreNormalizado())
                .set("documento", datos.documento())
                .set("rol", datos.rol())
                .set("estado", datos.estado());
        cambiarOpcional(cambios, "telefono", datos.telefono());
        cambiarOpcional(cambios, "correo", datos.correo());
        cambiarOpcional(cambios, "correoNormalizado", datos.correoNormalizado());
        if (datos.contrasenaHash() != null) cambios.set("contrasenaHash", datos.contrasenaHash());
        UsuarioDocumento actualizado = mongo.findAndModify(porId(datos.id()), cambios,
                FindAndModifyOptions.options().returnNew(true), UsuarioDocumento.class);
        if (actualizado == null) throw new IllegalArgumentException("Usuario no encontrado.");
        return actualizado.aUsuario();
    }

    @Override
    public boolean eliminar(int id) {
        return mongo.remove(porId(id), UsuarioDocumento.class).getDeletedCount() == 1;
    }

    private static void cambiarOpcional(Update cambios, String campo, String valor) {
        if (valor == null) cambios.unset(campo);
        else cambios.set(campo, valor);
    }

    private static Query porId(Object id) {
        return Query.query(Criteria.where("_id").is(id));
    }

    @Document("secuencias")
    private record Secuencia(@Id String id, long valor) { }
}

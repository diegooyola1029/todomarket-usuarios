package minimercado.backend;

import minimercado.repositorio.UsuarioRepository;
import minimercado.servicio.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UsuarioConfiguration {
    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    UsuarioService usuarioService(UsuarioRepository repository, PasswordEncoder encoder) {
        return new UsuarioService(repository, encoder);
    }
}

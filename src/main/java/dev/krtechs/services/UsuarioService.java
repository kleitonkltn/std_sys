package dev.krtechs.services;

import dev.krtechs.model.Usuarios;
import dev.krtechs.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UsuarioService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;


    @Override
    public UserDetails loadUserByUsername(String matricula) throws UsernameNotFoundException {
        Usuarios usuario =
                usuarioRepository.findByMatricula(matricula).orElseThrow(()
                        -> new UsernameNotFoundException("Matricula não encontrada"));
        return User.builder()
                .username(usuario.getMatricula())
                .password(usuario.getPassword())
                .roles(usuario.getAccessLevel().toString())
                .build();
    }

    public Usuarios setLastLogin(Usuarios user){
        user.setLastlogin(LocalDateTime.now());
        return usuarioRepository.save(user);
    }


}

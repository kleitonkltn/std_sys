package dev.krtechs.controllers;

import dev.krtechs.model.Usuarios;
import dev.krtechs.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioRepository usuarioRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Usuarios create(@RequestBody @Valid final Usuarios user) {
        return new Usuarios().RemovePassword(usuarioRepository.save(user));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_PUBLIC') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERVISOR')")
    public Usuarios getByID(@PathVariable final Integer id) {
        final Usuarios user = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario Não Encontrado"));
        return new Usuarios().RemovePassword(user);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_PUBLIC') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERVISOR')")
    public List<Usuarios> getAll() {
        final List<Usuarios> usuarios = usuarioRepository.findAll();
        return new Usuarios().RemovePasswordList(usuarios);
    }

    @PutMapping("{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void update(@PathVariable final Integer id, @RequestBody @Valid final Usuarios user) {
        usuarioRepository.findById(id).map(userMap -> {
            user.setPassword(userMap.getPassword());
            return usuarioRepository.save(user);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario Não Encontrado"));
    }

}
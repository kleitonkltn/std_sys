package dev.krtechs.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@TypeDef(name="AccessLevel", typeClass= Usuarios.AccessLevel.class)
public class Usuarios {
    public static enum AccessLevel {
        ADMIN,
        SUPERVISOR,
        PUBLIC
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String username;

    @Column(unique = true)
    private String matricula;

    @Column
    private String password;

    @Column
    @JsonFormat(pattern = "dd/MM/yyyy@HH:mm:ss", locale = "pt_BR")
    private LocalDateTime lastlogin;

    @Column
    @Enumerated(EnumType.STRING)
    @Type(type="AccessLevel")
    private AccessLevel accessLevel = AccessLevel.PUBLIC;

    @Column(columnDefinition = "boolean default true")
    private boolean newUser = true;

    @PrePersist
    public void PrePersist()
    {
        setAccessLevel(AccessLevel.PUBLIC);
        setPassword(new BCryptPasswordEncoder().encode(getPassword()));
    }

    public List<Usuarios> RemovePasswordList(List<Usuarios> users) {
        List<Usuarios> newListUsersNotFielPassword = users.stream().map(user -> {
            user.setPassword(null);
            return user;
        }).collect(Collectors.toList());
        return newListUsersNotFielPassword;
    }

    public Usuarios RemovePassword(Usuarios user) {
        user.setPassword(null);
        return user;
    }


}


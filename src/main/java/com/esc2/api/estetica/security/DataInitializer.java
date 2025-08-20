package com.esc2.api.estetica.security;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.esc2.api.estetica.enums.CargoEnum;
import com.esc2.api.estetica.models.UsuarioModel;
import com.esc2.api.estetica.repositories.UsuarioRepository;

/* Este é um arquivo temporário pra quando não
tem usuario no banco e precisa de um admin. Não vai pra produção*/ 

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public DataInitializer(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (repo.count() == 0) {
            UsuarioModel admin = new UsuarioModel();
            admin.setNome("Admin");
            admin.setEmail("admin@sistema.com");
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("123456"));
            admin.setCargoEnum(CargoEnum.ADMINISTRADOR);
            admin.setCreationDate(LocalDateTime.now());
            repo.save(admin);
            System.out.println("Admin criado: admin / 123456");
        }
    }
}

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
        // Verifica se o usuário admin já existe
        if (!repo.findByEmail("admin@sistema.com").isPresent()) {
            UsuarioModel admin = new UsuarioModel();
            admin.setNome("Admin");
            admin.setEmail("admin@sistema.com");
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("123456"));
            admin.setCargoEnum(CargoEnum.ADMINISTRADOR);
            admin.setCreationDate(LocalDateTime.now());
            repo.save(admin);
            System.out.println("Admin criado: admin@sistema.com / 123456");
        }
        
        // Verifica se o usuário douglas já existe
        if (!repo.findByEmail("douglas@admin.com").isPresent()) {
            UsuarioModel douglas = new UsuarioModel();
            douglas.setNome("Douglas");
            douglas.setEmail("douglas@admin.com");
            douglas.setUsername("douglas");
            douglas.setPassword(encoder.encode("douglas"));
            douglas.setCargoEnum(CargoEnum.ADMINISTRADOR);
            douglas.setCreationDate(LocalDateTime.now());
            repo.save(douglas);
            System.out.println("Usuário Douglas criado: douglas@admin.com / douglas");
        }
        
        System.out.println("Total de usuários no sistema: " + repo.count());
    }
}

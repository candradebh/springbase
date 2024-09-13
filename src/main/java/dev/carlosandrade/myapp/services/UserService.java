package dev.carlosandrade.myapp.services;

import dev.carlosandrade.myapp.entity.UserEntity;
import dev.carlosandrade.myapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity registerUser(UserEntity user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username já existe");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email já existe");
        }

        // Criptografar a senha
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Salvar usuário no banco de dados
        return userRepository.save(user);
    }
}


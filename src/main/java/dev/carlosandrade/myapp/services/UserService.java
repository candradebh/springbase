package dev.carlosandrade.myapp.services;

import dev.carlosandrade.myapp.dto.request.ChangePasswordDTO;
import dev.carlosandrade.myapp.dto.request.UpdateProfileDTO;
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

    // Obter o usuário pelo username
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserEntity updateProfileByUsername(String username, UpdateProfileDTO updateProfileDTO) {
        UserEntity user = getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        // Atualizar os dados de perfil
        user.setName(updateProfileDTO.getName());
        user.setEmail(updateProfileDTO.getEmail());

        return userRepository.save(user);
    }

    public void changePasswordByUsername(String username, ChangePasswordDTO changePasswordDTO) {
        UserEntity user = getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        // Verifica a senha antiga
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Senha antiga incorreta");
        }

        // Atualizar a nova senha
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
    }
}


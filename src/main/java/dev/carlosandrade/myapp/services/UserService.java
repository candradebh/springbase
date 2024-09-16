package dev.carlosandrade.myapp.services;

import dev.carlosandrade.myapp.dto.request.ChangePasswordDTO;
import dev.carlosandrade.myapp.dto.request.UpdateProfileDTO;
import dev.carlosandrade.myapp.entity.UserEntity;
import dev.carlosandrade.myapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    // Atualizar perfil do usuário
    public UserEntity updateProfile(Long userId, UpdateProfileDTO updateProfileDTO) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            user.setName(updateProfileDTO.getName());
            user.setEmail(updateProfileDTO.getEmail());
            return userRepository.save(user);
        } else {
            throw new RuntimeException("Usuário não encontrado");
        }
    }

    public void changePassword(Long userId, ChangePasswordDTO changePasswordDTO) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();

            // Verifica se a senha antiga corresponde à armazenada
            if (passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
                userRepository.save(user);
            } else {
                throw new RuntimeException("Senha antiga incorreta");
            }
        } else {
            throw new RuntimeException("Usuário não encontrado");
        }
    }
}


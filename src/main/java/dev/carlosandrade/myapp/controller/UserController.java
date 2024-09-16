package dev.carlosandrade.myapp.controller;

import dev.carlosandrade.myapp.dto.request.ChangePasswordDTO;
import dev.carlosandrade.myapp.dto.request.UpdateProfileDTO;
import dev.carlosandrade.myapp.entity.UserEntity;
import dev.carlosandrade.myapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserEntity user) {
        try {
            UserEntity newUser = userService.registerUser(user);
            return ResponseEntity.ok(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{userId}/profile")
    public ResponseEntity<UserEntity> updateProfile(
            @PathVariable Long userId,
            @RequestBody UpdateProfileDTO updateProfileDTO) {
        UserEntity updatedUser = userService.updateProfile(userId, updateProfileDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/{userId}/change-password")
    public ResponseEntity<String> changePassword(
            @PathVariable Long userId,
            @RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(userId, changePasswordDTO);
        return ResponseEntity.ok("Senha alterada com sucesso!");
    }
}


package dev.carlosandrade.myapp.controller;

import dev.carlosandrade.myapp.dto.request.AuthRequest;
import dev.carlosandrade.myapp.dto.request.ChangePasswordDTO;
import dev.carlosandrade.myapp.dto.request.UpdateProfileDTO;
import dev.carlosandrade.myapp.dto.response.AuthResponse;
import dev.carlosandrade.myapp.entity.UserEntity;
import dev.carlosandrade.myapp.provider.JwtTokenProvider;
import dev.carlosandrade.myapp.repository.UserRepository;
import dev.carlosandrade.myapp.services.CustomUserDetailsService;
import dev.carlosandrade.myapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            // Autenticação do usuário
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Carrega os detalhes do usuário autenticado
            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

            // Obtém o usuário do banco de dados para pegar nome e email
            UserEntity user = userRepository.findByUsername(authRequest.getUsername());

            // Cria o token JWT com username, roles, id, nome e email
            String token = jwtTokenProvider.createToken(
                    userDetails.getUsername(),
                    userDetails.getAuthorities().stream()
                            .map(grantedAuthority -> grantedAuthority.getAuthority())  // Extrai os nomes das roles
                            .collect(Collectors.toList()),  // Converte para uma lista de String
                    user.getId(),
                    user.getName(),
                    user.getEmail()
            );

            return ResponseEntity.ok(new AuthResponse(token));


        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos");
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserEntity> getProfile() {
        // Obter o usuário logado a partir do SecurityContextHolder
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        // Obter os dados do usuário baseado no username
        UserEntity user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserEntity> updateProfile(@RequestBody UpdateProfileDTO updateProfileDTO) {
        // Obter o usuário logado a partir do SecurityContextHolder
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        // Atualizar o perfil baseado no username do usuário logado
        UserEntity updatedUser = userService.updateProfileByUsername(username, updateProfileDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // Atualizar senha do usuário logado
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        // Obter o usuário logado a partir do SecurityContextHolder
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        // Atualizar a senha baseada no username do usuário logado
        userService.changePasswordByUsername(username, changePasswordDTO);
        return ResponseEntity.ok("Senha alterada com sucesso!");
    }
}



package ua.ugolek.controller.api;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ua.ugolek.exception.UserNotFoundException;
import ua.ugolek.model.PasswordResetToken;
import ua.ugolek.model.Role;
import ua.ugolek.model.User;
import ua.ugolek.model.UserSetting;
import ua.ugolek.security.JwtUtils;
import ua.ugolek.service.MailService;
import ua.ugolek.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/admin/signin")
    public ResponseEntity<?> authenticateAdmin(@Valid @RequestBody AuthRequest loginRequest) {
        log.info("AuthRequest: email - {} , password - {}", loginRequest.email, loginRequest.password);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        User user = (User) authentication.getPrincipal();
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);

        return ResponseEntity.ok(new JwtResponse(user, jwt, roles));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);

        User user = (User) authentication.getPrincipal();
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(user, jwt, roles));
    }

    @GetMapping("/fetchUserInfo")
    public ResponseEntity<?> fetchUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(authentication);

        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(user, jwt, roles));
    }

    @PostMapping("/resetPassword")
    public HttpStatus resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        String email = resetPasswordRequest.getEmail();
        User user = userService.findByEmail(email).orElseThrow(UserNotFoundException::new);
        PasswordResetToken passwordResetToken = userService.createPasswordResetTokenForUser(user);
        mailService.sendResetPasswordMessage(email, passwordResetToken.getToken());
        return HttpStatus.OK;
    }

    @PostMapping("/changePassword")
    public HttpStatus changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        String password = changePasswordRequest.getPassword();
        String token = changePasswordRequest.getToken();
        userService.changePasswordForUserByToken(password, token);
        return HttpStatus.OK;
    }

    @Data
    private static class AuthRequest {
        private String email;
        private String password;

        public User toUser() {
            return new User(email, password);
        }
    }

    @Data
    private static class ResetPasswordRequest {
        private String email;
    }

    @Data
    private static class ChangePasswordRequest {
        private String password;
        private String token;
    }

    @Data
    private static class JwtResponse {
        private String token;
        private Long id;
        private String email;
        private String firstName;
        private String lastName;
        private List<String> roles;
        private List<UserSetting> settings;

        public JwtResponse(User user, String token, List<String> roles) {
            this.token = token;
            this.roles = roles;
            this.email = user.getEmail();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.id = user.getId();
            this.settings = user.getSettings();
        }
    }

    @AllArgsConstructor
    private static class MessageResponse {
        private String message;
    }
}

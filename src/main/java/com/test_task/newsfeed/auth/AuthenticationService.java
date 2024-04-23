package com.test_task.newsfeed.auth;

import com.test_task.newsfeed.exception.IncorrectPasswordException;
import com.test_task.newsfeed.exception.TokenExpiredException;
import com.test_task.newsfeed.exception.UserAlreadyExists;
import com.test_task.newsfeed.exception.UserNotFoundException;
import com.test_task.newsfeed.model.User;
import com.test_task.newsfeed.network.AuthenticationRequest;
import com.test_task.newsfeed.network.AuthenticationResponse;
import com.test_task.newsfeed.network.LoginRequest;
import com.test_task.newsfeed.network.RegisterRequest;
import com.test_task.newsfeed.repo.UserRepository;
import com.test_task.newsfeed.validator.ObjectValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ObjectValidator<AuthenticationRequest> objectValidator;

    public AuthenticationResponse register(RegisterRequest request) {
        objectValidator.validate(request);
        String login = request.getLogin().trim();
        String password = request.getPassword().trim();
        String name = request.getName().trim();
        String surname = request.getSurname().trim();
        String avatarLink = (request.getAvatarLink() == null) ? null : request.getAvatarLink().trim();
        String middleName = (request.getMiddleName() == null) ? null : request.getMiddleName().trim();
        if (userRepository.existsByLogin(login)) {
            throw new UserAlreadyExists(String.format("User with login %s already exists", request.getLogin()));
        }
        if(!request.getConfirmationPassword().trim().equals(request.getPassword().trim())){
            throw new IncorrectPasswordException("Confirmation password should be the same as password");
        }
        var user = User.builder()
                .login(login)
                .name(name)
                .surname(surname)
                .middleName(middleName)
                .avatarLink(avatarLink)
                .password(passwordEncoder.encode(password))
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(LoginRequest request) {
        objectValidator.validate(request);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByLogin(request.getLogin())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public AuthenticationResponse refreshToken(
            HttpServletRequest request
    ) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userLogin;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new TokenExpiredException("No token found.");
        }
        refreshToken = authHeader.substring(7);
        userLogin = jwtService.extractUsername(refreshToken);
        if (userLogin != null) {
            var user = userRepository.findByLogin(userLogin)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                return AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
            throw new TokenExpiredException("Your refresh token has expired.");
        }

        throw new UserNotFoundException("Invalid login provided.");
    }
}
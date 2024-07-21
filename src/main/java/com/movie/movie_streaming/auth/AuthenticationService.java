package com.movie.movie_streaming.auth;



import com.movie.movie_streaming.email.EmailService;
import com.movie.movie_streaming.email.EmailTemplateName;
import com.movie.movie_streaming.exceptions.UserNotFoundException;
import com.movie.movie_streaming.exceptions.UsernameAlreadyTakenException;
import com.movie.movie_streaming.security.JwtService;
import com.movie.movie_streaming.Token.Token;
import com.movie.movie_streaming.Token.TokenRepository;
import com.movie.movie_streaming.user.User;
import com.movie.movie_streaming.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.movie.movie_streaming.role.Role.USER;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;
    @Value("${token.expiration-minutes}")
    private Integer minutesForExpiration;
    @Value("${token.chars}")
    private String chars;

    public void register(RegistrationRequest request) throws MessagingException {

        if (userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new UsernameAlreadyTakenException();
        }

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .role(USER)
                .build();

        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.getUsername(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(minutesForExpiration))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        Map<String,Object> claims = new HashMap<>();
        var user = ((User) auth.getPrincipal());
        claims.put("username",user.getUsername());
        var jwtToken = jwtService.generateToken(claims,(User) auth.getPrincipal());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token."));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())){
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Token has expired. New token has been sent.");
        }
        var id = savedToken.getUser().getId();

        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

    private String generateActivationCode(int length) {
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randInd = secRandom.nextInt(chars.length());
            codeBuilder.append(chars.charAt(randInd));
        }
        return codeBuilder.toString();
    }

}

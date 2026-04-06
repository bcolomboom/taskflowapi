package brunn.taskflowapi.service;

import brunn.taskflowapi.dto.user.UserRequestDTO;
import brunn.taskflowapi.dto.user.UserResponseDTO;
import brunn.taskflowapi.exception.EmailAlreadyExistsException;
import brunn.taskflowapi.model.User;
import brunn.taskflowapi.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Primary
public class UserService implements UserDetailsService {

    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserResponseDTO createUser(UserRequestDTO dto) {
        if (repository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException(dto.email());
        }
        String hash = encoder.encode(dto.password());

        User user = new User(dto.name(), dto.email(), hash);
        User saved = repository.save(user);

        return new UserResponseDTO(saved.getId(), saved.getName(), saved.getEmail(), saved.getCreatedAt());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
}



package com.paulok777.services;

import com.paulok777.dto.UserDto;
import com.paulok777.entities.User;
import com.paulok777.exceptions.cashregister.registration.DuplicateUsernameException;
import com.paulok777.repositories.UserRepository;
import com.paulok777.utils.ExceptionKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveNewUser(UserDto userDTO) {
        User user = new User(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("(username: {}) {}.", SecurityContextHolder.getContext().getAuthentication().getName(),
                    ExceptionKeys.DUPLICATE_USERNAME);
            throw new DuplicateUsernameException(ExceptionKeys.DUPLICATE_USERNAME);
        }
    }

    public User getCurrentUser() {
        return userRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow();
    }
}
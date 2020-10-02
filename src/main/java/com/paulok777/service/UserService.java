package com.paulok777.service;

import com.paulok777.dto.UserDTO;
import com.paulok777.entity.Role;
import com.paulok777.entity.User;
import com.paulok777.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public void saveNewUser(UserDTO userDTO) {
        User user = new User(userDTO);
        userRepository.save(user);
    }

    public User getCurrentUser() {
        return userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public Role getCurrentUserRole() {
        Set<Role> roles = getCurrentUser().getRoles();
        for (Role role: roles) {
            return role;
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}

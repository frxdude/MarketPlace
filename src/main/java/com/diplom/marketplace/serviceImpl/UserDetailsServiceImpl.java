package com.diplom.marketplace.serviceImpl;

import com.diplom.marketplace.entity.User;
import com.diplom.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findById(uid);
        final User user;

        if (!optionalUser.isPresent()) throw new UsernameNotFoundException("User '" + uid + "' not found");
        else user = optionalUser.get();

        if (!user.isActive()) throw new UsernameNotFoundException(uid + " is deactivated user");
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getId())
                .password(user.getPassword())
                .authorities(user.getRoles())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    public UserDetails loadUserByRoleTemp(String subject) {
        return org.springframework.security.core.userdetails.User
                .withUsername(subject)
                .password("")
                .authorities("ROLE_TEMP")
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

}

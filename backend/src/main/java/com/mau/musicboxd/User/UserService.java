package com.mau.musicboxd.User;

import java.time.LocalDateTime;
import java.util.List;

import com.mau.musicboxd.User.dto.RegisterUserDto;
import com.mau.musicboxd.exception.EmailAlreadyExistsException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User registerUser(RegisterUserDto dto){
        if(userRepository.existsByEmail(dto.getEmail())){//check if the email has been used
            throw new EmailAlreadyExistsException("Email taken");
        }
        User user = User.builder()
                        .email(dto.getEmail())
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .build();
        userRepository.save(user);
        return user;
    }

    public void deleteUser(Long id){
        boolean exists = userRepository.existsById(id);
        if(!exists){
            throw new IllegalStateException("id not found");
        }
        userRepository.deleteById(id);
    }
    /*@Transactional
    public void updateName(Long userid, String name){
        User updateUser = userRepository.findById(userid).orElseThrow(() -> new IllegalStateException("User id " + userid + " not found"));
        if(name != null && name.length() != 0 && !Objects.equals(updateUser.getFullName(), name)){
            updateUser.setName(name);
        }
    }*/

    public User findByEmail(String email){
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email" + email));
        return user;
    }

    public User findUserById(Long userId){
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id " + userId));
        return user;
    }
}

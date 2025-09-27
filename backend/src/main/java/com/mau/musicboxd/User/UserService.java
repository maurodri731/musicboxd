package com.mau.musicboxd.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    public void addNewUser(User user){
        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());
        if(userOptional.isPresent()){//check if the email has been used
            throw new IllegalStateException("email taken");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));//encode the password before actually saving it in the database
        userRepository.save(user);
    }

    public void deleteUser(Long id){
        boolean exists = userRepository.existsById(id);
        if(!exists){
            throw new IllegalStateException("id not found");
        }
        userRepository.deleteById(id);
    }
    @Transactional
    public void updateName(Long userid, String name){
        User updateUser = userRepository.findById(userid).orElseThrow(() -> new IllegalStateException("User id " + userid + " not found"));
        if(name != null && name.length() != 0 && !Objects.equals(updateUser.getName(), name)){
            updateUser.setName(name);
        }
    }

    public User findUserById(Long userId){
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id " + userId));
        return user;
    }
}

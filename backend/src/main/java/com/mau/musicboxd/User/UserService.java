package com.mau.musicboxd.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public void addNewUser(User user){
        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());
        if(userOptional.isPresent()){
            throw new IllegalStateException("email taken");
        }
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
}

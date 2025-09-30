package com.mau.musicboxd.User;

import java.util.List;
import com.mau.musicboxd.User.dto.UserDto;

import jakarta.validation.Valid;

import com.mau.musicboxd.User.dto.RegisterUserDto;

import org.springframework.boot.actuate.web.exchanges.HttpExchange.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping
public class UserController {
    private final UserService userService;

    
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @PostMapping(path = "/register")
    public ResponseEntity<UserDto> registerNewUser(@Valid @RequestBody RegisterUserDto userDto){//RequestBody fits the request body into the user object
        User user = userService.registerUser(userDto);
        return ResponseEntity.ok(UserDto.fromEntity(user));
    }

    @DeleteMapping(path = "{id}")
    public void deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
    }
    /*@PutMapping(path = "{id}")
    public void updateName(@PathVariable("id") Long id, @RequestParam(required = false) String name) {
        userService.updateName(id, name);
    }*/

}

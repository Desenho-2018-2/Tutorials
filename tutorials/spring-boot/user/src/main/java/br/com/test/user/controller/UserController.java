package br.com.test.user.controller;

import br.com.test.user.dto.UserDto;
import br.com.test.user.model.UserEntity;
import br.com.test.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/user/create")
    public void createUser(@RequestBody UserDto user){

        service.create(user);
    }

    @GetMapping("/user/all")
    public Iterable<UserEntity> getAll(){

        return service.getAll();
    }

}

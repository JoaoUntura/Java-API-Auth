package dev.joaountura.auth.user.controllers;

import dev.joaountura.auth.user.models.UserCreateDTO;
import dev.joaountura.auth.user.models.UserResponseDTO;
import dev.joaountura.auth.user.services.UserServices;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServices userServices;

    @PostMapping
    public ResponseEntity<List<UserResponseDTO>> postUser(@RequestBody UserCreateDTO userCreateDTO){

        List<UserResponseDTO> userResponseDTO = userServices.createUser(userCreateDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);

    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll(@AuthenticationPrincipal String user){

        System.out.println(user);

        return ResponseEntity.status(HttpStatus.OK).body(userServices.findAllUsers());

    }

    @PutMapping
    public ResponseEntity<String> putUser(){

        return ResponseEntity.status(HttpStatus.OK).body("Admin");

    }


}

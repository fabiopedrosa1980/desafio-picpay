package br.com.pedrosa.desafio.picpay.users;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody UserRequest userRequest){
        return this.userService.create(userRequest);
    }

    @GetMapping
    public List<UserResponse> listAll(){
        return this.userService.listAll();
    }
}

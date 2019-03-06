package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import ch.uzh.ifi.seal.soprafs19.controller.UserAlreadyExists;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    Iterable<User> all() {
        return service.getUsers();
    }

    @PostMapping("/logcheck")
    User checkPWandName(@RequestBody User newUser) {
        String username = newUser.getUsername();
        String password = newUser.getPassword();
        if ((this.service.findUserByUsername(username)==null)||
           (!this.service.findUserByUsername(username).getPassword().equals(password))) throw new UserAlreadyExists();
        else return this.service.findUserByUsername(username);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)User deleteUser(@PathVariable long id) {this.service.deleteUser(id);
        return null;
    }

    @GetMapping("/users/{id}")
    User getUser( @PathVariable long id) {
        return service.getUser(id);
    }

    @PostMapping("/users")
    User createUser(@RequestBody User newUser) {
        String username = newUser.getUsername();
        if (this.service.findUserByUsername(username)!=null) throw new UserAlreadyExists();
        else return this.service.createUser(newUser);
    }
}

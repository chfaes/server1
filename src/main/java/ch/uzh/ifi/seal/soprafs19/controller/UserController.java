package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import ch.uzh.ifi.seal.soprafs19.controller.UserAlreadyExists;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Hashtable;
import java.util.UUID;

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
                (!this.service.findUserByUsername(username).getPassword().equals(password)))
            throw new UserAlreadyExists();
        else
            //this.service.findUserByUsername(username).setToken(UUID.randomUUID().toString());
            //this.service.findUserByUsername(username).setStatus(UserStatus.ONLINE);
            return this.service.findUserByUsername(username);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)User deleteUser(@PathVariable long id) {this.service.deleteUser(id);
        return null;
    }

    @GetMapping("/users/{id}")
    User getUser( @PathVariable long id) {
        User user = this.service.getUser(id);
        if (user == null) throw new UserNotFound();
        else return user;
    }

    @PutMapping("/users/{id}")
    @CrossOrigin
    User putid(@PathVariable long id, @RequestBody User updatedUser) {
        User user = this.service.getUser(id);
        if (user == null) throw new UserNotFound();
        else return this.service.updateUser(id, updatedUser);
    }

    @PostMapping("/users")
    @NotBlank //Soll angeblich verhindern, dass man mit leerem Userinput hier anklopfen kann.
    User createUser(@RequestBody User newUser) {
        String username = newUser.getUsername();
        if (this.service.findUserByUsername(username)!=null) throw new UserAlreadyExists();
        else
            return this.service.createUser(newUser);
    }
}

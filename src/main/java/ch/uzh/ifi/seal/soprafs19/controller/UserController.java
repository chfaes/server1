package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import ch.uzh.ifi.seal.soprafs19.controller.UserAlreadyExists;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.DenyAll;
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
    public User checkPWandName(@RequestBody User newUser) {
        String username = newUser.getUsername();
        String password = newUser.getPassword();
        if ((this.service.findUserByUsername(username)==null)||
                (!this.service.findUserByUsername(username).getPassword().equals(password)))
            throw new UserAlreadyExists();
        else
            //this.service.findUserByUsername(username).setToken(UUID.randomUUID().toString());
            this.service.findUserByUsername(username).setStatus(UserStatus.ONLINE);
            this.service.saveLogin(this.service.findUserByUsername(username));
            return this.service.findUserByUsername(username);
    }

    /*
    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)User deleteUser(@PathVariable long id) {this.service.deleteUser(id);
        return null;
    }*/

    @GetMapping("/users/{id}")
    public User getUser( @PathVariable long id) {
        User user = this.service.getUser(id);
        if (user == null) throw new UserNotFound();
        else return user;
    }

    @GetMapping("/logout/{id}")
    @CrossOrigin
    public User logOutUser(@PathVariable long id) {
        User user = this.service.getUser(id);
        if (user == null) throw new UserNotFound();
        else
        user.setStatus(UserStatus.OFFLINE);
        this.service.saveLogout(user);
        return user;
    }

    @PutMapping("/users/{id}")
    @CrossOrigin
    @ResponseStatus(value=HttpStatus.NO_CONTENT)
    public User putid(@PathVariable long id, @RequestBody User updatedUser) {
        User user = this.service.getUser(id);
        if (user == null) throw new UserNotFound();
        else return this.service.updateUser(id, updatedUser);
    }

    @PostMapping("/users")
    @NotBlank //Soll angeblich verhindern, dass man mit leerem Userinput hier anklopfen kann.
    @ResponseStatus(value=HttpStatus.CREATED)
    public String createUser(@RequestBody User newUser) {
        String username = newUser.getUsername();
        if (this.service.findUserByUsername(username) != null) throw new UserAlreadyExists();
        else{
            User UserCreated = this.service.createUser(newUser);
            String String1 = "/users/"+ UserCreated.getId().toString();
            return String1;
        }
    }
}

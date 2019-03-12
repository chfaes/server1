package ch.uzh.ifi.seal.soprafs19.controller;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason = "Username exists already.")
public class UserAlreadyExists extends RuntimeException {

}

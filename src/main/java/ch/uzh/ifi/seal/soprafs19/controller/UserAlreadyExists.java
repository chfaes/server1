package ch.uzh.ifi.seal.soprafs19.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

class UserAlreadyExists extends Exception {
    public UserAlreadyExists(){
        super();
    }
}

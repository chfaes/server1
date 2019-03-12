package controller;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.controller.UserController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.bind.annotation.RequestParam;
import ch.uzh.ifi.seal.soprafs19.controller.UserAlreadyExists;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserController
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class UserControllerTest {


    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserController userController;

    @Test
    public void createUser() {
        Assert.assertNull(userRepository.findByUsername("testUser1UN"));

        User testUser1 = new User();
        testUser1.setUsername("testUser1UN");
        testUser1.setPassword("testUser1PW");
        testUser1.setBirthday("testUser1BD");
        testUser1.setCurrdate("testUser1CD");

        String returnString1 = userController.createUser(testUser1); //Routine under Test

        Assert.assertEquals("/users/"+userRepository.findByUsername("testUser1UN").getId().toString(),returnString1);
    }
    @Test(expected = UserAlreadyExists.class)
    public void createExistingUNUser() {
        Assert.assertNull(userRepository.findByUsername("testUser1bUN"));
        Assert.assertNull(userRepository.findByUsername("testUser1cUN"));

        User testUser1b = new User();
        testUser1b.setUsername("testUser1bUN");
        testUser1b.setPassword("testUser1bPW");
        testUser1b.setBirthday("testUser1bBD");
        testUser1b.setCurrdate("testUser1bCD");
        userController.createUser(testUser1b);

        User testUser1c = new User();
        testUser1c.setUsername("testUser1bUN"); //note the same Name as User1b above
        testUser1c.setPassword("testUser1cPW");
        testUser1c.setBirthday("testUser1cBD");
        testUser1c.setCurrdate("testUser1cCD");

        String returnString1b = userController.createUser(testUser1c); //Routine under Test

        Assert.assertNull(userRepository.findByUsername("testUser1cUN"));
        Assert.assertNotEquals("/users/"+userRepository.findByUsername("testUser1cUN").getId().toString(),returnString1b);


    }
    @Test
    public void checkCorrectPWandName() {
        Assert.assertNull(userRepository.findByUsername("testUser2UN"));
        User testUser2 = new User();
        testUser2.setUsername("testUser2UN");
        testUser2.setPassword("testUser2PW");
        testUser2.setBirthday("testUser2BD");
        testUser2.setCurrdate("testUser2CD");
        userController.createUser(testUser2);
        User testUser3 = new User();
        testUser3.setUsername("testUser2UN");
        testUser3.setPassword("testUser2PW");

        User returnUser = userController.checkPWandName(testUser3); //Routine under Test

        Assert.assertEquals(returnUser.getUsername(), testUser2.getUsername());
        Assert.assertEquals(returnUser.getUsername(), testUser3.getUsername());
        Assert.assertEquals(returnUser.getPassword(), testUser2.getPassword());
        Assert.assertEquals(returnUser.getPassword(), testUser3.getPassword());

    }/*
    @Test
    public void checkcorrectPWwrongName() {
        userController.checkPWandName();
    }
    @Test
    public void checkwrongPWcorrectName() {
        userController.checkPWandName();
    }
    @Test
    public void checkemptyPWandName() {
        userController.checkPWandName();
    }
    @Test
    public void getUser() {
        userController.getUser();
    }
    @Test
    public void getNonExistentUser() {
        userController.getUser();
    }
    @Test
    public void logoutUser() {
        userController.logOutUser();
    }
    @Test
    public void logoutNonExistentUser() {
        userController.logOutUser();
    }
    @Test
    public void updateUser() {
        userController.putid();
    }
    @Test
    public void updateNonExistentUser() {
        userController.putid();
    }
    @Test
    public void updateWithEmptyInputUser() {
        userController.putid();
    }*/
}


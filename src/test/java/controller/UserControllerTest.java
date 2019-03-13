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
import ch.uzh.ifi.seal.soprafs19.controller.UserNotFound;

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

        Assert.assertNull(returnString1b);
        Assert.assertEquals(userRepository.findByUsername("testUser1bUN").getPassword(),"testUser1bPW");
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

    }
    @Test(expected = UserAlreadyExists.class)
    public void checkCorrectPWwrongName() {
        Assert.assertNull(userRepository.findByUsername("testUser2bUN"));

        User testUser2b = new User();
        testUser2b.setUsername("testUser2bUN");
        testUser2b.setPassword("testUser2bPW");
        testUser2b.setBirthday("testUser2bBD");
        testUser2b.setCurrdate("testUser2bCD");
        userController.createUser(testUser2b);
        User testUser3b = new User();
        testUser3b.setUsername("NotUsername");
        testUser3b.setPassword("testUser2PW");

        User returnUser = userController.checkPWandName(testUser3b); //Routine under Test

        Assert.assertNotEquals(returnUser.getUsername(), testUser2b.getUsername());
        Assert.assertNotEquals(returnUser.getUsername(), testUser3b.getUsername());
    }
    @Test(expected = UserAlreadyExists.class)
    public void checkWrongPWcorrectName() {
        Assert.assertNull(userRepository.findByUsername("testUser2cUN"));

        User testUser2c = new User();
        testUser2c.setUsername("testUser2cUN");
        testUser2c.setPassword("testUser2cPW");
        testUser2c.setBirthday("testUser2cBD");
        testUser2c.setCurrdate("testUser2cCD");
        userController.createUser(testUser2c);
        User testUser3c = new User();
        testUser3c.setUsername("testUser2cUN");
        testUser3c.setPassword("NotPassword");

        User returnUser = userController.checkPWandName(testUser3c); //Routine under Test

        Assert.assertNotEquals(returnUser.getUsername(), testUser2c.getPassword());
        Assert.assertNotEquals(returnUser.getUsername(), testUser3c.getPassword());
    }
    @Test(expected = UserAlreadyExists.class)
    public void checkEmptyPWandName() {
        Assert.assertNull(userRepository.findByUsername("testUser2dUN"));

        User testUser2d = new User();
        testUser2d.setUsername("testUser2dUN");
        testUser2d.setPassword("testUser2dPW");
        testUser2d.setBirthday("testUser2dBD");
        testUser2d.setCurrdate("testUser2dCD");
        userController.createUser(testUser2d);
        User testUser3d = new User();

        User returnUser = userController.checkPWandName(testUser3d); //Routine under Test

        Assert.assertEquals(returnUser, null);
    }
    @Test
    public void getUser() {
        Assert.assertNull(userRepository.findByUsername("testUser4UN"));

        User testUser4 = new User();
        testUser4.setUsername("testUser4UN");
        testUser4.setPassword("testUser4PW");
        testUser4.setBirthday("testUser4BD");
        testUser4.setCurrdate("testUser4CD");
        userController.createUser(testUser4);
        long id = userRepository.findByUsername("testUser4UN").getId();

        User returnUser = userController.getUser(id); //Routine under Test: getUser()

        String id_str = ""+id; //conversion to check returned User id with this
        Assert.assertEquals(returnUser.getUsername(), "testUser4UN");
        Assert.assertEquals(returnUser.getId().toString(), id_str);
    }
    @Test(expected = UserNotFound.class)
    public void getNonExistentUser() {
        Assert.assertNull(userRepository.findByUsername("testUser5UN"));

        User testUser5 = new User();
        testUser5.setUsername("testUser5UN");
        testUser5.setPassword("testUser5PW");
        testUser5.setBirthday("testUser5BD");
        testUser5.setCurrdate("testUser5CD");
        userController.createUser(testUser5);
        long id = userRepository.findByUsername("testUser5UN").getId()+1000;

        User returnUser = userController.getUser(id); //Routine under Test

        Assert.assertNull(userRepository.findById(id));
        Assert.assertNull(returnUser);
    }
    @Test
    public void logoutUser() {
        Assert.assertNull(userRepository.findByUsername("testUser6UN"));

        User testUser6 = new User();
        testUser6.setUsername("testUser6UN");
        testUser6.setPassword("testUser6PW");
        testUser6.setBirthday("testUser6BD");
        testUser6.setCurrdate("testUser6CD");
        userController.createUser(testUser6);
        long id = userRepository.findByUsername("testUser6UN").getId();

        User returnUser = userController.logOutUser(id); //Routine under Test

        String id_str = ""+id;
        Assert.assertEquals(returnUser.getId().toString(), id_str);
        Assert.assertEquals(returnUser.getUsername(), "testUser6UN");
        Assert.assertEquals(userRepository.findByUsername("testUser6UN").getStatus(), UserStatus.OFFLINE);
    }
    @Test(expected = UserNotFound.class)
    public void logoutNonExistentUser() {
        Assert.assertNull(userRepository.findById(0));

        User returnUser = userController.logOutUser(0); //Routine under Test

        Assert.assertNull(userRepository.findById(0));
        Assert.assertNull(returnUser);
    }
    @Test
    public void updateUser() {
        Assert.assertNull(userRepository.findByUsername("testUser7UN"));
        Assert.assertNull(userRepository.findByUsername("testUser8UN"));

        User testUser7 = new User();
        testUser7.setUsername("testUser7UN");
        testUser7.setPassword("testUser7PW");
        testUser7.setBirthday("testUser7BD");
        testUser7.setCurrdate("testUser7CD");
        userController.createUser(testUser7);
        long id = userRepository.findByUsername("testUser7UN").getId();
        User testUser8 = new User();
        testUser8.setUsername("testUser8UN");
        testUser8.setPassword("testUser8PW");
        testUser8.setBirthday("testUser8BD");
        testUser8.setCurrdate("testUser8CD");

        User returnUser = userController.putid(id, testUser8);

        Assert.assertEquals(returnUser.getUsername(), "testUser8UN");
        Assert.assertEquals(returnUser.getPassword(), "testUser7PW");
        Assert.assertEquals(returnUser.getBirthday(), "testUser8BD");
        Assert.assertEquals(returnUser.getCurrdate(), "testUser7CD");

    }
    @Test(expected = UserNotFound.class)
    public void updateNonExistentUser() {
        Assert.assertNull(userRepository.findById(0));

        User testUser9 = new User();
        testUser9.setUsername("testUser9UN");
        testUser9.setPassword("testUser9PW");
        testUser9.setBirthday("testUser9BD");
        testUser9.setCurrdate("testUser9CD");

        userController.putid(0, testUser9);

        Assert.assertNull(userRepository.findById(0));
    }
}


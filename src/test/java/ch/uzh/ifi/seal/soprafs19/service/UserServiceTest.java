package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class UserServiceTest {


    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void createUser() {
        Assert.assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setBirthday("testBirthday");
        testUser.setCurrdate("testcurrDate");

        User createdUser = userService.createUser(testUser);

        Assert.assertNotNull(createdUser.getToken());
        Assert.assertEquals(createdUser.getStatus(),UserStatus.OFFLINE);
        Assert.assertEquals(createdUser, userRepository.findByToken(createdUser.getToken()));
    }

    @Test
    public void updateUser() {
        Assert.assertNull(userRepository.findByUsername("testUsername2"));

        User testUser2 = new User();
        testUser2.setUsername("testUsername2");
        testUser2.setPassword("testPassword2");
        testUser2.setBirthday("testBirthday2");
        testUser2.setCurrdate("testcurrDate2");

        User createdUser2 = userService.createUser(testUser2);

        User updatedUser = new User();
        updatedUser.setUsername("NewName");
        updatedUser.setBirthday("NewBirthday");

        User testUser2updated = userService.updateUser(createdUser2.getId(), updatedUser);

        Assert.assertEquals(testUser2updated.getUsername(), updatedUser.getUsername());
        Assert.assertEquals(testUser2updated.getBirthday(), updatedUser.getBirthday());
        Assert.assertEquals(testUser2updated.getPassword(), "testPassword2");
    }

    @Test
    public void findUserByUsername() {
        Assert.assertNull(userRepository.findByUsername("testUsername3"));

        User testUser3 = new User();
        testUser3.setUsername("testUsername3");
        testUser3.setPassword("testPassword3");
        testUser3.setBirthday("testBirthday3");
        testUser3.setCurrdate("testcurrDate3");

        User createdUser3 = userService.createUser(testUser3);

        User testUser3returned = userService.findUserByUsername(createdUser3.getUsername());

        Assert.assertEquals(testUser3returned.getUsername(), createdUser3.getUsername());
        Assert.assertEquals(testUser3returned.getId(), createdUser3.getId());
    }

    @Test
    public void SaveLogin() {
        Assert.assertNull(userRepository.findByUsername("testUsername4"));

        User testUser4 = new User();
        testUser4.setUsername("testUsername4");
        testUser4.setPassword("testPassword4");
        testUser4.setBirthday("testBirthday4");
        testUser4.setCurrdate("testcurrDate4");
        User createdUser4 = userService.createUser(testUser4);
        createdUser4.setStatus(UserStatus.ONLINE);

        userService.saveLogin(createdUser4);

        Assert.assertEquals(userRepository.findByUsername("testUsername4").getStatus(),UserStatus.ONLINE);
    }

    @Test
    public void SaveLogout() {
        Assert.assertNull(userRepository.findByUsername("testUsername5"));

        User testUser5 = new User();
        testUser5.setUsername("testUsername5");
        testUser5.setPassword("testPassword5");
        testUser5.setBirthday("testBirthday5");
        testUser5.setCurrdate("testcurrDate5");
        User createdUser5 = userService.createUser(testUser5);
        createdUser5.setStatus(UserStatus.ONLINE);
        userService.saveLogin(createdUser5);
        createdUser5.setStatus(UserStatus.OFFLINE);

        userService.saveLogout(createdUser5);

        Assert.assertEquals(userRepository.findByUsername("testUsername5").getStatus(),UserStatus.OFFLINE);
    }

    @Test
    public void getUser() {
        Assert.assertNull(userRepository.findByUsername("testUsername6"));

        User testUser6 = new User();
        testUser6.setUsername("testUsername6");
        testUser6.setPassword("testPassword6");
        testUser6.setBirthday("testBirthday6");
        testUser6.setCurrdate("testcurrDate6");

        User createdUser6 = userService.createUser(testUser6);

        User returnedUser6 = userService.getUser(createdUser6.getId());

        Assert.assertEquals(returnedUser6.getUsername(), createdUser6.getUsername());
        Assert.assertEquals(returnedUser6.getId(), createdUser6.getId());
    }
}

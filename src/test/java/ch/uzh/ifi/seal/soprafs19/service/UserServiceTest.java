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
}

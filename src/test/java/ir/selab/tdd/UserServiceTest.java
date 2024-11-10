package ir.selab.tdd;

import ir.selab.tdd.domain.User;
import ir.selab.tdd.repository.UserRepository;
import ir.selab.tdd.service.UserService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UserServiceTest {
    private UserService userService;

    @Before
    public void setUp() {
        UserRepository userRepository = new UserRepository(List.of());
        userService = new UserService(userRepository);
        userService.registerUser("admin", "1234", "admin@gmail.com");
        userService.registerUser("ali", "qwert", "ali@gmail.com");
    }

    @Test
    public void createNewValidUser__ShouldSuccess() {
        String username = "reza";
        String password = "123abc";
        String email = "reza@example.com";
        boolean result = userService.registerUser(username, password, email);
        assertTrue(result);
    }

    @Test
    public void createNewDuplicateUser__ShouldFail() {
        String username = "ali";
        String password = "123abc";
        String email = "ali_new@example.com";
        boolean result = userService.registerUser(username, password, email);
        assertFalse(result);
    }

    @Test
    public void createUserWithExistingEmail__ShouldFail() {
        String username = "reza";
        String password = "123abc";
        String email = "ali@example.com"; // Email already used
        boolean result = userService.registerUser(username, password, email);
        assertFalse(result);
    }

    @Test
    public void loginWithValidUsernameAndPassword__ShouldSuccess() {
        boolean login = userService.loginWithUsername("admin", "1234");
        assertTrue(login);
    }

    @Test
    public void loginWithValidUsernameAndInvalidPassword__ShouldFail() {
        boolean login = userService.loginWithUsername("admin", "abcd");
        assertFalse(login);
    }

    @Test
    public void loginWithInvalidUsernameAndInvalidPassword__ShouldFail() {
        boolean login = userService.loginWithUsername("ahmad", "abcd");
        assertFalse(login);
    }

    @Test
    public void loginWithEmail__ValidCredentials__ShouldSuccess() {
        boolean login = userService.loginWithEmail("admin@example.com", "1234");
        assertTrue(login);
    }

    @Test
    public void loginWithEmail__InvalidPassword__ShouldFail() {
        boolean login = userService.loginWithEmail("admin@example.com", "wrongpassword");
        assertFalse(login);
    }

    @Test
    public void loginWithEmail__NonExistingEmail__ShouldFail() {
        boolean login = userService.loginWithEmail("nonexistent@example.com", "password");
        assertFalse(login);
    }

    @Test
    public void removeExistingUser__ShouldReturnTrue() {
        boolean result = userService.removeUser("ali");
        assertTrue(result);
        assertFalse(userService.loginWithUsername("ali", "qwert"));
    }

    @Test
    public void removeNonExistingUser__ShouldReturnFalse() {
        boolean result = userService.removeUser("nonexistent");
        assertFalse(result);
    }

    @Test
    public void getAllUsers__ShouldReturnListOfUsers() {
        List<User> users = userService.getAllUsers();
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    public void changeUserEmail__ShouldReturnTrue() {
        boolean result = userService.changeUserEmail("ali", "ali_new@example.com");
        assertTrue(result);
        boolean loginWithOldEmail = userService.loginWithEmail("ali@example.com", "qwert");
        assertFalse(loginWithOldEmail);
        boolean loginWithNewEmail = userService.loginWithEmail("ali_new@example.com", "qwert");
        assertTrue(loginWithNewEmail);
    }

    @Test
    public void changeUserEmailToExistingEmail__ShouldReturnFalse() {
        boolean result = userService.changeUserEmail("ali", "admin@example.com");
        assertFalse(result);
    }

    @Test
    public void changeUserEmailForNonExistingUser__ShouldReturnFalse() {
        boolean result = userService.changeUserEmail("nonexistent", "new@example.com");
        assertFalse(result);
    }
}

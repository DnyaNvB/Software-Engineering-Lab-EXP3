package ir.selab.tdd;

import ir.selab.tdd.domain.User;
import ir.selab.tdd.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class UserRepositoryTest {
    private UserRepository repository;

    @Before
    public void setUp() {
        List<User> userList = Arrays.asList(
                new User("admin", "1234", "admin@gmail.com"),
                new User("ali", "qwert", "ali@gmail.com"),
                new User("mohammad", "123asd", "mohammad@gmail.com"));
        repository = new UserRepository(userList);
    }

    @Test
    public void getUserByUsername__ExistingUser_ShouldReturnUser() {
        User ali = repository.getUserByUsername("ali");
        assertNotNull(ali);
        assertEquals("ali", ali.getUsername());
        assertEquals("qwert", ali.getPassword());
    }

    @Test
    public void getUserByUsername__NonExistingUser_ShouldReturnNull() {
        User user = repository.getUserByUsername("reza");
        assertNull(user);
    }

    @Test
    public void getUserByEmail__ExistingUser_ShouldReturnUser() {
        User user = repository.getUserByEmail("mohammad@gmail.com");
        assertNotNull(user);
        assertEquals("mohammad", user.getUsername());
    }

    @Test
    public void getUserByEmail__NonExistingEmail__ShouldReturnNull() {
        User user = repository.getUserByEmail("nonexistent@gmail.com");
        assertNull(user);
    }

    @Test
    public void createRepositoryWithDuplicateUsers__ShouldThrowException() {
        User user1 = new User("ali", "1234", "ali1@gmail.com");
        User user2 = new User("ali", "4567", "ali2@gmail.com");
        assertThrows(IllegalArgumentException.class, () -> {
            new UserRepository(List.of(user1, user2));
        });
    }

    @Test
    public void createRepositoryWithDuplicateEmails__ShouldThrowException() {
        User user1 = new User("dani", "1234", "dni@gmail.com");
        User user2 = new User("doni", "4567", "dni@gmail.com");
        assertThrows(IllegalArgumentException.class, () -> {
            new UserRepository(List.of(user1, user2));
        });
    }

    @Test
    public void addNewUser__ShouldIncreaseUserCount() {
        int oldUserCount = repository.getUserCount();

        // Given
        String username = "reza";
        String password = "123abc";
        String email = "reza@sharif.edu";
        User newUser = new User(username, password, email);

        // When
        boolean result = repository.addUser(newUser);

        // Then
        assertTrue(result);
        assertEquals(oldUserCount + 1, repository.getUserCount());
        User retrievedUser = repository.getUserByUsername(username);
        assertNotNull(retrievedUser);
        assertEquals(email, retrievedUser.getEmail());    }

    @Test
    public void addExistingUser__ShouldReturnFalse() {
        User existingUser = new User("ali", "somepassword", "ali_new@example.com");
        boolean result = repository.addUser(existingUser);
        assertFalse(result);
    }

    @Test
    public void addUserWithDuplicateEmail__ShouldReturnFalse() {
        User newUser = new User("reza", "123abc", "ali@gmail.com"); // Email already exists
        boolean result = repository.addUser(newUser);
        assertFalse(result);
    }

    @Test
    public void removeExistingUser__ShouldReturnTrue() {
        boolean result = repository.removeUser("ali");
        assertTrue(result);
        assertNull(repository.getUserByUsername("ali"));
        assertNull(repository.getUserByEmail("ali@gmail.com"));
    }

    @Test
    public void removeNonExistingUser__ShouldReturnFalse() {
        boolean result = repository.removeUser("nonexistent");
        assertFalse(result);
    }

    @Test
    public void changeUserEmail__ShouldReturnTrue() {
        boolean result = repository.changeUserEmail("ali", "ali_new@example.com");
        assertTrue(result);
        User user = repository.getUserByUsername("ali");
        assertEquals("ali_new@example.com", user.getEmail());
        assertNull(repository.getUserByEmail("ali@gmail.com"));
        assertEquals(user, repository.getUserByEmail("ali_new@example.com"));
    }

    @Test
    public void changeUserEmailToExistingEmail__ShouldReturnFalse() {
        boolean result = repository.changeUserEmail("ali", "mohammad@gmail.com"); // Email already used
        assertFalse(result);
    }

    @Test
    public void changeUserEmailForNonExistingUser__ShouldReturnFalse() {
        boolean result = repository.changeUserEmail("nonexistent", "new@example.com");
        assertFalse(result);
    }

    @Test
    public void getAllUsers__ShouldReturnAllUsers() {
        List<User> users = repository.getAllUsers();
        assertNotNull(users);
        assertEquals(3, users.size());
    }
}

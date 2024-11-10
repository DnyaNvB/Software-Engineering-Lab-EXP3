package ir.selab.tdd.repository;

import ir.selab.tdd.domain.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {
    private final Map<String, User> usersByUserName;
    private final Map<String, User> usersByEmail;

    public UserRepository(List<User> users) {
        this.usersByUserName = new HashMap<>();
        this.usersByEmail = new HashMap<>();

        for (User user : users) {
            if (usersByUserName.containsKey(user.getUsername())) {
                throw new IllegalArgumentException("Two users cannot have the same username");
            }
            usersByUserName.put(user.getUsername(), user);

            String email = user.getEmail();
            if (email != null) {
                if (usersByEmail.containsKey(email)) {
                    throw new IllegalArgumentException("Two users cannot have the same email");
                }
                usersByEmail.put(email, user);
            }
        }        
    }

    public User getUserByUsername(String username) {
        return usersByUserName.get(username);
    }

    public User getUserByEmail(String email) {
        return usersByEmail.get(email);
    }

    public boolean addUser(User user) {
        if (usersByUserName.containsKey(user.getUsername()) ||
                (user.getEmail() != null && usersByEmail.containsKey(user.getEmail()))) {
            return false;
        }
        usersByUserName.put(user.getUsername(), user);
        if (user.getEmail() != null) {
            usersByEmail.put(user.getEmail(), user);
        }
        return true;
    }

    public boolean removeUser(String username) {
        User user = usersByUserName.remove(username);
        if (user == null) {
            return false;
        }
        if (user.getEmail() != null) {
            usersByEmail.remove(user.getEmail());
        }
        return true;
    }

    public int getUserCount() {
        return usersByUserName.size();
    }

    public boolean changeUserEmail(String username, String newEmail) {
        User user = usersByUserName.get(username);
        if (user == null || newEmail == null || newEmail.isEmpty() || usersByEmail.containsKey(newEmail)) {
            return false;
        }
        String oldEmail = user.getEmail();
        if (oldEmail != null) {
            usersByEmail.remove(oldEmail);
        }
        user.setEmail(newEmail);
        usersByEmail.put(newEmail, user);
        return true;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(usersByUserName.values());
    }

}

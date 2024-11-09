package ir.selab.tdd.repository;

import ir.selab.tdd.domain.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserRepository {
    private final Map<String, User> usersByUserName;
    private final Map<String, User> usersByEmail;

    public UserRepository(List<User> users) {
        this.usersByUserName = users.stream().collect(Collectors.toMap(User::getUsername, u -> u, (u1, u2) -> {
            throw new IllegalArgumentException("Two users can not have the same username");
        }));

        this.usersByEmail = new HashMap<>();
        for (User user : users) {
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
        if (usersByUserName.containsKey(user.getUsername())) {
            return false;
        }
        if (user.getEmail() != null && usersByEmail.containsKey(user.getEmail())) {
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
}

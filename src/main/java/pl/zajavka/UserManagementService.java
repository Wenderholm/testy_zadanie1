package pl.zajavka;

import java.util.*;
import java.util.stream.Collectors;

public class UserManagementService {

    private final Map<String, User> userMap = new HashMap<>();


    public void create(User user) {
        if (userMap.containsKey(user.getEmail())) {
            throw new RuntimeException(
                    String.format("User with email: [%s] is already created", user.getEmail()));
        }
        userMap.put(user.getEmail(), user);
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userMap.get(email));
    }

    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    public List<User> findByName(String name) {
        return userMap.values().stream()
                .filter(user -> name.equals(user.getName()))
                .collect(Collectors.toList());
    }

    public void update(String email, User user) {
        if (!userMap.containsKey(email)) {
            throw new RuntimeException(
                    String.format("User with email: [%s] doesn't exist", email));
        }
        if (!email.equals(user.getEmail())) {
            userMap.remove(email);
        }
        userMap.put(user.getEmail(), user);

    }


    public void delete(String email) {
        if (!userMap.containsKey(email)) {
            throw new RuntimeException(
                    String.format("User with email: [%s] doesn't exist", email));

        }
        userMap.remove(email);
    }
}

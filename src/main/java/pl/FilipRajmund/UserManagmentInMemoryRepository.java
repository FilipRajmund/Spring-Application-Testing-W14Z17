package pl.FilipRajmund;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserManagmentInMemoryRepository implements UserManagementRepository {

    private final Map<String, User> userMap = new HashMap<>();

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userMap.get(email));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public void create(User user) {
        userMap.put(user.getEmail(), user);

    }

    @Override
    public List<User> findByName(String name) {
        return userMap.values().stream()
                .filter(user -> name.equals(user.getName()))
                .toList();
    }

    @Override
    public void update(String email, User user) {
        userMap.put(email, user);
    }

    @Override
    public void delete(String email) {
        userMap.remove(email);

    }
}

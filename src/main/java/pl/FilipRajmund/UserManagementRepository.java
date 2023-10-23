package pl.FilipRajmund;

import java.util.List;
import java.util.Optional;

public interface UserManagementRepository {
    Optional<User> findByEmail(final String email);

    List<User> findAll();

    void create(final User user);

    List<User> findByName(final String name);

    void update(final String email, final User user);
    void delete(final String email);
}

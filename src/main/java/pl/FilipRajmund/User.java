package pl.FilipRajmund;

import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder
@With
public class User implements Comparable<User> {
    String name;
    String surname;
    String email;

    @Override
    public int compareTo(final User o) {
        return o.email.compareTo(email);
    }
}

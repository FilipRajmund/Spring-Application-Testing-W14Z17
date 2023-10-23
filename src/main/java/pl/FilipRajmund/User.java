package pl.FilipRajmund;

import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder
@With
public class User {
    String name;
    String surname;
    String email;
}

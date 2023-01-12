package pl.zajavka;

import lombok.Builder;
import lombok.Value;
import lombok.With;

@With
@Value
@Builder
public class User implements Comparable<User> {
    String name;
    String surname;
    String email;


    @Override
    public int compareTo(User o) {
        return o.email.compareTo(email);
    }
}

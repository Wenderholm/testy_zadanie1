package pl.zajavka;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class UserManagementServiceTest {

    private UserManagementService userManagementService;

    @BeforeEach
    void init() {
        this.userManagementService = new UserManagementService();
    }

    @Test
    void shouldCreateUserCorrectly() {
        var user = someUser();

        userManagementService.create(user);

        var result = userManagementService.findByEmail(user.getEmail());

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(user, result.get());
    }

    @Test
    void shouldCreateMultipleUsersCorrectly() {
        var user1 = someUser().withEmail("email1@gmail.com");
        var user2 = someUser().withEmail("email2@gmail.com");
        var user3 = someUser().withEmail("email3@gmail.com");

        userManagementService.create(user1);
        userManagementService.create(user2);
        userManagementService.create(user3);

        var result1 = userManagementService.findByEmail(user1.getEmail());
        var result2 = userManagementService.findByEmail(user2.getEmail());
        var result3 = userManagementService.findByEmail(user3.getEmail());
        var all = userManagementService.findAll();


        Assertions.assertEquals(3, all.size());
        Assertions.assertTrue(result1.isPresent());
        Assertions.assertEquals(user1, result1.get());
        Assertions.assertTrue(result2.isPresent());
        Assertions.assertEquals(user2, result2.get());
        Assertions.assertTrue(result3.isPresent());
        Assertions.assertEquals(user3, result3.get());
    }

    @Test
    void shouldFailWhenDuplicatedUserIsCreated() {
        var user1 = someUser();
        var user2 = someUser();

        userManagementService.create(user1);
        Throwable exception = Assertions.assertThrows(RuntimeException.class, () ->
                userManagementService.create(user2));
        Assertions.assertEquals(String.format("User with email: [%s] is already created", user1.getEmail()),
                exception.getMessage());
    }

    @Test
    void shouldFindUsersWithName() {
        var user1 = someUser().withEmail("email1@gmail.com");
        var user2 = someUser().withEmail("email2@gmail.com");
        var user3 = someUser().withName("newName").withEmail("email3@gmail.com");

        userManagementService.create(user1);
        userManagementService.create(user2);
        userManagementService.create(user3);

        var result1 = userManagementService.findByName(user1.getName());
        var result2 = userManagementService.findByName(user2.getName());
        var result3 = userManagementService.findByName(user3.getName());
        var all = userManagementService.findAll();

        Assertions.assertEquals(3, all.size());
        Assertions.assertEquals(
                Stream.of(user1, user2).sorted().collect(Collectors.toList()),
                result2.stream().sorted().collect(Collectors.toList())
        );
        Assertions.assertEquals(List.of(user3), result3);

    }

    @Test
    void shouldModifyUserDataCorrectly() {
        var user1 = someUser().withEmail("email1@gmail.com");
        var user2 = someUser().withEmail("email2@gmail.com");
        var user3 = someUser().withEmail("email3@gmail.com");
        String newEmail = "newEmail@gmail.com";

        userManagementService.create(user1);
        userManagementService.create(user2);
        userManagementService.create(user3);

        var all = userManagementService.findAll();
        Assertions.assertEquals(3, all.size());

        var result1 = userManagementService.findByEmail(user1.getEmail());
        userManagementService.update(user1.getEmail(), user1.withEmail(newEmail));
        var result2 = userManagementService.findByEmail(user1.getEmail());
        var result3 = userManagementService.findByEmail(newEmail);
        var allAgain = userManagementService.findAll();

        Assertions.assertEquals(3, allAgain.size());
        Assertions.assertTrue(result1.isPresent());
        Assertions.assertEquals(user1, result1.get());
        Assertions.assertTrue(result3.isPresent());
        Assertions.assertTrue(result2.isEmpty());
        Assertions.assertEquals(user1.withEmail(newEmail), result3.get());

    }

    @Test
    void shouldThrowWhenModifyingNonExistingUser() {
        var user1 = someUser();
        String newEmail = "email1@gmail.com";

        Throwable exception = Assertions.assertThrows(RuntimeException.class,
                () -> userManagementService.update(user1.getEmail(), user1.withEmail(newEmail)));
        Assertions.assertEquals(String.format("User with email: [%s] doesn't exist", user1.getEmail()),
                exception.getMessage());
    }

    @Test
    void shouldDeleteUserCorrectly() {
        var user1 = someUser().withEmail("email1@gmail.com");
        var user2 = someUser().withEmail("email2@gmail.com");
        var user3 = someUser().withEmail("email3@gmail.com");

        userManagementService.create(user1);
        userManagementService.create(user2);
        userManagementService.create(user3);

        var all = userManagementService.findAll();
        Assertions.assertEquals(3, all.size());

        userManagementService.delete(user1.getEmail());

        var result1 = userManagementService.findByEmail(user1.getEmail());
        var result2 = userManagementService.findByEmail(user2.getEmail());
        var result3 = userManagementService.findByEmail(user3.getEmail());
        var allAgain = userManagementService.findAll();

        Assertions.assertEquals(2, allAgain.size());
        Assertions.assertTrue(result1.isEmpty());
        Assertions.assertTrue(result2.isPresent());
        Assertions.assertEquals(user2, result2.get());
        Assertions.assertTrue(result3.isPresent());
        Assertions.assertEquals(user3, result3.get());

    }

    @Test
    void shouldThrowWhenDeletingNonExistingUser() {
        var user1 = someUser().withEmail("email1@gmail.com");

        Throwable exception = Assertions.assertThrows(RuntimeException.class, () ->
                userManagementService.delete(user1.getEmail()));
        Assertions.assertEquals(String.format("User with email: [%s] doesn't exist", user1.getEmail()),
                exception.getMessage());
    }


    private static User someUser() {
        return User.builder()
                .name("name")
                .surname("surname")
                .email("email@gmail.com")
                .build();
    }

}
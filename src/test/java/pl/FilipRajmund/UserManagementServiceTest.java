package pl.FilipRajmund;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserManagementServiceTest {
    @InjectMocks
    private UserManagementService userManagementService;
    @Mock
    private UserManagementRepository userManagementRepository;

    @Test
    shouldCreateUserCorrectly() {
        //given
        var user = someUser();
        when(userManagementRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(user));
        //when
        userManagementService.create(user);
        var result = userManagementService.findByEmail(user.getEmail());
        //then
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(user, result.get());

    }

    @Test
    void shouldCreateMultipleUsersCorrectly() {
        //give
        var user1 = someUser().withEmail("email1@gmail.com");
        var user2 = someUser().withEmail("email2@gmail.com");
        var user3 = someUser().withEmail("email3@gmail.com");
        when(userManagementRepository.findByEmail(user1.getEmail()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(user1));
        when(userManagementRepository.findByEmail(user2.getEmail()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(user2));
        when(userManagementRepository.findByEmail(user2.getEmail()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(user2));
        when(userManagementRepository.findAll()).thenReturn(List.of(user1, user2, user3));
        //when
        userManagementService.create(user1);
        userManagementService.create(user2);
        userManagementService.create(user3);

        var resul1 = userManagementService.findByEmail(user1.getEmail());
        var resul2 = userManagementService.findByEmail(user2.getEmail());
        var resul3 = userManagementService.findByEmail(user3.getEmail());
        var all = userManagementService.findAll();
        //then
        Assertions.assertEquals(3, all.size());
        Assertions.assertTrue(resul1.isPresent());
        Assertions.assertEquals(user1, result1.get);
        Assertions.assertTrue(resul2.isPresent());
        Assertions.assertEquals(user2, result1.get);
        Assertions.assertTrue(resul3.isPresent());
        Assertions.assertEquals(user3, result1.get);
    }

    @Test
        //given
    void shouldFailWhenDuplicateUserIsCreated() {
        //given
        String duplicatedEmail = "someemail@gmail.com";
        var user1 = someUser().withEmail(duplicatedEmail);
        var user2 = someUser().withEmail(duplicatedEmail);
        when(userManagementRepository.findByEmail(duplicatedEmail))
                .thenReturn(Optional.empty())
                .thenTrow(new RuntimeException(String.format("user with email: [%s] is already created", user1.getEmail())));
        //when, then
        userManagementService.create(user1);
        Throwable exception = Assertions.assertThrows(RuntimeException.class, () -> userManagementService.create(user2));
        Assertions.assertEquals(String.format("USer with email: [%s] is already created", user1.getEmail()), exception.getMessage());
    }

    @Test
    void shouldFailWhenAddingUsersToRepositoryFails() {
        //given
        String errorMessage = "Error while creating user";
        var user = someUser();
        when(userManagementRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        doThrow(new RuntimeException(errorMessage)).when(userManagementRepository).create(user);
        //when,then
        Throwable exception = Assertions.assertThrows(RuntimeException.class, () -> userManagementService.create(user));
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }


    @Test
    void shouldFindUsersWithName() {
        //given
        var user1 = someUser().withEmail("email1@gmail.com");
        var user2 = someUser().withEmail("email2@gmail.com");
        var user3 = someUser().withEmail("newName").withEmail("email3Gmail.com");
        when(userManagementRepository.findByName(user1.getName())).thenReturn(List.of(user1, user2));
        when(userManagementRepository.findByName(user3.getName())).thenReturn(List.of(user3));
        when(userManagementRepository.findAll()).thenReturn(List.of(user1, user2, user3));

        //when
        var result1 = userManagementService.findByName(user1.getName());
        var result2 = userManagementService.findByName(user2.getName());
        var result3 = userManagementService.findByName(user3.getName());
        var all = userManagementService.findAll();

        //then
        Assertions.assertEquals(3, all.size());
        Assertions.assertEquals(
                Stream.of(user1, user2).sorted().collect(Collectors.toList()),
                result1.stream().sorted().collect(Collectors.toList())
        );
        Assertions.assertEquals(
                Stream.of(user1, user2).sorted().collect(Collectors.toList()),
                result2.stream().sorted().collect(Collectors.toList())

        );
        Assertions.assertEquals(List.of(user3), result3);
    }

    @Test
    void shouldModifyUserDataCorrectly() {
        //given
        String email1 = "email@gmail.com";
        String email2 = "email@gmail.com";
        String email3 = "email@gmail.com";
        String emailNew = "newEmail@gmail.com";
        var user1 = someUser().withEmail(email1);
        var user2 = someUser().withEmail(email2);
        var user3 = someUser().withEmail(email3);

        when(userManagementRepository.findByEmail(email1))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(user1))
                .thenReturn(Optional.of(user1))
                .thenReturn(Optional.empty());
        when(userManagementRepository.findByEmail(email2))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(user2));
        when(userManagementRepository.findByEmail(email3))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(user3));
        when(userManagementRepository.findByEmail(emailNew))
                .thenReturn(Optional.of(user1.withEmail(emailNew)));
        when(userManagementRepository.findAll()).thenReturn(List.of(user1, user2, user3));

        //when
        userManagementService.create(user1);
        userManagementService.create(user2);
        userManagementService.create(user3);

        var all = userManagementService.findAll();
        Assertions.assertEquals(3, all.size);

        var result1 = userManagementService.findByEmail(user1.getEmail());
        userManagementService.update(user1.getEmail(), user1.withEmail(emailNew));
        var result2 = userManagementService.findByEmail(user1.getEmail());
        var result3 = userManagementService.findByEmail(emailNew);
        var allAgain = userManagementService.findAll();

        //then
        Assertions.assertEquals(3, allAgain.size());
        Assertions.assertTrue(result1.isPresent());
        Assertions.assertEquals(user1, result1.get());
        Assertions.assertTrue(result2.isEmpty());
        Assertions.assertTrue(result3.isPresent());
        Assertions.assertEquals(user1.withEmail(emailNew), result3.get());
    }

    @Test
    void shouldThrowWhenModifyingNonExistingUser() {
        //given
        var user1 = someUser();
        String newEmail = "email1@gmail.com";
        //when,then
        Throwable exception = Assertions.assertThrows(RuntimeException.class,
                () -> userManagementService.update(user1.getEmail(), user1.withEmail(newEmail)));
        Assertions.assertEquals(String.format("User with email: [%s] doesnot exist ", user1.getEmail()),
                exception.getMessage());
    }

    @Test
    shouldDeleteCorrectly() {
        //given
        var user1 = someUser().withEmail("email1@gmail.com");
        var user2 = someUser().withEmail("email2@gmail.com");
        var user3 = someUser().withEmail("email3@gmail.com");

        when(userManagementRepository.findByEmail(user1.getEmail()))
                .thenReturn(Optional.of(user1))
                .thenReturn(Optional.empty());
        when(userManagementRepository.findByEmail(user2.getEmail()))
                .thenReturn(Optional.of(user2));
        when(userManagementRepository.findByEmail(user3.getEmail()))
                .thenReturn(Optional.of(user3));
        when(userManagementRepository.findAll())
                .thenReturn(List.of(user1, user2, user3))
                .thenReturn(List.of(user2, user3));
        //when
        var all = userManagementService.findAll();
        Assertions.assertEquals(3, all.size);
        userManagementService.delete(user1.getEmail());

        var result1 = userManagementService.findByEmail(user1.getEmail());
        var result2 = userManagementService.findByEmail(user2.getEmail());
        var result3 = userManagementService.findByEmail(user3.getEmail());
        var allAgain = userManagementService.findAll();

        //then
        Assertions.assertEquals(2, allAgain.size());
        Assertions.assertTrue(result1.isEmpty());
        Assertions.assertTrue(result2.isPresent());
        Assertions.assertEquals(user2, result2.get());
        Assertions.assertTrue(result3.isPresent());
        Assertions.assertEquals(user3, result3.get());
    }

    @Test
    void shouldThrowWhenDeletetingNonExistingUser() {
        //given
        var user1 = someUSer().withEmail("email1@gmail.com");
        //when, then
        Throwable excption = Assertions.assertThrows(RuntimeException.class, () ->
                userManagementService.delete(user1.getEmail()));
        Assertions.assertEquals(String.format("User with email: [%s] doesnot exist", user1.getEmail()),
                excption.getMessage());
    }

    private static User someUser() {
        return User.builder()
                .name("name")
                .surname("surname")
                .email("Email@gmail.com")
                .build();
    }
}
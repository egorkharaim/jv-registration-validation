package core.basesyntax.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;
import core.basesyntax.service.RegistrationService;
import core.basesyntax.service.RegistrationServiceImpl;
import org.junit.jupiter.api.Test;

public class RegistrationServiceTest {
    private RegistrationService registrationService = new RegistrationServiceImpl();
    private StorageDao storageDao = new StorageDaoImpl();

    @Test
    void register_validUser_isOk() {
        User user = new User();
        user.setLogin("ValidLogin"); // > 6
        user.setPassword("password123"); // > 6
        user.setAge(20); // > 18

        User result = registrationService.register(user);
        assertNotNull(result);
        assertNotNull(storageDao.get(user.getLogin()));
        assertEquals(user, result);
    }

    @Test
    void register_shortLogin_notOk() {
        User user = new User();
        user.setLogin("12345"); // < 6
        user.setPassword("ValidPassword");
        user.setAge(20);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_shortPassword_notOk() {
        User user = new User();
        user.setLogin("ValidLogin");
        user.setPassword("12345"); // < 6
        user.setAge(20);

        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_underAge() {
        User user = new User();
        user.setLogin("ValidLogin");
        user.setPassword("ValidPassword");
        user.setAge(17); // < 18
        assertThrows(RegistrationException.class, () -> registrationService.register(user));

    }

    @Test
    void register_duplicateLog_notOk() {
        User firstUser = new User();
        firstUser.setLogin("Log1234");
        firstUser.setPassword("Pass1234");
        firstUser.setAge(20);
        storageDao.add(firstUser);

        User duplicateUser = new User();
        duplicateUser.setLogin("Log1234");
        duplicateUser.setPassword("Pass1234");
        duplicateUser.setAge(22);
        assertThrows(RegistrationException.class,
                () -> registrationService.register(duplicateUser));

    }

    @Test
    void register_loginNull_notOk() {
        User user = new User();
        user.setLogin(null);
        user.setPassword("ValidPassword");
        user.setAge(20);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));

    }

    @Test
    void register_passwordNull_notOk() {
        User user = new User();
        user.setLogin("ValidLogin");
        user.setPassword(null);
        user.setAge(20);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_agNull_notOk() {
        User user = new User();
        user.setLogin("ValidLogin");
        user.setPassword("ValidPassword");
        user.setAge(null);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_userNull_notOk() {
        assertThrows(RegistrationException.class, () -> registrationService.register(null));
    }
}

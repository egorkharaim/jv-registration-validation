package core.basesyntax.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.db.Storage;
import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;
import core.basesyntax.service.RegistrationService;
import core.basesyntax.service.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegistrationServiceTest {
    private RegistrationService registrationService;
    private StorageDao storageDao;

    @BeforeEach
    void setUp() {
        Storage.people.clear();
        registrationService = new RegistrationServiceImpl();
        storageDao = new StorageDaoImpl();
    }

    @Test
    void register_validUser_isOk() {
        User user = new User();
        user.setLogin("ValidLogin");
        user.setPassword("password123");
        user.setAge(20);

        User result = registrationService.register(user);
        assertNotNull(result);
        assertEquals(user, result);
        assertNotNull(storageDao.get(user.getLogin()));
    }

    @Test
    void register_boundaryValues_isOk() {
        User user = new User();
        user.setLogin("sixChr");
        user.setPassword("123456");
        user.setAge(18);

        User result = registrationService.register(user);
        assertNotNull(result);
        assertNotNull(storageDao.get(user.getLogin()));
    }

    @Test
    void register_negativeAge_notOk() {
        User user = new User();
        user.setLogin("ValidLogin");
        user.setPassword("ValidPassword");
        user.setAge(-5);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
    }

    @Test
    void register_shortLogin_notOk() {
        User user = new User();
        user.setLogin("12345");
        user.setPassword("validPass");
        user.setAge(20);
        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
    }

    @Test
    void register_duplicateLogin_notOk() {
        User firstUser = new User();
        firstUser.setLogin("uniqueLog");
        firstUser.setPassword("pass123");
        firstUser.setAge(20);
        storageDao.add(firstUser);

        User duplicateUser = new User();
        duplicateUser.setLogin("uniqueLog");
        duplicateUser.setPassword("newPass");
        duplicateUser.setAge(25);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(duplicateUser));
    }

    @Test
    void register_nullLogin_notOk() {
        User user = new User();
        user.setLogin(null);
        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
    }

    @Test
    void register_nullUser_notOk() {
        assertThrows(RegistrationException.class,
                () -> registrationService.register(null));
    }

}

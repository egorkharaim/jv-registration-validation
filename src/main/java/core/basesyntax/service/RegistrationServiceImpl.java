package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;

public class RegistrationServiceImpl implements RegistrationService {
    private static final int MIN_AGE = 18;
    private static final int MIN_CHARACTER_LENGTH = 6;
    private final StorageDao storageDao = new StorageDaoImpl();

    @Override
    public User register(User user) {
        if (user == null) {
            throw new RegistrationException("User cannot be null");
        }
        if (user.getLogin() == null) {
            throw new RegistrationException("Login cannot be null");
        }
        if (user.getPassword() == null) {
            throw new RegistrationException("Password cannot be null");
        }
        if (user.getAge() == null) {
            throw new RegistrationException("Age cannot be null");
        }
        if (user.getLogin().length() < MIN_CHARACTER_LENGTH) {
            throw new RegistrationException("Login must be at least "
                    + MIN_CHARACTER_LENGTH + " characters");
        }
        if (user.getPassword().length() < MIN_CHARACTER_LENGTH) {
            throw new RegistrationException("Password must be at least "
                    + MIN_CHARACTER_LENGTH + " characters");
        }
        if (user.getAge() < MIN_AGE) {
            throw new RegistrationException("User must be at least "
                    + MIN_AGE + " years old");
        }
        if (storageDao.get(user.getLogin()) != null) {
            throw new RegistrationException("User with this login already exists");
        }
        return storageDao.add(user);
    }
}

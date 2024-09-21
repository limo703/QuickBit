package quickbit.core.service;

import quickbit.core.form.CreateUserForm;
import quickbit.core.form.EditUserForm;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.Image;
import quickbit.dbcore.entity.User;
import quickbit.dbcore.entity.Valet;
import quickbit.dbcore.repositories.UserRepository;
import quickbit.core.exception.UserNotFoundException;
import quickbit.dbcore.entity.UserRole;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final ValetService valetService;
    private final ImageService imageService;

    @Autowired
    public UserServiceImpl(
        UserRepository repository,
        PasswordEncoder passwordEncoder,
        ValetService valetService,
        ImageService imageService
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.valetService = valetService;
        this.imageService = imageService;
    }

    @NotNull
    @Override
    public User getById(@NotNull Long id) {
        return repository.findById(id)
            .orElseThrow(UserNotFoundException::new);
    }

    @NotNull
    @Override
    public User editUser(
        @NotNull EditUserForm form,
        @NotNull Long userId
    ) {
        User user = getById(userId);

        user
            .setFirstName(form.getFirstName())
            .setLastName(form.getLastName())
        ;

        return repository.save(user);
    }

    @Nullable
    @Override
    public User getByUsername(@NotNull String username) {
        return repository.findByUsername(username).orElse(null);
    }

    @Nullable
    @Override
    public User getByEmail(@NotNull String email) {
        return repository.findByEmail(email).orElse(null);
    }

    @Override
    public Optional<User> findByUsername(@NotNull String username) {
        return repository.findByUsername(username);
    }

    @Override
    @Transactional
    public User create(@NotNull CreateUserForm form) {
        User newUser = new User();
        newUser
            .setEmail(form.getEmail())
            .setUsername(form.getUsername())
            .setRole(UserRole.USER)
            .setPassword(
                passwordEncoder.encode(form.getPassword())
            );
        User user = repository.save(newUser);

        Valet valet = new Valet();
        valet
            .setScore(0L)
            .setCurrency(Currency.USD)
            .setUser(user);

        valet = valetService.save(valet);
        user.setValetId(valet.getId());

        Image image = imageService.generateAndSaveAvatar(newUser);
        user.setAvatarId(image.getId());

        return user;
    }

    @Override
    public Page<User> findAll(@NotNull Pageable pageable) {
        return repository.findAll(pageable);
    }

}

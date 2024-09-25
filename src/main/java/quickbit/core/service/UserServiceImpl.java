package quickbit.core.service;

import quickbit.core.form.CreateUserForm;
import quickbit.core.form.EditUserForm;
import quickbit.dbcore.entity.Image;
import quickbit.dbcore.entity.User;
import quickbit.dbcore.entity.Wallet;
import quickbit.dbcore.repositories.UserRepository;
import quickbit.core.exception.UserNotFoundException;
import quickbit.core.util.UserRole;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final WalletService walletService;
    private final ImageService imageService;
    private final CurrencyService currencyService;

    @Autowired
    public UserServiceImpl(
        UserRepository repository,
        PasswordEncoder passwordEncoder,
        WalletService walletService,
        ImageService imageService,
        CurrencyService currencyService
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.walletService = walletService;
        this.imageService = imageService;
        this.currencyService = currencyService;
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

        Wallet wallet = new Wallet();

        wallet
            .setScore(BigDecimal.ZERO)
            .setCurrency(currencyService.getDefault())
            .setUser(user);

        wallet = walletService.save(wallet);
        user.setWalletId(wallet.getId());

        Image image = imageService.generateAndSaveAvatar(newUser);
        user.setAvatarId(image.getId());

        return user;
    }

    @Override
    public Page<User> findAll(@NotNull Pageable pageable) {
        return repository.findAll(pageable);
    }

}

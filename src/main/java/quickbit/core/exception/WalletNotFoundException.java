package quickbit.core.exception;

public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException() {
    }

    public WalletNotFoundException(String message) {
        super(message);
    }
}

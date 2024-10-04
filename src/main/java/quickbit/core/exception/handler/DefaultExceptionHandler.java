package quickbit.core.exception.handler;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import quickbit.core.exception.FileNotFoundException;
import quickbit.core.exception.UserNotFoundException;
import quickbit.core.exception.WalletNotFoundException;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler({
        EntityNotFoundException.class,
        FileNotFoundException.class,
        UserNotFoundException.class,
        WalletNotFoundException.class
    })
    public ModelAndView handleAllExceptions(Exception ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return new ModelAndView("errors/error-404")
            .addObject("message", ex.getMessage());
    }
}

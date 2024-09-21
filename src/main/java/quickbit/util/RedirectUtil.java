package quickbit.util;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class RedirectUtil {

    public static ModelAndView redirect(String path) {
        RedirectView view = new RedirectView();
        view.setUrl(path);
        view.setExposeModelAttributes(false);
        return new ModelAndView(view);
    }

    public static ModelAndView redirect(String path, Object ... args) {
        String url = String.format(path, args);
        RedirectView view = new RedirectView();
        view.setUrl(url);
        view.setExposeModelAttributes(false);
        return new ModelAndView(view);
    }

}

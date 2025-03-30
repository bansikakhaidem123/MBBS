package Application.MBBS.controller;

import Application.MBBS.entity.User;
import Application.MBBS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FormController {

    @Autowired
    private UserService userService;

    @GetMapping("/form")
    public String showForm(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.findByEmail(authentication.getName());
            if (user != null) {
                model.addAttribute("firstName", user.getFirstName());
                model.addAttribute("lastName", user.getLastName());
                model.addAttribute("fullName", user.getFirstName() + " " + user.getLastName());
            }
        }
        return "form"; // Should match the name of the Thymeleaf template (form.html)
    }

    @GetMapping("/application")
    public String showApplicationPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.findByEmail(authentication.getName());
            if (user != null) {
                model.addAttribute("firstName", user.getFirstName());
                model.addAttribute("lastName", user.getLastName());
                model.addAttribute("fullName", user.getFirstName() + " " + user.getLastName());
            }
        }
        return "application"; // Should match the name of the Thymeleaf template (application.html)
    }
}
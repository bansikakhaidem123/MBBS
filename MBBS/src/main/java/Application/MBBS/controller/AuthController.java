package Application.MBBS.controller;

import Application.MBBS.entity.User;
import Application.MBBS.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthController(PasswordEncoder passwordEncoder, UserService userService, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/home";
        }
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        ResponseEntity<Map<String, String>> response = userService.registerUser(user);

        if (response.getStatusCode().is2xxSuccessful()) {
            redirectAttributes.addFlashAttribute("success", "Sign up successful! You can now log in.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", response.getBody().get("error"));
            return "redirect:/register";
        }
    }

    @GetMapping(value={"/login", "/"})
    public String showLoginPage(Authentication auth) {
        if(auth != null ){
            System.out.println("AUTH NOT NULL");
            if(auth.isAuthenticated())
                System.out.println("IS AUTHENTICATED");
            return "redirect:/home";
        }
        System.out.println("RETURNING LOGIN PAGE");
        return "login";
    }

//    @PostMapping("/login")
//    public String login(@RequestParam String emailOrPhone, @RequestParam String password, RedirectAttributes redirectAttributes) {
//        System.out.println("🟡 Attempting login with: " + emailOrPhone);
//
//        User user = userService.findByEmailOrPhone(emailOrPhone);
//
//        if (user == null) {
//            System.out.println("❌ User not found!");
//            redirectAttributes.addFlashAttribute("error", "User not found.");
//            return "redirect:/login";
//        }
//
//        System.out.println("✅ User found: " + user.getEmail());
//
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            System.out.println("❌ Invalid password entered!");
//            redirectAttributes.addFlashAttribute("error", "Invalid password.");
//            return "redirect:/login";
//        }
//
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(user.getEmail(), password)
//            );
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            System.out.println("✅ Login successful!");
//            return "redirect:/form";
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(" Authentication failed!");
//            redirectAttributes.addFlashAttribute("error", "Authentication failed.");
//            return "redirect:/login";
//        }
//    }



//    @GetMapping("/form")
//    public String showFormPage() {
//        return "form";
//    }
//
//    @GetMapping("/home")
//    public String showHomePage() {
//        return "home";
//    }
}

package Application.MBBS.controller;

import Application.MBBS.entity.Documents;
import Application.MBBS.entity.User;
import Application.MBBS.service.DocumentsService;
import Application.MBBS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class HomeController {


    private final UserService userService;
    private DocumentsService documentsService;

    public HomeController(DocumentsService documentsService,UserService userService) {
        this.documentsService = documentsService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String showHomePage(Model model, Authentication authentication) {
        model.addAttribute("admissions", documentsService.getLatestAdmissions());
        model.addAttribute("notifications", documentsService.getLatestNotifications());

        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.findByEmail(authentication.getName());
            if (user != null) {
                model.addAttribute("firstName", user.getFirstName());
                model.addAttribute("lastName", user.getLastName());
                model.addAttribute("fullName", user.getFirstName() + " " + user.getLastName());
            }
        }
        return "home";
    }


    @GetMapping("/admissions")
    public String showAdmissionsPage(Model model) {
        model.addAttribute("admissions", documentsService.getAllAdmissions());
        return "admissions"; // This should match a Thymeleaf template in src/main/resources/templates
    }

    @GetMapping("/notifications")
    public String showNotificationsPage(Model model) {
        model.addAttribute("notifications", documentsService.getAllNotifications());
        return "notifications"; // Should match src/main/resources/templates/notifications.html
    }

    @GetMapping("/admissions/preview/{id}")
    public ResponseEntity<Resource> viewPdf(@PathVariable Long id) {
        Documents document = documentsService.getAdmissionById(id).orElseThrow(() -> new RuntimeException("Document not found"));
        try {
            Path path = Paths.get(document.getDFilePath());
            if (!Files.exists(path)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + path.getFileName() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/notifications/view/{id}")
    public ResponseEntity<Resource> viewNotificationPdf(@PathVariable Long id) {
        Documents document = documentsService.getNotificationById(id).orElseThrow(() -> new RuntimeException("Document not found"));
        try {
            Path path = Paths.get(document.getDFilePath());
            if (!Files.exists(path)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + path.getFileName() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/admissions/download/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) {
        Documents document = documentsService.getAdmissionById(id).orElseThrow(() -> new RuntimeException("Document not found"));
        Path path = Paths.get(document.getDFilePath());
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName().toString() + "\"")
                .body(resource);
    }

}
package Application.MBBS.service;

import Application.MBBS.entity.Documents;
import Application.MBBS.repository.DocumentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentsService {

    @Autowired
    private DocumentsRepository documentsRepository;

    public DocumentsService(DocumentsRepository documentsRepository) {
        this.documentsRepository = documentsRepository;
    }

    // Fetch all admissions
    public List<Documents> getAllAdmissions() {
        return documentsRepository.findBydType("admissions")
                .stream()
                .sorted((d1, d2) -> d2.getDDate().compareTo(d1.getDDate()))
                .collect(Collectors.toList());
    }

    // Fetch latest 3 admissions
    public List<Documents> getLatestAdmissions() {
        return documentsRepository.findBydType("admissions")
                .stream()
                .sorted((d1, d2) -> d2.getDDate().compareTo(d1.getDDate()))
                .limit(5)
                .collect(Collectors.toList());
    }

    // Fetch a specific admission by ID
    public Optional<Documents> getAdmissionById(Long id) {
        return documentsRepository.findById(id);
    }

    // Fetch all notifications
    public List<Documents> getAllNotifications() {
        return documentsRepository.findBydType("notifications")
                .stream()
                .sorted((d1, d2) -> d2.getDDate().compareTo(d1.getDDate()))
                .collect(Collectors.toList());
    }

    // Fetch latest 3 notifications
    public List<Documents> getLatestNotifications() {
        return documentsRepository.findBydType("notifications")
                .stream()
                .sorted((d1, d2) -> d2.getDDate().compareTo(d1.getDDate()))
                .limit(5)
                .collect(Collectors.toList());
    }

    // Fetch a specific notification by ID
    public Optional<Documents> getNotificationById(Long id) {
        return documentsRepository.findById(id);
    }

    // Save a document
    public void saveDocument(Documents document) {
        documentsRepository.save(document);
    }
}
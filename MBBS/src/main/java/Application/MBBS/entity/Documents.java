package Application.MBBS.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "documents")
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "d_type", nullable = false)
    private String dType;

    @Column(name = "d_description")
    private String dDescription;

    @Column(name = "d_no", unique = true, nullable = false)
    private String dNo;

    @Column(name = "d_date")
    private LocalDate dDate;

    @Column(name = "d_file_path", nullable = false)
    private String dFilePath; // Storing the file path instead of byte array

    // Constructors
    public Documents() {}

    public Documents(String dType, String dDescription, String dNo, LocalDate dDate, String dFilePath) {
        this.dType = dType;
        this.dDescription = dDescription;
        this.dNo = dNo;
        this.dDate = dDate;
        this.dFilePath = dFilePath;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDType() {
        return dType;
    }

    public void setDType(String dType) {
        this.dType = dType;
    }

    public String getDDescription() {
        return dDescription;
    }

    public void setDDescription(String dDescription) {
        this.dDescription = dDescription;
    }

    public String getDNo() {
        return dNo;
    }

    public void setDNo(String dNo) {
        this.dNo = dNo;
    }

    public LocalDate getDDate() {
        return dDate;
    }

    public void setDDate(LocalDate dDate) {
        this.dDate = dDate;
    }

    public String getDFilePath() {
        return dFilePath;
    }

    public void setDFilePath(String dFilePath) {
        this.dFilePath = dFilePath;
    }
}
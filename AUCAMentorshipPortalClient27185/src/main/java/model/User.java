package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user")
    private MentorProfile mentorProfile;

    @OneToMany(mappedBy = "mentor")
    private List<MentorshipSession> sessionsAsMentor;

    @OneToMany(mappedBy = "mentee")
    private List<MentorshipSession> sessionsAsMentee;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    public User() {
    }

    public User(Long id, String firstName, String lastName, String email, String password, String phoneNumber, UserRole role, boolean isActive, LocalDateTime createdAt, MentorProfile mentorProfile, List<MentorshipSession> sessionsAsMentor, List<MentorshipSession> sessionsAsMentee, List<Notification> notifications) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.mentorProfile = mentorProfile;
        this.sessionsAsMentor = sessionsAsMentor;
        this.sessionsAsMentee = sessionsAsMentee;
        this.notifications = notifications;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public MentorProfile getMentorProfile() {
        return mentorProfile;
    }

    public void setMentorProfile(MentorProfile mentorProfile) {
        this.mentorProfile = mentorProfile;
    }

    public List<MentorshipSession> getSessionsAsMentor() {
        return sessionsAsMentor;
    }

    public void setSessionsAsMentor(List<MentorshipSession> sessionsAsMentor) {
        this.sessionsAsMentor = sessionsAsMentor;
    }

    public List<MentorshipSession> getSessionsAsMentee() {
        return sessionsAsMentee;
    }

    public void setSessionsAsMentee(List<MentorshipSession> sessionsAsMentee) {
        this.sessionsAsMentee = sessionsAsMentee;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    
}
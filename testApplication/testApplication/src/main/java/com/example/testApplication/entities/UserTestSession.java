package com.example.testApplication.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_test_session")
public class UserTestSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userTestSessionId;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Boolean isCompleted;

    public UserTestSession(Long userTestSessionId, User user, Test test, LocalDateTime startTime, LocalDateTime endTime,
            Boolean isCompleted) {
        this.userTestSessionId = userTestSessionId;
        this.user = user;
        this.test = test;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isCompleted = isCompleted;
    }

    public Long getUserTestSessionId() {
        return userTestSessionId;
    }

    public void setUserTestSessionId(Long userTestSessionId) {
        this.userTestSessionId = userTestSessionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}

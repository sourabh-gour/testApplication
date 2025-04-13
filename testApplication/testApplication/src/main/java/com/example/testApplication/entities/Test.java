package com.example.testApplication.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tests")
public class Test {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long testId;

    private String subjectTitle;
    private Integer duration;
    private Boolean isActive;

    public Test(Long testId, String subjectTitle
    , Integer duration, Boolean isActive) {
        this.testId = testId;
        this.subjectTitle
         = subjectTitle
        ;
        this.duration = duration;
        this.isActive = isActive;
    }
    public Long getTestId() {
        return testId;
    }
    public void setTestId(Long testId) {
        this.testId = testId;
    }
    public String getSubject() {
        return subjectTitle
        ;
    }
    public void setSubject(String subjectTitle
    ) {
        this.subjectTitle
         = subjectTitle
        ;
    }
    public Integer getDuration() {
        return duration;
    }
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
}

package com.interviewplatform.IESP.repository;

import com.interviewplatform.IESP.entity.InterviewExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewExperienceRepository
        extends JpaRepository<InterviewExperience, Long> {

    List<InterviewExperience> findByCompanyContainingIgnoreCase(
            String company);

    List<InterviewExperience> findByRoleContainingIgnoreCase(
            String role);

    List<InterviewExperience> findByTechnologyContainingIgnoreCase(
            String technology);
}
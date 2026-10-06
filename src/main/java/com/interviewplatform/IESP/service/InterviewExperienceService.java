package com.interviewplatform.IESP.service;

import com.interviewplatform.IESP.entity.InterviewExperience;
import com.interviewplatform.IESP.entity.User;
import com.interviewplatform.IESP.repository.InterviewExperienceRepository;
import com.interviewplatform.IESP.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewExperienceService {

    private final InterviewExperienceRepository repository;
    private final UserRepository userRepository;

    public InterviewExperienceService(
            InterviewExperienceRepository repository,
            UserRepository userRepository) {

        this.repository = repository;
        this.userRepository = userRepository;
    }

    public InterviewExperience saveExperience(
            InterviewExperience experience,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        experience.setUser(user);

        return repository.save(experience);
    }

    public List<InterviewExperience> getAllExperiences() {

        return repository.findAll();
    }
    public List<InterviewExperience> searchExperiences(String search) {

        if (search == null || search.trim().isEmpty()) {
            return repository.findAll();
        }

        String keyword = search.replaceAll("\\s+", "").toLowerCase();

        List<InterviewExperience> allExperiences =
                repository.findAll();

        List<InterviewExperience> results =
                new java.util.ArrayList<>();

        for (InterviewExperience experience : allExperiences) {

            String company = experience.getCompany() == null
                    ? ""
                    : experience.getCompany();

            String role = experience.getRole() == null
                    ? ""
                    : experience.getRole();

            String technology = experience.getTechnology() == null
                    ? ""
                    : experience.getTechnology();

            company = company.replaceAll("\\s+", "").toLowerCase();
            role = role.replaceAll("\\s+", "").toLowerCase();
            technology = technology.replaceAll("\\s+", "").toLowerCase();

            if (company.contains(keyword)
                    || role.contains(keyword)
                    || technology.contains(keyword)) {

                results.add(experience);
            }
        }

        return results;
    }

    public InterviewExperience getExperienceById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Experience not found"));
    }

    public InterviewExperience getExperienceForEdit(
            Long id,
            String email) {

        InterviewExperience experience =
                getExperienceById(id);

        checkOwnership(experience, email);

        return experience;
    }

    public void updateExperience(
            Long id,
            InterviewExperience updatedExperience,
            String email) {

        InterviewExperience existingExperience =
                getExperienceById(id);

        checkOwnership(existingExperience, email);

        existingExperience.setCompany(
                updatedExperience.getCompany()
        );

        existingExperience.setRole(
                updatedExperience.getRole()
        );

        existingExperience.setTechnology(
                updatedExperience.getTechnology()
        );

        existingExperience.setQuestions(
                updatedExperience.getQuestions()
        );

        existingExperience.setExperience(
                updatedExperience.getExperience()
        );

        existingExperience.setResult(
                updatedExperience.getResult()
        );

        repository.save(existingExperience);
    }

    public void deleteExperience(
            Long id,
            String email) {

        InterviewExperience experience =
                getExperienceById(id);

        checkOwnership(experience, email);

        repository.delete(experience);
    }

    private void checkOwnership(
            InterviewExperience experience,
            String email) {

        if (experience.getUser() == null ||
                !experience.getUser()
                        .getEmail()
                        .equals(email)) {

            throw new AccessDeniedException(
                    "You are not allowed to modify this experience"
            );
        }
    }
    public void deleteExperienceByAdmin(Long id){
        InterviewExperience experience =repository.findById(id).orElseThrow(()->
                new RuntimeException("Experience not found"));
        repository.delete(experience);
    }

    public long getTotalExperiences(){
        return repository.count();
    }
}
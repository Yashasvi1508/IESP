package com.interviewplatform.IESP.repository;

import com.interviewplatform.IESP.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository
        extends JpaRepository<Question, Long> {

    List<Question> findByCategoryContainingIgnoreCase(
            String category);

    List<Question> findByDifficultyContainingIgnoreCase(
            String difficulty);

    List<Question> findByQuestionContainingIgnoreCase(
            String question);
}
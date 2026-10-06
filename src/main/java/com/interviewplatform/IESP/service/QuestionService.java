package com.interviewplatform.IESP.service;

import com.interviewplatform.IESP.entity.Question;
import com.interviewplatform.IESP.entity.User;
import com.interviewplatform.IESP.repository.QuestionRepository;
import com.interviewplatform.IESP.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository repository;
    private final UserRepository userRepository;

    public QuestionService(
            QuestionRepository repository,
            UserRepository userRepository) {

        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<Question> getAllQuestions() {
        return repository.findAll();
    }

    public Question getQuestionById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Question not found"));
    }

    public Question saveQuestion(
            Question question,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        question.setUser(user);

        return repository.save(question);
    }

    public List<Question> searchQuestions(String search) {

        if (search == null || search.trim().isEmpty()) {
            return repository.findAll();
        }

        // Remove spaces and convert to lowercase
        String keyword = search.replaceAll("\\s+", "").toLowerCase();

        List<Question> allQuestions = repository.findAll();
        List<Question> results = new java.util.ArrayList<>();

        for (Question question : allQuestions) {

            String questionText = question.getQuestion() == null
                    ? ""
                    : question.getQuestion();

            String category = question.getCategory() == null
                    ? ""
                    : question.getCategory();

            String difficulty = question.getDifficulty() == null
                    ? ""
                    : question.getDifficulty();

            // Remove spaces + lowercase
            questionText = questionText.replaceAll("\\s+", "").toLowerCase();
            category = category.replaceAll("\\s+", "").toLowerCase();
            difficulty = difficulty.replaceAll("\\s+", "").toLowerCase();

            if (questionText.contains(keyword)
                    || category.contains(keyword)
                    || difficulty.contains(keyword)) {

                results.add(question);
            }
        }

        return results;
    }

    public Question getQuestionForEdit(
            Long id,
            String email) {

        Question question = getQuestionById(id);

        checkOwnership(question, email);

        return question;
    }

    public void updateQuestion(
            Long id,
            Question updatedQuestion,
            String email) {

        Question existingQuestion =
                getQuestionById(id);

        checkOwnership(existingQuestion, email);

        existingQuestion.setQuestion(
                updatedQuestion.getQuestion()
        );

        existingQuestion.setCategory(
                updatedQuestion.getCategory()
        );

        existingQuestion.setDifficulty(
                updatedQuestion.getDifficulty()
        );

        existingQuestion.setAnswer(
                updatedQuestion.getAnswer()
        );

        repository.save(existingQuestion);
    }

    public void deleteQuestion(
            Long id,
            String email) {

        Question question =
                getQuestionById(id);

        checkOwnership(question, email);

        repository.delete(question);
    }

    private void checkOwnership(
            Question question,
            String email) {

        if (question.getUser() == null ||
                !question.getUser()
                        .getEmail()
                        .equals(email)) {

            throw new AccessDeniedException(
                    "You are not allowed to modify this question"
            );
        }
    }
    public void deleteQuestionByAdmin(Long id){
        Question question=repository.findById(id).orElseThrow(()->
                new RuntimeException("Question not found"));
        repository.delete(question);
    }

    public long getTotalQuestions(){
        return repository.count();
    }
}
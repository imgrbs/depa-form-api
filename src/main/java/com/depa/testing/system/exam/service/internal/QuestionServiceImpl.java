package com.depa.testing.system.exam.service.internal;

import com.depa.testing.system.category.model.Category;
import com.depa.testing.system.category.service.CategoryService;
import com.depa.testing.system.exam.dto.QuestionDTO;
import com.depa.testing.system.exam.dto.impl.QuestionDTOImpl;
import com.depa.testing.system.exam.model.question.Question;
import com.depa.testing.system.exam.repository.QuestionRepository;
import com.depa.testing.system.exam.service.QuestionService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Setter
@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public List<QuestionDTO> getQuestions() {
        List<QuestionDTO> questions = new ArrayList<>();
        questionRepository.findAll().forEach(question -> questions.add(new QuestionDTOImpl(question)));
        return questions;
    }

    @Override
    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        Question question = questionRepository.save(questionDTO.toQuestion());
        return new QuestionDTOImpl(question);
    }

}

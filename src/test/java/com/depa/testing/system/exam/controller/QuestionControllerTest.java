package com.depa.testing.system.exam.controller;

import com.depa.testing.system.exam.dto.QuestionDTO;
import com.depa.testing.system.exam.dto.impl.QuestionDTOImpl;
import com.depa.testing.system.exam.model.question.SubjectiveQuestion;
import com.depa.testing.system.exam.model.question.Question;
import com.depa.testing.system.exam.service.QuestionService;
import org.hamcrest.CoreMatchers;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

class QuestionControllerTest {
    private Mockery mockery = new JUnit4Mockery();

    private QuestionController underTest;
    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        underTest = new QuestionController();
        questionService = mockery.mock(QuestionService.class);
        underTest.setQuestionService(questionService);
    }

    @Test
    void testGetQuestionsShouldReturnListOfQuestionModel() {
        SubjectiveQuestion expectedQuestion = SubjectiveQuestion.create("1 + 1 = ?", null);
        QuestionDTO expectedQuestionDTO = new QuestionDTOImpl(expectedQuestion);
        expectedGetQuestions(expectedQuestionDTO);

        ResponseEntity<List<QuestionDTO>> result = underTest.getQuestions();
        List<QuestionDTO> actualResult = result.getBody();

        Assert.assertThat(actualResult.size(), CoreMatchers.equalTo(1));
        Assert.assertThat(actualResult.get(0), CoreMatchers.equalTo(expectedQuestionDTO));
    }

    @Test
    void testCreateQuestion() {
        Question question = SubjectiveQuestion.create("1 + 1 = ?", null);
        QuestionDTOImpl request = new QuestionDTOImpl(question);

        expectedCreateQuestion(request);

        ResponseEntity<QuestionDTO> result = underTest.createQuestion(request);

        Assert.assertThat(result.getStatusCode(), CoreMatchers.equalTo(HttpStatus.CREATED));
        Assert.assertThat(result.getBody(), CoreMatchers.equalTo(request));
    }

    private void expectedCreateQuestion(QuestionDTO request) {
        mockery.checking(new Expectations() {
            {
                oneOf(questionService).createQuestion(request);
                will(returnValue(request));
            }
        });
    }

    private void expectedGetQuestions(QuestionDTO expectedQuestionDTO) {
        mockery.checking(new Expectations(){
            {
                oneOf(questionService).getQuestions();
                will(returnValue(Arrays.asList(expectedQuestionDTO)));
            }
        });
    }
}
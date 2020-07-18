package com.depa.testing.system.exam.dto;

import com.depa.testing.system.exam.dto.impl.ExamDTOImpl;
import com.depa.testing.system.exam.model.exam.Exam;
import org.bson.types.ObjectId;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class ExamDTOTest {

    @Test
    public void testCreateFormDTO() {
        ExamDTO underTest = new ExamDTOImpl();

        Assert.assertThat(underTest.getName(),CoreMatchers.nullValue());
        Assert.assertThat(underTest.getDescription(),CoreMatchers.nullValue());
        Assert.assertThat(underTest.getQuestions(),CoreMatchers.nullValue());
    }

    @Test
    void testCreateFormDTOWithForm() {
        Exam exam = new Exam();
        ExamDTO underTest = new ExamDTOImpl(exam);

        Assert.assertThat(underTest.getName(), CoreMatchers.nullValue());
        Assert.assertThat(underTest.getDescription(), CoreMatchers.nullValue());
        Assert.assertThat(underTest.getQuestions().size(), CoreMatchers.equalTo(0));
    }

    @Test
    void testToExam() {
        ObjectId id = new ObjectId("5f03163c00657756d47d0884");
        Exam exam = new Exam(id, "exam name", "exam description", new ArrayList<>());
        ExamDTOImpl underTest = new ExamDTOImpl(exam);

        Exam actual = underTest.toExam();

        Assert.assertThat(actual.getName(), CoreMatchers.equalTo(exam.getName()));
        Assert.assertThat(actual.getDescription(), CoreMatchers.equalTo(exam.getDescription()));
        Assert.assertThat(actual.getQuestions().size(), CoreMatchers.equalTo(0));
    }
}
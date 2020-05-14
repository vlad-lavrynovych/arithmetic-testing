package com.example.arithmetic_testing.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String expression;
    private Integer correctAnswer;
    private Integer givenAnswer;
    private Boolean isCorrect;
    private Date appearTime;
    private Date answerTime;
    private Long duration;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}

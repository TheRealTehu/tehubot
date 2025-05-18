package com.therealtehu.discordbot.TehuBot.database.model.quiz;

import jakarta.persistence.*;
//TODO: Fill out with necessary fields
@Entity
public class QuizQuestionData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private QuizData quizData;
}

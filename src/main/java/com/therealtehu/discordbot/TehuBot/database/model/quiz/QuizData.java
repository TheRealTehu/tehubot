package com.therealtehu.discordbot.TehuBot.database.model.quiz;

import com.therealtehu.discordbot.TehuBot.database.model.GuildData;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class QuizData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String publicId;
    @OneToMany(fetch = FetchType.EAGER)
    private List<QuizQuestionData> questions = new ArrayList<>();
    @ManyToOne
    private GuildData guild;
    private String quizTitle;
    private boolean isOngoing = false;

    public QuizData() {
    }

    public QuizData(long id, String publicId, List<QuizQuestionData> questions, GuildData guild, String quizTitle, boolean isOngoing) {
        this.id = id;
        this.publicId = publicId;
        this.questions = questions;
        this.guild = guild;
        this.quizTitle = quizTitle;
        this.isOngoing = isOngoing;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public List<QuizQuestionData> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuizQuestionData> questions) {
        this.questions = questions;
    }

    public GuildData getGuild() {
        return guild;
    }

    public void setGuild(GuildData guild) {
        this.guild = guild;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public boolean addQuestion(QuizQuestionData quizQuestionData) {
        return questions.add(quizQuestionData);
    }

    public boolean isOngoing() {
        return isOngoing;
    }

    public void setOngoing(boolean ongoing) {
        isOngoing = ongoing;
    }

    public String getListInformation() {
        return "**Quiz title: **" + quizTitle + "** public id: **" + publicId + "** questions: **" + questions.size();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        QuizData quizData = (QuizData) o;
        return id == quizData.id && Objects.equals(publicId, quizData.publicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, publicId);
    }

    @Override
    public String toString() {
        return "QuizData{" +
                "id=" + id +
                ", publicId='" + publicId + '\'' +
                ", questions=" + questions +
                ", guild=" + guild +
                ", quizTitle='" + quizTitle + '\'' +
                '}';
    }
}

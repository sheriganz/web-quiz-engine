package engine.quiz;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Completion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quizId;
    private String userEmail;
    private LocalDateTime completedAt;

    protected Completion() {}

    public Completion(Integer quizId, String userEmail) {
        this.quizId = quizId;
        this.userEmail = userEmail;
        this.completedAt = LocalDateTime.now();
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public Integer getQuizId() {
        return quizId;
    }
}

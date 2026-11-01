package engine.quiz;

import java.time.LocalDateTime;

public record CompletionResponse (
        Integer id,
        LocalDateTime completedAt
) {
    static CompletionResponse from(Completion completion) {
        return new CompletionResponse(completion.getQuizId(), completion.getCompletedAt());
    }
}

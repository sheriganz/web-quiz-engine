package engine.quiz;

public record AnswerResult(boolean success, String feedback) {

    static AnswerResult of(boolean success) {
        return new AnswerResult(success,
                success ? "Congratulations, you're right!" : "Wrong answer! Please, try again.");
    }
}

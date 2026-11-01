package engine.quiz;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class QuizController {

    private final QuizService quizService;
    private final QuizRepository quizRepository;

    public QuizController(QuizService quizService, QuizRepository quizRepository) {
        this.quizService = quizService;
        this.quizRepository = quizRepository;
    }

    @PostMapping("/api/quizzes")
    public Quiz createQuiz(@RequestBody @Valid Quiz quiz, Authentication authentication) {
        return quizService.create(quiz, authentication.getName());
    }

    @GetMapping("/api/quizzes/{id}")
    public Quiz getQuiz(@PathVariable int id) {
        return findQuiz(id);
    }

    @GetMapping("/api/quizzes")
    public Page<Quiz> getAllQuizzes(@RequestParam(defaultValue = "0") int page) {
        return quizService.findAll(page);
    }

    @PostMapping("/api/quizzes/{id}/solve")
    public AnswerResult solveQuiz(@PathVariable int id, @RequestBody SolveRequest request, Authentication authentication) {
        Quiz quiz = findQuiz(id);
        boolean correct = quiz.isCorrect(request.answer());
        if (correct) { quizService.recordCompletion(id, authentication.getName()); }
        return AnswerResult.of(correct);
    }

    @DeleteMapping("/api/quizzes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuiz(@PathVariable int id, Authentication authentication) {
        quizService.delete(id, authentication.getName());
    }

    @GetMapping("/api/quizzes/completed")
    public Page<CompletionResponse> getCompletedQuizzes(@RequestParam(defaultValue = "0") int page, Authentication authentication) {
        return quizService.getCompletions(authentication.getName(), page);
    }

    private Quiz findQuiz(int id) {
        return quizService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}

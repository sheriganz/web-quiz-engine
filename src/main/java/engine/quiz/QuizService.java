package engine.quiz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final CompletionRepository completionRepository;

    public QuizService(QuizRepository quizRepository, CompletionRepository completionRepository) {
        this.quizRepository = quizRepository;
        this.completionRepository = completionRepository;
    }

    public Quiz create(Quiz quiz, String author) {
        List<Integer> answer = quiz.answer() == null ? List.of() : quiz.answer();
        QuizEntity entity = new QuizEntity(quiz.title(), quiz.text(),
                new ArrayList<>(quiz.options()), new ArrayList<>(answer), author);
        return toQuiz(quizRepository.save(entity));
    }

    public Optional<Quiz> findById(int id) {
        return quizRepository.findById(id).map(QuizService::toQuiz);
    }

    public Page<Quiz> findAll(int page) {
        return quizRepository.findAll(PageRequest.of(page, 10)).map(QuizService::toQuiz);
    }

    private static Quiz toQuiz(QuizEntity entity) {
        return new Quiz(entity.getId(), entity.getTitle(), entity.getText(),
                List.copyOf(entity.getOptions()), List.copyOf(entity.getAnswer()));
    }

    public void  delete(int id, String currentUser) {
        QuizEntity quiz = quizRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!currentUser.equals(quiz.getAuthor())) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        quizRepository.delete(quiz);
    }

    public void recordCompletion(int quizId, String userEmail) {
        completionRepository.save(new Completion(quizId, userEmail));
    }

    public Page<CompletionResponse> getCompletions(String userEmail, int page) {
        return completionRepository.findAllByUserEmailOrderByCompletedAtDescIdDesc(userEmail, PageRequest.of(page, 10))
                .map(CompletionResponse::from);
    }

}

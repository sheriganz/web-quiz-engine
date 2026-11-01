package engine.quiz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompletionRepository extends JpaRepository<Completion, Long> {
    Page<Completion> findAllByUserEmailOrderByCompletedAtDescIdDesc(String userEmail, Pageable pageable);
}

package engine.quiz;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

public record Quiz(
        Integer id,
        @NotBlank String title,
        @NotBlank String text,
        @NotNull @Size(min=2) List<String> options,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) List<Integer> answer
) {

    public boolean isCorrect(List<Integer> chosen) {
        List<Integer> correct = answer == null ? List.of() : answer;
        List<Integer> given = chosen == null ? List.of() : chosen;
        return Set.copyOf(correct).equals(Set.copyOf(given));
    }
}

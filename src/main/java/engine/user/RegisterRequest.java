package engine.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// @Email пропускает "well@googlecom", а тест ждёт 400, поэтому проверяем точку в домене регуляркой
public record RegisterRequest(
        @NotBlank @Pattern(regexp = "[^@\\s]+@[^@\\s]+\\.[^@\\s]+") String email,
        @NotBlank @Size(min = 5) String password) {
}

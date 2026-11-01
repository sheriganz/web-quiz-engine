# Web Quiz Engine

A REST backend for creating and solving quizzes, built with Spring Boot as a project of the
Hyperskill track "Java Backend Developer (Spring Boot)".

Users register, create multiple-choice quizzes with any number of correct options, solve quizzes
of other users and see the history of their successful attempts. Lists are paginated.

## Tech stack

- Java 21, Spring Boot 3.5
- Spring Web, Spring Validation
- Spring Security with HTTP Basic auth and BCrypt
- Spring Data JPA / Hibernate, H2 (file database)
- Gradle

## Running

```bash
./gradlew bootRun
```

The service starts on `http://localhost:8889` and stores data in `./data/quizdb.mv.db`.

## API

All endpoints except registration require HTTP Basic auth (email and password).

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/register` | Register a user |
| `POST` | `/api/quizzes` | Create a quiz |
| `GET` | `/api/quizzes/{id}` | Get a quiz (without the answer) |
| `GET` | `/api/quizzes?page=0` | List quizzes, 10 per page |
| `POST` | `/api/quizzes/{id}/solve` | Solve a quiz |
| `DELETE` | `/api/quizzes/{id}` | Delete a quiz (author only) |
| `GET` | `/api/quizzes/completed?page=0` | Your successful attempts, newest first |

### Register

```http
POST /api/register
{"email": "user@mail.org", "password": "secret"}
```

`200 OK`; `400` for an invalid email, a password shorter than 5 characters or an email that is
already taken.

### Create a quiz

```http
POST /api/quizzes
{
  "title": "The Java Logo",
  "text": "What is depicted on the Java logo?",
  "options": ["Robot", "Tea leaf", "Cup of coffee", "Bug"],
  "answer": [2]
}
```

```json
{"id": 1, "title": "The Java Logo", "text": "What is depicted on the Java logo?",
 "options": ["Robot", "Tea leaf", "Cup of coffee", "Bug"]}
```

`title` and `text` are required, `options` must contain at least two items. `answer` lists the
indexes of all correct options and may be empty or absent. It is never returned by the API.

### Solve a quiz

```http
POST /api/quizzes/1/solve
{"answer": [2]}
```

```json
{"success": true, "feedback": "Congratulations, you're right!"}
```

The answer is correct only if it contains exactly the right set of options, in any order.
Every correct answer is stored as a completion.

### Delete a quiz

`204 No Content` for the author, `403` for other users, `404` if the quiz does not exist.
Completions of a deleted quiz stay in the history.

### Paginated lists

`GET /api/quizzes?page=0` and `GET /api/quizzes/completed?page=0` return a Spring Data `Page`:

```json
{
  "content": [{"id": 1, "completedAt": "2026-11-02T01:23:45.678"}],
  "totalPages": 1,
  "totalElements": 1,
  "first": true,
  "last": true
}
```

## Implementation notes

- `options` and `answer` are stored with `@ElementCollection`; one of them uses
  `@Fetch(FetchMode.SUBSELECT)` to avoid `MultipleBagFetchException` with two eager collections.
- The answer is hidden from responses with `@JsonProperty(access = WRITE_ONLY)`.
- Completions keep the quiz id as a plain column rather than a foreign key, so deleting a quiz
  does not break the history.

## Notes

This is a learning project. `/actuator/shutdown` and the H2 console are open because the course
tests rely on them; they should be closed in any real deployment.

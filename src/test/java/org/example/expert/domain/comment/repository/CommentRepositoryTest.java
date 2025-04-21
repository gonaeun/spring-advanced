package org.example.expert.domain.comment.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.comment.entity.Comment;
import static org.assertj.core.api.Assertions.assertThat;



import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TodoRepository todoRepository;

    User user;
    Todo todo;

    @BeforeEach
    void setup() {
        user = userRepository.save(new User("test@example.com", "1234", UserRole.USER));
        todo = todoRepository.save(new Todo("제목", "내용", "맑음", user));
    }

    @Test
    void 길이제한_확인용_테스트() {
        // give : 500자 길이의 댓글 내용 준비
        String content = "a".repeat(500);
        // when : 댓글을 저장하면
        Comment comment = commentRepository.save(new Comment(content, user, todo));
        // then : 정상적으로 저장되는지 검증
        assertThat(comment.getContents().length()).isEqualTo(500);
    }

    @Test
    void 길이제한_500자로_리팩토링() {
        // give : 500자 또는 501자의 댓글 내용 준비
        String content = "a".repeat(501);
        // when : 댓글을 저장하면
        Comment comment = commentRepository.save(new Comment(content, user, todo));
        // then : 정상적으로 저장되는지 검증
        assertThat(comment.getContents().length()).isEqualTo(501);
    }
}

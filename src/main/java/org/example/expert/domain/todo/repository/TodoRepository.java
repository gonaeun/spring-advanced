package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // n+1 문제 해결 : fetch join -> @EntityGraph 방식
//    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    @EntityGraph(attributePaths = "user") // n+1를 방지하기 위해 연관 엔티티(user) 즉시 로딩
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

//    @Query("SELECT t FROM Todo t " +
//            "LEFT JOIN FETCH t.user " +
//            "WHERE t.id = :todoId")
    @EntityGraph(attributePaths = "user")
    Optional<Todo> findById(Long todoId);

    int countById(Long todoId);
}

package org.chudinova.sofia.repositories;

import org.chudinova.sofia.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}

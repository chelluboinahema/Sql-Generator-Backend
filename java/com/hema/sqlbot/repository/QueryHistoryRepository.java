package com.hema.sqlbot.repository;

import com.hema.sqlbot.modal.QueryHistory;
import com.hema.sqlbot.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QueryHistoryRepository
        extends JpaRepository<QueryHistory, Long> {

    List<QueryHistory> findByUser(User user);

    Optional<QueryHistory> findByIdAndUser(Long id, User user);
}
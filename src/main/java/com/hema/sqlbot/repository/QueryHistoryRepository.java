package com.hema.sqlbot.repository;


import com.hema.sqlbot.modal.QueryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryHistoryRepository
        extends JpaRepository<QueryHistory, Long> {
}
package com.hema.sqlbot.service;

import com.hema.sqlbot.modal.*;
import com.hema.sqlbot.repository.*;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SqlGeneratorService {

    private final OpenAIService openAIService;

    private final QueryHistoryRepository repository;

    private final UserRepository userRepository;

    public SqlGeneratorService(
            OpenAIService openAIService,
            QueryHistoryRepository repository,
            UserRepository userRepository
    ) {

        this.openAIService = openAIService;

        this.repository = repository;

        this.userRepository = userRepository;
    }

    public SqlResponse generateSql(
            SqlRequest request
    ) {

        String prompt =
                """
                Convert this into SQL.

                %s
                """
                        .formatted(
                                request.getInput()
                        );

        String sql =
                openAIService.generateResponse(
                        prompt
                );

        saveHistory(
                request.getInput(),
                sql
        );

        return new SqlResponse(sql);
    }

    public SqlResponse explainSql(
            SqlRequest request
    ) {

        String explanation =
                openAIService.generateResponse(
                        "Explain SQL:\n"
                                +
                                request.getInput()
                );

        saveHistory(
                request.getInput(),
                explanation
        );

        return new SqlResponse(explanation);
    }

    public SqlResponse optimizeSql(
            SqlRequest request
    ) {

        String output =
                openAIService.generateResponse(
                        "Optimize SQL:\n"
                                +
                                request.getInput()
                );

        saveHistory(
                request.getInput(),
                output
        );

        return new SqlResponse(output);
    }

    private void saveHistory(
            String input,
            String output
    ) {

        User user =
                getCurrentUser();

        QueryHistory history =
                new QueryHistory();

        history.setInputText(
                input
        );

        history.setGeneratedSql(
                output
        );

        history.setCreatedAt(
                LocalDateTime.now()
        );

        history.setUser(
                user
        );

        repository.save(
                history
        );
    }

    public List<QueryHistory> getMyHistory() {

        return repository.findByUser(
                getCurrentUser()
        );
    }

    public QueryHistory getHistory(
            Long id
    ) {

        return repository
                .findByIdAndUser(
                        id,
                        getCurrentUser()
                )

                .orElseThrow(
                        () ->
                                new RuntimeException(
                                        "History not found"
                                )
                );
    }

    public void deleteHistory(
            Long id
    ) {

        QueryHistory history =
                repository
                        .findByIdAndUser(
                                id,
                                getCurrentUser()
                        )

                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "History not found"
                                        )
                        );

        repository.delete(
                history
        );
    }

    public List<QueryHistory> getAllHistory() {

        return repository.findAll();
    }

    private User getCurrentUser() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return userRepository
                .findByEmail(
                        email
                )

                .orElseThrow();
    }

    public QueryHistory getAdminHistoryById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("History not found"));
    }

    public void deleteAdminHistory(Long id) {
        repository.deleteById(id);
    }
}
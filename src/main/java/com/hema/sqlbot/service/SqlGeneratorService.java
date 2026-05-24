package com.hema.sqlbot.service;

import com.hema.sqlbot.modal.QueryHistory;
import com.hema.sqlbot.modal.SqlRequest;
import com.hema.sqlbot.modal.SqlResponse;
import com.hema.sqlbot.repository.QueryHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service

public class SqlGeneratorService {

    @Autowired
    private final OpenAIService openAIService;
    @Autowired
    private final QueryHistoryRepository repository;

    public SqlGeneratorService(OpenAIService openAIService,
                               QueryHistoryRepository repository) {
        this.openAIService = openAIService;
        this.repository = repository;
    }

    // ---------------- GENERATE SQL ----------------
    public SqlResponse generateSql(SqlRequest request) {

        if (request == null || request.getInput() == null || request.getInput().trim().isEmpty()) {
            return null;
        }
        String userInput = request.getInput();

        String prompt = """
                Convert the following natural language request into SQL query.
                Return only SQL query.

                Request:
                %s
                """.formatted(userInput);

        String sql = openAIService.generateResponse(prompt);

        saveHistory(userInput, sql);

        return new SqlResponse(sql);
    }

    // ---------------- EXPLAIN SQL ----------------
    public SqlResponse explainSql(SqlRequest request) {
        if (request == null || request.getInput() == null || request.getInput().trim().isEmpty()) {
            return null;
        }
        String sqlQuery = request.getInput();

        String prompt = """
                Explain this SQL query in simple human-readable language.

                SQL:
                %s
                """.formatted(sqlQuery);

        String explanation = openAIService.generateResponse(prompt);

        saveHistory(sqlQuery, explanation);

        return new SqlResponse(explanation);
    }

    // ---------------- OPTIMIZE SQL ----------------
    public SqlResponse optimizeSql(SqlRequest request) {
        if (request == null || request.getInput() == null || request.getInput().trim().isEmpty()) {
            return null;
        }
        String sqlQuery = request.getInput();

        String prompt = """
                Optimize this SQL query.
                Suggest better query and explain performance improvements.

                SQL:
                %s
                """.formatted(sqlQuery);

        String optimized = openAIService.generateResponse(prompt);

        saveHistory(sqlQuery, optimized);

        return new SqlResponse(optimized);
    }

    // ---------------- SAVE HISTORY ----------------
    private void saveHistory(String input, String output) {

        QueryHistory history = new QueryHistory();
        history.setInputText(input);
        history.setGeneratedSql(output);
        history.setCreatedAt(LocalDateTime.now());

        repository.save(history);
    }

    // ---------------- GET ALL HISTORY ----------------
    public List<QueryHistory> getAllHistory() {
        return repository.findAll();
    }

    // ---------------- GET HISTORY BY ID ----------------
    public QueryHistory getHistoryById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("History not found"));
    }

    // ---------------- DELETE HISTORY ----------------
    public void deleteHistory(Long id) {
        repository.deleteById(id);
    }
}
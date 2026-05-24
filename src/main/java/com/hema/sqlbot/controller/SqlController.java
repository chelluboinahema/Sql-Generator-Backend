package com.hema.sqlbot.controller;

import com.hema.sqlbot.modal.QueryHistory;
import com.hema.sqlbot.modal.SqlRequest;
import com.hema.sqlbot.modal.SqlResponse;
import com.hema.sqlbot.service.SqlGeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sql")
@Tag(name = "SQL Generator API", description = "AI-powered API to generate, explain, optimize SQL queries and manage history")
public class SqlController {

    private final SqlGeneratorService sqlGeneratorService;

    public SqlController(SqlGeneratorService sqlGeneratorService) {
        this.sqlGeneratorService = sqlGeneratorService;
    }

    // ---------------- GENERATE SQL ----------------
    @PostMapping("/generate")
    @Operation(
            summary = "Generate SQL query",
            description = "Converts natural language input into SQL query using AI"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SQL generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> generateSql(@RequestBody SqlRequest request) {
        SqlResponse response = sqlGeneratorService.generateSql(request);
        return ResponseEntity.ok(response);
    }

    // ---------------- EXPLAIN SQL ----------------
    @PostMapping("/explain")
    @Operation(
            summary = "Explain SQL query",
            description = "Explains SQL query in human-readable format"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SQL explained successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<?> explainSql(@RequestBody SqlRequest request) {

        if (request == null || request.getInput() == null || request.getInput().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Input cannot be empty");
        }

        SqlResponse response = sqlGeneratorService.explainSql(request);
        return ResponseEntity.ok(response);
    }

    // ---------------- OPTIMIZE SQL ----------------
    @PostMapping("/optimize")
    @Operation(
            summary = "Optimize SQL query",
            description = "Optimizes SQL query and suggests improvements"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SQL optimized successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<?> optimizeSql(@RequestBody SqlRequest request) {

        if (request == null || request.getInput() == null || request.getInput().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Input cannot be empty");
        }

        SqlResponse response = sqlGeneratorService.optimizeSql(request);
        return ResponseEntity.ok(response);
    }

    // ---------------- GET ALL HISTORY ----------------
    @GetMapping("/history")
    @Operation(
            summary = "Get all query history",
            description = "Fetches all SQL generation history records"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "History fetched successfully")
    })
    public ResponseEntity<List<QueryHistory>> getAllHistory() {
        List<QueryHistory> history = sqlGeneratorService.getAllHistory();
        return ResponseEntity.ok(history);
    }

    // ---------------- GET HISTORY BY ID ----------------
    @GetMapping("/history/{id}")
    @Operation(
            summary = "Get query history by ID",
            description = "Fetches a specific query history record by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "History record found"),
            @ApiResponse(responseCode = "404", description = "History record not found")
    })
    public ResponseEntity<?> getHistoryById(@PathVariable Long id) {
        try {
            QueryHistory history = sqlGeneratorService.getHistoryById(id);
            return ResponseEntity.ok(history);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // ---------------- DELETE HISTORY ----------------
    @DeleteMapping("/history/{id}")
    @Operation(
            summary = "Delete query history",
            description = "Deletes a query history record by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "History deleted successfully"),
            @ApiResponse(responseCode = "404", description = "History record not found")
    })
    public ResponseEntity<?> deleteHistory(@PathVariable Long id) {
        try {
            sqlGeneratorService.deleteHistory(id);
            return ResponseEntity.ok("History deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("History not found");
        }
    }
}
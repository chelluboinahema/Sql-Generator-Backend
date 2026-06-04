package com.hema.sqlbot.controller;

import com.hema.sqlbot.modal.QueryHistory;
import com.hema.sqlbot.modal.SqlRequest;
import com.hema.sqlbot.modal.SqlResponse;
import com.hema.sqlbot.service.SqlGeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("v1/api/sql")
@Tag(
        name = "SQL Generator API",
        description = "AI-powered API to generate, explain, optimize SQL queries and manage user history"
)
public class SqlController {

    private final SqlGeneratorService sqlGeneratorService;

    public SqlController(SqlGeneratorService sqlGeneratorService) {
        this.sqlGeneratorService = sqlGeneratorService;
    }

    // ---------------- GENERATE SQL ----------------

    @PostMapping("/generate")
    @Operation(
            summary = "Generate SQL query",
            description = "Converts natural language into SQL using AI and stores history for logged-in user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SQL generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> generateSql( @Valid @RequestBody SqlRequest request) {

        if (request == null || request.getInput() == null || request.getInput().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Input cannot be empty");
        }

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
    public ResponseEntity<?> explainSql( @Valid @RequestBody SqlRequest request) {

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
            description = "Optimizes SQL query and suggests performance improvements"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SQL optimized successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<?> optimizeSql( @Valid @RequestBody SqlRequest request) {

        if (request == null || request.getInput() == null || request.getInput().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Input cannot be empty");
        }

        SqlResponse response = sqlGeneratorService.optimizeSql(request);

        return ResponseEntity.ok(response);
    }

    // ---------------- GET MY HISTORY ----------------

    @GetMapping("/history")
    @Operation(
            summary = "Get current user's history",
            description = "Returns only history belonging to logged-in user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "History fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<QueryHistory>> getHistory() {

        return ResponseEntity.ok(sqlGeneratorService.getMyHistory());
    }

    // ---------------- GET HISTORY BY ID ----------------

    @GetMapping("/history/{id}")
    @Operation(
            summary = "Get history by ID",
            description = "Returns selected history only if owned by logged-in user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "History fetched"),
            @ApiResponse(responseCode = "404", description = "History not found")
    })
    public ResponseEntity<?> getHistoryById(@PathVariable Long id) {

        QueryHistory history = sqlGeneratorService.getHistory(id);

        return ResponseEntity.ok(history);
    }

    // ---------------- DELETE HISTORY ----------------

    @DeleteMapping("/history/{id}")
    @Operation(
            summary = "Delete history",
            description = "Deletes only history belonging to current user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted successfully"),
            @ApiResponse(responseCode = "404", description = "History not found")
    })
    public ResponseEntity<String> deleteHistory(@PathVariable Long id) {

        sqlGeneratorService.deleteHistory(id);

        return ResponseEntity.ok("History deleted successfully");
    }
}

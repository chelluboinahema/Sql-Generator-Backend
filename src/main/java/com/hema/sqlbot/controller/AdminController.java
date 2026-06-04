package com.hema.sqlbot.controller;

import com.hema.sqlbot.modal.QueryHistory;
import com.hema.sqlbot.service.SqlGeneratorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("v1/admin")
@Tag(
        name = "Admin API",
        description = "APIs for administrators to manage all user histories"
)
public class AdminController {

    private final SqlGeneratorService sqlGeneratorService;

    public AdminController(SqlGeneratorService sqlGeneratorService) {
        this.sqlGeneratorService = sqlGeneratorService;
    }

    // ---------------- GET ALL HISTORY ----------------

    @GetMapping("/history")
    @Operation(
            summary = "Get all histories",
            description = "Returns all query histories for all users (ADMIN only)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histories fetched successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<List<QueryHistory>> getAllHistory() {

        return ResponseEntity.ok(
                sqlGeneratorService.getAllHistory()
        );
    }

    // ---------------- GET HISTORY BY ID ----------------

    @GetMapping("/history/{id}")
    @Operation(
            summary = "Get history by ID",
            description = "Returns any user's history using ID (ADMIN only)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "History fetched successfully"),
            @ApiResponse(responseCode = "404", description = "History not found")
    })
    public ResponseEntity<?> getHistoryById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                sqlGeneratorService.getAdminHistoryById(id)
        );
    }

    // ---------------- DELETE HISTORY ----------------

    @DeleteMapping("/history/{id}")
    @Operation(
            summary = "Delete history",
            description = "Deletes any user's history (ADMIN only)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "History deleted successfully"),
            @ApiResponse(responseCode = "404", description = "History not found")
    })
    public ResponseEntity<String> deleteHistory(
            @PathVariable Long id
    ) {

        sqlGeneratorService.deleteAdminHistory(id);

        return ResponseEntity.ok(
                "History deleted successfully"
        );
    }
}
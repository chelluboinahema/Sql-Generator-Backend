package com.hema.sqlbot.modal;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.validation.Valid;


@Schema(description = "Request object for SQL generation")
public class SqlRequest {
    @Valid
    @Parameter(description = "Natural language query request", required = true)
    private String input;

    public SqlRequest() {
    }
    public SqlRequest(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}

package com.hema.sqlbot.modal;

import jakarta.validation.constraints.NotBlank;

public class SqlRequest {

    @NotBlank(
            message =
                    "Input cannot be empty"
    )
    private String input;

    public SqlRequest() {
    }

    public SqlRequest(
            String input
    ) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(
            String input
    ) {
        this.input = input;
    }
}
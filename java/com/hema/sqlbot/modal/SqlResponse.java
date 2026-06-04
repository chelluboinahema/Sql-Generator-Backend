package com.hema.sqlbot.modal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;


@Schema(description = "Response object containing generated SQL")
public class SqlResponse {
    private String sql;

    public SqlResponse(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}

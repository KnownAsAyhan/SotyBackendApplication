package com.starsoftheyear.soty.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,          //  "Bad Request"
        String message,        // human-friendly detail
        String path,           // request pat h
        Map<String, String> validationErrors // field -> message (only for 400s)    ?
) {}

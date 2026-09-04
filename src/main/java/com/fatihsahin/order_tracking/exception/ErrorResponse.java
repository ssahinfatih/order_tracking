package com.fatihsahin.order_tracking.exception;

import java.util.List;

public record ErrorResponse(
        List<String> messages,
        int statusCode,
        String path,
        String timestamp
) {
}

package com.branas.clean_architecture.driver;

public record AccountResponse(
        String accountId,
        String email,
        String name
) {
}

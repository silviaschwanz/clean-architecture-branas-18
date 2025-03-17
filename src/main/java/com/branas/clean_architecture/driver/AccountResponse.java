package com.branas.clean_architecture.driver;

import java.util.UUID;

public record AccountResponse(
        UUID accountId,
        String email,
        String name
) {
}

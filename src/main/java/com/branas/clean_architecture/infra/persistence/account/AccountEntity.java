package com.branas.clean_architecture.infra.persistence.account;


import jakarta.persistence.*;

import java.util.UUID;

@MappedSuperclass
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID accountUuid;

    private String name;

    private String email;

    private String cpf;

}

package com.branas.clean_architecture.domain;

import java.util.UUID;

public class Account {

    private UUID accountId;
    private Name name;
    private Email email;
    private Cpf cpf;
    private Boolean isDriver;
    private CarPlate carPlate;
    private Password password;
    private String passwordAlgorithm;


    // Design Pattern - Facade
    // Domain Entity
    private Account(
            String name,
            String email,
            String cpf,
            String carPlate,
            Boolean isDriver,
            String password,
            String passwordAlgorithm
    ) {
        this.accountId = UUID.randomUUID();
        this.name = new Name(name);
        this.email = new Email(email);
        this.cpf = new Cpf(cpf);
        this.password = new Password(password);
        this.passwordAlgorithm = passwordAlgorithm;
        if(isDriver) this.carPlate = new CarPlate(carPlate);
        this.isDriver = isDriver;
    }

    private Account(
            String accountId,
            String name,
            String email,
            String cpf,
            String carPlate,
            Boolean isDriver
    ) {
        this.accountId = UUID.fromString(accountId);
        this.name = new Name(name);
        this.email = new Email(email);
        this.cpf = new Cpf(cpf);
        if(isDriver) this.carPlate = new CarPlate(carPlate);
        this.isDriver = isDriver;
    }

    // Static Factory Method
    public static Account create(
            String name,
            String email,
            String cpf,
            String carPlate,
            Boolean isDriver,
            String password,
            String passwordAlgorithm
    ) {
        return new Account(
                name,
                email,
                cpf,
                carPlate,
                isDriver,
                password,
                passwordAlgorithm
        );
    }

    public static Account restore(
            String accountId,
            String name,
            String email,
            String cpf,
            String carPlate,
            Boolean isDriver
    ) {
        return new Account(
                accountId,
                name,
                email,
                cpf,
                carPlate,
                isDriver
        );
    }

    public String getAccountId() {
        return accountId.toString();
    }

    public String getName() {
        return name.getValue();
    }

    public String getEmail() {
        return email.getValue();
    }

    public String getCpf() {
        return cpf.value();
    }

    public String getCarPlate() {
        return (isDriver) ? carPlate.getValue() : "";
    }

    public Boolean isDriver() {
        return isDriver;
    }

    public Boolean isPassenger() {
        return !isDriver;
    }

    public String getPassword() {
        return password.getValue();
    }

    public String getPasswordAlgorithm() {
        return passwordAlgorithm;
    }

}

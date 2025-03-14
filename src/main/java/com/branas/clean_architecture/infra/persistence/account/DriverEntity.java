package com.branas.clean_architecture.infra.persistence.account;

import jakarta.persistence.*;

@Entity
@Table(name = "driver")
public class DriverEntity extends AccountEntity {

    private String carPlate;

}

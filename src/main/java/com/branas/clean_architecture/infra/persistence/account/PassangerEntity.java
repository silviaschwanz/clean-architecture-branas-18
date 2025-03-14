package com.branas.clean_architecture.infra.persistence.account;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "passanger")
public class PassangerEntity extends AccountEntity{
    
}

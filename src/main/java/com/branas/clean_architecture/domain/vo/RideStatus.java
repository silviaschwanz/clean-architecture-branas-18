package com.branas.clean_architecture.domain.vo;

public interface RideStatus {

    String getValue();
    void request();
    void accept();
    void start();

}

package com.branas.clean_architecture.domain.vo;

public interface RideStatus {

    String getValue();
    RideStatus request();
    RideStatus accept();
    RideStatus start();

}

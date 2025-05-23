package com.branas.clean_architecture.domain.vo;

public interface RideStatus {

    String getValue();
    RideStatus accept();
    RideStatus start();
    RideStatus finish();

}

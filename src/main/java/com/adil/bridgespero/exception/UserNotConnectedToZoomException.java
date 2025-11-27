package com.adil.bridgespero.exception;

import com.adil.bridgespero.domain.model.enums.ErrorCode;

public class UserNotConnectedToZoomException extends BaseException {

    public UserNotConnectedToZoomException(String username) {
        super("User has not connected Zoom: " + username, ErrorCode.USER_NOT_CONNECTED_TO_ZOOM);
    }
}

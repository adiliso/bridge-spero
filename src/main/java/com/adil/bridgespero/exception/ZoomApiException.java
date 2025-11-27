package com.adil.bridgespero.exception;

import com.adil.bridgespero.domain.model.enums.ErrorCode;

public class ZoomApiException extends BaseException {

    public ZoomApiException(int statusCode, String responseBody) {
        super(String.format("Zoom API call failed. Status: %d. Response: %s",
                statusCode, responseBody), ErrorCode.ZOOM_API_CALL_FAILED);
    }
}
package com.adil.bridgespero.exception;

import com.adil.bridgespero.domain.model.enums.ErrorCode;

public class ZoomOAuthCallbackProcessingException extends BaseException {

    public ZoomOAuthCallbackProcessingException(int statusCode, String responseBody) {
        super(String.format("Zoom oauth callback processing error statusCode=%s responseBody=%s", statusCode, responseBody)
                , ErrorCode.ZOOM_CALLBACK_PROCESSING_ERROR);
    }
}

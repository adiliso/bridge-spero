package com.adil.bridgespero.exception;

import com.adil.bridgespero.domain.model.enums.ErrorCode;

public class ZoomOAuthTokenExchangeFailedException extends BaseException {

    public ZoomOAuthTokenExchangeFailedException(int statusCode, String responseBody) {
        super(String.format("Zoom OAuth token exchange failed. Status: %d. Response: %s",
                statusCode, responseBody), ErrorCode.ZOOM_OAUTH_TOKEN_EXCHANGE_FAILED);
    }
}

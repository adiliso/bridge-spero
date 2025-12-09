package com.adil.bridgespero.exception;

import com.adil.bridgespero.domain.model.enums.ErrorCode;

public class ZoomApiCommunicationException extends BaseException {

    public ZoomApiCommunicationException() {
        super("Zoom API GET failed! ", ErrorCode.ZOOM_API_CALL_FAILED);
    }
}

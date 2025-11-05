package com.adil.bridgespero.exception;

import static com.adil.bridgespero.domain.model.enums.ErrorCode.SCHEDULE_NOT_FOUND;

public class ScheduleNotFoundException extends BaseException {

    public ScheduleNotFoundException(Long id) {
        super("Schedule with ID " + id + " not found", SCHEDULE_NOT_FOUND);
    }
}

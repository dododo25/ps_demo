package com.proxyseller.demo.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
class EntityNullPointerException extends NullPointerException {

    EntityNullPointerException(String cause) {
        super(cause)
    }
}

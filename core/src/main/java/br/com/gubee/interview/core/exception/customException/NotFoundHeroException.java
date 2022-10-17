package br.com.gubee.interview.core.exception.customException;

import java.util.UUID;

public class NotFoundHeroException extends RuntimeException {

    public NotFoundHeroException(UUID id) {
        super(String.format("Not found hero with id = %s", id.toString()));
    }
}

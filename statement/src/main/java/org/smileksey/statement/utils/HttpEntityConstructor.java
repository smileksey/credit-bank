package org.smileksey.statement.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/** Util class for request entities construction */
public class HttpEntityConstructor {

    /**
     * Util method for HttpEntity building
     * @param body - object to be placed as the request body
     * @return filled HttpEntity object
     */
    public static HttpEntity<Object> createHttpEntity(Object body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(body ,httpHeaders);
    }
}

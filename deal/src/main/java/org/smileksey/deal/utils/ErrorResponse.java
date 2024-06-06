package org.smileksey.deal.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/** Placed in the response body in case of an error */
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private String message;
}

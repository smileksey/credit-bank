package org.smileksey.deal.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/** Object of this class is placed in the response body in case of an error */
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private String message;
}

package org.smileksey.calculator.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailMessage {
    private String address;
    private Theme theme;
    private long statementId;
}

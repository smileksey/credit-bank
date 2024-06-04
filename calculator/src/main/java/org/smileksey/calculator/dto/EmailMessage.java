package org.smileksey.calculator.dto;

import lombok.Getter;
import lombok.Setter;
import org.smileksey.calculator.dto.enums.Theme;

@Getter
@Setter
public class EmailMessage {
    private String address;
    private Theme theme;
    private Long statementId;
}

package org.smileksey.deal.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SESCodeDto {

    @NotBlank(message = "'sesCode' is not specified")
    private String sesCode;
}

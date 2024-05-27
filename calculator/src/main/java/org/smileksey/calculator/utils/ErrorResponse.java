package org.smileksey.calculator.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/** Класс объекта, который возвращается клиенту в теле ответа в случае ошибки */
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private String message;
}

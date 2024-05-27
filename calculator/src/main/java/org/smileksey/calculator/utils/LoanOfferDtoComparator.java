package org.smileksey.calculator.utils;

import org.smileksey.calculator.dto.LoanOfferDto;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class LoanOfferDtoComparator implements Comparator<LoanOfferDto> {

    @Override
    public int compare(LoanOfferDto o1, LoanOfferDto o2) {
        return o2.getRate().compareTo(o1.getRate());
    }
}

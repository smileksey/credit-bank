package org.smileksey.calculator.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smileksey.calculator.calculators.CreditParamsCalculator;
import org.smileksey.calculator.dto.CreditDto;
import org.smileksey.calculator.dto.EmploymentDto;
import org.smileksey.calculator.dto.ScoringDataDto;
import org.smileksey.calculator.dto.enums.EmploymentStatus;
import org.smileksey.calculator.dto.enums.Gender;
import org.smileksey.calculator.dto.enums.MaritalStatus;
import org.smileksey.calculator.dto.enums.Position;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditServiceImplTest {

    @Mock
    private CreditParamsCalculator creditParamsCalculator;

    @InjectMocks
    private CreditServiceImpl creditServiceImpl;

    ScoringDataDto scoringDataDto;

    BigDecimal baseRate;


    @BeforeEach
    void setUp() {
        String stringBaseRate = "21.00";
        ReflectionTestUtils.setField(creditServiceImpl, "stringBaseRate", stringBaseRate);
        baseRate = new BigDecimal(stringBaseRate);

        scoringDataDto = new ScoringDataDto();

        EmploymentDto employmentDto = new EmploymentDto();
        employmentDto.setEmploymentStatus(EmploymentStatus.EMPLOYED);
        employmentDto.setSalary(new BigDecimal("100000.00"));
        employmentDto.setPosition(Position.EMPLOYEE);
        employmentDto.setWorkExperienceTotal(50);
        employmentDto.setWorkExperienceCurrent(12);

        scoringDataDto.setAmount(new BigDecimal("1000000"));
        scoringDataDto.setTerm(12);
        scoringDataDto.setGender(Gender.MALE);
        scoringDataDto.setBirthdate(LocalDate.of(2004, 1, 1));
        scoringDataDto.setMaritalStatus(MaritalStatus.NOT_MARRIED);
        scoringDataDto.setIsInsuranceEnabled(false);
        scoringDataDto.setIsSalaryClient(false);
        scoringDataDto.setEmployment(employmentDto);
    }


    @Test
    void getCreditDtoAgeLessThan18ShouldReturnZero() {

        scoringDataDto.setBirthdate(LocalDate.of(2020, 1, 1));

        assertEquals(Optional.empty(), creditServiceImpl.getCreditDto(scoringDataDto));
    }

    @Test
    void getCreditDtoAgeMoreThan65ShouldReturnZero() {

        scoringDataDto.setBirthdate(LocalDate.of(1950, 1, 1));

        assertEquals(Optional.empty(), creditServiceImpl.getCreditDto(scoringDataDto));
    }

    @Test
    void getCreditDtoTotalExpLessThan18ShouldReturnZero() {

        scoringDataDto.getEmployment().setWorkExperienceTotal(17);

        assertEquals(Optional.empty(), creditServiceImpl.getCreditDto(scoringDataDto));
    }

    @Test
    void getCreditDtoCurrentExpLessThan3ShouldReturnZero() {

        scoringDataDto.getEmployment().setWorkExperienceCurrent(2);

        assertEquals(Optional.empty(), creditServiceImpl.getCreditDto(scoringDataDto));
    }


    @Test
    void getCreditDtoAmountExceed25SalariesShouldReturnZero() {

        scoringDataDto.getEmployment().setSalary(new BigDecimal("30000"));

        assertEquals(Optional.empty(), creditServiceImpl.getCreditDto(scoringDataDto));
    }

    @Test
    void getCreditDtoUnemployedShouldReturnZero() {

        scoringDataDto.getEmployment().setEmploymentStatus(EmploymentStatus.UNEMPLOYED);

        assertEquals(Optional.empty(), creditServiceImpl.getCreditDto(scoringDataDto));
    }

    @Test
    void getCreditDtoSelfEmployedShouldIncrRateBy2() {

        scoringDataDto.getEmployment().setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED);

        when(creditParamsCalculator.calculateRate(any(), any(), any())).thenReturn(baseRate);

        assertEquals(baseRate.add(new BigDecimal("2")), creditServiceImpl.getCreditDto(scoringDataDto).get().getRate());
    }

    @Test
    void getCreditDtoBusinessOwnerShouldIncrRateBy3() {

        scoringDataDto.getEmployment().setEmploymentStatus(EmploymentStatus.BUSINESS_OWNER);

        when(creditParamsCalculator.calculateRate(any(), any(), any())).thenReturn(baseRate);

        assertEquals(baseRate.add(new BigDecimal("3")), creditServiceImpl.getCreditDto(scoringDataDto).get().getRate());
    }

    @Test
    void getCreditDtoMiddleManagerShouldReduceRateBy1() {

        scoringDataDto.getEmployment().setPosition(Position.MIDDLE_MANAGER);

        when(creditParamsCalculator.calculateRate(any(), any(), any())).thenReturn(baseRate);

        assertEquals(baseRate.subtract(new BigDecimal("1")), creditServiceImpl.getCreditDto(scoringDataDto).get().getRate());
    }

    @Test
    void getCreditDtoTopManagerShouldReduceRateBy2() {

        scoringDataDto.getEmployment().setPosition(Position.TOP_MANAGER);

        when(creditParamsCalculator.calculateRate(any(), any(), any())).thenReturn(baseRate);

        assertEquals(baseRate.subtract(new BigDecimal("2")), creditServiceImpl.getCreditDto(scoringDataDto).get().getRate());
    }

    @Test
    void getCreditDtoMarriedShouldReduceRateBy3() {

        scoringDataDto.setMaritalStatus(MaritalStatus.MARRIED);

        when(creditParamsCalculator.calculateRate(any(), any(), any())).thenReturn(baseRate);

        assertEquals(baseRate.subtract(new BigDecimal("3")), creditServiceImpl.getCreditDto(scoringDataDto).get().getRate());
    }

    @Test
    void getCreditDtoFemale40ShouldReduceRateBy2() {

        scoringDataDto.setGender(Gender.FEMALE);
        scoringDataDto.setBirthdate(LocalDate.of(1984, 1, 1));

        when(creditParamsCalculator.calculateRate(any(), any(), any())).thenReturn(baseRate);

        assertEquals(baseRate.subtract(new BigDecimal("2")), creditServiceImpl.getCreditDto(scoringDataDto).get().getRate());
    }

    @Test
    void getCreditDtoMale40ShouldReduceRateBy2() {

        scoringDataDto.setGender(Gender.MALE);
        scoringDataDto.setBirthdate(LocalDate.of(1984, 1, 1));

        when(creditParamsCalculator.calculateRate(any(), any(), any())).thenReturn(baseRate);

        assertEquals(baseRate.subtract(new BigDecimal("2")), creditServiceImpl.getCreditDto(scoringDataDto).get().getRate());
    }

    @Test
    void getCreditDtoNonBinaryShouldIncrRateBy8() {

        scoringDataDto.setGender(Gender.NON_BINARY);

        when(creditParamsCalculator.calculateRate(any(), any(), any())).thenReturn(baseRate);

        assertEquals(baseRate.add(new BigDecimal("8")), creditServiceImpl.getCreditDto(scoringDataDto).get().getRate());
    }

    @Test
    void getCreditDtoRateLessThanMinShouldSetRate9() {

        when(creditParamsCalculator.calculateRate(any(), any(), any())).thenReturn(new BigDecimal("-10"));

        assertEquals(new BigDecimal("9.00"), creditServiceImpl.getCreditDto(scoringDataDto).get().getRate());
    }

    @Test
    void getCreditDtoShouldReturnNonEmpty() {

        when(creditParamsCalculator.calculateRate(any(), any(), any())).thenReturn(baseRate);

        Optional<CreditDto> creditDtoOptional = creditServiceImpl.getCreditDto(scoringDataDto);

        assertTrue(creditDtoOptional.isPresent());

    }

    @Test
    void getCreditDtoShouldReturnFilledObject() {

        when(creditParamsCalculator.calculateRate(any(), any(), any())).thenReturn(baseRate);
        when(creditParamsCalculator.calculateInsurancePrice(any(), any(), any())).thenReturn(new BigDecimal("50000"));
        when(creditParamsCalculator.calculateMonthlyPayment(any(), any(), any(), any())).thenReturn(new BigDecimal("35000"));
        when(creditParamsCalculator.calculateTotalAmount(any(), any())).thenReturn(new BigDecimal("1150000"));
        when(creditParamsCalculator.calculatePSK(any(), any(), any())).thenReturn(new BigDecimal("12"));
        when(creditParamsCalculator.getPaymentSchedule(any(), any(), any(), any())).thenReturn(new ArrayList<>());

        CreditDto creditDto = creditServiceImpl.getCreditDto(scoringDataDto).get();

        assertNotNull(creditDto.getAmount());
        assertNotEquals(0, creditDto.getTerm());
        assertNotNull(creditDto.getMonthlyPayment());
        assertNotNull(creditDto.getRate());
        assertNotNull(creditDto.getPsk());
        assertEquals(scoringDataDto.getIsInsuranceEnabled(), creditDto.getIsInsuranceEnabled());
        assertEquals(scoringDataDto.getIsSalaryClient(), creditDto.getIsSalaryClient());
        assertNotNull(creditDto.getPaymentSchedule());
    }






}
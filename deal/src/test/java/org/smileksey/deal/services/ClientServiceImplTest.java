package org.smileksey.deal.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smileksey.deal.dto.LoanStatementRequestDto;
import org.smileksey.deal.exceptions.ClientAlreadyExistsException;
import org.smileksey.deal.models.Client;
import org.smileksey.deal.models.Passport;
import org.smileksey.deal.repositories.ClientRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientServiceImpl;


    @Test
    void createAndSaveClientShouldThrowExceptionWhenEmailAlreadyExists() {

        when(clientRepository.findFirstByEmail(any())).thenReturn(Optional.of(new Client()));

        assertThrows(ClientAlreadyExistsException.class, () -> clientServiceImpl.createAndSaveClient(new LoanStatementRequestDto()));
    }

    @Test
    void createAndSaveClientShouldThrowExceptionWhenPassportAlreadyExists() {

        when(clientRepository.findFirstByPassportSeriesAndNumber(any(), any())).thenReturn(Optional.of(new Client()));

        assertThrows(ClientAlreadyExistsException.class, () -> clientServiceImpl.createAndSaveClient(new LoanStatementRequestDto()));
    }

    @Test
    void createAndSaveClientShouldReturnFilledObject() {

        LoanStatementRequestDto loanStatementRequestDto = createLoanStatementRequestDto();
        Client mockClient = createClient(loanStatementRequestDto);

        when(clientRepository.findFirstByEmail(any())).thenReturn(Optional.empty());
        when(clientRepository.findFirstByPassportSeriesAndNumber(any(), any())).thenReturn(Optional.empty());
        when(clientRepository.save(any())).thenReturn(mockClient);

        Client savedClient = clientServiceImpl.createAndSaveClient(loanStatementRequestDto);

        assertNotNull(savedClient);

        verify(clientRepository, times(1)).save(any());

        assertEquals(mockClient.getFirstName(), savedClient.getFirstName());
        assertEquals(mockClient.getLastName(), savedClient.getLastName());
        assertEquals(mockClient.getMiddleName(), savedClient.getMiddleName());
        assertEquals(mockClient.getEmail(), savedClient.getEmail());
        assertEquals(mockClient.getBirthDate(), savedClient.getBirthDate());
        assertEquals(mockClient.getPassport().getSeries(), savedClient.getPassport().getSeries());
        assertEquals(mockClient.getPassport().getNumber(), savedClient.getPassport().getNumber());
    }


    private LoanStatementRequestDto createLoanStatementRequestDto() {
        return LoanStatementRequestDto.builder()
                .term(12)
                .firstName("Ivan")
                .lastName("Ivanov")
                .middleName("Ivanovich")
                .email("ivanov@gmail.com")
                .birthdate(LocalDate.of(1990, 1, 1))
                .passportSeries("1111")
                .passportNumber("222222")
                .build();
    }


    private Client createClient(LoanStatementRequestDto loanStatementRequestDto) {
        return Client.builder()
                .lastName(loanStatementRequestDto.getLastName())
                .firstName(loanStatementRequestDto.getFirstName())
                .middleName(loanStatementRequestDto.getMiddleName())
                .birthDate(loanStatementRequestDto.getBirthdate())
                .email(loanStatementRequestDto.getEmail())
                .passport(Passport.builder()
                        .series(loanStatementRequestDto.getPassportSeries())
                        .number(loanStatementRequestDto.getPassportNumber())
                        .build())
                .build();
    }
}
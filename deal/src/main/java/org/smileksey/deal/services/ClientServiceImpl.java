package org.smileksey.deal.services;

import lombok.RequiredArgsConstructor;
import org.smileksey.deal.dto.LoanStatementRequestDto;
import org.smileksey.deal.models.Client;
import org.smileksey.deal.models.Passport;
import org.smileksey.deal.repositories.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClientServiceImpl {

    private final ClientRepository clientRepository;


    @Transactional
    public Client createAndSaveClient(LoanStatementRequestDto loanStatementRequestDto) {

        Client client = Client.builder()
                .clientId(UUID.randomUUID())
                .lastName(loanStatementRequestDto.getLastName())
                .firstName(loanStatementRequestDto.getFirstName())
                .middleName(loanStatementRequestDto.getMiddleName())
                .birthDate(loanStatementRequestDto.getBirthdate())
                .email(loanStatementRequestDto.getEmail())
                .passport(Passport.builder()
                        .passportId(UUID.randomUUID())
                        .series(loanStatementRequestDto.getPassportSeries())
                        .number(loanStatementRequestDto.getPassportNumber())
                        .build())
                .build();

        return clientRepository.save(client);
    }
}

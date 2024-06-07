package org.smileksey.deal.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.deal.dto.LoanStatementRequestDto;
import org.smileksey.deal.models.Client;
import org.smileksey.deal.models.Passport;
import org.smileksey.deal.repositories.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;


    @Transactional
    @Override
    public Client createAndSaveClient(LoanStatementRequestDto loanStatementRequestDto) {

        //TODO разобраться, что делать, если клиент уже есть в базе
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

        log.info("Created client: {}", client);

        return clientRepository.save(client);
    }

    private Optional<Client> findClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }
}

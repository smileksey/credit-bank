package org.smileksey.deal.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.deal.dto.LoanStatementRequestDto;
import org.smileksey.deal.exceptions.ClientAlreadyExistsException;
import org.smileksey.deal.models.Client;
import org.smileksey.deal.models.Passport;
import org.smileksey.deal.repositories.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;


    /**
     * Method creates a new Client entity and saves it to the database
     * @param loanStatementRequestDto - input data from client
     * @return Client entity saved to the database
     */
    @Transactional
    @Override
    public Client createAndSaveClient(LoanStatementRequestDto loanStatementRequestDto) {

        checkIfClientAlreadyExists(loanStatementRequestDto);

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


    /**
     * Method checks if a Client entity with same email or passport data was already saved to the database
     * @param loanStatementRequestDto - input data from client
     */
    private void checkIfClientAlreadyExists(LoanStatementRequestDto loanStatementRequestDto) {

        if (clientRepository.findFirstByEmail(loanStatementRequestDto.getEmail()).isPresent()) {
            throw new ClientAlreadyExistsException("Client with given email already exists");
        }

        if (clientRepository.findFirstByPassportSeriesAndNumber(loanStatementRequestDto.getPassportSeries(), loanStatementRequestDto.getPassportNumber()).isPresent()) {
            throw new ClientAlreadyExistsException("Client with given passport series and number already exists");
        }
    }

}

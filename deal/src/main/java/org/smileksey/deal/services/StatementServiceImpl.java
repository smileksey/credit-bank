package org.smileksey.deal.services;

import lombok.RequiredArgsConstructor;
import org.smileksey.deal.models.Client;
import org.smileksey.deal.models.Statement;
import org.smileksey.deal.repositories.StatementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatementServiceImpl {

    private final StatementRepository statementRepository;

    @Transactional
    public Statement createAndSaveStatement(Client client) {

        Statement statement = Statement.builder()
                .statementId(UUID.randomUUID())
                .client(client)
                .creationDate(LocalDate.now())
                .build();

        return statementRepository.save(statement);
    }

}

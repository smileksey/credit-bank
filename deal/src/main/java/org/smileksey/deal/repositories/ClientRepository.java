package org.smileksey.deal.repositories;

import org.smileksey.deal.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    Optional<Client> findFirstByEmail(String email);

    @Query(value = "SELECT * FROM client WHERE passport_id ->> 'series' = ?1 AND passport_id ->> 'number' = ?2 LIMIT 1", nativeQuery = true)
    Optional<Client> findFirstByPassportSeriesAndNumber(String series, String number);
}

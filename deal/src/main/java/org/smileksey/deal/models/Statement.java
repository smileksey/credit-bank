package org.smileksey.deal.models;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.smileksey.deal.dto.LoanOfferDto;
import org.smileksey.deal.dto.enums.ApplicationStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "statement")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Statement {

    @Id
    @GeneratedValue
    @Column(name = "statement_id")
    private UUID statementId;

    @OneToOne
    @JoinColumn(name = "client_id")
    @ToString.Exclude
    private Client client;

    @OneToOne
    @JoinColumn(name = "credit_id")
    @ToString.Exclude
    private Credit credit;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "creation_date")
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Column(name = "applied_offer", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private LoanOfferDto appliedOffer;

    @Column(name = "sign_date")
    @CreationTimestamp
    private LocalDateTime signDate;

    @Column(name = "ses_code")
    private String sesCode;

    @Column(name = "status_history", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private List<StatusHistory> statusHistory;
}

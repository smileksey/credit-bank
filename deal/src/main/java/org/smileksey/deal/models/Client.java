package org.smileksey.deal.models;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.smileksey.deal.dto.enums.Gender;
import org.smileksey.deal.dto.enums.MaritalStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "client")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Client {

    @Id
    @Column(name = "client_id")
    private UUID clientId;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private Gender gender;

    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    @Column(name = "dependent_amount")
    private Integer dependentAmount;

    @Column(name = "passport_id")
    @Type(type = "jsonb")
    private Passport passport;

    @Column(name = "employment_id")
    @Type(type = "jsonb")
    private Employment employment;

    @Column(name = "account_number")
    private String accountNumber;

    @OneToOne(mappedBy = "client")
    private Statement statement;



}

package be.genesis.persistence;

import be.genesis.dto.Contact;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"companyToContacts"})

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"vatNr"}))
// TODO not entirely sure that constraint works since two vatNr can be null: read doc / test it
public class ContactEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String address; // make it easy for now
    @Pattern(regexp = "^\\d{10}$")
    private String vatNr;
    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY)
    @Builder.Default
    @Valid
    private Set<CompanyToContact> companyToContacts = new HashSet<>();

    public static ContactEntity from(Contact contact) {
        return ContactEntity.builder()
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .address(contact.getAddress())
                .vatNr(contact.getVatNr())
                .build();
    }
}

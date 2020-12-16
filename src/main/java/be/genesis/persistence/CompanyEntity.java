package be.genesis.persistence;

import be.genesis.dto.Company;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"companyToContacts", "addresses"})

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"vatNr"}))
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    private String vatNr;
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<CompanyToContact> companyToContacts = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id", nullable = false)
    @Builder.Default
    private Set<CompanyAddressEntity> addresses = new HashSet<>();

    public static CompanyEntity from(Company company) {
        return CompanyEntity.builder()
                .vatNr(company.getVatNr())
                .addresses(company.getAddresses().stream().map(CompanyAddressEntity::from).collect(Collectors.toSet()))
                .build();
    }

    public void addAddress(CompanyAddressEntity extraAddress) {
        addresses.add(extraAddress);
    }

    public void addAddresses(Set<CompanyAddressEntity> extraAddresses) {
        addresses.addAll(extraAddresses);
    }
}

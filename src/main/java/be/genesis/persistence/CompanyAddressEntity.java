package be.genesis.persistence;

import be.genesis.dto.CompanyAddress;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class CompanyAddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    private boolean hq;
    private String address; // make it easy for now

    public static CompanyAddressEntity from(CompanyAddress companyAddress) {
        return CompanyAddressEntity.builder()
                .hq(companyAddress.isHq())
                .address(companyAddress.getAddress())
                .build();
    }
}

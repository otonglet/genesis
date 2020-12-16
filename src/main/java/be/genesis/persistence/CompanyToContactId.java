package be.genesis.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Embeddable
public class CompanyToContactId implements Serializable {
    private Long embeddedCompanyId;
    private Long embeddedContactId;
}

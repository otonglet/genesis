package be.genesis.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class CompanyToContact {
    @EmbeddedId
    private CompanyToContactId id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CompanyEntity company;

    @ManyToOne(fetch = FetchType.LAZY)
    private ContactEntity contact;
}

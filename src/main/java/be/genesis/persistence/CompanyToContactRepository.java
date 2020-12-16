package be.genesis.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyToContactRepository extends JpaRepository<CompanyToContact, CompanyToContactId> {
    List<CompanyToContact> findByIdEmbeddedCompanyId(Long id);

    List<CompanyToContact> findByIdEmbeddedContactId(Long id);
}

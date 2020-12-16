package be.genesis.service;

import be.genesis.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CompanyToContactService {
    @Autowired
    private CompanyToContactRepository companyToContactRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ContactRepository contactRepository;

    public void create(CompanyEntity companyEntity, ContactEntity contactEntity) {
        if (companyToContactRepository.findById(new CompanyToContactId(companyEntity.getId(), contactEntity.getId())).isPresent())
            return;

        CompanyToContact companyToContact = CompanyToContact.builder()
                .id(new CompanyToContactId(companyEntity.getId(), contactEntity.getId()))
                .company(companyEntity)
                .contact(contactEntity)
                .build();
        companyToContactRepository.save(companyToContact);
    }

    public void deleteCompanyOrphans(Long companyEntityId, Set<ContactEntity> remainingContactEntities) {
        List<CompanyToContact> companyToContacts = companyToContactRepository.findByIdEmbeddedCompanyId(companyEntityId);
        Set<CompanyToContact> companyToContactToRemove = companyToContacts.stream()
                .filter(companyToContact -> !remainingContactEntities.contains(companyToContact.getContact()))
                .collect(Collectors.toSet());

        companyToContactToRemove.forEach(companyToContact -> {
            if (companyToContactRepository.findByIdEmbeddedContactId(companyToContact.getContact().getId()).size() == 1)
                contactRepository.delete(companyToContact.getContact()); // delete orphans since contact MUST be related to AT LEAST ONE company (per the spec)
            companyToContactRepository.delete(companyToContact);
        });
    }

    public void deleteContactOrphans(Long contactEntityId, Set<CompanyEntity> remainingCompanyEntities) {
        List<CompanyToContact> companyToContacts = companyToContactRepository.findByIdEmbeddedContactId(contactEntityId);
        Set<CompanyToContact> companyToContactToRemove = companyToContacts.stream()
                .filter(companyToContact -> !remainingCompanyEntities.contains(companyToContact.getCompany()))
                .collect(Collectors.toSet());

        companyToContactToRemove.forEach(companyToContact -> companyToContactRepository.delete(companyToContact));
    }
}

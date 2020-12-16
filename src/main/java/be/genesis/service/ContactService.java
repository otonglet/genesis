package be.genesis.service;

import be.genesis.dto.Contact;
import be.genesis.persistence.CompanyEntity;
import be.genesis.persistence.CompanyRepository;
import be.genesis.persistence.ContactEntity;
import be.genesis.persistence.ContactRepository;
import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyToContactService companyToContactService;

    public long create(Contact contact) {
        ContactEntity contactEntity = ContactEntity.from(contact);
        contactRepository.save(contactEntity);

        Set<CompanyEntity> companyEntities = getOrCreateCompanyEntities(contact);
        companyEntities.forEach(companyEntity -> companyToContactService.create(companyEntity, contactEntity));

        return contactEntity.getId();
    }

    private Set<CompanyEntity> getOrCreateCompanyEntities(Contact contact) {
        return contact.getCompanies().stream().map(company ->
        {
            CompanyEntity companyEntity;
            Optional<CompanyEntity> companyOpt = companyRepository.findByVatNr(company.getVatNr());
            if (companyOpt.isPresent())
                companyEntity = companyOpt.get();
            else {
                companyEntity = CompanyEntity.from(company);
                companyRepository.save(companyEntity);
            }
            return companyEntity;
        }).collect(Collectors.toSet());
    }

    private ContactEntity getContact(Long id) {
        Optional<ContactEntity> contactOpt = contactRepository.findById(id);
        if (!contactOpt.isPresent())
            throw new NoSuchElementException();
        return contactOpt.get();
    }

    public void update(Long id, Contact contact) {
        ContactEntity contactEntity = getContact(id);
        contactEntity.setFirstName(contact.getFirstName());
        contactEntity.setLastName(contact.getLastName());
        contactEntity.setAddress(contact.getAddress());
        contactEntity.setVatNr(contact.getVatNr());
        contactRepository.save(contactEntity);

        Set<CompanyEntity> companyEntities = getOrCreateCompanyEntities(contact);
        companyEntities.forEach(companyEntity -> companyToContactService.create(companyEntity, contactEntity));
        companyToContactService.deleteContactOrphans(contactEntity.getId(), companyEntities);
    }

    public void delete(Long id) {
        ContactEntity contactEntity = getContact(id);

        companyToContactService.deleteContactOrphans(contactEntity.getId(), Sets.newHashSet());

        contactRepository.delete(contactEntity);
    }
}

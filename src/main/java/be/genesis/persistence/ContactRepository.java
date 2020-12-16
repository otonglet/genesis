package be.genesis.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
    // TODO won't support homonyms in same household; would need some other functional identifier like the contact's birth date
    Optional<ContactEntity> findByFirstNameAndLastNameAndAddress(String firstName, String lastName, String address);
}

package backend.person.children.legal_person;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegalPersonRepository extends CrudRepository<LegalPerson, Long>  {

}

package backend.person.children.natural_person;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NaturalPersonRepository extends CrudRepository<NaturalPerson, Long>  {

}

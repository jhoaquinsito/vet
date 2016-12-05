package backend.person;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
	@Query("select case when count(p) > 0 then true else false end from Person p where lower(p.iName) = lower(?1) and active = true")
	boolean existsActiveByName(String name);
}

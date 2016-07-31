package backend.report.person_balance;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonBalanceRepository extends CrudRepository<PersonBalance, Long>{
	Iterable<PersonBalance> findAll(Sort sort);
}

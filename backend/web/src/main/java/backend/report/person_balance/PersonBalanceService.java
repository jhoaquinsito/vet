package backend.report.person_balance;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonBalanceService {
	@Autowired private PersonBalanceRepository iPersonBalanceRepository;

	/**
	 * Constructor.
	 */
	public PersonBalanceService() {
		super();
	}

	public Iterable<PersonBalance> getAll() {
		return this.iPersonBalanceRepository.findAll(this.getAllSort());
	}

	private Sort getAllSort(){
		String sortByField = "iDebtTotal";
		return new Sort(Direction.DESC, sortByField);
	}

}
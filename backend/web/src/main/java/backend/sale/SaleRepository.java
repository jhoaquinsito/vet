package backend.sale;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * La interfaz <code>SaleRepository</code> es la definición del repositorio
 * para entidades <strong>Sale</strong>. Los repositorios son responsables de
 * proveer y implementar consultas así como recibir y persistir sobre entidades.
 */
@Repository
public interface SaleRepository extends CrudRepository<Sale, Long>{
	Iterable<Sale> findAll(Sort sort);
}

package backend.saleline;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * La interfaz <code>SaleLineRepository</code> es la definición del repositorio
 * para entidades <strong>SaleLine</strong>. Los repositorios son responsables de
 * proveer y implementar consultas así como recibir y persistir sobre entidades.
 */
@Repository
public interface SaleLineRepository extends CrudRepository<SaleLine, Long>{

}

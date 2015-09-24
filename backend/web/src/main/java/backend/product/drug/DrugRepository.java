package backend.product.drug;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * La interfaz <code>DrugRepository</code> es la definición del repositorio
 * para entidades <strong>Drug</strong>. Los repositorios son responsables de
 * proveer y implementar consultas así como recibir y persistir sobre entidades.
 */
@Repository
public interface DrugRepository extends CrudRepository<Drug, Long> {

}

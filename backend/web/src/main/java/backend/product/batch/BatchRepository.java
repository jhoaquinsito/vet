package backend.product.batch;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * La interfaz <code>BatchRepository</code> es la definición del repositorio
 * para entidades <strong>Batch</strong>. Los repositorios son responsables de
 * proveer y implementar consultas así como recibir y persistir sobre entidades.
 */
@Repository
public interface BatchRepository extends CrudRepository<Batch, Long> {

}

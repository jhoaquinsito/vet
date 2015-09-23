package backend.product.presentation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * La interfaz <code>PresentationRepository</code> es la definición del repositorio
 * para entidades <strong>Presentation</strong>. Los repositorios son responsables de
 * proveer y implementar consultas así como recibir y persistir sobre entidades.
 */
@Repository
public interface PresentationRepository extends CrudRepository<Presentation, Long> {

}

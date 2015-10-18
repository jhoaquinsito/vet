package backend.product.measure_unit;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import backend.product.measure_unit.MeasureUnit;

/**
 * La interfaz <code>MeasureUnitRepository</code> es la definición del repositorio
 * para entidades <strong>MeasureUnit</strong>. Los repositorios son responsables de
 * proveer y implementar consultas así como recibir y persistir sobre entidades.
 */
@Repository
public interface MeasureUnitRepository extends CrudRepository<MeasureUnit, Long> {

}

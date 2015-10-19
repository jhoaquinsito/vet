package backend.product.measure_unit;

import org.springframework.data.domain.Sort;
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

	/***
	 * Método extendido de findAll del CrudRepository.
	 * Obtiene la lista de <strong>MeasureUnitRepository</strong> pero ordenadas bajo el objeto <strong>Sort</strong>
	 * pasado como parámetro.
	 * El ordenamiento es realizado mediante SQL
	 * @param sort:
	 * 				Objeto de ordenamiento que especifica la <strong>Direction</strong> y los campos por 
	 * 				el cual se hace el ordenamiento.
	 * @return
	 * 			Lista de <strong>MeasureUnitRepository</strong> ordenada por el objeto <strong>Sort</strong>
	 */
	Iterable<MeasureUnit> findAll(Sort sort);
	
}

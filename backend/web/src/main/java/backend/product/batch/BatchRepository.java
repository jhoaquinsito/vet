package backend.product.batch;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * La interfaz <code>BatchRepository</code> es la definición del repositorio
 * para entidades <strong>Batch</strong>. Los repositorios son responsables de
 * proveer y implementar consultas así como recibir y persistir sobre entidades.
 * @author ggorosito
 */
@Repository
public interface  BatchRepository extends CrudRepository<Batch, Long> {
	/***
	 * Método extendido de findAll del CrudRepository.
	 * Obtiene la lista de <strong>Batch</strong> pero ordenadas bajo el objeto <strong>Sort</strong>
	 * pasado como parámetro.
	 * El ordenamiento es realizado mediante SQL
	 * @param sort:
	 * 				Objeto de ordenamiento que especifica la <strong>Direction</strong> y los campos por 
	 * 				el cual se hace el ordenamiento.
	 * @return
	 * 			Lista de <strong>Batch</strong> ordenada por el objeto <strong>Sort</strong>
	 */
	Iterable<Batch> findAll(Sort sort);
}

package backend.product.presentation;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * La interfaz <code>PresentationRepository</code> es la definición del repositorio
 * para entidades <strong>Presentation</strong>. Los repositorios son responsables de
 * proveer y implementar consultas así como recibir y persistir sobre entidades.
 */
@Repository
public interface PresentationRepository extends CrudRepository<Presentation, Long> {

	/***
	 * Método extendido de findAll del CrudRepository.
	 * Obtiene la lista de <strong>Presentation</strong> pero ordenadas bajo el objeto <strong>Sort</strong>
	 * pasado como parámetro.
	 * El ordenamiento es realizado mediante SQL
	 * @param sort:
	 * 				Objeto de ordenamiento que especifica la <strong>Direction</strong> y los campos por 
	 * 				el cual se hace el ordenamiento.
	 * @return
	 * 			Lista de <strong>Presentation</strong> ordenada por el objeto <strong>Sort</strong>
	 */
	Iterable<Presentation> findAll(Sort sort);
}

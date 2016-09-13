package backend.form_of_sale;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * La interfaz <code>FormOfSaleRepository</code> es la definicion del repositorio
 * para entidades <strong>FormOfSale</strong>. Los repositorios son responsables de
 * proveer y implementar consultas así como recibir y persistir sobre entidades.
 */
@Repository
public interface FormOfSaleRepository extends CrudRepository<FormOfSale, Long>{
	// NOTA: acá se pueden agregar firmas para metodos complejos que no estén
	// definidos en CrudRepository. Por ejemplo:
	// public List<Item> findBySurname(Long id);
	// Tener en cuenta que, además de CrudRepository, existen otras clases que
	// ofrecen funcionalidades, como las de paginación, que también se pueden
	// utilizar en los repositorios.
	/***
	 * Método extendido de findAll del CrudRepository.
	 * Obtiene la lista de <strong>FormOfSale</strong> pero ordenadas bajo el objeto <strong>Sort</strong>
	 * pasado como parámetro.
	 * El ordenamiento es realizado mediante SQL
	 * @param sort:
	 * 				Objeto de ordenamiento que especifica la <strong>Direction</strong> y los campos por 
	 * 				el cual se hace el ordenamiento.
	 * @return
	 * 			Lista de <strong>FormOfSale</strong> ordenada por el objeto <strong>Sort</strong>
	 */
	Iterable<FormOfSale> findAll(Sort sort);
}

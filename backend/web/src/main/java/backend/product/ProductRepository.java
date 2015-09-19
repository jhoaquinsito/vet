package backend.product;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

/**
 * La interfaz <code>ProductRepository</code> es la definición del repositorio para
 * entidades <strong>Product</strong>.
 * Los repositorios son responsables de proveer y implementar consultas así como
 * recibir y persistir sobre entidades.
 */
@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
	
	// NOTA: acá se pueden agregar firmas para metodos complejos que no estén
	// definidos en CrudRepository. Por ejemplo:
	//public List<Item> findBySurname(Long id);
	// Tener en cuenta que, además de CrudRepository, existen otras clases que
	// ofrecen funcionalidades, como las de paginación, que también se pueden
	// utilizar en los repositorios.
}

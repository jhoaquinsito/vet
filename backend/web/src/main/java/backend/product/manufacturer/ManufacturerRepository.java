package backend.product.manufacturer;


import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

/**
 * La interfaz <code>ManufacturerRepository</code> es la definicion del repositorio
 * para entidades <strong>Manufacturer</strong>. Los repositorios son responsables de
 * proveer y implementar consultas así como recibir y persistir sobre entidades.
 */
@Repository
public interface ManufacturerRepository extends CrudRepository<Manufacturer, Long> {

	// NOTA: acá se pueden agregar firmas para metodos complejos que no estén
	// definidos en CrudRepository. Por ejemplo:
	// public List<Item> findBySurname(Long id);
	// Tener en cuenta que, además de CrudRepository, existen otras clases que
	// ofrecen funcionalidades, como las de paginación, que también se pueden
	// utilizar en los repositorios.
}
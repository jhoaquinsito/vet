package backend.product;

import org.springframework.stereotype.Repository;

//import java.util.List;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
	
	// acá se pueden agregar firmas para metodos complejos que no estén
	// definidos en CrudRepository
	//public List<Item> findBySurname(Long id);
	
}

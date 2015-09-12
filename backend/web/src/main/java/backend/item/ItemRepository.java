package backend.item;

import org.springframework.stereotype.Repository;

//import java.util.List;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface ItemRepository extends CrudRepository<ItemDTO, Long> {
	
	// acá se pueden agregar firmas para metodos complejos que no estén
	// definidos en CrudRepository
	//public List<Item> findBySurname(Long id);
	
}

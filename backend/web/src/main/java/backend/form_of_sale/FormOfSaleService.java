package backend.form_of_sale;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import backend.exception.BusinessException;
import backend.utils.EntityValidator;



@Service
public class FormOfSaleService {
	@Autowired private FormOfSaleRepository iFormOfSaleRepository;
	private EntityValidator iEntityValidator;
	
	// TODO para que sirve esto? refactorizarlo y documentarlo
	public Iterable<FormOfSale> getAll() throws BusinessException {
		
		try {
			//Creamos la Direccion y la lista de Propiedades para hacer el Sorting.
			Direction direction = Direction.ASC;
			List<String> properties = new ArrayList<String>();
			properties.add("iDescription");
			//Creamos el objeto Sort para pasarle al query.
			Sort sort = new Sort(direction,properties);
		
			return this.iFormOfSaleRepository.findAll(sort);
	

		} catch (Exception e) {
			// TODO enviar un mensaje más amigable
			throw new BusinessException(e.getMessage());
		}
		

	}
}

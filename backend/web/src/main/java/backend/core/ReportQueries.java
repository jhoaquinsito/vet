package backend.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backend.exception.BusinessException;
import backend.person.children.natural_person.NaturalPerson;
import backend.product.Product;
import backend.product.ProductDTO;
import backend.product.ProductService;
import backend.report.person_balance.PersonBalance;
import backend.report.person_balance.PersonBalanceDTO;
import backend.report.person_balance.PersonBalanceService;
import backend.utils.OrikaMapperFactory;
import ma.glasnost.orika.MapperFacade;

@Component
public class ReportQueries {
	// MapperFacade de Orika (librería para mapping)
	private MapperFacade iMapper; 
	
	@Autowired private PersonBalanceService iPersonBalanceService;
	@Autowired private ProductService iProductService;
	
	/**
	 * Constructor.
	 */
	public ReportQueries() {
		super();
		// obtengo un mapper facade de la factory Orika
		this.iMapper = OrikaMapperFactory.getMapperFacade();
	}
	
	public List<PersonBalanceDTO> getAllPersonBalances() {
		
		Iterable<PersonBalance> mPersonBalances = this.iPersonBalanceService.getAll();
		
		// TODO refactor this:
		List<PersonBalanceDTO> mPersonBalanceDTOs = new ArrayList<PersonBalanceDTO>();
		for (PersonBalance bPersonBalance : mPersonBalances){
			PersonBalanceDTO bPersonBalanceDTO = new PersonBalanceDTO();
			bPersonBalanceDTO.setDebtTotal(bPersonBalance.getDebtTotal());
			bPersonBalanceDTO.setCreditTotal(bPersonBalance.getCreditTotal());
			if (bPersonBalance.getPerson().getClass() == NaturalPerson.class){ // si es persona natural
				String bPersonLastName = ((NaturalPerson) bPersonBalance.getPerson()).getLastName();
				bPersonBalanceDTO.setPersonName(bPersonBalance.getPerson().getName()+' '+ bPersonLastName);
			}else{
				bPersonBalanceDTO.setPersonName(bPersonBalance.getPerson().getName());
			}
			mPersonBalanceDTOs.add(bPersonBalanceDTO);
		}
		
		return mPersonBalanceDTOs;
		
	}
	
	public List<ProductDTO> getProductsWithoutMinimumStock() throws BusinessException{
		Iterable<Product> mProductsWithoutMinimumStock = this.iProductService.getProductsWithoutMinimumStock();
		return this.iMapper.mapAsList(mProductsWithoutMinimumStock, ProductDTO.class);
	}
}

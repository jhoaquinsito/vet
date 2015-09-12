package backend.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prod")
public class ProductAPI {

	@Autowired ProductRepository gProdRepo;
	
    @RequestMapping(method = RequestMethod.GET)
    public String home() {
    	
    	//ItemRepository mItemRepo = gContext.getBean(ItemRepository.class);
    	
    	Product prod1 = new Product();
		prod1.setName("product32423");
		
		prod1 = gProdRepo.save(prod1);
		
        return "Que comience el juego......" + prod1.toString();
    }
}

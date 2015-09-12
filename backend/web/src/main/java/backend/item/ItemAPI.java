package backend.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
public class ItemAPI {

	@Autowired ItemRepository gItemRepo;
	
    @RequestMapping(method = RequestMethod.GET)
    public String home() {
    	
    	//ItemRepository mItemRepo = gContext.getBean(ItemRepository.class);
    	
    	ItemDTO item1 = new ItemDTO();
		item1.setProduct("product32423");
		item1.setPrice(1);
		item1.setQuantity(11);
    	
		item1 = gItemRepo.save(item1);
		
        return "Que comience el juego......" + item1.toString();
    }
}

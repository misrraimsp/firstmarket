package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.PurchaseRepository;
import misrraimsp.uned.pfg.firstmarket.model.Item;
import misrraimsp.uned.pfg.firstmarket.model.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchaseServer {

    private PurchaseRepository purchaseRepository;

    @Autowired
    public PurchaseServer(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public Purchase create(List<Item> items){
        if (items.isEmpty()){
            return null;
        }
        Purchase purchase = new Purchase();
        purchase.setItems(items);
        purchase.setCreated(LocalDateTime.now());
        return purchaseRepository.save(purchase);
    }

}

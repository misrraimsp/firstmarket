package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.PurchaseRepository;
import misrraimsp.uned.pfg.firstmarket.model.Item;
import misrraimsp.uned.pfg.firstmarket.model.Purchase;
import misrraimsp.uned.pfg.firstmarket.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class PurchaseServer {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private PurchaseRepository purchaseRepository;

    @Autowired
    public PurchaseServer(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public Purchase persist(Set<Item> items, User user){
        if (items.isEmpty()){
            LOGGER.warn("Trying to persist a purchase with no items. User=({})", user.toString());
            return null;
        }
        if (user == null) {
            LOGGER.warn("Trying to persist a purchase with no user");
            return null;
        }
        Purchase purchase = new Purchase();
        purchase.setItems(items);
        purchase.setUser(user);
        purchase.setCreated(LocalDateTime.now());
        Purchase savedPurchase = purchaseRepository.save(purchase);
        LOGGER.debug("Purchase (id={}, userId={}) successfully persisted", savedPurchase.getId(), savedPurchase.getUser().getId());
        return savedPurchase;
    }

    public Set<Purchase> getPurchasesByUser(User user) {
        return purchaseRepository.findByUser(user);
    }



}

package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseServer {

    private PurchaseRepository purchaseRepository;

    @Autowired
    public PurchaseServer(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }
}

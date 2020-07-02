package misrraimsp.uned.pfg.firstmarket.core.service;

import misrraimsp.uned.pfg.firstmarket.core.data.SaleRepository;
import misrraimsp.uned.pfg.firstmarket.core.model.Item;
import misrraimsp.uned.pfg.firstmarket.core.model.Sale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SaleServer {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final SaleRepository saleRepository;

    @Autowired
    public SaleServer(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    private Sale persist(Sale reference) {
        return saleRepository.save(reference);
    }

    private Sale create(Item item) {
        Sale sale = new Sale();
        sale.setBook(item.getBook());
        sale.setPrice(item.getBook().getPrice());
        sale.setQuantity(item.getQuantity());
        sale = this.persist(sale);
        LOGGER.debug("Sale(id={}) created (book(id={}) with price {} EUR)", sale.getId(), item.getBook().getId(), item.getBook().getPrice());
        return sale;
    }

    public Set<Sale> createAll(Set<Item> items) {
        Set<Sale> sales = new HashSet<>();
        LOGGER.debug("Sale creation({}) started", items.size());
        items.forEach(item -> sales.add(this.create(item)));
        LOGGER.debug("Sale creation({}) ended", items.size());
        return sales;
    }

    private void delete(Sale sale) {
        saleRepository.delete(sale);
        LOGGER.debug("Sale(id={}) deleted (book(id={}) with price {} EUR)", sale.getId(), sale.getBook().getId(), sale.getPrice());
    }

    public void deleteAll(Set<Sale> sales) {
        LOGGER.debug("Sale deletion({}) started", sales.size());
        sales.forEach(this::delete);
        LOGGER.debug("Sale deletion({}) ended", sales.size());
    }

}

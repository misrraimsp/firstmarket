package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.AddressRepository;
import misrraimsp.uned.pfg.firstmarket.model.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServer {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private AddressRepository addressRepository;

    @Autowired
    public AddressServer(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address persist(Address address) {
        return addressRepository.save(address);
    }
}

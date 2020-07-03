package misrraimsp.uned.pfg.firstmarket.core.service;

import misrraimsp.uned.pfg.firstmarket.core.data.AddressRepository;
import misrraimsp.uned.pfg.firstmarket.core.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServer {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressServer(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address persist(Address address) {
        return addressRepository.save(address);
    }
}

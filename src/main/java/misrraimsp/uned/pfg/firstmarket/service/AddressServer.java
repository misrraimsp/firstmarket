package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.AddressRepository;
import misrraimsp.uned.pfg.firstmarket.model.Address;
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

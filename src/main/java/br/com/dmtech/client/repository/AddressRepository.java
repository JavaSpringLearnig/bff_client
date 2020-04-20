package br.com.dmtech.client.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.dmtech.client.model.Address;
import br.com.dmtech.client.model.Client;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {

	Optional<Address> getByClient(Client client);
}

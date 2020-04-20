package br.com.dmtech.client.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.dmtech.client.exception.AddressException;
import br.com.dmtech.client.exception.CpfFoundException;
import br.com.dmtech.client.exception.ResourceNotFoundException;
import br.com.dmtech.client.model.Address;
import br.com.dmtech.client.model.Client;
import br.com.dmtech.client.repository.AddressRepository;
import br.com.dmtech.client.repository.ClientRepository;
import lombok.Getter;

@Getter
@Service
public class ClientService {

	private final ClientRepository clientRepository;

	private final AddressService addressService;

	private final AddressRepository addressRepository;

	@Autowired
	public ClientService(final ClientRepository clientRepository, final AddressService addressService,
			final AddressRepository addressRepository) {
		this.clientRepository = clientRepository;
		this.addressService = addressService;
		this.addressRepository = addressRepository;
	}

	public Client save(Client client) {

		if (getByCpf(client.getCpf()) != null) {
			throw new CpfFoundException();
		}

		Address address = client.getAddress();

		Client newClient = getClientRepository().save(new Client(client));

		if (address.getId() == null) {
			address.setClient(newClient);
			getAddressRepository().save(address);
		}

		newClient.setAddress(address);

		return newClient;

	}

	private Client getByCpf(String cpf) {
		Optional<Client> client = getClientRepository().getByCpf(Optional.ofNullable(cpf).orElse(""));
		if (client.isPresent()) {
			return client.get();
		}
		return null;
	}

	public Client getById(Long id) {
		Optional<Client> client = getClientRepository().findById(Optional.ofNullable(id).orElse(0L));
		if (!client.isPresent()) {
			throw new ResourceNotFoundException(Client.class, "id",
					String.valueOf(Optional.ofNullable(id).orElse(null)));
		}
		return client.get();
	}

	public ResponseEntity<?> getAll(boolean paginado, int pagina, int tamanho, String colunaOrdenacao) {

		if (paginado) {
			return ResponseEntity.ok(getClientRepository()
					.findAll(PageRequest.of(pagina, tamanho, Sort.by(colunaOrdenacao).descending())));
		}

		return ResponseEntity.ok(getClientRepository().findAll());

	}

	public void updateAddressClient(Long idClient, Long idAddress, Address address) throws RuntimeException {
		Client client = getById(idClient);

		Address actualAddress = addressService.getById(idAddress);

		if (actualAddress.getClient().getId() != client.getId() ) {
			throw new AddressException("Endereço de outro cliente");
		}

		address.setId(actualAddress.getId());
		address.setClient(client);

		getAddressRepository().save(address);
	}

}

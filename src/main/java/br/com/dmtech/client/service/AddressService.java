package br.com.dmtech.client.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.dmtech.client.exception.ResourceNotFoundException;
import br.com.dmtech.client.model.Address;
import br.com.dmtech.client.model.Client;
import br.com.dmtech.client.proxy.ViaCepProxy;
import br.com.dmtech.client.proxy.ViaCepResponse;
import br.com.dmtech.client.repository.AddressRepository;
import br.com.dmtech.client.util.CepValidator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Service
public class AddressService {

	private final ViaCepProxy viaCepProxy;
	private final AddressRepository addressRepository;

	@Autowired
	public AddressService(final ViaCepProxy viaCepProxy, AddressRepository addressRepository) {
		this.viaCepProxy = viaCepProxy;
		this.addressRepository = addressRepository;
	}

	public Address getById(Long id) {
		Optional<Address> address = getAddressRepository().findById(Optional.ofNullable(id).orElse(0L));
		if (!address.isPresent()) {
			throw new ResourceNotFoundException(Address.class, "id",
					String.valueOf(Optional.ofNullable(id).orElse(null)));
		}
		return address.get();
	}

	public Address getByClient(Client client) {
		Optional<Address> address = getAddressRepository().getByClient(Optional.ofNullable(client).orElse(null));
		if (!address.isPresent()) {
			throw new ResourceNotFoundException(Address.class, "id",
					String.valueOf(Optional.ofNullable(client.getId()).orElse(null)));
		}
		return address.get();
	}

	public Address getByCep(String cep) {

		CepValidator.validate(cep);

		ResponseEntity<ViaCepResponse> viaCep = viaCepProxy.buscarPorCep(cep);

		Address address = new Address();
		address.setCep(cep.replace("-", ""));
		address.setEndereco(getAtributo(viaCep, "logradouro"));
		address.setComplemento(getAtributo(viaCep, "complemento"));
		address.setBairro(getAtributo(viaCep, "bairro"));
		address.setCidade(getAtributo(viaCep, "localidade"));
		address.setEstado(getAtributo(viaCep, "uf"));
		address.setPais("Brasil");
		return address;
	}

	private static String getAtributo(ResponseEntity<ViaCepResponse> viaCep, String propriedade) {
		ViaCepResponse viaCepResponse = viaCep.getBody();
		String properties = "";
		try {
			properties = PropertyUtils.getProperty(viaCepResponse, propriedade).toString();
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			log.debug("Propriedade: " + propriedade + " Está null.");
		}
		return properties;
	}

}

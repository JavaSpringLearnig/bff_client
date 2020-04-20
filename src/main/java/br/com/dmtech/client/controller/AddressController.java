package br.com.dmtech.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.dmtech.client.model.Address;
import br.com.dmtech.client.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Getter;

@Getter
@RestController
@RequestMapping(value = "/api/addresses", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "AddressController", produces = MediaType.APPLICATION_JSON_VALUE, protocols = "http", tags = "Operações relacionadas para o recurso endereço.")
public class AddressController {

	private final AddressService addressService;

	@Autowired
	public AddressController(final AddressService addressService) {
		this.addressService = addressService;
	}

	@GetMapping(value = "/{cep}")
	@ApiOperation(value = "Método get para retornar um endereço na via cep", notes = "recuperar endereco por cep")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Recurso recuperado com sucesso."),
			@ApiResponse(code = 401, message = "Credenciais de autenticação inválidas para o recurso de destino."),
			@ApiResponse(code = 403, message = "O servidor entendeu a solicitação, mas se recusa a autorizá-la"),
			@ApiResponse(code = 404, message = "O recurso não foi encontrado") })
	public ResponseEntity<Address> getByCEP(@PathVariable("cep") String cep) {
		Address address = getAddressService().getByCep(cep);

		return ResponseEntity.ok(address);
	}
}

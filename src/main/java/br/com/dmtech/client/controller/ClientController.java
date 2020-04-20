package br.com.dmtech.client.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.dmtech.client.model.Address;
import br.com.dmtech.client.model.Client;
import br.com.dmtech.client.repository.ClientRepository;
import br.com.dmtech.client.service.ClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Getter;

@Getter
@RestController
@RequestMapping(value = "/api/customers", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "ClientController",  produces = MediaType.APPLICATION_JSON_VALUE, protocols = "http", tags = "Operações relacionadas para o recurso cliente.")
public class ClientController {

	private final ClientService clientService;

	private final ClientRepository clientRepository;

	@Autowired
	public ClientController(final ClientService clientService, final ClientRepository clientRepository) {
		this.clientService = clientService;
		this.clientRepository = clientRepository;
	}

	@GetMapping
	@ApiOperation(value = "Método get para retornar todos os clientes", notes = "recuperar todos os clientes")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Recurso recuperado com sucesso."),
			@ApiResponse(code = 401, message = "Credenciais de autenticação inválidas para o recurso de destino."),
			@ApiResponse(code = 403, message = "O servidor entendeu a solicitação, mas se recusa a autorizá-la") })
	public ResponseEntity<?> getAll(
			@RequestParam(value = "pageable", defaultValue = "true", required = true) boolean pageable,
			@RequestParam(value = "page", defaultValue = "0", required = false) int page,
			@RequestParam(value = "size", defaultValue = "10", required = false) int size,
			@RequestParam(value = "sortingColumn", defaultValue = "id", required = false) String sortingColumn) {
		return getClientService().getAll(pageable, page, size, sortingColumn);
	}

	@GetMapping(value = "/{id}")
	@ApiOperation( value = "Método get para retornar um cliente", notes = "recuperar cliente por id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Recurso recuperado com sucesso."),
			@ApiResponse(code = 401, message = "Credenciais de autenticação inválidas para o recurso de destino."),
			@ApiResponse(code = 403, message = "O servidor entendeu a solicitação, mas se recusa a autorizá-la"),
			@ApiResponse(code = 404, message = "O recurso não foi encontrado") })
	public ResponseEntity<Client> getById(@PathVariable("id") Long id) {
		Client client = getClientService().getById(id);
		return ResponseEntity.ok(client);
	}

	@PostMapping
	@ApiOperation( value = "Serviço responsável por cadastrar um cliente.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Cliente cadastrado com sucesso."),
			@ApiResponse(code = 400, message = "Ocorreu um erro na validação dos dados."),
			@ApiResponse(code = 401, message = "Credenciais de autenticação inválidas para o recurso de destino."),
			@ApiResponse(code = 403, message = "O servidor entendeu a solicitação, mas se recusa a autorizá-la"),
			@ApiResponse(code = 500, message = "Ocorreu um erro na validação dos dados.") })
	public ResponseEntity<Client> save(@Valid @RequestBody Client client) {
		client = getClientService().save(client);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(client.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@PatchMapping(value = "/{id}/address/{idAddress}")
	@ApiOperation( value = "Serviço responsável por alterar o endereço de um cliente.")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Endereço alterado com sucesso."),
			@ApiResponse(code = 400, message = "Ocorreu um erro na validação dos dados."),
			@ApiResponse(code = 401, message = "Credenciais de autenticação inválidas para o recurso de destino."),
			@ApiResponse(code = 403, message = "O servidor entendeu a solicitação, mas se recusa a autorizá-la"),
			@ApiResponse(code = 500, message = "Ocorreu um erro na validação dos dados.") })
	public ResponseEntity<Void> changeClientAddress(@PathVariable("id") Long id,
			@PathVariable("idAddress") Long idAddress,@Valid @RequestBody Address address) throws Exception {
		getClientService().updateAddressClient(id, idAddress, address);
		return ResponseEntity.noContent().build();
	}

}

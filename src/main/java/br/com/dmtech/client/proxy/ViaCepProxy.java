package br.com.dmtech.client.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "${api.viacep.endpoint}", path = "/ws", name = "viaCepProxy")
public interface ViaCepProxy {

	@GetMapping(path = "/{cep}/json")
	ResponseEntity<ViaCepResponse> buscarPorCep(@PathVariable("cep") String cep);

}

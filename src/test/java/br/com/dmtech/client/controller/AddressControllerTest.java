package br.com.dmtech.client.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.exception.HystrixRuntimeException.FailureType;

import br.com.dmtech.client.proxy.ViaCepProxy;
import br.com.dmtech.client.proxy.ViaCepResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerTest {

	private static final String RESOURCE = "/api/addresses";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ViaCepProxy viacep;

	@Test
	public void testBuscarPorCep() throws Exception {
		String cep = "01313020";

		ViaCepResponse viaCepResponse = new ViaCepResponse();
		viaCepResponse.setCep(cep);
		viaCepResponse.setBairro("Bela Vista");
		viaCepResponse.setComplemento("complemento");
		viaCepResponse.setLocalidade("São Paulo");
		viaCepResponse.setLogradouro("Rua Doutor Plínio Barreto");
		viaCepResponse.setUf("SP");

		when(viacep.buscarPorCep(cep)).thenReturn(new ResponseEntity<ViaCepResponse>(viaCepResponse, HttpStatus.OK));

		this.mockMvc.perform(get(RESOURCE + "/" + cep).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());

	}

	@Test
	public void testBuscarPorCepViaCepOut() throws Exception {
		String cep = "01313021";

		HystrixRuntimeException hystrixRuntimeException = new HystrixRuntimeException(FailureType.TIMEOUT, null,
				"Erro Timeout", mock(TimeoutException.class), null);

		when(viacep.buscarPorCep(cep)).thenThrow(hystrixRuntimeException);

		MvcResult retorno = mockMvc.perform(get(RESOURCE + "/" + cep)).andExpect(status().isBadRequest()).andReturn();

		assertTrue(HystrixRuntimeException.class.isAssignableFrom(retorno.getResolvedException().getClass()));

	}

}

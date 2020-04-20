package br.com.dmtech.client.controller;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.dmtech.client.exception.CpfFoundException;
import br.com.dmtech.client.model.Address;
import br.com.dmtech.client.model.Client;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	private static final String RESOURCE = "/api/customers";

	@Test
	public void testPayloadInvalid() throws Exception {

		Client client = new Client();
		client.setCpf("04064392049");

		this.mockMvc.perform(post(RESOURCE + "/").content(mapper.writeValueAsString(client))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest()).andReturn();

	}

	@Test
	public void testPayloadMalFormated() throws Exception {
		Client client = new Client();
		client.setCpf("04064392049");

		this.mockMvc.perform(post(RESOURCE + "/").content(" { }").contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest()).andReturn();

	}

	@Test
	public void testGetAll() throws Exception {
		this.mockMvc.perform(get(RESOURCE).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
				.andReturn();

	}

	@Test
	public void testGetById() throws Exception {
		this.mockMvc.perform(get(RESOURCE + "/" + 100).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNotFound()).andReturn();
	}

	@Test
	public void testCpfAlreadyExist() throws Exception {
		Client client = new Client();
		client.setCpf("04064392049");
		client.setNome("Diego Oliveira");
		client.setSexo("Masculino");
		client.setAddress(new Address());
		client.getAddress().setBairro("Bela Vista");
		client.getAddress().setCep("01313022");
		client.getAddress().setCidade("São Paulo");
		client.getAddress().setComplemento("Rua B");
		client.getAddress().setEndereco("Rua Dr Araujo pontes");
		client.getAddress().setEstado("São Paulo");
		client.getAddress().setNumero("1");
		client.getAddress().setPais("Brasil");

		MvcResult retorno = this.mockMvc.perform(post(RESOURCE + "/").content(mapper.writeValueAsString(client))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest()).andReturn();

		assertTrue(CpfFoundException.class.isAssignableFrom(retorno.getResolvedException().getClass()));

	}

	@Test
	public void testSave() throws Exception {
		Client client = new Client();
		client.setCpf("04064392049");
		client.setNome("Diego Oliveira");
		client.setSexo("Masculino");
		client.setAddress(new Address());
		client.getAddress().setBairro("Bela Vista");
		client.getAddress().setCep("01313022");
		client.getAddress().setCidade("São Paulo");
		client.getAddress().setComplemento("Rua B");
		client.getAddress().setEndereco("Rua Dr Araujo pontes");
		client.getAddress().setEstado("São Paulo");
		client.getAddress().setNumero("1");
		client.getAddress().setPais("Brasil");

		MvcResult retorno = this.mockMvc.perform(post(RESOURCE + "/").content(mapper.writeValueAsString(client))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isCreated()).andReturn();

		String uri = retorno.getResponse().getHeader("location");

		this.mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
				.andReturn();

	}

	@Test
	public void testChangeAddress() throws Exception {

		Address address = new Address();
		address.setBairro("Bela Vista");
		address.setCep("01313022");
		address.setCidade("São Paulo");
		address.setComplemento("Rua B");
		address.setEndereco("Rua Dr Araujo pontes");
		address.setEstado("São Paulo");
		address.setNumero("1");
		address.setPais("Brasil");

		Long id = 1L;
		Long idAddress = 1L;

		this.mockMvc.perform(
				patch(RESOURCE + "/" + id + "/address/" + idAddress)
				.content(mapper.writeValueAsString(address))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNoContent()).andReturn();

	}

}

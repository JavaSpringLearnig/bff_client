package br.com.dmtech.client.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.dmtech.client.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

	Optional<Client> getByCpf(String cpf);
}

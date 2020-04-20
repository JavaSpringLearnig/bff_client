package br.com.dmtech.client.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Client implements Serializable {

	private static final long serialVersionUID = -3050361515217302013L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(hidden = true)
	private Long id;

	@NotEmpty
	private String cpf;

	@NotEmpty
	private String nome;

	@NotEmpty
	private String sexo;

	@Valid
	@OneToOne(mappedBy = "client", fetch = FetchType.LAZY)
	private Address address;

	public Client(Client client) {
		this.id = client.getId();
		this.cpf = client.getCpf();
		this.nome = client.getNome();
		this.sexo = client.getSexo();
		this.address = null;
	}

}

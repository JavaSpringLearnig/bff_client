package br.com.dmtech.client.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Address implements Serializable {

	private static final long serialVersionUID = -4237023444460359830L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(hidden = true)
	private Long id;

	@NotEmpty
	private String endereco;

	private String numero;

	@NotEmpty
	private String complemento;

	private String bairro;

	@NotEmpty
	private String cidade;

	@NotEmpty
	private String estado;

	private String pais;

	@NotEmpty
	private String cep;

	@OneToOne
	@JoinColumn(name = "id_client", columnDefinition = "id_client", referencedColumnName = "id")
	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private Client client;

}

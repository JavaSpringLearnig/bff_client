package br.com.dmtech.client.util;

import br.com.dmtech.client.exception.CepInvalidException;

public final class CepValidator {

	public static void validate(String cep) {
		String standardHyphen = "\\d{5}-\\d{3}";
		String standardNoHyphen = "\\d{8}";

		if (!cep.matches(standardHyphen) && !cep.matches(standardNoHyphen)) {
			throw new CepInvalidException();
		}
	}

}

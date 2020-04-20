package br.com.dmtech.client.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	private final HttpStatus httpStatus;

	public ResourceNotFoundException(Class clazz, String... searchParamsMap) {
		super(ResourceNotFoundException.generateMessage(clazz.getSimpleName(),
				toMap(String.class, String.class, searchParamsMap)));
		this.httpStatus = HttpStatus.resolve(HttpStatus.NOT_FOUND.value());
	}

	private static String generateMessage(String entity, Map<String, String> searchParams) {
		return StringUtils.capitalize(entity) + " não foi encontrado com os parâmetros  " + searchParams;
	}

	private static <K, V> Map<K, V> toMap(Class<K> keyType, Class<V> valueType, Object... entries) {
		if (entries.length % 2 == 1)
			throw new IllegalArgumentException("Entradas inválidas");
		return IntStream.range(0, entries.length / 2).map(i -> i * 2).collect(HashMap::new,
				(m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])), Map::putAll);
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}

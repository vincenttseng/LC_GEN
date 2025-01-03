package com.vincent.coretest.yaml.vo;

import java.util.Objects;

import org.springframework.http.HttpMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RESTfulKey {
	String path;
	HttpMethod method;

	@Override
	public int hashCode() {
		return Objects.hash(method, path);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RESTfulKey other = (RESTfulKey) obj;
		return method == other.method && Objects.equals(path, other.path);
	}
}

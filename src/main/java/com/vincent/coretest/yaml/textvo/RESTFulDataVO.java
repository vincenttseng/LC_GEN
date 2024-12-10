package com.vincent.coretest.yaml.textvo;

import java.util.List;

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
public class RESTFulDataVO {
	String path;
	String httpMethod;
	String comments;
	List<String> content;

	public RESTFulKey getKey() {
		return new RESTFulKey(path, httpMethod);
	}
}

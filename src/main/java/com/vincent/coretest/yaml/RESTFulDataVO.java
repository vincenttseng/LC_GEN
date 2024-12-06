package com.vincent.coretest.yaml;

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
	List<String> content;
}

package com.vincent.coretest.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CVSVO {
	private String apiName = "";
	private String apiNode = "";
	private String groupName = "";
	private String fieldName = "";
	private String fieldDesc = "";
	private String dataType = "";
	private String method = "";
	private String fullUrl = "";
	private String reqResp = "";
	private String mo = "";

	public String toCsvLine() {
		StringBuilder sb = new StringBuilder();
		sb.append("\"").append(apiName).append("\"").append(",");
		sb.append("\"").append(apiNode).append("\"").append(",");
		sb.append("\"").append(groupName).append("\"").append(",");
		sb.append("\"").append(fieldName).append("\"").append(",");
		sb.append("\"").append(fieldDesc).append("\"").append(",");
		sb.append("\"").append(dataType).append("\"").append(",");
		sb.append("\"").append(method).append("\"").append(",");
		sb.append("\"").append(fullUrl).append("\"").append(",");
		sb.append("\"").append(reqResp).append("\"").append(",");
		sb.append("\"").append(mo).append("\"").append(",");
		return sb.toString();
	}
}

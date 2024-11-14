package com.vincent.coretest.util;

import com.vincent.coretest.enumeration.GenTypeEnum;

public class SchemaBodyUtil {
	public static final String genSchemaText(GenTypeEnum type, String apiName) {
		String refKey = TextUtil.nameToLowerCaseAndDash(apiName + " " + type.getMessage());
		if (type == GenTypeEnum.REQUEST) {
			return genRequestSchemaText(refKey);
		} else {
			return genResponseSchemaText(refKey);
		}
	}

	/**
	 * #formatter:off
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/test-refactor2-request'
	 * #formatter:on
	 * @param refKey
	 * @return
	 */
	public static final String genRequestSchemaText(String refKey) {
		StringBuilder sb = new StringBuilder();
		sb.append("      requestBody:").append("\n");
		sb.append("        content:").append("\n");
		sb.append("          application/json:").append("\n");
		sb.append("            schema:").append("\n");
		sb.append("              $ref: '#/components/schemas/").append(refKey).append("'").append("\n");

		return sb.toString();
	}

	/**
	 * #formatter:off
              schema:
                anyOf:
                  - $ref: '#/components/schemas/test-refactor2-response'
                  - $ref: '#/components/schemas/api-messages'
	 * #formatter:on
	 * @param refKey
	 * @return
	 */
	public static final String genResponseSchemaText(String refKey) {
		StringBuilder sb = new StringBuilder();
		sb.append("              schema:").append("\n");
		sb.append("                anyOf:").append("\n");
		sb.append("                  - $ref: '#/components/schemas/").append(refKey).append("'").append("\n");
		sb.append("                  - $ref: '#/components/schemas/api-messages'").append("\n");

		return sb.toString();
	}
}

package com.vincent.coretest.util;

import com.vincent.coretest.enumeration.GenTypeEnum;

public class SchemaBodyUtil {
	public static final String genSchemaText(GenTypeEnum type, String apiName) {
		String refKey = TextUtil.nameToLowerCaseAndDash(apiName + " " + type.getMessage());
		if (type == GenTypeEnum.REQUEST) {
			return genRequestSchemaText(refKey);
		} else {
			return genResponseSchemaText(refKey, true);
		}
	}

	/**
	 * @formatter:off 
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/new-ilc-reception-modify-request'
	 * @formatter:on
	 * 
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
	 * @formatter:off 
      responses:
        '200':
          description: OK
          content:
            application/json:
              schema:
                anyOf:
                  - $ref: '#/components/schemas/new-ilc-reception-enquire-response'
                  - $ref: '#/components/schemas/api-messages'
        '400':
          description: INVALID INPUT
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/api-messages'
        '500':
          description: FAILURE
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/api-messages'
	 * $formatter:on
	 * 
	 * @param refKey
	 * @return
	 */
	public static final String genResponseSchemaText(String refKey, boolean showResponseObj) {
		StringBuilder sb = new StringBuilder();
		sb.append("      responses:").append("\n");
		sb.append("        '200':").append("\n");
		sb.append("          description: OK").append("\n");
		sb.append("          content:").append("\n");
		sb.append("            application/json:").append("\n");

		sb.append("              schema:").append("\n");
		sb.append("                anyOf:").append("\n");
		if (showResponseObj) {
			sb.append("                  - $ref: '#/components/schemas/").append(refKey).append("'").append("\n");
		}
		sb.append("                  - $ref: '#/components/schemas/api-messages'").append("\n");

		sb.append("        '400':").append("\n");
		sb.append("          description: INVALID INPUT").append("\n");
		sb.append("          content:").append("\n");
		sb.append("            application/json:").append("\n");
		sb.append("              schema:").append("\n");
		sb.append("                $ref: '#/components/schemas/api-messages'").append("\n");

		sb.append("        '500':").append("\n");
		sb.append("          description: FAILURE").append("\n");
		sb.append("          content:").append("\n");
		sb.append("            application/json:").append("\n");
		sb.append("              schema:").append("\n");
		sb.append("                $ref: '#/components/schemas/api-messages'").append("\n");

		return sb.toString();
	}
}

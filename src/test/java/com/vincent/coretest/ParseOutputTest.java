package com.vincent.coretest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.util.TextUtil;
import com.vincent.coretest.vo.ro.ColumnDefVo;

public class ParseOutputTest {
	protected final Logger logger = LoggerFactory.getLogger(ParseOutputTest.class);

	String domainFilename = ".\\src\\test\\input\\domainRef.csv";

	String responseFilename = ".\\src\\test\\input\\response.txt";


	/**
	 * letter-of-credit-draft-details-request: type: object properties:
	 * lc-draft-details: $ref:
	 * '#/components/schemas/letter-of-credit-draft-details-request-lc-draft-details'
	 * ilc-draft-details: $ref:
	 * '#/components/schemas/letter-of-credit-draft-details-request-ilc-draft-details'
	 * elc-draft-details: $ref:
	 * '#/components/schemas/letter-of-credit-draft-details-request-elc-draft-details'
	 * lc-bill-lodgement-draft-details: $ref:
	 * '#/components/schemas/letter-of-credit-draft-details-request-lc-bill-lodgement-draft-details'
	 * ilc-bill-lodgement-draft-details: $ref:
	 * '#/components/schemas/letter-of-credit-draft-details-request-ilc-bill-lodgement-draft-details'
	 * elc-bill-lodgement-draft-details: $ref:
	 * '#/components/schemas/letter-of-credit-draft-details-request-elc-bill-lodgement-draft-details'
	 * lc-deferred-payment-details: $ref:
	 * '#/components/schemas/letter-of-credit-draft-details-request-lc-deferred-payment-details'
	 * description: LETTER-OF-CREDIT-DRAFT-DETAILS-REQUEST
	 */

	/**
	 * letter-of-credit-draft-details-request-lc-deferred-payment-details: type:
	 * object properties: date-of-presentation-of-bill: type: string description:
	 * Date of Presentation of Bill. Applicable when ILC Draft tenor-type is set as
	 * deferred payment format: date amount-payable: type: number description:
	 * Amount payable. Applicable when ILC Draft tenor-type is set as deferred
	 * payment remarks: type: string description: Remarks. Applicable when ILC Draft
	 * tenor-type is set as deferred payment description:
	 * LETTER-OF-CREDIT-DRAFT-DETAILS-REQUEST-LC-DEFERRED-PAYMENT-DETAILS
	 */

	public static final String REQ = "request";
	public static final String RESP= "response";
	
	@Test
	public void genResponseObjectAndRef() throws IOException {
		String apiName = "Draft Details";

		String refKey = TextUtil.nameToLowerCaseAndDash(apiName + " " + RESP);

		File file = new File(responseFilename);
		logger.info("file " + file.getAbsolutePath() + " existed: " + file.exists());

		Map<String, List<ColumnDefVo>> objectMap = TextUtil.parseExcelInputOutputObject(file);
		Set<String> keySet = objectMap.keySet();

		for (String key : keySet) {
			List<ColumnDefVo> list = objectMap.get(key);
			if (list.size() > 0) {
				logger.info("object key " + key);
				for (ColumnDefVo value : list) {
					logger.info("value " + value);
				}
			}
		}

		logger.info("array");
		logger.info("======================================");

		Map<String, List<ColumnDefVo>> objectArrayMap = TextUtil.parseExcelInputOutputObjectArray(file);
		Set<String> objectKeySet = objectArrayMap.keySet();

		for (String key : objectKeySet) {
			List<ColumnDefVo> list = objectArrayMap.get(key);
			if (list.size() > 0) {
				logger.info("array key " + key);
				for (ColumnDefVo value : list) {
					logger.info("value " + value);
				}
			}
		}

		System.out.println("==========================================");
		System.out.println("=========ref in RESTFUL REQ ==============");
		System.out.println("==========================================");

		getRequestBody(refKey);

		System.out.println("==========================================");
		System.out.println("=========ref in components ===============");
		System.out.println("==========================================");
		System.out.println("    " + refKey + ":");
		System.out.println("      type: object");
		System.out.println("      properties:");

		for (String key : keySet) {
			List<ColumnDefVo> list = objectMap.get(key);
			if (list.size() > 0) {
				System.out.println("        " + key + ":");
				String subkey = refKey + "-" + key;
				System.out.println("          $ref: '#/components/schemas/" + subkey + "'");
			}
		}

		/**
		 * type: array items: $ref: >-
		 * #/components/schemas/enquire-shipping-bill-details-response-shipping-bill-details
		 */
		for (String key : objectKeySet) {
			List<ColumnDefVo> list = objectArrayMap.get(key);
			if (list.size() > 0) {
				System.out.println("        " + key + ":");
				System.out.println("          type: array");
				System.out.println("          items:");
				String subkey = refKey + "-" + key;
				System.out.println("            $ref: '#/components/schemas/" + subkey + "'");
			}
		}
		System.out.println("      description: " + refKey.toUpperCase());

		System.out.println("==========================================");
		System.out.println("=========ref in sub components ===========");
		System.out.println("==========================================");

		File domainFile = new File(domainFilename);
		Map<String, String> domainMap = TextUtil.readFileToDomainMap(domainFile);

		Map<String, String> typeToDomainMap = TextUtil.readFileTypeToDomainMap();

		for (String key : keySet) {
			List<ColumnDefVo> list = objectMap.get(key);
			if (list.size() > 0) {
				String subkey = refKey + "-" + key;
				System.out.println("    " + subkey + ":");

				int cnt = 0;
				StringBuilder sba = new StringBuilder();
				for (ColumnDefVo vo : list) {
					if (vo.required) {
						cnt++;
						sba.append("        - " + vo.name + "\n");
					}
				}
				if (cnt > 0) {
					System.out.println("      required:");
					System.out.print(sba.toString());
				}

				System.out.println("      type: object");
				System.out.println("      properties:");

				for (ColumnDefVo vo : list) {
					String objKey = vo.name;
					System.out.println("        " + objKey + ":");
					System.out.println("          type: " + vo.type);

					String domainName = typeToDomainMap.get(vo.name);

					if (!domainMap.containsKey(domainName)) {
						System.out.println("          description: " + vo.desc);
					} else {
						String showName = vo.name.toUpperCase();
						StringBuilder sb = new StringBuilder();
						sb.append("description: '" + showName + " Allowed values:");
						sb.append(domainMap.get(domainName));
						sb.append(";'");

						System.out.println("          " + sb.toString());
					}
					if (vo.isDate) {
						System.out.println("          format: date");
					}
				}
			}
		}

		for (String key : objectKeySet) {
			List<ColumnDefVo> list = objectArrayMap.get(key);
			if (list.size() > 0) {
				String subkey = refKey + "-" + key;
				System.out.println("    " + subkey + ":");

				int cnt = 0;
				StringBuilder sba = new StringBuilder();
				for (ColumnDefVo vo : list) {
					if (vo.required) {
						cnt++;
						sba.append("        - " + vo.name + "\n");
					}
				}
				if (cnt > 0) {
					System.out.println("      required:");
					System.out.print(sba.toString()); // already has \n
				}

				System.out.println("      type: object");
				System.out.println("      properties:");

				for (ColumnDefVo vo : list) {
					String objKey = vo.name;
					System.out.println("        " + objKey + ":");
					System.out.println("          type: " + vo.type);

					String domainName = typeToDomainMap.get(vo.name);

					if (!domainMap.containsKey(domainName)) {
						System.out.println("          description: " + vo.desc);
					} else {
						String showName = vo.name.toUpperCase();
						StringBuilder sb = new StringBuilder();
						sb.append("description: '" + showName + " Allowed values:");
						sb.append(domainMap.get(domainName));
						sb.append(";'");

						System.out.println("          " + sb.toString());
					}
					if (vo.isDate) {
						System.out.println("          format: date");
					}
				}
			}
		}
	}

	/**
	 * requestBody: content: application/json: schema: $ref:
	 * '#/components/schemas/letter-of-credit-draft-details-request'
	 **/
	public void getRequestBody(String refKey) {
		System.out.println("      requestBody:");
		System.out.println("        content:");
		System.out.println("          application/json:");
		System.out.println("            schema:");
		System.out.println("              $ref: '#/components/schemas/" + refKey + "'");
	}

}

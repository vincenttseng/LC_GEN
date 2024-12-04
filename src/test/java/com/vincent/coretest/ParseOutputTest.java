package com.vincent.coretest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.enumeration.GenTypeEnum;
import com.vincent.coretest.reader.DomainMapUtil;
import com.vincent.coretest.reader.ReqRespParserUtil;
import com.vincent.coretest.reader.TFTypeUtil;
import com.vincent.coretest.util.RefDefDetailUtil;
import com.vincent.coretest.util.SchemaBodyUtil;
import com.vincent.coretest.util.TextUtil;
import com.vincent.coretest.vo.ColumnDefVo;
import com.vincent.coretest.vo.TFType;

public class ParseOutputTest {
	protected final Logger logger = LoggerFactory.getLogger(ParseOutputTest.class);

	String inputFilename = ".\\src\\test\\input\\input.txt";
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

	String apiName = "test refactor2";

	Map<String, TFType> tfTypeMap = null;
	Map<String, String> typeToDomainMap = null;
	Map<String, String> domainToTypesMap = null;

	@Test
	public void genResponseObjectAndRef() throws IOException {
		tfTypeMap = TFTypeUtil.readDTMap();
		typeToDomainMap = DomainMapUtil.readFileTypeToDomainMap();
		domainToTypesMap = DomainMapUtil.readDomainNameToTypesMap();

		// input part
		File inputFile = new File(inputFilename);
		logger.info("inputFile " + inputFile.getAbsolutePath() + " existed: " + inputFile.exists());

		System.out.println("==========================================");
		System.out.println("=========INPUT INPUT INPUT ===============");
		System.out.println("==========================================");
		
		Map<String, List<ColumnDefVo>> inputObjectMap = ReqRespParserUtil.parseExcelInputOutputObject(inputFile);
		Map<String, List<ColumnDefVo>> inputObjectArrayMap = ReqRespParserUtil
				.parseExcelInputOutputObjectArray(inputFile);

		// response part
		File responseFile = new File(responseFilename);
		logger.info("responseFile " + responseFile.getAbsolutePath() + " existed: " + responseFile.exists());

		System.out.println("==========================================");
		System.out.println("=========RESPONSE RESPONSE ===============");
		System.out.println("==========================================");
		
		Map<String, List<ColumnDefVo>> respObjectMap = ReqRespParserUtil.parseExcelInputOutputObject(responseFile);
		Map<String, List<ColumnDefVo>> respObjectArrayMap = ReqRespParserUtil
				.parseExcelInputOutputObjectArray(responseFile);

		System.out.println("==========================================");
		System.out.println("=========ref in RESTFUL REQ ==============");
		System.out.println("==========================================");

		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("===> request");
		if (inputObjectMap.size() > 0 || inputObjectArrayMap.size() > 0) {
			System.out.println(SchemaBodyUtil.genSchemaText(GenTypeEnum.REQUEST, apiName));
		}
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("===> response");
		if (respObjectMap.size() > 0 || respObjectArrayMap.size() > 0) {
			System.out.println(SchemaBodyUtil.genSchemaText(GenTypeEnum.RESPONSE, apiName));
		}
		System.out.println("------------------------------------------------------------------------------------");

		// Reference Part
		System.out.println("==========================================");
		System.out.println("=========ref in components ===============");
		System.out.println("==========================================");
		System.out.println("### \'" + apiName + "\' REF-MAIN PART");
		if (inputObjectMap.size() > 0 || inputObjectArrayMap.size() > 0) {
			genRefDefObject(GenTypeEnum.REQUEST, inputObjectMap, inputObjectArrayMap);
		}
		genRefDefObject(GenTypeEnum.RESPONSE, respObjectMap, respObjectArrayMap);

		System.out.println("### \'" + apiName + "\' REF-DETAIL PART");
		// RefDetail Part
		if (inputObjectMap.size() > 0 || inputObjectArrayMap.size() > 0) {
			genRefDetailObject(GenTypeEnum.REQUEST, inputObjectMap, inputObjectArrayMap);
		}
		genRefDetailObject(GenTypeEnum.RESPONSE, respObjectMap, respObjectArrayMap);
	}

	public void genRefDefObject(GenTypeEnum type, Map<String, List<ColumnDefVo>> objectMap,
			Map<String, List<ColumnDefVo>> objectArrayMap) {

		String refKey = TextUtil.nameToLowerCaseAndDash(apiName + " " + type.getMessage());

		System.out.println("    " + refKey + ":");
		System.out.println("      type: object");
		System.out.println("      properties:");

		for (String key : objectMap.keySet()) {
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
		for (String key : objectArrayMap.keySet()) {
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
	}

	public void genRefDetailObject(GenTypeEnum type, Map<String, List<ColumnDefVo>> objectMap,
			Map<String, List<ColumnDefVo>> objectArrayMap) {
		String refKey = TextUtil.nameToLowerCaseAndDash(apiName + " " + type.getMessage());
		
		RefDefDetailUtil.printRefDefDetail(refKey, objectMap, domainToTypesMap, typeToDomainMap, tfTypeMap);
		RefDefDetailUtil.printRefDefDetail(refKey, objectArrayMap, domainToTypesMap, typeToDomainMap, tfTypeMap);
	}
}

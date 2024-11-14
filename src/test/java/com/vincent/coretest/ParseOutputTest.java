package com.vincent.coretest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.enumeration.GenTypeEnum;
import com.vincent.coretest.util.DomainMapUtil;
import com.vincent.coretest.util.RefDefDetailUtil;
import com.vincent.coretest.util.ReqRespParserUtil;
import com.vincent.coretest.util.SchemaBodyUtil;
import com.vincent.coretest.util.TFTypeUtil;
import com.vincent.coretest.util.TextUtil;
import com.vincent.coretest.vo.TFType;
import com.vincent.coretest.vo.ro.ColumnDefVo;

public class ParseOutputTest {
	protected final Logger logger = LoggerFactory.getLogger(ParseOutputTest.class);

	String domainFilename = ".\\src\\test\\input\\domainRef.csv";

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
	Map<String, String> domainMap = null;

	@Test
	public void genResponseObjectAndRef() throws IOException {
		tfTypeMap = TFTypeUtil.readDTMap();
		typeToDomainMap = DomainMapUtil.readFileTypeToDomainMap();

		// domain mapping
		File domainFile = new File(domainFilename);
		domainMap = TextUtil.readFileToDomainMap(domainFile);

		GenTypeEnum type = GenTypeEnum.RESPONSE;

		String refKey = TextUtil.nameToLowerCaseAndDash(apiName + " " + type.getMessage());

		File inputFile = new File(inputFilename);
		logger.info("inputFile " + inputFile.getAbsolutePath() + " existed: " + inputFile.exists());

		// input part
		Map<String, List<ColumnDefVo>> inputObjectMap = ReqRespParserUtil.parseExcelInputOutputObject(inputFile);
		logger.info("array");
		Map<String, List<ColumnDefVo>> inputObjectArrayMap = ReqRespParserUtil
				.parseExcelInputOutputObjectArray(inputFile);

		// response part
		File responseFile = new File(responseFilename);
		logger.info("responseFile " + responseFile.getAbsolutePath() + " existed: " + responseFile.exists());

		Map<String, List<ColumnDefVo>> respObjectMap = ReqRespParserUtil.parseExcelInputOutputObject(responseFile);
		logger.info("array");
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
		System.out.println(SchemaBodyUtil.genSchemaText(GenTypeEnum.RESPONSE, apiName));
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

	public void genRefDefObject(GenTypeEnum type, Map<String, List<ColumnDefVo>> respObjectMap,
			Map<String, List<ColumnDefVo>> respObjectArrayMap) {

		String refKey = TextUtil.nameToLowerCaseAndDash(apiName + " " + type.getMessage());

		System.out.println("    " + refKey + ":");
		System.out.println("      type: object");
		System.out.println("      properties:");

		for (String key : respObjectMap.keySet()) {
			List<ColumnDefVo> list = respObjectMap.get(key);
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
		for (String key : respObjectArrayMap.keySet()) {
			List<ColumnDefVo> list = respObjectArrayMap.get(key);
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
		
		RefDefDetailUtil.printRefDefDetail(refKey, objectMap, domainMap, typeToDomainMap, tfTypeMap);
		RefDefDetailUtil.printRefDefDetail(refKey, objectArrayMap, domainMap, typeToDomainMap, tfTypeMap);
	}
}

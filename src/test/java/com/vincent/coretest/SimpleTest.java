package com.vincent.coretest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vincent.coretest.reader.DomainMapUtil;
import com.vincent.coretest.reader.PathUtil;
import com.vincent.coretest.util.TextUtil;
import com.vincent.coretest.vo.ColumnDefVo;

public class SimpleTest {
	protected final Logger logger = LoggerFactory.getLogger(SimpleTest.class);

	String domainFilename = ".\\src\\test\\input\\domainRef.csv";
	String typeToDomainFilename = ".\\src\\test\\input\\typeToDomain.txt";

	@Test
	public void testDomain() throws IOException {
		File domainFile = new File(domainFilename);
		logger.info("domainFile " + domainFile.getAbsolutePath() + " existed: " + domainFile.exists());

		Map<String, String> map = DomainMapUtil.readDomainNameToTypesMap(domainFile);

		String name = "XXXXX";

		for (String key : map.keySet()) {
			StringBuilder sb = new StringBuilder();
			sb.append("description: '" + key + " Allowed values:");
			sb.append(map.get(key));
			sb.append(";'");

			//System.out.println(sb.toString());
		}

		name = "twct-personal-guarantee";
		String type = "d_YesNoFlg";



//		d_YesNoFlg
		
		if(map.containsKey(type)) {
			StringBuilder sb = new StringBuilder();
			sb.append("description: '" + name + " Allowed values:");
			sb.append(map.get(type));
			sb.append(";'");
	
			System.out.println(sb.toString());
		} else {
			System.out.println("not enum");
		}
	}

	/**
	 * #formatter:off
        - name: from-date
          in: path
          required: true
          schema:
            type: string
          description: FROM DATE
          
        - name: page_num
          in: query
          required: false
          schema:
            type: number
          description: PAGE_NUM
	 * #formatter:on
	 */
	@Test
	public void getGetParamAndQuery() {
		String fullPath = "/v2/collateral-insurance/{collateral-id}/insurance-renewal";

		int index = fullPath.indexOf("?");
		String path = fullPath;
		if(index >= 0) {
			path = fullPath.substring(0, index);
		}
		logger.info("path " + path);
		List<String> tokens = TextUtil.splitBrackets(path);
		logger.info("tokens " + tokens);
		StringBuilder sb = new StringBuilder();
		for (String token : tokens) {
			sb.append("        - name: ").append(token).append("\n");
			sb.append("          in: path").append("\n");
			sb.append("          required: true").append("\n");
			sb.append("          schema:").append("\n");
			sb.append("            type: string").append("\n");
			String desc = TextUtil.phaseWordToDesc(token);
			sb.append("          description: ").append(desc);
			sb.append("\n");
		}
		
		if(index >= 0) {
			path = fullPath.substring(index + 1);
			path = path.replaceAll("-", "_");
			logger.info("path " + path);
	
			String[] queryArray = path.split("&");
			for (String query : queryArray) {
				String[] values = query.split("=");
				String token = values[0];
				String type = values[1];
				type = type.replace("<", "");
				type = type.replace(">", "");
				String convertedType = ColumnDefVo.convertType(type);
	
				sb.append("        - name: ").append(token).append("\n");
				sb.append("          in: query").append("\n");
				sb.append("          required: false").append("\n");
				sb.append("          schema:").append("\n");
				sb.append("            type: ").append(convertedType).append("\n");
				String desc = TextUtil.phaseWordToDesc(token);
				sb.append("          description: ").append(desc);
				sb.append("\n");
			}
		}
		System.out.println(sb.toString());
	}

	/**
	 * #formatter:off
        - name: from-date
          in: path
          required: true
          schema:
            type: string
          description: FROM DATE
          
        - name: page_num
          in: query
          required: false
          schema:
            type: number
          description: PAGE_NUM
	 * #formatter:on
	 */
	@Test
	public void getGetParamAndQuery1() {
		String fullPath = "/export-lc-bill-payment/{from-date}/{to-date-}/list?bill-payment-reference=<Numeric>&bill-reference=<String>&bill-amount-currency=<String>&bill-amount=<Numeric>&export-lc-advising-reference=<String>&maturity-date=<Date>&bill-status=<Numeric>&rate-request-status=<Numeric>&page-num=<Numeric>&page-size=<Numeric>";

		System.out.println(PathUtil.getPathParamString(fullPath));
	}
	
	@Test
	public void testNameToLowerCaseAndDash() {
		String name = "Enquire Draft Details";

		logger.info("=>" + TextUtil.nameToLowerCaseAndDash(name));
	}
}

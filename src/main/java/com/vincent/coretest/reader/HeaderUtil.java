package com.vincent.coretest.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.vincent.coretest.util.FileOutputUtil;

public class HeaderUtil {
	private static final String headerFileSrc = ".\\src\\test\\resources\\header.txt";
	private static final String methodHeaderFileSrc = ".\\src\\test\\resources\\methodHeaders.txt";

	public static void printHeader(String outputFile) {
		File headerFile = new File(headerFileSrc);

		if (headerFile == null || !headerFile.exists()) {
			return;
		}

		FileOutputUtil.printOut(outputFile, "", false);
		FileReader in = null;
		BufferedReader br = null;
		boolean firstLine = true;
		try {
			in = new FileReader(headerFile);
			br = new BufferedReader(in);

			String line;
			while ((line = br.readLine()) != null) {
				boolean append = firstLine ? false : true; // first line is not append. it is new file
				FileOutputUtil.printOut(outputFile, line, append);
				firstLine = false;
			}
		} catch (IOException e) {

		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static String methodHeadersString = null;

	public static String getMethodHeadersString() {
		StringBuilder sb = new StringBuilder();
		if (methodHeadersString == null) {
			File methodHeaderFile = new File(methodHeaderFileSrc);

			if (methodHeaderFile == null || !methodHeaderFile.exists()) {
				return "";
			}

			FileReader in = null;
			BufferedReader br = null;
			try {
				in = new FileReader(methodHeaderFile);
				br = new BufferedReader(in);

				String line;
				while ((line = br.readLine()) != null) {
					if (StringUtils.isNotBlank(line)) {
						sb.append(line).append("\n");
					}
				}
			} catch (IOException e) {

			} finally {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

}

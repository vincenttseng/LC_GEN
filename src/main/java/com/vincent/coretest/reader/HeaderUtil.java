package com.vincent.coretest.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class HeaderUtil {
	private static final String headerFileSrc = ".\\src\\test\\resources\\header.txt";
	private static final String methodHeaderFileSrc = ".\\src\\test\\resources\\methodHeaders.txt";

	public static void printHeader()  {
		try {
			printHeader(headerFileSrc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void printHeader(String fileName) throws IOException {
		File headerFile = new File(fileName);

		if (headerFile == null || !headerFile.exists()) {
			return;
		}

		FileReader in = null;
		BufferedReader br = null;
		try {
			in = new FileReader(headerFile);
			br = new BufferedReader(in);

			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
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
					sb.append(line).append("\n");
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

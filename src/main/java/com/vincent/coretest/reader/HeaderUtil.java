package com.vincent.coretest.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeaderUtil {
	private static final String headerFileSrc = ".\\src\\test\\resources\\header.txt";

	public static void printHeader() throws IOException {
		printHeader(headerFileSrc);
	}

	public static void printHeader(String fileName) throws IOException {
		File typeToDomainFile = new File(fileName);

		if (typeToDomainFile == null || !typeToDomainFile.exists()) {
			return;
		}

		FileReader in = null;
		BufferedReader br = null;
		try {
			in = new FileReader(typeToDomainFile);
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

}

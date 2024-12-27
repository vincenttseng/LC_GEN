package com.vincent.coretest.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileOutputUtil {
	protected static final Logger logger = LoggerFactory.getLogger(FileOutputUtil.class);

	private static final String OUTPUT_ROOT = ".\\src\\test\\output\\";

	static {
		File file = new File(OUTPUT_ROOT);
		if (file.exists() == false) {
			file.mkdirs();
		}
	}

	public static void printOut(String fileName, String line) {
		printOut(fileName, line, true);
	}

	public static void printOut(String fileName, String line, boolean append) {
		if (StringUtils.isBlank(line)) {
			return;
		}
		FileWriter fw = null;
		BufferedWriter writer = null;
		try {
			fw = new FileWriter(composeFullFlePath(fileName), append);
			writer = new BufferedWriter(fw);
			writer.write(line);
			writer.newLine();
			logger.debug("append " + line);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
			try {
				fw.close();
			} catch (Exception e) {
			}
		}
	}

	private static String composeFullFlePath(String fileName) {
		return OUTPUT_ROOT + fileName;
	}
}

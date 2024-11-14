package com.vincent.coretest.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.vincent.coretest.vo.TFType;

public class TFTypeUtil {
	private static final String TF_DAT_FILENAME = ".\\src\\test\\input\\TF.dat";

	public static final Map<String, TFType> readDTMap() throws IOException {
		return readDTMap(TF_DAT_FILENAME);
	}

	public static final Map<String, TFType> readDTMap(String fileName) throws IOException {
		File tfDataFile = new File(fileName);

		if (tfDataFile == null || !tfDataFile.exists()) {
			return new HashMap<String, TFType>();
		}

		Map<String, TFType> map = new HashMap<String, TFType>();

		FileReader in = null;
		BufferedReader br = null;
		try {
			in = new FileReader(tfDataFile);
			br = new BufferedReader(in);

			String line;
			while ((line = br.readLine()) != null) {
				TFType type = TFType.toTFType(line);
				if (type != null) {
					map.put(type.name, type);
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

		return map;
	}
}

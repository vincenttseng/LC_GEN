/**
 * 
 */
package com.vincent.coretest.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

import com.vincent.coretest.exception.DqsError;
import com.vincent.coretest.exception.NotValidParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * com.vincent.coretest.util ApiUtilize.java
 *
 * @author vincenttseng 2019年7月24日
 */
public class ApiUtilize {
	protected static final Logger logger = LoggerFactory.getLogger(ApiUtilize.class);

	public static String decodeTimezone(String timezone) {
		try {
			timezone = URLDecoder.decode(timezone, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			throw new NotValidParameterException(e.getMessage());
		}
		timezone = timezone.replace("GMT ", "GMT+");
		return timezone;
	}

	public static final String TIME_FORMAT = "yyyyMMdd HHmmss";
	public static final SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);

	public static Long toTimeMillis(Long input) {
		if (input == null) {
			return System.currentTimeMillis();
		}

		if (input.compareTo(100000000000L) < 0) {
			return input * 1000;
		} else {
			return input;
		}
	}

	public static String genErrMsg(DqsError err, String errMsg) {
		return err + errMsg;
	}
	
	public static String encode(String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			return value;
		}
	}
}

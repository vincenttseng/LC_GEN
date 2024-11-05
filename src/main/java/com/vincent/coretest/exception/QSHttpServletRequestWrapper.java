package com.vincent.coretest.exception;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QSHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private static final Logger LOG = LoggerFactory.getLogger(QSHttpServletRequestWrapper.class);

	private final String rawBody;

	public QSHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);

		StringBuilder rawBody = new StringBuilder();
		BufferedReader reader = null;

		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				reader = new BufferedReader(new InputStreamReader(inputStream));
				String line = "";
				while ((line = reader.readLine()) != null) {
					rawBody.append(line).append("\n");
				}
			} else {
				rawBody.append("");
			}
		} catch (IOException e) {
			LOG.error("Error reading the request body...");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LOG.error("Error closing bufferedReader...");
				}
			}
		}

		this.rawBody = rawBody.toString();
	}

	public String getRawBody() {
		return rawBody;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final StringReader reader = new StringReader(rawBody);
		ServletInputStream inputStream = new ServletInputStream() {

			@Override
			public int read() throws IOException {
				return reader.read();
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener listener) { }

		};

		return inputStream;
	}

}

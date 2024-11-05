package com.vincent.coretest.exception;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class QSLogger {

	private static final String NEW_LINE = "\n";
	private static final String DEFAULT_BOUNDARY = "=====================================";

	private Logger log;

	private String boundaryMessage;

	private List<Object> boundaryObjects;

	private StringBuffer message;

	private List<Object> objects;

	public static QSLogger of(Class<?> clazz) {
		return new QSLogger(clazz);
	}

	private QSLogger(Class<?> clazz) {
		this.log = LoggerFactory.getLogger(clazz);
		this.boundaryMessage = DEFAULT_BOUNDARY;
		this.boundaryObjects = new ArrayList<Object>();
		this.message = new StringBuffer(NEW_LINE);
		this.objects = new ArrayList<Object>();
	}

	public QSLogger start() {
		this.message.append(boundaryMessage).append(NEW_LINE);
		return this;
	}

	public QSLogger start(String message, Object... objects) {
		addBoundary(message);
		addObjects(this.boundaryObjects, objects);
		this.objects.addAll(this.boundaryObjects);
		return this;
	}

	public QSLogger start(String message) {
		addBoundary(message);
		return this;
	}

	public QSLogger append(String message) {
		this.message.append(message).append(NEW_LINE);
		return this;
	}

	public QSLogger append(String message, Object... objects) {
		this.message.append(message).append(NEW_LINE);
		addObjects(this.objects, objects);
		return this;
	}

	public void printDebug() {
		if (log.isDebugEnabled()) {
			addEndLine();
			log.debug(message.toString(), objects.toArray());
		}
	}

	public void printInfo() {
		if (log.isInfoEnabled()) {
			addEndLine();
			log.info(message.toString(), objects.toArray());
		}
	}

	public void printError() {
		if (this.log.isErrorEnabled()) {
			addEndLine();
			log.error(message.toString(), objects.toArray());
		}
	}

	private void addBoundary(String message) {
		boundaryMessage = new StringBuffer().append("===== ").append(message).append(" =====").toString();
		append(boundaryMessage);
	}

	private void addObjects(List<Object> field, Object... objects) {
		for (Object object : objects) {
			field.add(object);
		}
	}

	private void addEndLine() {
		message.append(boundaryMessage).append(NEW_LINE);
		objects.addAll(boundaryObjects);
	}

}
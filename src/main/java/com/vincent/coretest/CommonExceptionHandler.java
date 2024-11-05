/**
 * 
 */
package com.vincent.coretest;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import com.vincent.coretest.exception.CommandExecutionException;
import com.vincent.coretest.exception.CommandSynTaxErrException;
import com.vincent.coretest.exception.DeviceNotFoundException;
import com.vincent.coretest.exception.DqsError;
import com.vincent.coretest.exception.DqsException;
import com.vincent.coretest.exception.KafkaSendingException;
import com.vincent.coretest.exception.NotValidParameterException;
import com.vincent.coretest.exception.QSHttpServletRequestWrapper;
import com.vincent.coretest.exception.QSLogger;
import com.vincent.coretest.exception.ScheduleDuplicateException;
import com.vincent.coretest.vo.FailedResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CommonExceptionHandler {
	@ExceptionHandler(KafkaSendingException.class)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Object handleException(HttpServletRequest request, KafkaSendingException e) {
		dumpError(request, e);
		return failedResult(e);
	}

	@ExceptionHandler(NotValidParameterException.class)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Object handleException(HttpServletRequest request, NotValidParameterException e) {
		dumpError(request, e);
		return failedResult(e);
	}

	@ExceptionHandler(DeviceNotFoundException.class)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Object handleException(HttpServletRequest request, DeviceNotFoundException e) {
		dumpError(request, e);
		return failedResult(e);
	}

	@ExceptionHandler(CommandSynTaxErrException.class)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Object handleException(HttpServletRequest request, CommandSynTaxErrException e) {
		dumpError(request, e);
		return failedResult(e);
	}

	@ExceptionHandler(CommandExecutionException.class)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Object handleException(HttpServletRequest request, CommandExecutionException e) {
		dumpError(request, e);
		return failedResult(e);
	}

	@ExceptionHandler(ScheduleDuplicateException.class)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Object handleException(HttpServletRequest request, ScheduleDuplicateException e) {
		dumpError(request, e);
		return failedResult(e);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Object handleException(HttpServletRequest request, Exception e) {
		DqsException nse = new DqsException(e);
		dumpError(request, nse);
		return failedResult(nse);
	}

	public FailedResult failedResult(DqsException e) {
		return failedResult(e.getCode(), e.getMessage());
	}

	public FailedResult failedResult(DqsError code, String message) {
		return new FailedResult(code.getCode(), message);
	}

	private void dumpError(HttpServletRequest request, DqsException e) {
		Throwable source = e.getSource();
		if (source != null) {
			dumpError(request, e.getCode(), source);
		} else {
			dumpError(request, e.getCode(), e.getMessage());
		}
	}

	private void dumpError(HttpServletRequest request, DqsError code, Throwable e) {
		dumpError(request, code, ExceptionUtils.getStackTrace(e));
	}

	private void dumpError(HttpServletRequest request, DqsError code, String message) {
		QSLogger log = QSLogger.of(getClass());
		log.start("API [{}] ERROR [{}]", request.getServletPath(), code.getCode());
		request.getParameterMap().forEach((name, value) -> {
			log.append("Parameter : {} = {}", name, value);
		});
		log.append("Message   : {}", message).append("Body      :").append(getBody(request)).printError();
	}

	private String getBody(HttpServletRequest request) {
		if (request instanceof QSHttpServletRequestWrapper) {
			return ((QSHttpServletRequestWrapper) request).getRawBody();
		}
		return "[Empty body]";
	}
}

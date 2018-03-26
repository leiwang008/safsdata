/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-06    (Lei Wang) Initial release.
 */
package org.safs.data.controller;

import org.safs.data.exception.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Lei Wang
 *
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {
	private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

	@ExceptionHandler
	public ResponseEntity<String> handleException(Exception e){

		log.error("EHA",e);

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		if(e instanceof RestException){
			HttpStatus tempStatus = ((RestException)e).getHttpStatus();
			if(tempStatus!=null) status = tempStatus;
		}

		return ResponseEntity.status(status).body(e.getMessage());
	}

    public static final String DEFAULT_ERROR_VIEW = "error";

//    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
////    @ExceptionHandler
//    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) {
//    	ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW);
//
////    	HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
////    	if(e instanceof RestException){
////    		HttpStatus tempStatus = ((RestException)e).getHttpStatus();
////    		if(tempStatus!=null) status = tempStatus;
////    	}
////    	mav.setStatus(status);
////
//////    	mav.addObject("datetime", new Date());
////    	mav.addObject("exception", e);
////    	mav.addObject("url", request.getRequestURL());
//
//    	return mav;
//    }

}

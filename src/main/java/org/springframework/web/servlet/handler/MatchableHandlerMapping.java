package org.springframework.web.servlet.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.lang.Nullable;

public interface MatchableHandlerMapping {

	@Nullable
	RequestMatchResult match(HttpServletRequest request, String pattern);
}

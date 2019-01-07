package org.springframework.web.servlet.handler;

import java.util.Map;

import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;

public class RequestMatchResult {

	private final String matchingPattern;
	
	private final String lookupPath;
	
	private final PathMatcher patchMatcher;
	
	public RequestMatchResult(String matchingPattern, String lookupPath, PathMatcher pathMatcher) {
		Assert.hasText(matchingPattern, "'matchingPattern' is required");
		Assert.hasText(lookupPath, "'lookupPath' is required");
		Assert.notNull(pathMatcher, "'pathMatcher' is required");
		this.matchingPattern = matchingPattern;
		this.lookupPath = lookupPath;
		this.patchMatcher = pathMatcher;
	}
	
	public Map<String, String> extractUriTemplateVariables() {
		return this.patchMatcher.extractUriTemplateVariables(this.matchingPattern, this.lookupPath);
	}
}

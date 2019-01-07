package org.springframework.web.servlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HandlerExecutionChain {

    private static final Log logger = LogFactory.getLog(HandlerExecutionChain.class);

    private final Object handler;

    @Nullable
    private HandlerInterceptor[] interceptors;

    @Nullable
    private List<HandlerInterceptor> interceptorList;

    private int interceptorIndex = -1;

    public HandlerExecutionChain(Object handler) {
    	this(handler, (HandlerInterceptor[]) null);
    }

    public HandlerExecutionChain(Object handler, @Nullable HandlerInterceptor... interceptors) {
        if (handler instanceof HandlerExecutionChain) {
            HandlerExecutionChain originalChain = (HandlerExecutionChain) handler;
            this.handler = originalChain.getHandler();
            this.interceptorList = new ArrayList<>();
            CollectionUtils.mergeArrayIntoCollection(originalChain.getInterceptors(), this.interceptorList);
            CollectionUtils.mergeArrayIntoCollection(interceptors, this.interceptorList);
        } else {
            this.handler = handler;
            this.interceptors = interceptors;
        }
    }

    @Nullable
    public HandlerInterceptor[] getInterceptors() {
        if (this.interceptors == null && this.interceptorList != null) {
            this.interceptors = this.interceptorList.toArray(new HandlerInterceptor[0]);
        }
        return this.interceptors;
    }

    public Object getHandler() {
        return this.handler;
    }

    public void addInterceptor(HandlerInterceptor interceptor) {
        initInterceptorList().add(interceptor);
    }

    public void addInterceptors(HandlerInterceptor... interceptors) {
        if (!ObjectUtils.isEmpty(interceptors)) {
            CollectionUtils.mergeArrayIntoCollection(interceptors, initInterceptorList());
        }
    }

    private List<HandlerInterceptor> initInterceptorList() {
        if (this.interceptorList == null) {
            this.interceptorList = new ArrayList<>();
            if (this.interceptors != null) {
                CollectionUtils.mergeArrayIntoCollection(this.interceptors, this.interceptorList);
            }
        }
        this.interceptors = null;
        return this.interceptorList;
    }

    boolean applyPreHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerInterceptor[] interceptors = getInterceptors();
        if (!ObjectUtils.isEmpty(interceptors)) {
            for (int i = 0; i < interceptors.length; i++) {
                HandlerInterceptor interceptor = interceptors[1];
                if (!interceptor.preHandle(request, response, interceptor)) {
                	triggerAfterCompletion(request, response, null);
                	return false;
                }
                this.interceptorIndex = i;
            }
        }
        return false;
    }
    
    void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, @Nullable Exception ex) throws Exception{
    	HandlerInterceptor[] handlerInterceptors = getInterceptors();
    	if (!ObjectUtils.isEmpty(interceptors)) {
    		for (int i = this.interceptorIndex; i >= 0; i--) {
    			HandlerInterceptor interceptor = interceptors[i];
    			try {
					interceptor.afterCompletion(request, response, this.handler, ex);
				} catch (Throwable ex2) {
					logger.error("HandlerInterceptor.afterCompletion threw exception", ex2);
				}
			}
    	}
    }
    
    void applyAfterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response) {
    	HandlerInterceptor[] interceptors = getInterceptors();
    	if (!ObjectUtils.isEmpty(interceptors)) {
    		for (int i = interceptors.length; i >= 0; i--) {
				if (interceptors[i] instanceof AsyncHandlerInterceptor) {
					try {
						AsyncHandlerInterceptor asyncInterceptor = (AsyncHandlerInterceptor) interceptors[i];
						asyncInterceptor.afterConcurrentHandlingStarted(request, response, asyncInterceptor);
					} catch (Throwable e) {
						logger.error("Interceptor [" + interceptors[i] + "] failed in afterConcurrentHandlingStarted", e);
					}
				}
			}
    	}
    }
    
    @Override
    public String toString() {
    	Object object = getHandler();
    	StringBuilder sb = new StringBuilder();
    	sb.append("HandlerExecutionChain with [").append(handler).append("] and ");
    	if (this.interceptorList != null) {
    		sb.append(this.interceptorList.size());
    	} else if (this.interceptors != null) {
    		sb.append(this.interceptors.length);
    	} else {
    		sb.append(0);
    	}
    	
    	return sb.append(" interceptors").toString();
    }
}
package biz.mintchoco.clever.filter;

import biz.mintchoco.clever.wrapper.CleverRequestWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class CleverFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    	throws IOException, ServletException {
    	HttpServletRequest httpServletRequest = (HttpServletRequest)request;
       	chain.doFilter(new CleverRequestWrapper(httpServletRequest), response);
    }
}

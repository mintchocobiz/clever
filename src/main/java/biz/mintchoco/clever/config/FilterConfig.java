package biz.mintchoco.clever.config;

import biz.mintchoco.clever.filter.CleverFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class FilterConfig {
		
	@Bean
	public FilterRegistrationBean<Filter> multipartFilter() {
		FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new CleverFilter());
		registrationBean.setOrder(0);
		registrationBean.addUrlPatterns("/*");
		return registrationBean;
	}
}

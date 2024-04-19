//package com.wallet;
//
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.wallet.filters.AuthenPincodeFilter;
//@Configuration
//public class FilterConfig {
//	@Bean
//    public FilterRegistrationBean<AuthenPincodeFilter> myFilterRegistrationBean() {
//        FilterRegistrationBean<AuthenPincodeFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new AuthenPincodeFilter());
//        registrationBean.addUrlPatterns("/transaction/*"); 
//
//        return registrationBean;
//    }
//}

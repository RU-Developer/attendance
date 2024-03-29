package yonam.attendence;

import net.nurigo.java_sdk.api.Message;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import yonam.attendence.domain.message.CoolsmsConnectionConst;
import yonam.attendence.web.argumentresolver.LoginParentArgumentResolver;
import yonam.attendence.web.argumentresolver.LoginTeacherArgumentResolver;
import yonam.attendence.web.interceptor.HealthCheckInterceptor;
import yonam.attendence.web.interceptor.LoginCheckInterceptor;
import yonam.attendence.web.interceptor.LoginParentCheckInterceptor;
import yonam.attendence.web.interceptor.UrlCheckInterceptor;

import javax.sql.DataSource;

import java.util.List;

import static yonam.attendence.connection.ConnectionConst.*;
import static yonam.attendence.domain.message.CoolsmsConnectionConst.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public DataSource dataSource() {
        return new DriverManagerDataSource(URL, USERNAME, PASSWORD);
    }

    @Bean
    public Message message() {
        return new Message(API_KEY, API_SECRET);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginTeacherArgumentResolver());
        resolvers.add(new LoginParentArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HealthCheckInterceptor())
                .order(1)
                .addPathPatterns("/healthcheck");

        registry.addInterceptor(new UrlCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/healthcheck", "/error");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(3)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/teachers/add", "/teachers/sendvalidation", "/teachers/validation",
                        "/login", "/logout",
                        "/css/**", "/*.ico", "/error", "/healthcheck", "/parents/**");

        registry.addInterceptor(new LoginParentCheckInterceptor())
                .order(4)
                .addPathPatterns("/parents/**")
                .excludePathPatterns("/parents/login", "/parents/sendvalidation", "/parents/validation");
    }
}

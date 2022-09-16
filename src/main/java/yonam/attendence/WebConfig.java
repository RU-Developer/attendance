package yonam.attendence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import yonam.attendence.web.argumentresolver.LoginTeacherArgumentResolver;
import yonam.attendence.web.interceptor.HealthCheckInterceptor;
import yonam.attendence.web.interceptor.LoginCheckInterceptor;
import yonam.attendence.web.interceptor.UrlCheckInterceptor;

import javax.sql.DataSource;

import java.util.List;

import static yonam.attendence.connection.ConnectionConst.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public DataSource dataSource() {
        return new DriverManagerDataSource(URL, USERNAME, PASSWORD);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginTeacherArgumentResolver());
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
                .excludePathPatterns("/", "/teachers/add", "/login", "logout",
                        "/css/**", "/*.ico", "/error", "/healthcheck");

    }
}

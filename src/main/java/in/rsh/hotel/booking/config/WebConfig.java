package in.rsh.hotel.booking.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new RequestResponseLoggingInterceptor());
  }

  @Slf4j
  public static class RequestResponseLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
        HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
      long startTime = System.currentTimeMillis();
      request.setAttribute("startTime", startTime);
      log.info(
          "Incoming Request: {} {} from {}",
          request.getMethod(),
          request.getRequestURI(),
          request.getRemoteAddr());
      return true;
    }

    @Override
    public void afterCompletion(
        HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
      long startTime = (long) request.getAttribute("startTime");
      long duration = System.currentTimeMillis() - startTime;
      log.info(
          "Request Completed: {} {} - Status: {} - Duration: {}ms",
          request.getMethod(),
          request.getRequestURI(),
          response.getStatus(),
          duration);
    }
  }
}

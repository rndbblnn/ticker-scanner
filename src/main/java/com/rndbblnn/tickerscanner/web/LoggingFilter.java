package com.rndbblnn.tickerscanner.web;

import com.google.common.base.Stopwatch;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggingFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;

    log.info("{} {}", req.getMethod(), req.getRequestURI());

    Stopwatch sw = Stopwatch.createStarted();
    chain.doFilter(request, response);

    log.info("{} {} [elapsed:{}]", req.getMethod(), req.getRequestURI(), sw.toString());

  }
}

package com.kanofans.framework.websocket;

import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@WebFilter(filterName = "WebsocketFilter", urlPatterns = "/websocket/**")
public class WebSocketFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String token = ((HttpServletRequest) servletRequest).getHeader("Sec-Websocket-Protocol");
        // 解决 Sent non-empty 'Sec-WebSocket-Protocol' header but no response was received
        response.setHeader("Sec-Websocket-Protocol", token);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}

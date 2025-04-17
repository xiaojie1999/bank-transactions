package com.example.banktransactions.log;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class TraceIdFilter implements Filter {

    private static final String TRACE_ID = "traceId";
    private static final String SPAN_ID = "spanId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            // 从请求头中获取 traceId 和 spanId，如果没有则生成新的
            String traceId = httpRequest.getHeader("X-Trace-Id");
            String spanId = httpRequest.getHeader("X-Span-Id");

            if (traceId == null) {
                traceId = UUID.randomUUID().toString().replace("-", "");
            }
            if (spanId == null) {
                spanId = UUID.randomUUID().toString().replace("-", "");
            }

            // 将 traceId 和 spanId 设置到 MDC 中
            MDC.put(TRACE_ID, traceId);
            MDC.put(SPAN_ID, spanId);

            // 继续请求链
            chain.doFilter(request, response);
        } finally {
            // 请求结束后清理 MDC
            MDC.remove(TRACE_ID);
            MDC.remove(SPAN_ID);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}

package com.lhl.rp.filter;

import com.lhl.rp.bean.LoginUser;
import com.lhl.rp.util.JwtUtil;
import com.lhl.rp.util.TokenCacheHolder;
import com.lhl.rp.util.TokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 认证过滤器：拦截请求、校验 JWT、注入用户信息
 *
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/3/26_23:27
 */
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private UserDetailsService userDetailsService; // 构造注入我们自己的实现类

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String token = TokenUtil.resolveToken(request);

        if (token != null) {
            // 令牌没用被篡改 并且 Token 在缓存中心有效
            if (JwtUtil.verify(token) && TokenCacheHolder.exists(token)) {
                String username = JwtUtil.getUsername(token);

                LoginUser loginUser = (LoginUser) userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                loginUser,
                                null,
                                loginUser.getAuthorities()
                        );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        chain.doFilter(request, response);
    }
}

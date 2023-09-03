package com.SBI.security.config.jwt;

import com.SBI.security.models.User;
import com.SBI.security.services.AuthorizationService;
import com.SBI.security.services.UserServices;
import com.SBI.security.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserServices userServices;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    )
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt_token;

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            jwt_token = authHeader.substring(7);

            if (jwt_token != null && jwtUtils.validateJwtToken(jwt_token)) {
                String userEmail = jwtUtils.getUserNameFromJwtToken(jwt_token); // extract userEmail from JWT token

                String uri =  request.getRequestURI();
                String[] parts = uri.split("/");
                String userIdStr = parts[parts.length - 1];
                Integer targetUserId = Integer.parseInt(userIdStr);

                if (authorizationService.isAuthorized(targetUserId, userEmail)) {

                    UserDetails userDetails = userServices.loadUserByUsername(userEmail);
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Forbidden: Token user does not match requested user");
                    return;
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Forbidden: Token does not match user");
        }
        filterChain.doFilter(request, response) ;
    }
}

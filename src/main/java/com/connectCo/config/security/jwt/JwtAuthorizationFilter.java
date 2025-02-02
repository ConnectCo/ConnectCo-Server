package com.connectCo.config.security.jwt;

import com.connectCo.config.security.auth.PrincipalDetails;
import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.domain.member.repository.MemberRepository;
import com.connectCo.global.common.BaseResponse;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(request);

        if (token != null) {
            try {
                jwtTokenProvider.validateToken(token);

                Claims claims = jwtTokenProvider.parseClaims(token);

                Long memberId = Long.valueOf(claims.getSubject());
                Long profileId = claims.get("profileId", Long.class);
                String profileTypeStr = claims.get("profileType", String.class);
                ProfileType profileType = profileTypeStr != null ? ProfileType.valueOf(profileTypeStr) : null;

                // 데이터베이스에서 Member 조회
                Member member = memberRepository.getMember(memberId);

                PrincipalDetails principal = new PrincipalDetails(member, profileId, profileType);
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (CustomApiException e) {
                SecurityContextHolder.clearContext();
                sendErrorResponse(response, e.getErrorCode());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());

        BaseResponse<Object> errorResponse = BaseResponse.onFailure(
            errorCode.getCode(),
            errorCode.getMessage(),
            null
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}

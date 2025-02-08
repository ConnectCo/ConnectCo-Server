package com.connectCo.config.security.auth;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.member.entity.ProfileType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record PrincipalDetails(Member member, Long profileId, ProfileType profileType) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority("ROLE_" + member.getRole().name()) // 🔹 기본 Role 추가
        );

        // 🔹 ProfileType이 존재할 경우만 추가
        if (profileType != null) {
            authorities = new ArrayList<>(authorities);
            authorities.add(new SimpleGrantedAuthority("TYPE_" + profileType.name()));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return null; // 비밀번호는 소셜 로그인만 사용
    }

    @Override
    public String getUsername() {
        return String.valueOf(member.getId());
    }

    public boolean isProfileSelected() {
        return profileId != null && profileType != null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}




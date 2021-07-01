package jp.ac.kobe_u.cs.itspecialist.todoapp.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jp.ac.kobe_u.cs.itspecialist.todoapp.entity.Member;

/**
 * 認証に必要なUserDetailsの実装クラス．Memberをラップする
 */
public class UserDetailsImpl implements UserDetails {
    Member member;
    Collection<GrantedAuthority> authorities = new ArrayList<>();

    /**
     * コンストラクタ
     * @param member
     */
    public UserDetailsImpl(Member member) {
        this.member=member;
        //メンバーのロールから権限を生成して追加
        this.authorities.add(new SimpleGrantedAuthority("ROLE_" + member.getRole()));
    }

    public Member getMember() {
        return member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getMid();
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

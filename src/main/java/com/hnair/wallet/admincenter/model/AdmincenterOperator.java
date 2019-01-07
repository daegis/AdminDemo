package com.hnair.wallet.admincenter.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * AdmincenterOperator Entity.
 */
@Data
public class AdmincenterOperator implements Serializable, UserDetails {

    //列信息
    private Integer id;

    private String operatorName;

    private String operatorUniqueSalt;

    private String password;

    private String operatorNickName;

    private String operatorSource;

    private Set<GrantedAuthority> grantedAuthoritySet;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthoritySet;
    }

    @Override
    public String getUsername() {
        return this.operatorName;
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


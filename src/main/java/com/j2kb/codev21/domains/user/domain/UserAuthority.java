package com.j2kb.codev21.domains.user.domain;

import com.j2kb.codev21.global.common.BaseTimeEntity;
import com.mysema.commons.lang.Assert;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "USER_AUTHORITY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAuthority extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHORITY_ID")
    private Authority authority;

    @Builder
    public UserAuthority(User user, Authority authority) {
        Assert.notNull(user, "member must not be null!!");
        Assert.notNull(authority, "authority must not be null!!");
        this.user = user;
        this.authority = authority;
    }

    public void setUser(User user){
        this.user = user;
    }

}

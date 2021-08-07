package com.j2kb.codev21.domains.user.domain;

import com.j2kb.codev21.global.common.BaseTimeEntity;
import com.mysema.commons.lang.Assert;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "AUTHORITY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="ID")
    private Long id;

    @Column(name = "AUTHORITY_NAME", length = 50, nullable = false)
    private String authorityName;

    @Builder
    public Authority(String authorityName){
        Assert.notNull(authorityName, "authorityName must not be null!!");
        this.authorityName = authorityName;
    }

}

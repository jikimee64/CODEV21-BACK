package com.j2kb.codev21.domains.user.domain;

import com.j2kb.codev21.global.common.BaseTimeEntity;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "USER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="ID")
    private Long id;

    @Column(name = "EMAIL", length = 100, unique = true)
    private String email;

    @Column(name = "PASSWORD", length = 200)
    private String password;

    @Column(name = "NAME", length = 45)
    private String name;

    @Column(name = "JOIN_GISU", length = 45)
    private String joinGisu;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 45)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "FIELD", length = 45)
    private Field field;

    @Column(name = "GITHUB_ID", length = 100)
    private String githubId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserAuthority> authorities = new HashSet<>();

    @Builder
    public User(String email, String password, String name,
        String joinGisu, Status status, Field field, String githubId){
        this.email = email;
        this.password = password;
        this.name = name;
        this.joinGisu = joinGisu;
        this.status = status;
        this.field = field;
        this.githubId = githubId;
    }

    public void addAuthority(UserAuthority userAuthority) {
        authorities.add(userAuthority);
        userAuthority.setUser(this);
    }

    public void changePassword(String password){
        this.password = password;
    }

    public void changeUserInfoByAdmin(String joinGisu,
        Status status, Field field){
        this.joinGisu = joinGisu;
        this.status = status;
        this.field = field;
    }


}

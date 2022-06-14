package com.bobmate.bobmate.domain;

import com.bobmate.bobmate.dto.CreateMemberDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter @Setter
public class Member implements UserDetails {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String userName;

    @Column(length = 300, nullable = false)
    private String password;

    @Column(length = 100, nullable = false, unique = true)
    private String nickname;

    @OneToMany(mappedBy = "member")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberMeet> memberMeets = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<LikeReview> likeReviews = new ArrayList<>();

    @OneToMany(mappedBy = "toMember")
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "fromMember")
    private List<Follow> following = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Bookmark> bookmarks = new ArrayList<>();

    private MemberStatus memberStatus;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    public String getUserName() {
        return this.userName;
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

    //==생성 메서드==//
    public static Member createMember(CreateMemberDto memberDto) {
        Member member = new Member();
        member.setUserName(memberDto.getUserName());
        member.setPassword(memberDto.getPassword());
        member.setNickname(memberDto.getNickname());
        member.setRoles(memberDto.getRoles());
        member.setMemberStatus(MemberStatus.VALID);

        return member;
    }

    //==비즈니스 로직==//
    public void delete() {
        this.memberStatus = MemberStatus.DELETED;
    }
}

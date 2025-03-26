package shop.nongdam.nongdambackend.member.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.nongdam.nongdambackend.global.domain.BaseEntity;
import shop.nongdam.nongdambackend.restaurant.domain.Restaurant;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

    private String email;

    private String name;

    private String picture;

    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private boolean firstLogin;

    private String certificate;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Restaurant> restaurants = new ArrayList<>();

    @Builder
    public Member(String email, String name, String picture, SocialType socialType, Role role, boolean firstLogin,
                  String certificate) {
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.socialType = socialType;
        this.role = role;
        this.firstLogin = firstLogin;
        this.certificate = certificate;
    }

    public void updateRole(Role role) {
        this.role = role;
    }
}

package shop.nongdam.nongdambackend.restaurant.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.nongdam.nongdambackend.global.domain.BaseEntity;
import shop.nongdam.nongdambackend.member.domain.Member;
import shop.nongdam.nongdambackend.region.domain.Region;
import shop.nongdam.nongdambackend.restaurant.menu.domain.Menu;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends BaseEntity {

    @Column(name = "restaurant_name", nullable = false)
    private String restaurantName;

    @Column(name = "restaurant_representative", nullable = false)
    private String restaurantRepresentative;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "business_registration_number", nullable = false)
    private String businessRegistrationNumber;

    @Column(name = "address", nullable = false)
    private String address;

    private double latitude;

    private double longitude;

    @Column(name = "represent_image")
    private String restaurantImage;

    @Column(name = "open_time")
    private String openTime;

    @Column(name = "close_time")
    private String closeTime;

    private String precautions;

    private boolean isRegistered = false;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menu = new ArrayList<>();

    @JoinColumn(name = "region_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Region region;

    @Builder
    public Restaurant(String restaurantName, String restaurantRepresentative, Member member, String phoneNumber,
                      String businessRegistrationNumber, String address, double latitude, double longitude,
                      String restaurantImage, String openTime, String closeTime, String precautions,
                      boolean isRegistered,
                      List<Menu> menu, Region region) {
        this.restaurantName = restaurantName;
        this.restaurantRepresentative = restaurantRepresentative;
        this.member = member;
        this.phoneNumber = phoneNumber;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.restaurantImage = restaurantImage;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.precautions = precautions;
        this.isRegistered = isRegistered;
        this.menu = menu;
        this.region = region;
    }

    public void updateDetail(double latitude, double longitude, String restaurantImage, String openTime,
                             String closeTime, String precautions, boolean isRegistered) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.restaurantImage = restaurantImage;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.precautions = precautions;
        this.isRegistered = isRegistered;
    }

    public boolean isOwner(Member member) {
        return this.member.equals(member);
    }

    public Menu getMainMenu() {
        return this.menu.stream()
                .filter(Menu::isMainMenu)
                .findFirst()
                .orElse(null);
    }
}

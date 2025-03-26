package shop.nongdam.nongdambackend.restaurant.menu.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.nongdam.nongdambackend.global.domain.BaseEntity;
import shop.nongdam.nongdambackend.restaurant.domain.Restaurant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {

    private String name;

    private int price;

    private String image;

    private String farmProduce;

    private String farmProduceImage;

    private String mainDescription;

    private String subDescription;

    @Column(name = "is_main_menu")
    private boolean isMainMenu;

    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    @Builder
    public Menu(String name, int price, String image, String farmProduce, String farmProduceImage,
                String mainDescription,
                String subDescription, boolean isMainMenu, Restaurant restaurant) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.farmProduce = farmProduce;
        this.farmProduceImage = farmProduceImage;
        this.mainDescription = mainDescription;
        this.subDescription = subDescription;
        this.isMainMenu = isMainMenu;
        this.restaurant = restaurant;
    }
}

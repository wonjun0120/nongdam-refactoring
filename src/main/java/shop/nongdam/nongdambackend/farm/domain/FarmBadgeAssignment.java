package shop.nongdam.nongdambackend.farm.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class FarmBadgeAssignment {

    @EmbeddedId
    private FarmBadgeAssignmentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("farmId")
    @JoinColumn(name = "farm_id")
    private Farm farm;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("badgeId")
    @JoinColumn(name = "badge_id")
    private FarmBadge farmBadge;

    public FarmBadgeAssignment(Farm farm, FarmBadge farmBadge) {
        this.id = new FarmBadgeAssignmentId(farm.getId(), farmBadge.getId());
        this.farm = farm;
        this.farmBadge = farmBadge;
    }
}
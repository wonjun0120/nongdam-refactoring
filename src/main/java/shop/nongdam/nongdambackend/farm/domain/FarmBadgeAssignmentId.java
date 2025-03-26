package shop.nongdam.nongdambackend.farm.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
public class FarmBadgeAssignmentId implements Serializable {
    private Long farmId;
    private Long badgeId;

    public FarmBadgeAssignmentId() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FarmBadgeAssignmentId that = (FarmBadgeAssignmentId) o;
        return Objects.equals(farmId, that.farmId) && Objects.equals(badgeId, that.badgeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(farmId, badgeId);
    }
}
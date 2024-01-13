package com.nebula.notescape.persistence.specification;

import com.nebula.notescape.persistence.RecordState;
import com.nebula.notescape.persistence.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserSpecification {

    public Specification<User> idNotNull() {
        return (root, query, builder) -> builder.isNotNull(root.get("id"));
    }

    public Specification<User> hasKeyword(String keyword) {
        return (root, query, builder) -> builder.or(
                builder.like(root.get("username"), "%%%s%%".formatted(keyword)),
                builder.like(root.get("fullName"), "%%%s%%".formatted(keyword))
        );
    }

    public Specification<User> isActive() {
        return (root, query, builder) -> builder.and(
                builder.isNotNull(root.get("id")),
                builder.equal(root.get("recordState"), RecordState.ACTIVE)
        );
    }

}

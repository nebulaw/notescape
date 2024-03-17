package com.nebula.notescape.persistence.specification;

import com.nebula.notescape.persistence.Access;
import com.nebula.notescape.persistence.RecordState;
import com.nebula.notescape.persistence.entity.Note;
import com.nebula.notescape.persistence.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class NoteSpecification {

    public Specification<Note> idNotNull() {
        return (root, query, builder) -> builder.isNotNull(root.get("id"));
    }

    public Specification<Note> isActive() {
        return (root, query, builder) -> builder.and(
                builder.isNotNull(root.get("id")),
                builder.equal(root.get("recordState"), RecordState.ACTIVE)
        );
    }

    public Specification<Note> movieIdEquals(Long id) {
        return (root, query, builder) -> builder.equal(root.get("movieId"), id);
    }

    public Specification<Note> authorEquals(User user) {
        return (root, query, builder) -> builder.equal(root.get("author"), user);
    }

    public Specification<Note> isPublic() {
        return (root, query, builder) -> builder.equal(root.get("access"), Access.PUBLIC);
    }

    public Specification<Note> isPrivate() {
        return (root, query, builder) -> builder.equal(root.get("access"), Access.PRIVATE);
    }

}


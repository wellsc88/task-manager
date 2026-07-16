package com.well.tech.task.manager.repository.specification;

import com.well.tech.task.manager.dto.request.TaskFilterRequest;
import com.well.tech.task.manager.entity.Task;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class TaskSpecification {

    public static Specification<Task> filter(
            TaskFilterRequest filter,
            UUID userId) {

        return (root, query, criteriaBuilder) -> {

            var predicate = criteriaBuilder.conjunction();

            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.equal(
                            root.get("user").get("id"),
                            userId
                    )
            );

            if (filter.getStatus() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(
                                root.get("status"),
                                filter.getStatus()
                        )
                );
            }

            if (filter.getPriority() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(
                                root.get("priority"),
                                filter.getPriority()
                        )
                );
            }

            if (filter.getTitle() != null &&
                    !filter.getTitle().isBlank()) {

                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("title")),
                                "%" + filter.getTitle().toLowerCase() + "%"
                        )
                );
            }

            return predicate;
        };
    }
}
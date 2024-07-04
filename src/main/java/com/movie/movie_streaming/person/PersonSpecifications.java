package com.movie.movie_streaming.person;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class PersonSpecifications {

    private static Predicate caseInsensitiveEqual(CriteriaBuilder criteriaBuilder, Path<String> path, String value) {
        return criteriaBuilder.equal(criteriaBuilder.lower(path), value.toLowerCase());
    }

    public static <T extends Person> Specification<T> hasFullName(String firstName, String lastName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                caseInsensitiveEqual(criteriaBuilder, root.get("firstName"), firstName),
                caseInsensitiveEqual(criteriaBuilder, root.get("lastName"), lastName)
        );
    }

    public static <T extends Person> Specification<T> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) ->
                caseInsensitiveEqual(criteriaBuilder, root.get("firstName"), firstName);
    }

    public static <T extends Person> Specification<T> hasLastName(String lastName) {
        return (root, query, criteriaBuilder) ->
                caseInsensitiveEqual(criteriaBuilder, root.get("lastName"), lastName);
    }

}

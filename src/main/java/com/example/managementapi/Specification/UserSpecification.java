package com.example.managementapi.Specification;

import com.example.managementapi.Entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> hasKeyword(String keyword){
        return(root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()){
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.or(
                    criteriaBuilder
                            .like(criteriaBuilder
                                    .lower(root
                                            .get("userName")), "%" + keyword.toLowerCase() + "%"),
                    criteriaBuilder
                            .like(criteriaBuilder
                                    .lower(root
                                            .get("email")), "%" + keyword.toLowerCase() + "%"),
                    criteriaBuilder
                            .like(criteriaBuilder
                                    .lower(root
                                            .get("phone")), "%" + keyword.toLowerCase() + "%")
            );
        };
    }

    public static Specification<User> statusFilter(String status){
        return(root, query, criteriaBuilder) -> {
            if (status == null || status.isEmpty()){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.or(
                    criteriaBuilder
                            .like(criteriaBuilder
                            .lower(root.get("isActive")), "%" + status.toLowerCase() + "%")
            );
        };
    }

    public static Specification<User> searchByCriteria(String keyword, String status){
        return Specification.allOf(
                hasKeyword(keyword),
                statusFilter(status)
        );
    }
}

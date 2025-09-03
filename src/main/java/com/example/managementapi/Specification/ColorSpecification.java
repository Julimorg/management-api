package com.example.managementapi.Specification;

import com.example.managementapi.Entity.Color;
import org.springframework.data.jpa.domain.Specification;

public class ColorSpecification {
    //? Khai báo kiểu Specification để query key "keyword"
    public static Specification<Color> hasKeyword(String keyword){

        return (root, query, criteriaBuilder ) -> {

            if(keyword == null && keyword.isEmpty()){
               return null;
            }

            return criteriaBuilder.or(
                    criteriaBuilder
                            .like(criteriaBuilder
                                    .lower(root
                                            .get("colorId")), "%" + keyword.toLowerCase() + "%"),
                    criteriaBuilder
                            .like(criteriaBuilder
                                    .lower(root
                                            .get("colorName")), "%" + keyword.toLowerCase() + "%"),
                    criteriaBuilder
                            .like(criteriaBuilder
                                    .lower(root
                                            .get("colorCode")), "%" + keyword.toLowerCase() + "%")

            );

        };
    }
    public static Specification<Color> searchByCriteria(String keyword){
        return (root, query, cb) -> {
            query.distinct(true);
            return Specification.allOf(
                    hasKeyword(keyword)
            ).toPredicate(root, query, cb);
        };
    }
}

package com.hoangnm.cmsdemo.service;

import com.hoangnm.cmsdemo.entity.Order;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {

    public static Specification<Order> filterBy(Long userId, LocalDate orderDate, Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userId != null) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));
            }
            if (orderDate != null) {
                LocalDateTime startOfDay = orderDate.atStartOfDay();
                LocalDateTime endOfDay = orderDate.atTime(23, 59, 59);
                predicates.add(criteriaBuilder.between(root.get("orderDate"), startOfDay, endOfDay));
            }
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("totalPrice"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("totalPrice"), maxPrice));
            }
            
            query.orderBy(criteriaBuilder.desc(root.get("orderDate")));

            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

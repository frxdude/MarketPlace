package com.diplom.marketplace.helper.specification;

import com.diplom.marketplace.entity.Post;
import com.diplom.marketplace.entity.User;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.Date;

@Data
@Component
public class UserSpecification implements Specification<User> {

    private SearchCriteria criteria;
    private Expression<Object> expression;
    private String groupBy;


    public UserSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    public UserSpecification() {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey("id");
        searchCriteria.setOperation(">");
        searchCriteria.setValue(0);
        this.criteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate
            (@NonNull Root<User> root,
             @NonNull CriteriaQuery<?> query,
             @NonNull CriteriaBuilder builder) {

        Expression<Integer> year = builder.function("YEAR", Integer.class, root.get("createdDate"));
        Expression<Integer> month = builder.function("MONTH", Integer.class, root.get("createdDate"));
        Expression<Integer> volume = builder.function("MONTH", Integer.class, root.get("createdDate"));
        Expression<Integer> day = builder.function("DAY", Integer.class, root.get("createdDate"));
        Expression<Integer> expr = builder.sum(month);

        switch (criteria.getOperation()) {
            case ">":
                return builder.greaterThan(
                        root.get(criteria.getKey()), criteria.getValue().toString());
            case ">=":
                return builder.greaterThanOrEqualTo(
                        root.get(criteria.getKey()), criteria.getValue().toString());
            case "<":
                return builder.lessThan(
                        root.get(criteria.getKey()), criteria.getValue().toString());
            case "<=":
                return builder.lessThanOrEqualTo(
                        root.get(criteria.getKey()), criteria.getValue().toString());
            case "=":
                return builder.equal(
                        root.get(criteria.getKey()), criteria.getValue());
            case "<=>":
                return builder.between(
                        root.get(criteria.getKey()), (Date) criteria.getValue(), (Date) criteria.getValue2());
            case ".%":
                return builder.like(
                        root.get(criteria.getKey()), criteria.getValue().toString() + "%");
            case "%.":
                return builder.like(
                        root.get(criteria.getKey()), "%" + criteria.getValue().toString());
            case "%%":
                return builder.like(
                        root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
            case ".i%":
                return builder.like(
                        builder.lower(
                                root.get(criteria.getKey())), criteria.getValue().toString().toLowerCase() + "%");
            default:
                return null;
        }
    }
}

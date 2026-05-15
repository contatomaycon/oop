package com.example.vehicle.application.search;

import com.example.vehicle.domain.model.Brand;
import com.example.vehicle.domain.model.Model;
import com.example.vehicle.domain.model.Vehicle;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class VehicleSpecifications {
  private VehicleSpecifications() {
  }

  public static Specification<Vehicle> byCriteria(VehicleSearchCriteria criteria) {
    return (root, query, cb) -> {
      if (query != null && Vehicle.class.equals(query.getResultType())) {
        root.fetch("model", JoinType.LEFT).fetch("brand", JoinType.LEFT);
        query.distinct(true);
      }

      List<Predicate> predicates = new ArrayList<>();
      Join<Vehicle, Model> model = null;
      Join<Model, Brand> brand = null;

      if (criteria.brandId() != null || criteria.modelId() != null) {
        model = root.join("model");
      }
      if (criteria.brandId() != null) {
        brand = model.join("brand");
        predicates.add(cb.equal(brand.get("id"), criteria.brandId()));
      }
      if (criteria.modelId() != null) {
        predicates.add(cb.equal(model.get("id"), criteria.modelId()));
      }
      if (criteria.year() != null) {
        predicates.add(cb.equal(root.get("year"), criteria.year()));
      }
      if (criteria.status() != null) {
        predicates.add(cb.equal(root.get("status"), criteria.status()));
      }
      if (criteria.minPrice() != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("price"), criteria.minPrice()));
      }
      if (criteria.maxPrice() != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("price"), criteria.maxPrice()));
      }

      return cb.and(predicates.toArray(Predicate[]::new));
    };
  }
}

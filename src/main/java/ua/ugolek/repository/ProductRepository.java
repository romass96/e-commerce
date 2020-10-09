package ua.ugolek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ugolek.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}

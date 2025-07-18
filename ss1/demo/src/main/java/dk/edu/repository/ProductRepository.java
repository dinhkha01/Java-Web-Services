package dk.edu.repository;

import dk.edu.entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    List<Product> findAll();
    Optional<Product> findById(Long id);
    Product update(Product product);
    boolean deleteById(Long id);



}
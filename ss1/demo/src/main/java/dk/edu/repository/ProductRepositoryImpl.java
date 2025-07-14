package dk.edu.repository;

import dk.edu.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ProductRepositoryImpl implements ProductRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            entityManager.persist(product);
            return product;
        } else {
            return entityManager.merge(product);
        }
    }
    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        TypedQuery<Product> query = entityManager.createQuery(
                "SELECT p FROM Product p ORDER BY p.id", Product.class);
        return query.getResultList();
    }
    @Override
    public Product update(Product product) {
        if (product.getId() == null) {
            throw new IllegalArgumentException("Product ID cannot be null for update operation");
        }
        Product existingProduct = entityManager.find(Product.class, product.getId());
        if (existingProduct == null) {
            throw new RuntimeException("Product not found with id: " + product.getId());
        }

        return entityManager.merge(product);
    }
    @Override
    public boolean deleteById(Long id) {
        Product product = entityManager.find(Product.class, id);
        if (product != null) {
            entityManager.remove(product);
            return true;
        }
        return false;
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        Product product = entityManager.find(Product.class, id);
        return Optional.ofNullable(product);
    }
}
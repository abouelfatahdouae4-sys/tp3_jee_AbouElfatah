package com.emsi.productsapp.repositories;

import com.emsi.productsapp.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Recherche les produits dont le nom contient le mot-clé (insensible à la casse)
     * avec pagination
     */
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    /**
     * Recherche sans pagination
     */
    java.util.List<Product> findByNameContainingIgnoreCase(String keyword);
}

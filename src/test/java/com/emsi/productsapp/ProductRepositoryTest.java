package com.emsi.productsapp;

import com.emsi.productsapp.entities.Product;
import com.emsi.productsapp.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveAndFindProduct() {
        Product p = new Product(null, "Test Produit", 100.0, 5, true);
        Product saved = productRepository.save(p);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Produit");
        System.out.println("✅ Produit sauvegardé : " + saved);
    }

    @Test
    void testFindAllProducts() {
        List<Product> products = productRepository.findAll();
        System.out.println("✅ Nombre de produits : " + products.size());
        products.forEach(System.out::println);
        assertThat(products).isNotNull();
    }

    @Test
    void testSearchByKeyword() {
        List<Product> results = productRepository.findByNameContainingIgnoreCase("laptop");
        System.out.println("✅ Produits contenant 'laptop' : " + results.size());
        results.forEach(System.out::println);
    }

    @Test
    void testPagination() {
        Page<Product> page = productRepository.findAll(PageRequest.of(0, 3));
        System.out.println("✅ Page 1 (3 éléments max) : " + page.getContent().size());
        System.out.println("   Total pages : " + page.getTotalPages());
        System.out.println("   Total éléments : " + page.getTotalElements());
        assertThat(page).isNotNull();
    }

    @Test
    void testDeleteProduct() {
        Product p = productRepository.save(new Product(null, "À supprimer", 50.0, 1, false));
        Long id = p.getId();
        productRepository.deleteById(id);
        assertThat(productRepository.findById(id)).isEmpty();
        System.out.println("✅ Produit supprimé avec succès (id=" + id + ")");
    }
}

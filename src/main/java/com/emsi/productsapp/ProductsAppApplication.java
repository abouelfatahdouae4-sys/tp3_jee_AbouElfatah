package com.emsi.productsapp;

import com.emsi.productsapp.entities.Product;
import com.emsi.productsapp.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProductsAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductsAppApplication.class, args);
    }

    /**
     * Initialisation de quelques produits au démarrage (test couche DAO)
     */
    @Bean
    CommandLineRunner initData(ProductRepository productRepository) {
        return args -> {
            productRepository.save(new Product(null, "Laptop Dell XPS", 12999.99, 10, false));
            productRepository.save(new Product(null, "Écran Samsung 27\"", 3499.00, 25, false));
            productRepository.save(new Product(null, "Clavier Mécanique", 899.50, 50, true));
            productRepository.save(new Product(null, "Souris Logitech MX", 699.00, 30, false));
            productRepository.save(new Product(null, "Casque Sony WH-1000", 2999.99, 15, true));
            productRepository.save(new Product(null, "Webcam Logitech C920", 1199.00, 20, false));
            productRepository.save(new Product(null, "SSD 1TB Samsung", 1599.00, 40, false));
            productRepository.save(new Product(null, "Hub USB-C 7 ports", 449.00, 60, true));
            productRepository.save(new Product(null, "Chargeur USB-C 65W", 299.00, 100, false));
            productRepository.save(new Product(null, "Tapis de souris XL", 199.00, 80, true));
        };
    }
}

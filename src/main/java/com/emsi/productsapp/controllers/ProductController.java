package com.emsi.productsapp.controllers;

import com.emsi.productsapp.entities.Product;
import com.emsi.productsapp.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    // ─────────────────────────────────────────────────────────
    // LISTE DES PRODUITS avec recherche et pagination
    // ─────────────────────────────────────────────────────────
    @GetMapping({"/", "/products"})
    public String listProducts(
            Model model,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Product> productsPage;
        if (keyword == null || keyword.isBlank()) {
            productsPage = productRepository.findAll(PageRequest.of(page, size));
        } else {
            productsPage = productRepository.findByNameContainingIgnoreCase(
                    keyword, PageRequest.of(page, size));
        }

        model.addAttribute("products", productsPage.getContent());
        model.addAttribute("totalPages", productsPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", size);
        return "products/list";
    }

    // ─────────────────────────────────────────────────────────
    // FORMULAIRE AJOUT
    // ─────────────────────────────────────────────────────────
    @GetMapping("/products/new")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("formTitle", "Ajouter un produit");
        model.addAttribute("formAction", "/products/save");
        return "products/form";
    }

    // ─────────────────────────────────────────────────────────
    // ENREGISTRER (Ajout)
    // ─────────────────────────────────────────────────────────
    @PostMapping("/products/save")
    public String saveProduct(
            @Valid @ModelAttribute("product") Product product,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("formTitle", "Ajouter un produit");
            model.addAttribute("formAction", "/products/save");
            return "products/form";
        }
        productRepository.save(product);
        return "redirect:/products";
    }

    // ─────────────────────────────────────────────────────────
    // FORMULAIRE ÉDITION
    // ─────────────────────────────────────────────────────────
    @GetMapping("/products/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé : " + id));
        model.addAttribute("product", product);
        model.addAttribute("formTitle", "Modifier le produit");
        model.addAttribute("formAction", "/products/update/" + id);
        return "products/form";
    }

    // ─────────────────────────────────────────────────────────
    // METTRE À JOUR
    // ─────────────────────────────────────────────────────────
    @PostMapping("/products/update/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute("product") Product product,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("formTitle", "Modifier le produit");
            model.addAttribute("formAction", "/products/update/" + id);
            return "products/form";
        }
        product.setId(id);
        productRepository.save(product);
        return "redirect:/products";
    }

    // ─────────────────────────────────────────────────────────
    // SUPPRIMER
    // ─────────────────────────────────────────────────────────
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(
            @PathVariable Long id,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page) {

        productRepository.deleteById(id);
        return "redirect:/products?page=" + page + "&keyword=" + keyword;
    }
}

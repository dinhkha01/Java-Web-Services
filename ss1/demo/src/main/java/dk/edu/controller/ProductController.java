package dk.edu.controller;

import dk.edu.entity.Product;
import dk.edu.repository.ProductRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductRepositoryImpl productRepository;
    @GetMapping("/list")
    public String listProducts(Model model) {
            List<Product> products = productRepository.findAll();
            model.addAttribute("products", products);
            model.addAttribute("pageTitle", "Danh sách sản phẩm");
            return "product/list";

    }
    @GetMapping("/add")
    public String showForm(Model model){
        model.addAttribute("product", new Product());
        return "product/add";
    }
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product ){
        productRepository.save(product);
        return "redirect:/products/list";
    }
    @GetMapping("/update/{id}")
    public String showUpdateForm(@ModelAttribute("id") Long id, Model model) {
        Optional<Product> product = productRepository.findById(id);
        model.addAttribute("product", product.get());
        return "product/update";
    }
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, @ModelAttribute Product product) {
        product.setId(id);
        productRepository.update(product);
        return "redirect:/products/list";
    }
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productRepository.deleteById(id);
        return "redirect:/products/list";
    }
}

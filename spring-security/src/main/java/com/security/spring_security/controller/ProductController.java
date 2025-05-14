package com.security.spring_security.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private record Product(Integer productId , String productName , double price){}

    List<Product> products = new ArrayList<>(
            List.of(new Product(1,"laptop"  ,50000),
                    new Product(2,"Mobile Phone",20000))

    );

    @GetMapping
    public List<Product> getProduct(){
        return products;
    }

    @PostMapping
    public Product saveProduct(@RequestBody Product product){
        products.add(product);
        return product;
    }

}

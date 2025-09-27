package com.teamshyt.controller;

import com.teamshyt.dto.CategoryRequest;
import com.teamshyt.dto.CategoryResponse;
import com.teamshyt.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest request){
       return ResponseEntity.ok( categoryService.createCategory(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id,
                                                           @RequestBody CategoryRequest request){
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category Deleted Successfully");
    }

    @GetMapping("/get")
    public ResponseEntity<List<CategoryResponse>> getAllCategory(){
        return ResponseEntity.ok(categoryService.getAllCategory());
    }
}


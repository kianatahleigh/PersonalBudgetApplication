/* this is the service layer for category. It handles operations such as creating and retrieving
categories. It ensures data integrity through transactional boundaries and throws
 exceptions when the requested categories are not found. It acts as an inermediary
 between the controller layer and Category Repository.*/


package edu.usca.rmoment.personalbudgetapplication.services;





import jakarta.transaction.Transactional;
import edu.usca.rmoment.personalbudgetapplication.model.Category;
import edu.usca.rmoment.personalbudgetapplication.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;





@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }


    public Category getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category with id " + id + " not found"));
    }

    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }

    public List<Category> getCategoriesByUserId(long userId) {
        return categoryRepository.findByUserId(userId);

    }



}

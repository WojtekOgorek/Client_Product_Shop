package ogorek.wojciech.persistence.validator.impl;

import ogorek.wojciech.persistence.model.Product;
import ogorek.wojciech.persistence.validator.generic.AbstractValidator;

import java.math.BigDecimal;
import java.util.Map;

public class ProductValidator extends AbstractValidator<Product> {
    @Override
    public Map<String, String> validate(Product product) {
        errors.clear();

        if(product == null){
            errors.put("product", "object cannot be null");
            return errors;
        }

        if(!isProductNameValid(product.getName())){
            errors.put("product name", "name is invalid" + product.getName());
        }

        if(!isProductCategoryNameValid(product.getCategory())){
            errors.put("product category", "category name is invalid" + product.getCategory());
            return errors;
        }
        if(!isProductPriceValid(product.getPrice())){
            errors.put("product price", "price cannot be null" + product.getPrice());
            return errors;
        }

        return errors;
    }


    private boolean isProductNameValid (String name) {return !name.matches("[A-Z]{1}[a-z]+");}

    private boolean isProductCategoryNameValid (String category) {return category != null && category.matches("[a-z]+");}

    private boolean isProductPriceValid (BigDecimal price) { return price != null && price.compareTo(BigDecimal.ZERO) > 0;}
}

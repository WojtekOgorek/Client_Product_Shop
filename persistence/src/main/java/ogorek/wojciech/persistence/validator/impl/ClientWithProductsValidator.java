package ogorek.wojciech.persistence.validator.impl;

import ogorek.wojciech.persistence.model.Client;
import ogorek.wojciech.persistence.model.ClientWithProducts;
import ogorek.wojciech.persistence.model.Product;
import ogorek.wojciech.persistence.validator.generic.AbstractValidator;

import java.util.Map;
import java.util.Set;

public class ClientWithProductsValidator extends AbstractValidator<ClientWithProducts> {

    @Override
    public Map<String, String> validate(ClientWithProducts clientWithProducts) {

        errors.clear();

        if(clientWithProducts == null){
            errors.put("ClientWithProducts object", "cannot be null");
            return errors;
        }
        if(!isClientValid(clientWithProducts.getClient())){
            errors.put("ClientWithProduct object", "Client error is: " + clientWithProducts.getClient());
        }
        if(!areProductsValid(clientWithProducts.getProducts())){
            errors.put("ClientWithProduct object", "Product error is: " + clientWithProducts.getProducts());
        }

        return errors;
    }

    private boolean isClientValid(Client client){
        ClientValidator clientValidator = new ClientValidator();
        return client != null && !clientValidator.hasErrors();

    }

    private boolean areProductsValid(Set<Product> products){
        ProductValidator productValidator = new ProductValidator();
        return products != null && !products.stream().anyMatch(p->productValidator.hasErrors());
    }

}

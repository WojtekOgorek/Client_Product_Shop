package ogorek.wojciech.service;

import ogorek.wojciech.persistence.model.Client;
import ogorek.wojciech.persistence.model.ClientWithProducts;
import ogorek.wojciech.persistence.model.Product;


import java.util.Map;
import java.util.Set;

public class ShoppingService {

    private Map<Client, Map<Product,Long>> clientsWithProducts;


    public ShoppingService(Set<String> jsonFilenames){
        clientsWithProducts = getCustomersWithOrders(jsonFilenames);
    }

    //TODO
    private Set<ClientWithProducts> readClientsFromJsonFile(Set<String> jsonFilenames){
        return null;
    }

    //TODO
    private Map<Client, Map<Product,Long>> getCustomersWithOrders(Set<String> jsonFilenames){
        return null;
    }
}

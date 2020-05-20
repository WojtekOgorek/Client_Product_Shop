package ogorek.wojciech.service;

import ogorek.wojciech.persistence.converter.JsonClientWithProductsConverter;
import ogorek.wojciech.persistence.exception.AppException;
import ogorek.wojciech.persistence.model.Client;
import ogorek.wojciech.persistence.model.ClientWithProducts;
import ogorek.wojciech.persistence.model.Product;
import ogorek.wojciech.persistence.validator.impl.ClientWithProductsValidator;


import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ShoppingService {

    private Map<Client, Map<Product,Long>> clientsWithProducts;


    public ShoppingService(Set<String> jsonFilenames){
        clientsWithProducts = getCustomersWithOrders(jsonFilenames);
    }


    private Set<ClientWithProducts> readClientsFromJsonFile(Set<String> jsonFilenames){
        var clientWithProductsValidator = new ClientWithProductsValidator();
        var counter = new AtomicInteger(1);

        return jsonFilenames
                .stream()
                .flatMap(jsonFilename ->
                        new JsonClientWithProductsConverter(jsonFilename)
                                .fromJson()
                                .orElseThrow(() -> new AppException("shopping service - cannot read data from json file"))
                                .stream()
                                .filter(client -> {
                                    var errors = clientWithProductsValidator.validate(client);
                                    if (clientWithProductsValidator.hasErrors()) {
                                        System.out.println("\n-------------- validation errors for client no. " + counter.get() + " in file " + jsonFilename + " ----------");
                                        errors.forEach((k, v) -> System.out.println(k + ": " + v));
                                        System.out.println("\n\n");
                                    }
                                    counter.incrementAndGet();
                                    return !clientWithProductsValidator.hasErrors();
                                }))
                .collect(Collectors.toSet());
    }


    private Map<Client, Map<Product,Long>> getCustomersWithOrders(Set<String> jsonFilenames){
        Map<Client, Map<Product, Long>> m1 = readClientsFromJsonFile(jsonFilenames)
                .stream()
                .collect(Collectors.groupingBy(
                        ClientWithProducts::getClient,
                        Collectors.collectingAndThen(
                                Collectors.flatMapping(cwp -> cwp.getProducts().stream(), Collectors.toList()),
                                items -> items
                                        .stream()
                                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                        )
                ));
        System.out.println(m1);
        return m1;
    }
}

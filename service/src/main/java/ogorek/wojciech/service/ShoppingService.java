package ogorek.wojciech.service;

import ogorek.wojciech.persistence.converter.JsonClientWithProductsConverter;
import ogorek.wojciech.persistence.exception.AppException;
import ogorek.wojciech.persistence.exception.JsonException;
import ogorek.wojciech.persistence.model.Client;
import ogorek.wojciech.persistence.model.ClientWithProducts;
import ogorek.wojciech.persistence.model.Product;
import ogorek.wojciech.persistence.validator.impl.ClientWithProductsValidator;


import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ShoppingService {

    private Map<Client, Map<Product, Long>> clientsWithProducts;


    public ShoppingService(Set<String> jsonFilenames) {
        clientsWithProducts = getCustomersWithOrders(jsonFilenames);
    }


    private Set<ClientWithProducts> readClientsFromJsonFile(Set<String> jsonFilenames) {
        var clientWithProductsValidator = new ClientWithProductsValidator();
        var counter = new AtomicInteger(1);

        return jsonFilenames
                .stream()
                .flatMap(jsonFilename ->
                        new JsonClientWithProductsConverter(jsonFilename)
                                .fromJson()
                                .orElseThrow(() -> new JsonException("shopping service cannot read data from json file"))
                                .stream()
                                .filter(client -> {
                                    var errors = clientWithProductsValidator.validate(client);
                                    if (clientWithProductsValidator.hasErrors()) {
                                        System.out.println("----------- validation error for client nr." + counter.get() + " in file " + jsonFilename + " -------------");
                                        errors.forEach((k, v) -> System.out.println(k + ": " + v));
                                        System.out.println("/n/n");
                                    }
                                    counter.getAndIncrement();
                                    return !clientWithProductsValidator.hasErrors();
                                }))
                .collect(Collectors.toSet());

    }


    private Map<Client, Map<Product, Long>> getCustomersWithOrders(Set<String> jsonFilenames) {
        Map<Client, Map<Product, Long>> m1 = readClientsFromJsonFile(jsonFilenames)
                .stream()
                .collect(Collectors.groupingBy(ClientWithProducts::getClient,
                        Collectors.collectingAndThen(
                                Collectors.flatMapping(cwp -> cwp.getProducts().stream(), Collectors.toList()),
                                elements -> elements
                                        .stream()
                                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))


                        )));

        return m1;
    }

    //method 1. Show Client that paid the most for his products

    public Client getClientWhoPaidTheMost() {

        return clientsWithProducts
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        v -> countProductsPrice(v.getValue())
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow()
                .getKey();

    }

    //method 2.In separate method show client that paid the most in selected category.
    //Name of category pass as an method argument

    public Client getCategoryHighestPaymentClient(String category){
        if(category == null){
            throw new AppException("getCategoryHighestPaymentClient value is null");
        }

        return clientsWithProducts
                .entrySet()
                .stream()
                .max(Comparator.comparing(client -> totalPriceForProductsFromCategory(client.getValue(), category)))
                .orElseThrow()
                .getKey();

    }

    private BigDecimal totalPriceForProductsFromCategory(Map<Product, Long> products, String category){
        return products
                .entrySet()
                .stream()
                .filter(e -> e.getKey().getCategory().equals(category))
                .flatMap(value -> Collections.nCopies(value.getValue().intValue(), value.getKey()).stream())
                .map(p -> p.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    private BigDecimal countProductsPrice(Map<Product, Long> products) {

        return products
                .entrySet()
                .stream()
                .map(value -> value.getKey().getPrice().multiply(BigDecimal.valueOf(value.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

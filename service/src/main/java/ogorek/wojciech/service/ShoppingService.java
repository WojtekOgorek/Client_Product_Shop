package ogorek.wojciech.service;

import ogorek.wojciech.persistence.converter.JsonClientWithProductsConverter;
import ogorek.wojciech.persistence.exception.AppException;
import ogorek.wojciech.persistence.exception.JsonException;
import ogorek.wojciech.persistence.model.Client;
import ogorek.wojciech.persistence.model.ClientWithProducts;
import ogorek.wojciech.persistence.model.Product;
import ogorek.wojciech.persistence.validator.impl.ClientWithProductsValidator;
import ogorek.wojciech.service.enums.Category;
import ogorek.wojciech.service.statistics.Statistic;
import org.eclipse.collections.impl.collector.Collectors2;


import java.math.BigDecimal;
import java.util.*;
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
                                        System.out.println("\n----------- validation error for client nr." + counter.get() + " in file " + jsonFilename + " -------------");
                                        errors.forEach((k, v) -> System.out.println(k + ": " + v));
                                        System.out.println("\n\n");
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

    public Client getCategoryHighestPaymentClient(String category) {
        if (category == null) {
            throw new AppException("getCategoryHighestPaymentClient value is null");
        }

        return clientsWithProducts
                .entrySet()
                .stream()
                .max(Comparator.comparing(product -> totalPriceForProductsFromCategory(product.getValue(), category)))
                .orElseThrow()
                .getKey();

    }

    private BigDecimal totalPriceForProductsFromCategory(Map<Product, Long> products, String category) {
        return products
                .entrySet()
                .stream()
                .filter(e -> e.getKey().getCategory().equals(category))
                .map(price -> price.getKey().getPrice().multiply(BigDecimal.valueOf(price.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    //method 3. Create a map, where key is product category and value
    //is age of clients that bought the most in this category.

    public Map<String, Integer> getClientsAgeToCategory() {
        return clientsWithProducts
                .entrySet()
                .stream()
                .collect(Collectors.groupingBy(
                        category -> getMaxCategory(category.getValue()),
                        Collectors.collectingAndThen(
                                Collectors.mapping(age -> age.getKey().getAge(), Collectors.toList()),
                                items -> items
                                        .stream()
                                        .max(Integer::compareTo)
                                        .orElseThrow()
                        )));
    }


    private String getMaxCategory(Map<Product, Long> products) {

        return products
                .entrySet()
                .stream()
                .flatMap(product -> Collections.nCopies(product.getValue().intValue(), product.getKey().getCategory()).stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow()
                .getKey();
    }

    //method 4.Returns map with average, min, max price of specific category

    public Map<String, Statistic> getCategoryAndPriceStatistics() {

        return clientsWithProducts
                .entrySet()
                .stream()
                .flatMap(mapValue -> mapValue.getValue()
                        .entrySet()
                        .stream()
                        .flatMap(product -> Collections.nCopies(product.getValue().intValue(), product.getKey()).stream()))
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.collectingAndThen(Collectors.toList(), this::productsStatistic)));


    }

    private Statistic productsStatistic(List<Product> products) {
        var stats = products
                .stream()
                .collect(Collectors2.summarizingBigDecimal(Product::getPrice));
        return Statistic
                .builder()
                .min(stats.getMin())
                .max(stats.getMax())
                .avg(stats.getAverage())
                .build();
    }

    //   method 5.Map that show clients that bought the most products of specific category.

    public Map<String, Client> getClientsAndCategory() {
        return clientsWithProducts
                .entrySet()
                .stream()
                .flatMap(mapValue -> mapValue.getValue()
                        .entrySet()
                        .stream()
                        .flatMap(category -> Collections.nCopies(category.getValue().intValue(), category.getKey().getCategory()).stream()))
                .distinct()
                .collect(Collectors.toMap(category -> category, this::findClientWithMaxCategory));

    }

    private Client findClientWithMaxCategory(String category) {
        return clientsWithProducts
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        v -> countProductsCategory(v.getValue(), category)

                ))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .orElseThrow()
                .getKey();
    }

    private long countProductsCategory(Map<Product, Long> products, String category) {
        return products
                .entrySet()
                .stream()
                .filter(e -> e.getKey().getCategory().equals(category))
                .map(Map.Entry::getValue)
                .reduce(0L, Long::sum);

    }

    //method 6. Check if client is capable to pay for his order.

    public Map<String, BigDecimal> clientsWallet() {
        return clientsWithProducts
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        k -> k.getKey().getNameAndSurname(k.getKey()),
                        this::calculateDebt
                ));

    }

    private BigDecimal calculateDebt(Map.Entry<Client, Map<Product, Long>> entry) {
        var clientCash = entry.getKey().getCash();
        var valueToPay = countProductsPrice(entry.getValue());
        return clientCash.compareTo(valueToPay) < 0 ? clientCash.subtract(valueToPay) : BigDecimal.ZERO;
    }

    private BigDecimal countProductsPrice(Map<Product, Long> products) {

        return products
                .entrySet()
                .stream()
                .map(value -> value.getKey().getPrice().multiply(BigDecimal.valueOf(value.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    //method 7. Show all customers
    public List<Client> allCustomers(){
        return new ArrayList<>(clientsWithProducts.keySet());
    }


}

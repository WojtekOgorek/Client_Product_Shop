# Client Product Shop 

Client product shop is a simple multi-module java application to show how you can use more advanced java streams. 
The complexity of those appears because main data structure is Map nested in Map -> Map<Client, Map<Product, Long>>.  

## Installation

Use maven -> [link](https://maven.apache.org/download.cgi) <- to install client product shop.

```bash
#main folder
mvn clean install
#go to ui folder 
cd ui
#go to target folder
cd target
#start app
java -jar --enable-preview ui.jar
```

## Usage

```java
/*
 *
 *    ----------  APPLICATION MENU ----------
 *
 */
public class MenuService {

    private int chooseOptionForMainMenu() {

        System.out.println("1.Show client that paid the most for all of his purchases");
        System.out.println("2.Show client that paid the most in selected category");
        System.out.println("3.Show category that clients bought the most grouped by age");
        System.out.println("4.Show category and it's max/min/avg price");
        System.out.println("5.Show clients that bought the most products in specific category");
        System.out.println("6.Show if client can pay for his purchases");
        System.out.println("7.Show all customers");
        System.out.println("8.Exit");
        return UserDataService.getInt("Choose option");
    }
};
    /*
     *
     *    ----------  SERVICE METHOD EXAMPLE ----------
     *
     */

public class MethodExample() {
    
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
    }

```
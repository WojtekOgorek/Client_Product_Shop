package ogorek.wojciech.ui;

import ogorek.wojciech.service.ShoppingService;

import java.util.Set;

public class App
{

    public static void main( String[] args ) {

        final Set<String> jsonFiles = Set.of(
                "<filepath_to_resources>/clients1.json",
                "<filepath_to_resources>/clients2.json",
                "<filepath_to_resources>/clients3.json");

        var shoppingService = new ShoppingService(jsonFiles);
        MenuService menuService = new MenuService(shoppingService);
        menuService.mainMenu();

    }

}


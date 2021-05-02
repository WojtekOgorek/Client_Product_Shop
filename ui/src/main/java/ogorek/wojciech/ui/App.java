package ogorek.wojciech.ui;

import ogorek.wojciech.service.ShoppingService;

import java.util.Set;

public class App
{

    public static void main( String[] args ) {

        final Set<String> jsonFiles = Set.of("clients1.json", "clients2.json", "clients3.json");

        var shoppingService = new ShoppingService(jsonFiles);
        MenuService menuService = new MenuService(shoppingService);
        menuService.mainMenu();

    }

}


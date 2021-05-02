package ogorek.wojciech.ui;

import ogorek.wojciech.service.ShoppingService;

import java.util.Set;

public class App
{

    public static void main( String[] args ) {

        final Set<String> jsonFiles = Set.of(
                "C:\\Work\\KmPrograms\\Java\\Coding\\Projects_GIT\\Client_Product_Shop\\service\\src\\main\\resources\\clients1.json",
                "C:\\Work\\KmPrograms\\Java\\Coding\\Projects_GIT\\Client_Product_Shop\\service\\src\\main\\resources\\clients2.json",
                "C:\\Work\\KmPrograms\\Java\\Coding\\Projects_GIT\\Client_Product_Shop\\service\\src\\main\\resources\\clients3.json");

        var shoppingService = new ShoppingService(jsonFiles);
        MenuService menuService = new MenuService(shoppingService);
        menuService.mainMenu();

    }

}


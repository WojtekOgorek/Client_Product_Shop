package ogorek.wojciech.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import ogorek.wojciech.persistence.exception.AppException;
import ogorek.wojciech.service.ShoppingService;
import ogorek.wojciech.service.utils.UserDataService;


@RequiredArgsConstructor
public class MenuService {

    private final ShoppingService shoppingService;

    public void mainMenu() {
        while (true) {
            try {
                int option = chooseOptionForMainMenu();
                switch (option) {
                    case 1 -> option1();
                    case 2 -> option2();
                    case 3 -> option3();
                    case 4 -> option4();
                    case 5 -> option5();
                    case 6 -> option6();
                    case 7 -> option7();
                    case 8 -> {
                        UserDataService.close();
                        System.out.println("Have a nice day!");
                        return;
                    }
                    default -> System.out.println("\nWrong option, try again\n");
                }
            } catch (AppException e) {
                e.getMessage();
            }
        }
    }

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

    private void option1() {
        var clientWhichPaidTheMost = shoppingService.getClientWhoPaidTheMost();
        System.out.println(toJson(clientWhichPaidTheMost));
    }

    private void option2() {
        var category = UserDataService.getCategory().toString().toLowerCase();
        var clientToCategory = shoppingService.getCategoryHighestPaymentClient(category);
        System.out.println(toJson(clientToCategory));
    }

    private void option3() {
        var ageToCategoryMap = shoppingService.getClientsAgeToCategory();
        System.out.println(toJson(ageToCategoryMap));
    }

    private void option4() {
        var statisticMap = shoppingService.getCategoryAndPriceStatistics();
        System.out.println(toJson(statisticMap));
    }

    private void option5() {
        var categoryAndClientMap = shoppingService.getClientsAndCategory();
        System.out.println(toJson(categoryAndClientMap));
    }

    private void option6() {
        var canClientPay = shoppingService.clientsWallet();
        System.out.println(toJson(canClientPay));
    }

    private void option7(){
        var customers = shoppingService.allCustomers();
        System.out.println(toJson(customers));
    }


    private static <T> String toJson(T t){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(t);
    }


}

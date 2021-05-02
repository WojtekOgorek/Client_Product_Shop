package ogorek.wojciech.service.utils;

import lombok.experimental.UtilityClass;
import ogorek.wojciech.persistence.exception.AppException;
import ogorek.wojciech.service.enums.Category;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public class UserDataService {

    private Scanner sc = new Scanner(System.in);

    public int getInt(String message){
        System.out.println(message);
        var value = sc.nextLine();

        if(!value.matches("\\d+")){
            throw new AppException("value must be integer");
        }
        return Integer.parseInt(value);
    }

    public Category getCategory(){
        AtomicInteger counter = new AtomicInteger(1);
        Arrays
                .stream(Category.values())
                .forEach(value -> System.out.println(counter.getAndIncrement() + ". " + value));
        int option = getInt("Choose an option");
        if(option < 1 || option > Category.values().length){
            throw new AppException("incorrect category int number");
        }

        return Category.values()[option - 1];
    }

    public void close(){
        if(sc != null){
            sc.close();
            sc = null;
        }
    }
}

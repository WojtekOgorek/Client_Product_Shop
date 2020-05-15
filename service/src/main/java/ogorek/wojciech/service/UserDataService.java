package ogorek.wojciech.service;

import lombok.experimental.UtilityClass;
import ogorek.wojciech.persistence.exception.AppException;

import java.util.Scanner;

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

    public void close(){
        if( sc != null){
            sc.close();
            sc = null;
        }
    }
}

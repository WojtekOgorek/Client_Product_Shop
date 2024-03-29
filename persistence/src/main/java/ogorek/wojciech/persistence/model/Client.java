package ogorek.wojciech.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client {
    private String name;
    private String surname;
    private int age;
    private BigDecimal cash;

    public String getNameAndSurname(Client client){
        return client.getName() + " " + client.getSurname();
    }
}

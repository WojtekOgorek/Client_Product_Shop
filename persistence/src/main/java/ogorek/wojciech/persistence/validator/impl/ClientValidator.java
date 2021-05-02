package ogorek.wojciech.persistence.validator.impl;

import ogorek.wojciech.persistence.model.Client;
import ogorek.wojciech.persistence.validator.generic.AbstractValidator;

import java.math.BigDecimal;
import java.util.Map;

public class ClientValidator extends AbstractValidator<Client> {

    @Override
    public Map<String, String> validate(Client client) {

        errors.clear();

        if(client == null){
            errors.put("client", "object is null");
            return errors;
        }
        if(!isClientNameValid(client.getName())){
            errors.put("client name", "name object cannot be null and first letter must be uppercase" + client.getName());
        }
        if(!isClientSurnameValid(client.getSurname())){
            errors.put("client surname", "surname object cannot be null and first letter must be uppercase" + client.getSurname());
        }
        if(!isClientAgeValid(client.getAge())){
            errors.put("client age", "age cannot be null and cannot be lesser than 0" + client.getAge());
        }
        if(!isClientCashValid(client.getCash())){
            errors.put("client cash", "cash object cannot be null" + client.getCash());
        }


        return errors;
    }

    private boolean isClientNameValid (String name) {return name != null && name.matches("[A-Z]{1}[a-z]+");}

    private boolean isClientSurnameValid (String surname) {return surname != null && surname.matches("[A-Z]{1}[a-z]+");}

    private boolean isClientAgeValid (int age) { return age > 0;}

    private boolean isClientCashValid (BigDecimal cash) {return cash != null;}


}

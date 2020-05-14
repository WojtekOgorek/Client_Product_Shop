package ogorek.wojciech.persistence.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ClientWithProducts {
    private Client client;
    private Set<Product> products;

}

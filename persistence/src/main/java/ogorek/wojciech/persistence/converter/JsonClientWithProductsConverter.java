package ogorek.wojciech.persistence.converter;

import ogorek.wojciech.persistence.model.ClientWithProducts;

import java.util.List;

public class JsonClientWithProductsConverter extends JsonConverter<List<ClientWithProducts>> {
    public JsonClientWithProductsConverter(String jsonFilename) {
        super(jsonFilename);
    }
}

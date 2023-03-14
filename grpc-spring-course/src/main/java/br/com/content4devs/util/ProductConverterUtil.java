package br.com.content4devs.util;

import br.com.content4devs.domain.Product;
import br.com.content4devs.dto.ProductInputDTO;
import br.com.content4devs.dto.ProductOutputDTO;

import javax.persistence.Id;

public class ProductConverterUtil {
    public static ProductOutputDTO productToProductOutputDto(Product product){
        return new ProductOutputDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getQuantityInStock()
        );
    }
    public static Product productInputDtoToProduct(ProductInputDTO product){
        return new Product(
                null, //quando recebe o input, está se criando o id
                product.getName(),
                product.getPrice(),
                product.getQuantityInStock()

        );
    }

}

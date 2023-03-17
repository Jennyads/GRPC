package br.com.content4devs.service.imlp;

import br.com.content4devs.domain.Product;
import br.com.content4devs.dto.ProductInputDTO;
import br.com.content4devs.dto.ProductOutputDTO;
import br.com.content4devs.repository.ProductRepository;
import br.com.content4devs.service.imlp.impl.ProductServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    @Mock  //aqui é interface
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;


    @Test
    @DisplayName("when create product service is call with valid data a product is returned")
    public void createProductSucessTest() {
        Product product = new Product(1L, "product name", 10.00, 10);
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);

        ProductInputDTO inputDTO = new ProductInputDTO("product name", 10.00, 10);
        ProductOutputDTO outputDTO = productService.create(inputDTO);

        Assertions.assertThat(outputDTO)
                .usingRecursiveComparison()
                .isEqualTo(product);
    }




}

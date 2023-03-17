package br.com.content4devs.service.imlp.impl;
import br.com.content4devs.dto.ProductInputDTO;
import br.com.content4devs.dto.ProductOutputDTO;
import br.com.content4devs.repository.ProductRepository;
import br.com.content4devs.service.imlp.IproductService;
import br.com.content4devs.util.ProductConverterUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements IproductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }



    @Override
    public ProductOutputDTO create(ProductInputDTO inputDTO) {
        var product = ProductConverterUtil.productInputDtoToProduct(inputDTO);
        var productCreated = this.productRepository.save(product);
        return ProductConverterUtil.productToProductOutputDto(productCreated);
    }

    @Override
    public ProductOutputDTO findById(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<ProductOutputDTO> findAll() {
        return null;
    }
}


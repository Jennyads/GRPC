package br.com.content4devs.service.impl;
import br.com.content4devs.dto.ProductInputDTO;
import br.com.content4devs.dto.ProductOutputDTO;
import br.com.content4devs.exception.ProductAlreadyExistsException;
import br.com.content4devs.exception.ProductNotFoundException;
import br.com.content4devs.repository.ProductRepository;
import br.com.content4devs.service.IproductService;
import br.com.content4devs.util.ProductConverterUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IproductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }



    @Override
    public ProductOutputDTO create(ProductInputDTO inputDTO) {
        checkDuplicity(inputDTO.getName());
        var product = ProductConverterUtil.productInputDtoToProduct(inputDTO);
        var productCreated = this.productRepository.save(product);
        return ProductConverterUtil.productToProductOutputDto(productCreated);
    }

    @Override
    public ProductOutputDTO findById(Long id) {
        var product = this.productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return ProductConverterUtil.productToProductOutputDto(product);
    }

    @Override
    public void delete(Long id) {
        var product = this.productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        this.productRepository.delete(product);

    }

    @Override
    public List<ProductOutputDTO> findAll() {
        var products = this.productRepository.findAll();
        return products.stream()
                .map(ProductConverterUtil::productToProductOutputDto)
                .collect(Collectors.toList());

    }

    private void checkDuplicity(String name){
        this.productRepository.findByNameIgnoreCase(name)
                .ifPresent(e -> {
                    throw new ProductAlreadyExistsException(name);

                });
    }
}


package br.com.content4devs.service.imlp;

import br.com.content4devs.dto.ProductInputDTO;
import br.com.content4devs.dto.ProductOutputDTO;

import java.util.List;


public interface IproductService {
    ProductOutputDTO create(ProductInputDTO inputDTO);
    ProductOutputDTO findById(Long id);
    void delete(Long id);
    List<ProductOutputDTO> findAll();
}


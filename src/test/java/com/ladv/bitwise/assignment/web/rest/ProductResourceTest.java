package com.ladv.bitwise.assignment.web.rest;

import com.ladv.bitwise.assignment.AssignmentApplication;
import com.ladv.bitwise.assignment.TestUtil;
import com.ladv.bitwise.assignment.domain.Product;
import com.ladv.bitwise.assignment.domain.User;
import com.ladv.bitwise.assignment.repository.ProductRepository;
import com.ladv.bitwise.assignment.service.ProductService;
import com.ladv.bitwise.assignment.service.dto.ProductDTO;
import com.ladv.bitwise.assignment.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AssignmentApplication.class})
public class ProductResourceTest {
    public static final String DEFAULT_NAME = "test";
    public static final Integer DEFAULT_PRICE = 100;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    @Autowired
    private ExceptionTranslator exceptionTranslator;
    @Autowired
    private EntityManager em;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        ProductResource productResource = new ProductResource(productService);
        mockMvc = MockMvcBuilders.standaloneSetup(productResource)
                .setControllerAdvice(exceptionTranslator)
                .build();
    }

    @Test
    @Transactional
    public void saveProduct() throws Exception {
        User user = UserResourceTest.createUser(em);
        long productsCountBeforeSave = productRepository.count();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(DEFAULT_NAME);
        productDTO.setPrice(DEFAULT_PRICE);
        productDTO.setUserId(user.getId());
        mockMvc.perform(post("/api/products")
                .content(TestUtil.convertObjectToJsonBytes(productDTO))
                .contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());

        long updatedProductsCount = productRepository.count();
        assertThat(updatedProductsCount).isEqualTo(productsCountBeforeSave + 1);
        Optional<Product> lastCreatedProduct = productRepository.findTop1ByIdIsNotNullOrderByIdDesc();
        assertThat(lastCreatedProduct.isPresent()).isTrue();
        assertThat(lastCreatedProduct.get().getName()).isEqualTo(DEFAULT_NAME);
        assertThat(lastCreatedProduct.get().getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(lastCreatedProduct.get().getUser()).isNotNull();
        assertThat(lastCreatedProduct.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @Transactional
    public void getAllUsersAfterSave() throws Exception {
        //create product
        saveProduct();
        mockMvc.perform(get("/api/products")
                .contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void getUserByIdAfterSave() throws Exception {
        //create user
        saveProduct();
        Product lastCreatedProduct = productRepository.findTop1ByIdIsNotNullOrderByIdDesc().get();
        mockMvc.perform(get("/api/products/" + lastCreatedProduct.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.price", is(DEFAULT_PRICE)))
                .andExpect(jsonPath("$.name", is(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void when_product_not_exists_receive_404() throws Exception {
        //create product
        saveProduct();
        Product lastCreatedProduct = productRepository.findTop1ByIdIsNotNullOrderByIdDesc().get();
        mockMvc.perform(get("/api/products/" + (lastCreatedProduct.getId() + 1) )
                .contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }
}
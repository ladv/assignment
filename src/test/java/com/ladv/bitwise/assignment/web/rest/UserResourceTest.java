package com.ladv.bitwise.assignment.web.rest;

import com.ladv.bitwise.assignment.AssignmentApplication;
import com.ladv.bitwise.assignment.TestUtil;
import com.ladv.bitwise.assignment.domain.User;
import com.ladv.bitwise.assignment.repository.UserRepository;
import com.ladv.bitwise.assignment.service.UserService;
import com.ladv.bitwise.assignment.service.dto.UserDTO;
import com.ladv.bitwise.assignment.service.mapper.UserMapper;
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
public class UserResourceTest {
    public static final String DEFAULT_ADDRESS = "London";
    public static final String DEFAULT_NAME = "test";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        UserResource userResource = new UserResource(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userResource)
                .setControllerAdvice(exceptionTranslator)
                .build();
    }

    public static User createUser(EntityManager em) {
        User user = new User();
        user.setName(DEFAULT_NAME);
        user.setAddress(DEFAULT_ADDRESS);
        em.persist(user);
        em.flush();
        return user;
    }

    @Test
    @Transactional
    public void saveUser() throws Exception {
        long usersCountBeforeSave = userRepository.count();
        UserDTO userDTO = new UserDTO();
        userDTO.setName(DEFAULT_NAME);
        userDTO.setAddress(DEFAULT_ADDRESS);

        mockMvc.perform(post("/api/users")
                .content(TestUtil.convertObjectToJsonBytes(userDTO))
                .contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());

        long updatedUsersCount = userRepository.count();
        assertThat(updatedUsersCount).isEqualTo(usersCountBeforeSave + 1);
        Optional<User> lastCreatedUser = userRepository.findTop1ByIdIsNotNullOrderByIdDesc();
        assertThat(lastCreatedUser.isPresent()).isTrue();
        assertThat(lastCreatedUser.get().getName()).isEqualTo(userDTO.getName());
        assertThat(lastCreatedUser.get().getAddress()).isEqualTo(userDTO.getAddress());
    }

    @Test
    @Transactional
    public void getAllUsersAfterSave() throws Exception {
        //create user
        saveUser();
        mockMvc.perform(get("/api/users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @Test
    @Transactional
    public void getUserByIdAfterSave() throws Exception {
        //create user
        saveUser();
        User lastCreatedUser = userRepository.findTop1ByIdIsNotNullOrderByIdDesc().get();
        mockMvc.perform(get("/api/users/" + lastCreatedUser.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(DEFAULT_NAME)))
                .andExpect(jsonPath("$.address", is(DEFAULT_ADDRESS)));
    }

    @Test
    @Transactional
    public void when_user_not_exists_receive_404() throws Exception {
        //create user
        saveUser();
        User lastCreatedUser = userRepository.findTop1ByIdIsNotNullOrderByIdDesc().get();
        mockMvc.perform(get("/api/users/" + (lastCreatedUser.getId() + 1) )
                .contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }
}
// package com.example.Rider_Co.tests.controllerTests;

// import com.example.Rider_Co.controllers.ApiKeyController;
// import com.example.Rider_Co.models.ApiKeyManager;
// import com.example.Rider_Co.serviceInterfaces.ApiKeyServiceInterface;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.BeforeEach;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;

// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.transaction.annotation.Transactional;

// import java.util.List;

// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest(ApiKeyController.class)
// @AutoConfigureMockMvc
// @Transactional
// public class ApiKeyControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private ApiKeyServiceInterface apiKeyService;

//     private final ObjectMapper objectMapper = new ObjectMapper();

//     @BeforeEach
//     void setUp() {
//         // No setup needed for now
//     }

//     @Test
//     void testCreateApi() throws Exception {
//         String generatedKey = "API123KEY";

//         when(apiKeyService.createApiKey()).thenReturn(generatedKey);

//         mockMvc.perform(post("/api"))
//                 .andExpect(status().isCreated())
//                 .andExpect(content().string(generatedKey));
//     }

//     @Test
//     void testListApiKeys() throws Exception {
//         ApiKeyManager key1 = new ApiKeyManager();
//         key1.setApiId(123);
//         key1.setApiKeyIdentifier("ID1");

//         ApiKeyManager key2 = new ApiKeyManager();
//         key2.setApiId(456);
//         key2.setApiKeyIdentifier("ID2");

//         when(apiKeyService.getApiKeysList()).thenReturn(List.of(key1, key2));

//         mockMvc.perform(get("/api"))
//                 .andExpect(status().isFound())
//                 .andExpect(jsonPath("$.length()").value(2))
//                 .andExpect(jsonPath("$[0].apiKey").value("API123"))
//                 .andExpect(jsonPath("$[1].identifier").value("ID2"));
//     }

//     @Test
//     void testDeleteApiKey() throws Exception {
//         String identifier = "ID1";
//         String expectedResponse = "API key deleted successfully";

//         when(apiKeyService.deleteApiKey(identifier)).thenReturn(expectedResponse);

//         mockMvc.perform(delete("/api/{apiKeyIdentifier}", identifier))
//                 .andExpect(status().isAccepted())
//                 .andExpect(content().string(expectedResponse));
//     }
// }

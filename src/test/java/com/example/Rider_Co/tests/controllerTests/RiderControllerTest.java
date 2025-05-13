package com.example.Rider_Co.tests.controllerTests;

import com.example.Rider_Co.models.Rider;
import com.example.Rider_Co.serviceInterfaces.RiderServiceInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RiderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RiderServiceInterface riderService;

    @MockBean
    private Rider rider;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String jwtToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2hAZ21haWwuY29tIiwicm9sZSI6IlJJREVSIiwiaWF0IjoxNzQ3MDQ2MzI0LCJleHAiOjE3NDcwNDgxMjR9.UuGKcCWSaSAAC3LuaSkg4y9PpuGeeh1h5GPJVsvglIk";

    @BeforeEach
    void setUp() {

    }

    @Test
    void testGetAllRiders() throws Exception {
        when(riderService.getAllRiders()).thenReturn(List.of(rider));

        mockMvc.perform(get("/rider")
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].riderId").value(1))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }

    @Test
    void testGetRiderById() throws Exception {
        when(riderService.getRiderByID(1)).thenReturn(rider);

        mockMvc.perform(get("/rider/{riderId}", 1)
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riderId").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testAddRider() throws Exception {
        when(riderService.addRider(any(Rider.class))).thenReturn("Rider added successfully");

        mockMvc.perform(post("/rider")
                        .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rider)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Rider added successfully"));
    }

    @Test
    void testUpdateRider() throws Exception {
        when(riderService.updateRider(any(Rider.class))).thenReturn("Rider updated successfully");

        mockMvc.perform(put("/rider/{riderId}", 1)
                        .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rider)))
                .andExpect(status().isOk())
                .andExpect(content().string("Rider updated successfully"));
    }

    @Test
    void testDeleteRider() throws Exception {
        when(riderService.deleteRider(1)).thenReturn("Rider deleted successfully");

        mockMvc.perform(delete("/rider/{riderId}", 1)
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Rider deleted successfully"));
    }
}

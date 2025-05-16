package com.example.Rider_Co.tests.controllerTests;

import com.example.Rider_Co.dto.MatchRequestDto;
import com.example.Rider_Co.models.Rider;
import com.example.Rider_Co.models.User;
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

import static org.hamcrest.Matchers.containsString;
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

    private Rider rider;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String jwtToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2hAZ21haWwuY29tIiwicm9sZSI6IlJJREVSIiwiaWF0IjoxNzQ3Mzc0NzU4LCJleHAiOjE3NDczNzY1NTh9.6fCCP7fXAQ8MLuMt0sSXCtQxea9Q1v6VxMDN3Pe7RHI";

    @BeforeEach
    void setUp() {
        rider = Rider.builder()
                .riderId(1)
                .coordinateX(12.34)
                .coordinateY(56.78)
                .online(true)
                .user(User.builder()
                        .id(14)  // Set as appropriate
                        .username("John")
                        .role("RIDER")
                        .password("password") // or leave null if not needed
                        .build())
                .build();
    }

    @Test
    void testGetAllRiders() throws Exception {
        when(riderService.getAllRiders()).thenReturn(List.of(rider));

        mockMvc.perform(get("/rider")
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].riderId").value(1));
    }

    @Test
    void testGetRiderById() throws Exception {
        when(riderService.getRiderByID(1)).thenReturn(rider);

        mockMvc.perform(get("/rider/{riderId}", 1)
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riderId").value(1));
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


















    @Test
    void shouldReturnAllRiders() throws Exception {
        List<Rider> riders = List.of(new Rider(1, null, 10.5, true, 12.5, null));
        when(riderService.getAllRiders()).thenReturn(riders);

        mockMvc.perform(get("/rider")
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].riderId").value(1));
    }

    @Test
    void shouldReturnRiderById() throws Exception {
        Rider rider = new Rider(1, null, 10.5, true, 12.5, null);
        when(riderService.getRiderByID(1)).thenReturn(rider);

        mockMvc.perform(get("/rider/1")
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riderId").value(1));
    }

    @Test
    void shouldReturnNotFoundWhenRiderDoesNotExist() throws Exception {
        when(riderService.getRiderByID(999)).thenReturn(null);

        mockMvc.perform(get("/rider/999")
                        .header("Authorization", jwtToken))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Rider not found!"));
    }

    @Test
    void shouldAddRider() throws Exception {
        Rider rider = new Rider();
        rider.setCoordinateX(12.0);
        rider.setCoordinateY(15.0);

        when(riderService.addRider(any())).thenReturn("Successfully added the rider with ID: 1");

        mockMvc.perform(post("/rider")
                        .header("Authorization",  jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rider)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Successfully added")));
    }

    @Test
    void shouldUpdateRider() throws Exception {
        Rider rider = new Rider();
        rider.setCoordinateX(20);
        rider.setCoordinateY(30);

        when(riderService.updateRider(any())).thenReturn("Successfully updated the rider with ID: 1");

        mockMvc.perform(put("/rider/1")
                        .header("Authorization",  jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rider)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Successfully updated")));
    }

    @Test
    void shouldDeleteRider() throws Exception {
        when(riderService.deleteRider(1)).thenReturn("Successfully deleted the rider with ID: 1");

        mockMvc.perform(delete("/rider/1")
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Successfully deleted")));
    }

    @Test
    void shouldMatchDriverSuccessfully() throws Exception {
        MatchRequestDto dto = new MatchRequestDto();
        dto.setDestinationCoordinateX(10);
        dto.setDestinationCoordinateY(20);

        when(riderService.matchDrivers(eq(1), eq(10.0), eq(20.0)))
                .thenReturn("Successfully added the ride for rider 1");

        mockMvc.perform(post("/rider/match/1")
                        .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Successfully added")));
    }

    @Test
    void shouldReturnErrorWhenMatchingNonExistingRider() throws Exception {
        MatchRequestDto dto = new MatchRequestDto();
        dto.setDestinationCoordinateX(10);
        dto.setDestinationCoordinateY(20);

        when(riderService.matchDrivers(eq(99), anyDouble(), anyDouble()))
                .thenReturn("RIDER_NOT_FOUND");

        mockMvc.perform(post("/rider/match/99")
                        .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("RIDER_NOT_FOUND"));
    }


}

package com.example.Rider_Co.tests.controllerTests;

import com.example.Rider_Co.controllers.DriverController;
import com.example.Rider_Co.models.Driver;
import com.example.Rider_Co.models.Ride;
import com.example.Rider_Co.serviceInterfaces.DriverServiceInterface;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DriverServiceInterface driverService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String jwtToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkcml2ZXJAZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlhdCI6MTc0NzM3NzU0NiwiZXhwIjoxNzQ3Mzc5MzQ2fQ.HZgQTsPXB4NM00LnKryy_rzchROLqhh55eGvEkGFUkE";

    @Test
    void testGetAllDrivers() throws Exception {
        Driver driver = new Driver();
        driver.setDriverId(1);
        driver.setAvailable(true);

        Mockito.when(driverService.GetAllDrivers()).thenReturn(List.of(driver));

        mockMvc.perform(get("/driver")  .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].driverId").value(1));
    }

    @Test
    void testGetDriverById_Found() throws Exception {
        Driver driver = new Driver();
        driver.setDriverId(1);

        Mockito.when(driverService.GetDriverByID(1)).thenReturn(driver);

        mockMvc.perform(get("/driver/1")  .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.driverId").value(1));
    }

    @Test
    void testGetDriverById_NotFound() throws Exception {
        Mockito.when(driverService.GetDriverByID(99)).thenReturn(null);

        mockMvc.perform(get("/driver/99")  .header("Authorization", jwtToken))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Driver not found with ID: 99")));
    }

    @Test
    void testAddDriver() throws Exception {
        Driver driver = new Driver();
        driver.setDriverId(2);

        Mockito.when(driverService.AddDriver(any(Driver.class)))
                .thenReturn("Successfully added the driver with ID: 2");

        mockMvc.perform(post("/driver") .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driver)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Successfully added the driver with ID: 2")));
    }

    @Test
    void testUpdateDriver() throws Exception {
        Driver driver = new Driver();
        driver.setDriverId(3);

        Mockito.when(driverService.UpdateDriver(any(Driver.class)))
                .thenReturn("Successfully updated the driver with ID: 3");

        mockMvc.perform(put("/driver/3") .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driver)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Successfully updated the driver with ID: 3")));
    }

    @Test
    void testDeleteDriver() throws Exception {
        Mockito.when(driverService.DeleteDriver(4))
                .thenReturn("Successfully deleted the driver with ID: 4");

        mockMvc.perform(delete("/driver/4") .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Successfully deleted the driver with ID: 4")));
    }



}

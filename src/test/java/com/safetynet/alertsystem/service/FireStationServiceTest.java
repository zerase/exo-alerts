package com.safetynet.alertsystem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.safetynet.alertsystem.model.FireStation;
import com.safetynet.alertsystem.repository.FireStationRepository;

@SpringBootTest
public class FireStationServiceTest {
	
	private FireStation fireStation;
	
	@Autowired
	private FireStationService  fireStationService;
	
	@MockBean
	private FireStationRepository fireStationRepositoryMock;
	
	
    @BeforeEach
    public void setUp() {
        fireStation = new FireStation();
        fireStation.setId(0);
        fireStation.setStation("33");
        fireStation.setAddress("fireStationServiceTestAddress");
    }

    @Test
    void testGetAllFireStations_shouldReturnIsNotNull() {
        List<FireStation> fireStationsList = new ArrayList<FireStation>();
        fireStationsList.add(fireStation);
        
        when(fireStationRepositoryMock.findAll()).thenReturn(fireStationsList);
        
        assertThat(fireStationService.getAllFireStations()).isNotNull();
        assertThat(fireStationService.getAllFireStations()).isEqualTo(fireStationsList);
        
    }

    @Test
    void addStationAndAddressMapping_withSuccess() {
        when(fireStationRepositoryMock.findByAddress(any(String.class))).thenReturn(Optional.of(fireStation));
        
        assertThat(fireStationService.addFireStationAndAddressMapping(fireStation)).isNotNull();
        assertThat(fireStationService.addFireStationAndAddressMapping(fireStation)).isEmpty();
    }

    @Test
    void addStationAndAddressMapping_withErrorBecauseAddressAlreadyExist() {
        when(fireStationRepositoryMock.findByAddress(any(String.class))).thenReturn(Optional.empty());
        
        assertThat(fireStationService.addFireStationAndAddressMapping(fireStation)).isNotNull();
        assertThat(fireStationService.addFireStationAndAddressMapping(fireStation)).isEqualTo(Optional.of(fireStation));
    }

    @Test
    void updateFireStationNumberOfAnAddress_withSuccess() {
        when(fireStationRepositoryMock.findByAddress(any(String.class))).thenReturn(Optional.of(fireStation));
        
        assertThat(fireStationService.updateFireStationNumberOfAnAddress(fireStation)).isNotNull();
        assertThat(fireStationService.updateFireStationNumberOfAnAddress(fireStation)).isEqualTo(Optional.of(fireStation));
    }

    @Test
    void updateFireStationNumberOfAnAddress_withErrorBecauseFireStationNotFound() {
        when(fireStationRepositoryMock.findByAddress(any(String.class))).thenReturn(Optional.empty());
        
        assertThat(fireStationService.updateFireStationNumberOfAnAddress(fireStation)).isNotNull();
        assertThat(fireStationService.updateFireStationNumberOfAnAddress(fireStation)).isEmpty();
    }

    @Test
    void deleteMappingOfAStation_withErrorBecauseFireStationNumberNotExist() {
        when(fireStationRepositoryMock.findByStation(any(String.class))).thenReturn(Optional.empty());
        
        assertThat(fireStationService.deleteMappingOfAStation("33")).isNotNull();
        assertThat(fireStationService.deleteMappingOfAStation("33")).isEmpty();
    }

    @Test
    void deleteMappingOfAStation_withSuccess() {
        Optional<List<FireStation>> fireStationList = Optional.of(new ArrayList<>());
        fireStationList.get().add(fireStation);
        
        when(fireStationRepositoryMock.findByStation(any(String.class))).thenReturn(fireStationList);
        
        assertThat(fireStationService.deleteMappingOfAStation("33")).isNotNull();
        assertThat(fireStationService.deleteMappingOfAStation("33")).isEqualTo(fireStationList);
    }

}

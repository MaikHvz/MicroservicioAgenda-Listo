package com.fitconnect.agendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fitconnect.agendar.model.Agenda;
import com.fitconnect.agendar.model.AgendaEstado;
import com.fitconnect.agendar.repository.AgendaRepository;
import com.fitconnect.agendar.service.AgendaService;
import com.fitconnect.agendar.service.ServicioCliente;
import com.fitconnect.agendar.model.AgendaDTO;

@SpringBootTest
public class AgendaServiceTest {
    @Autowired
    private AgendaService agendaService;

    @MockBean
    private AgendaRepository agendaRepository;

    @MockBean
    private ServicioCliente servicioCliente;

    @Test
    public void testFindALL(){
        // Mock del repositorio
        when(agendaRepository.findAll())
            .thenReturn(List.of(
                new Agenda(
                    1,
                    "Juan Perez",
                    "20.844.931-1",
                    1, // YOGA
                    LocalDate.of(2026, 2, 9),
                    LocalTime.of(8, 18, 0),
                    "maic.hernandez@gmail.com",
                    AgendaEstado.AGENDADA
                ),
                new Agenda(
                    2,
                    "Maria Garcia",
                    "15.123.456-7",
                    2, // CALISTENIA
                    LocalDate.of(2026, 2, 10),
                    LocalTime.of(10, 30, 0),
                    "maria.garcia@gmail.com",
                    AgendaEstado.AGENDADA
                ),
                new Agenda(
                    3,
                    "Carlos Lopez",
                    "18.987.654-3",
                    3, // ACROYOGA
                    LocalDate.of(2026, 2, 11),
                    LocalTime.of(14, 0, 0),
                    "carlos.lopez@gmail.com",
                    AgendaEstado.AGENDADA
                ),
                new Agenda(
                    4,
                    "Ana Rodriguez",
                    "12.345.678-9",
                    4, // ENTRENAMIENTO FUNCIONAL
                    LocalDate.of(2026, 2, 12),
                    LocalTime.of(16, 45, 0),
                    "ana.rodriguez@gmail.com",
                    AgendaEstado.AGENDADA
                )
            ));

        // Mock del cliente de servicios para todos los IDs
        when(servicioCliente.obtenerNombreServicioPorId(1))
            .thenReturn("YOGA");
        when(servicioCliente.obtenerNombreServicioPorId(2))
            .thenReturn("CALISTENIA");
        when(servicioCliente.obtenerNombreServicioPorId(3))
            .thenReturn("ACROYOGA");
        when(servicioCliente.obtenerNombreServicioPorId(4))
            .thenReturn("ENTRENAMIENTO FUNCIONAL");

        List<AgendaDTO> agendas = agendaService.listarTodas();

        assertNotNull(agendas);
        assertEquals(4, agendas.size());
        
        // Verificar que cada agenda tiene el nombre correcto del servicio
        assertEquals("YOGA", agendas.get(0).getNombreServicio());
        assertEquals("CALISTENIA", agendas.get(1).getNombreServicio());
        assertEquals("ACROYOGA", agendas.get(2).getNombreServicio());
        assertEquals("ENTRENAMIENTO FUNCIONAL", agendas.get(3).getNombreServicio());
    }
}

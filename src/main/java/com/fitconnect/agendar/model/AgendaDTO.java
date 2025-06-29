package com.fitconnect.agendar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendaDTO {
    private Integer id;
    private String nombreCliente;
    private String rutCliente;
    private Integer idServicio;
    private String nombreServicio;
    private LocalDate fecha;
    private LocalTime hora;
    private String emailCliente;
    private AgendaEstado estadoAgenda;
} 
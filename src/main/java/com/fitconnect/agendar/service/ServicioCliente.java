package com.fitconnect.agendar.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ServicioCliente {

    private static final Logger logger = LoggerFactory.getLogger(ServicioCliente.class);

    @Value("${servicio.url}")
    private String servicioUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String obtenerNombreServicioPorId(Integer idServicio) {
        try {
            String url = servicioUrl + "/" + idServicio;
            ResponseEntity<ServicioDTO> response = restTemplate.getForEntity(url, ServicioDTO.class);
            return response.getBody() != null ? response.getBody().getNombre() : obtenerNombreServicioFallback(idServicio);
        } catch (Exception e) {
            // Si el microservicio no está disponible, usar fallback
            logger.warn("Error al obtener servicio con ID {} desde microservicio: {}. Usando fallback.", idServicio, e.getMessage());
            return obtenerNombreServicioFallback(idServicio);
        }
    }

    /**
     * Método de fallback que retorna el nombre del servicio basado en el ID
     * cuando el microservicio de servicios no está disponible
     */
    private String obtenerNombreServicioFallback(Integer idServicio) {
        return switch (idServicio) {
            case 1 -> "YOGA";
            case 2 -> "CALISTENIA";
            case 3 -> "ACROYOGA";
            case 4 -> "ENTRENAMIENTO FUNCIONAL";
            default -> "SERVICIO DESCONOCIDO (ID: " + idServicio + ")";
        };
    }

    // DTO interno para mapear la respuesta del microservicio de servicios
    public static class ServicioDTO {
        private Integer id;
        private String nombre;
        // getters y setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }
}
package com.fitconnect.agendar; 
import com.fitconnect.agendar.model.*; 
import com.fitconnect.agendar.repository.*; 
import java.util.List;
import com.fitconnect.agendar.util.*;

import net.datafaker.Faker; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.boot.CommandLineRunner; 
import org.springframework.context.annotation.Profile; 
import org.springframework.stereotype.Component; 
import java.util.Random; 
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
@Profile("dev")
@Component 
public class DataLoader implements CommandLineRunner { 
@Autowired 


private AgendaRepository AgendaRepository; 

    @Override 
    public void run(String... args) throws Exception { 
        Faker faker = new Faker(); 
        Random random = new Random();
        


    
        // Generar Agenda
        for (int i = 0; i < 2; i++) { 
            Integer idServicio = random.nextInt(1, 5);

            Agenda agenda = new Agenda(); 
            agenda.setNombreCliente(faker.name().fullName()); 
            agenda.setRutCliente(GeneradorRut.generarRUT());
            agenda.setIdServicio(idServicio);
            agenda.setFecha(generarFechaFutura(faker));
            agenda.setHora(generarHora(faker));
            agenda.setEmailCliente(faker.internet().emailAddress());
            AgendaRepository.save(agenda);
        } 
 

         List<Agenda> agenda = AgendaRepository.findAll();

 }

    private static LocalDate generarFechaFutura(Faker faker) {
        LocalDate hoy = LocalDate.now();
        LocalDate fechaMaxima = hoy.plus(1, ChronoUnit.YEARS);
        
        return hoy.plusDays(faker.random().nextInt(0, (int) ChronoUnit.DAYS.between(hoy, fechaMaxima)));
    }

        // Genera una hora entre 8:00 y 19:59 (formato 24H)
    private static LocalTime generarHora(Faker faker) {
        return LocalTime.of(
            faker.random().nextInt(8, 19), // Horas: 8 AM a 7 PM
            faker.random().nextInt(0, 59)  // Minutos: 00 a 59
        );
    }
    
 } 
package com.fitconnect.agendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AgendarApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgendarApplication.class, args);
	}

}
//post
//{
//	"nombreCliente": "Juan Pérez",
//	"rutCliente": "12345678-9",
//	"idServicio": 2,
//	"fecha": "2025-07-05",
//	"hora": "15:30",
//	"emailCliente": "juan.perez@example.com",
//	"estadoAgenda": "AGENDADA"
 // }

 //put
 //{
//	"id": 5,
//	"nombreCliente": "Juan Pérez Modificado",
//	"rutCliente": "12345678-9",
//	"idServicio": 2,
//	"fecha": "2025-07-10",
//	"hora": "16:00",
//	"emailCliente": "juan.perez.modificado@example.com",
//	"estadoAgenda": "REAGENDADA"
//  }
  
  
//http://54.227.149.213:8080/swagger-ui/index.html

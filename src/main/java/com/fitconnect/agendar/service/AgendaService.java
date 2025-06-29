package com.fitconnect.agendar.service;

import com.fitconnect.agendar.model.Agenda;
import com.fitconnect.agendar.model.AgendaEstado;
import com.fitconnect.agendar.repository.AgendaRepository;
import com.fitconnect.agendar.model.AgendaDTO;
import com.fitconnect.agendar.service.ServicioCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final ServicioCliente servicioCliente;

    @Autowired
    public AgendaService(AgendaRepository agendaRepository, ServicioCliente servicioCliente) {
        this.agendaRepository = agendaRepository;
        this.servicioCliente = servicioCliente;
    }

    private boolean hayConflicto(LocalDate fecha, LocalTime horaInicioNueva, Integer idExcluir) {
        List<Agenda> agendasDelDia = agendaRepository.findByFecha(fecha);
        LocalTime horaFinNueva = horaInicioNueva.plusHours(1);

        for (Agenda agendaExistente : agendasDelDia) {
            if (idExcluir != null && agendaExistente.getId().equals(idExcluir)) {
                continue;
            }

            LocalTime horaInicioExistente = agendaExistente.getHora();
            LocalTime horaFinExistente = horaInicioExistente.plusHours(1);

            boolean seTraslapan = horaInicioNueva.isBefore(horaFinExistente) && horaInicioExistente.isBefore(horaFinNueva);

            if (seTraslapan) {
                return true;
            }
        }
        return false;
    }

    private void actualizarEstadoSiCorresponde(Agenda agenda) {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicio = LocalDateTime.of(agenda.getFecha(), agenda.getHora());
        LocalDateTime fin = inicio.plusHours(1);

        if (ahora.isAfter(fin)) {
            agenda.setEstadoAgenda(AgendaEstado.COMPLETADA);
        } else if (ahora.isAfter(inicio) && ahora.isBefore(fin)) {
            agenda.setEstadoAgenda(AgendaEstado.EN_CURSO);
        } else {
            agenda.setEstadoAgenda(AgendaEstado.AGENDADA);
        }

        agendaRepository.save(agenda); // Persistir cambio de estado
    }

    public Agenda crearAgenda(Agenda nuevaAgenda) {
        if (hayConflicto(nuevaAgenda.getFecha(), nuevaAgenda.getHora(), null)) {
            throw new IllegalArgumentException("Ya existe una cita en ese horario");
        }

        nuevaAgenda.setEstadoAgenda(AgendaEstado.AGENDADA); // Por defecto
        return agendaRepository.save(nuevaAgenda);
    }

    public List<AgendaDTO> listarTodas() {
        List<Agenda> agendas = agendaRepository.findAll();
        agendas.forEach(this::actualizarEstadoSiCorresponde);
        return agendas.stream().map(this::convertirAgendaADTO).toList();
    }

    public Optional<AgendaDTO> buscarPorId(Integer id) {
        Optional<Agenda> agenda = agendaRepository.findById(id);
        agenda.ifPresent(this::actualizarEstadoSiCorresponde);
        return agenda.map(this::convertirAgendaADTO);
    }

    public Agenda actualizarAgenda(Integer id, Agenda agendaActualizada) {
        Optional<Agenda> agendaExistenteOpt = agendaRepository.findById(id);
        if (agendaExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Agenda no encontrada con id " + id);
        }

        if (hayConflicto(agendaActualizada.getFecha(), agendaActualizada.getHora(), id)) {
            throw new IllegalArgumentException("Ya existe una cita en ese horario");
        }

        Agenda agendaExistente = agendaExistenteOpt.get();
        agendaExistente.setNombreCliente(agendaActualizada.getNombreCliente());
        agendaExistente.setRutCliente(agendaActualizada.getRutCliente());
        agendaExistente.setIdServicio(agendaActualizada.getIdServicio());
        agendaExistente.setFecha(agendaActualizada.getFecha());
        agendaExistente.setHora(agendaActualizada.getHora());
        agendaExistente.setEmailCliente(agendaActualizada.getEmailCliente());
        agendaExistente.setEstadoAgenda(AgendaEstado.AGENDADA); // Reinicia estado

        return agendaRepository.save(agendaExistente);
    }

    public void eliminarAgenda(Integer id) {
        if (!agendaRepository.existsById(id)) {
            throw new IllegalArgumentException("Agenda no encontrada con id " + id);
        }
        agendaRepository.deleteById(id);
    }

    public AgendaDTO convertirAgendaADTO(Agenda agenda) {
        String nombreServicio = servicioCliente.obtenerNombreServicioPorId(agenda.getIdServicio());
        return AgendaDTO.builder()
                .id(agenda.getId())
                .nombreCliente(agenda.getNombreCliente())
                .rutCliente(agenda.getRutCliente())
                .idServicio(agenda.getIdServicio())
                .nombreServicio(nombreServicio)
                .fecha(agenda.getFecha())
                .hora(agenda.getHora())
                .emailCliente(agenda.getEmailCliente())
                .estadoAgenda(agenda.getEstadoAgenda())
                .build();
    }
}

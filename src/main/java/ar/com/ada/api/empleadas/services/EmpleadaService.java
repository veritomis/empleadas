package ar.com.ada.api.empleadas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.empleadas.entities.Empleada;
import ar.com.ada.api.empleadas.repos.EmpleadaRepository;

@Service
public class EmpleadaService {

    @Autowired
    EmpleadaRepository repo;

    public void crearEmpleada(Empleada empleada) {
        repo.save(empleada);
    }

    public List<Empleada> traerEmpleadas() {
        return repo.findAll();
    }

}

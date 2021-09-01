package ar.com.ada.api.empleadas.services;

import java.util.Date;
import java.util.*;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.empleadas.entities.Categoria;
import ar.com.ada.api.empleadas.entities.Empleada;
import ar.com.ada.api.empleadas.entities.Empleada.EstadoEmpleadaEnum;
import ar.com.ada.api.empleadas.models.request.SueldoNuevoEmpleada;
import ar.com.ada.api.empleadas.repos.EmpleadaRepository;

@Service
public class EmpleadaService {

    @Autowired
    EmpleadaRepository repo;
    
    @Autowired
    CategoriaService categoriaService;

    public void crearEmpleada(Empleada empleada) {
        repo.save(empleada);
    }

    public List<Empleada> traerEmpleadas() {
        return repo.findAll();
    }

    public Empleada buscarEmpleada(Integer empleadaId){
        Optional<Empleada> resultado = repo.findById(empleadaId);

        if(resultado.isPresent())
            return resultado.get();
        
        
        return null;
        
    }

    public List<Empleada> traerEmpleada() {
        List<Empleada> empleados = repo.findAll();

        List<Empleada> listaFiltrada = new ArrayList<>();

        for (Empleada empleado : empleados) {
            if (!empleado.getEstado().equals(EstadoEmpleadaEnum.BAJA)) {
                listaFiltrada.add(empleado);
            }
        }

        return listaFiltrada;
        
    }

    public void bajaEmpleadaPorId(Integer id) {
        Empleada empleada = this.buscarEmpleada(id);
        empleada.setEstado(EstadoEmpleadaEnum.BAJA);
        empleada.setFechaBaja(new Date());

        repo.save(empleada);
    }


	public List<Empleada> traerEmpleadaPorCategoria(Integer catId) {
		
        Categoria categoria = categoriaService.buscarCategoria(catId);
        
        return categoria.getEmpleadas();

	}

	public void guardar(Empleada empleada) {
        repo.save(empleada);
	}

    public void modificarSueldo(Integer id, SueldoNuevoEmpleada sueldoNuevoInfo) {

        Empleada empleada = this.buscarEmpleada(id);
        empleada.setSueldo(sueldoNuevoInfo.sueldoNuevo);
        this.guardar(empleada);
    }

    public List<Empleada> buscarEmpleadoPorNombre(String nombre) {
        return repo.findByNombre(nombre);
    }
}



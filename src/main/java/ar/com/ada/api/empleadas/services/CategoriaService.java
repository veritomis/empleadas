package ar.com.ada.api.empleadas.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.empleadas.entities.Categoria;
import ar.com.ada.api.empleadas.entities.Empleada;
import ar.com.ada.api.empleadas.repos.CategoriaRepository;
@Service
public class CategoriaService {

    @Autowired
    CategoriaRepository repo;

    public void crearCategoria(Categoria categoria){
        repo.save(categoria);
    }

    public List<Categoria> traerCategorias(){
        return repo.findAll();
    }

    public Categoria buscarCategoria(Integer categoriaId){

        Optional<Categoria> resultado =  repo.findById(categoriaId);
        Categoria categoria = null;

        if (resultado.isPresent())
            categoria = resultado.get();

        return categoria;

    }

    public Categoria findCategoria(String categoriaNombre) {
        return repo.findByNombre(categoriaNombre);
    }

    // buscar una categoria por id y eliminarla

    public void eliminarCategoria(Integer categoriaId) {
        repo.deleteById(categoriaId);
    }

    /// Administrativa: proximo sueldo es el sueldo base de la categoria si el
    /// sueldo esta por debajo de la categroia
    // Ventas: proximo sueldo es el sueldo base de categorias + 10% ventas anuales
    // Auxiliar: siempre cobra el sueldo base de la categoria.
    public List<Empleada> calcularProximosSueldos() {
        List<Empleada> listaEmpleadas = new ArrayList<>();

        // Uso de streams: flujo de objetos
        this.traerCategorias().stream().forEach(categoria -> {
            categoria.getEmpleadas().stream().forEach(empleada -> {
                empleada.setSueldo(categoria.calcularProximoSueldo(empleada));
                listaEmpleadas.add(empleada);
            });
        });

        return listaEmpleadas;
    }

    public List<Empleada> calcularProximosSueldosOldWay() {
        List<Empleada> listaEmpleadas = new ArrayList<>();

        for (Categoria categoria : this.traerCategorias()) {
            for (Empleada empleada : categoria.getEmpleadas()) {
                empleada.setSueldo(categoria.calcularProximoSueldo(empleada));
                listaEmpleadas.add(empleada);
            }
        }

        return listaEmpleadas;
    }

    public List<Empleada> obtenerSueldosActuales() {
        List<Empleada> listaEmpleadas = new ArrayList<>();

        traerCategorias().stream().forEach(cat -> listaEmpleadas.addAll(cat.getEmpleadas()));

        return listaEmpleadas;
    }

    public List<Empleada> obtenerSueldosActualesNoStream() {
        List<Empleada> listaEmpleadas = new ArrayList<>();

        for (Categoria categoria : this.traerCategorias()) {
            for (Empleada empleada : categoria.getEmpleadas()) {
                listaEmpleadas.add(empleada);
            }
        }

        return listaEmpleadas;
    }

    public List<Categoria> obtenerCategoriasSinEmpleadas() {
        return traerCategorias().stream().filter(categoria -> categoria.getEmpleadas().size() == 0) // lambda
                .collect(Collectors.toList());
    }

    public Categoria obtenerCategoriaConMinimoSueldo() {
        return this.traerCategorias().stream().min(Comparator.comparing(Categoria::getSueldoBase)).get();
    }

    public List<String> obtenerNombresCategorias() {
        // Mapea cada elemento del stream(colleccion) a otro tipo de elemento
        // en este caso, un String(getNombre)
        return this.traerCategorias().stream().map(cat -> cat.getNombre()).collect(Collectors.toList());
    }

}



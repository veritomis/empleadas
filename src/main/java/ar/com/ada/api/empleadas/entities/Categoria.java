package ar.com.ada.api.empleadas.entities;

import java.math.BigDecimal;
import java.util.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ar.com.ada.api.empleadas.entities.calculos.*;


@Entity
@Table(name = "categoria")
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoria_id")
    private Integer categoriaId;
    
    
    private String nombre;

    @Column(name = "sueldo_base")
    private BigDecimal sueldoBase;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Empleada> empleadas = new ArrayList<>();

    @JsonIgnore //para no devolverlo por el front
    @Transient //para que no impacte en el hibernate-> luego en la DB
    private ISueldoCalculator sueldoCalculator;



    public ISueldoCalculator getSueldoCalculator() {
        return sueldoCalculator;
    }

    public void setSueldoCalculator(ISueldoCalculator sueldoCalculator) {
        this.sueldoCalculator = sueldoCalculator;
    }

    public List<Empleada> getEmpleadas() {
        return empleadas;
    }

    public void setEmpleadas(List<Empleada> empleadas) {
        this.empleadas = empleadas;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getSueldoBase() {
        return sueldoBase;
    }

    public void setSueldoBase(BigDecimal sueldoBase) {
        this.sueldoBase = sueldoBase;
    }

    public void agregarEmpleada (Empleada empleada){
        this.empleadas.add(empleada);
    }

    /// Administrativa: proximo sueldo es el sueldo base de la categoria si el
    /// sueldo esta por debajo de la categroia
    // Ventas: proximo sueldo es el sueldo base de categorias + 10% ventas anuales
    // Auxiliar: siempre cobra el sueldo base de la categoria.
    public BigDecimal calcularProximoSueldo(Empleada empleada) {
        return sueldoCalculator.calcularSueldo(empleada);
    }

}

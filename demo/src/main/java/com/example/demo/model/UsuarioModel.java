package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "USUARIO")
public class UsuarioModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private String name;
    private String job;
    private Double salary;
    private Double commision;
    private Integer prioridad;


    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public Double getSalary() {
        return salary;
    }

    public Double getCommision() {
        return commision;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public void setCommision(Double commision) {
        this.commision = commision;
    }

}

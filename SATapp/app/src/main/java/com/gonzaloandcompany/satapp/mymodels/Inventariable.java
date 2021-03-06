package com.gonzaloandcompany.satapp.mymodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Inventariable {
    private String id;
    private String codigo;
    private String tipo;
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private String createdAt;
    private String updatedAt;
    private String imagen;

    public Inventariable(String imagen) {
        this.imagen = imagen;
    }

    public Inventariable(String tipo, String nombre, String descripcion, String ubicacion) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
    }
}

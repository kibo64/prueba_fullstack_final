package com.example.ms_profesores.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfesorResponse {
    
    private Long id;
    private String rutP;
    private String nombreP;
    private String emailP;
    private String especialidad;
    
    private FacultadResponse facultad;

}

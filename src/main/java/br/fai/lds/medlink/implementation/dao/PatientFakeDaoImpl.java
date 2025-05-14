package br.fai.lds.medlink.implementation.dao;

import br.fai.lds.medlink.domain.dto.Patient;

import java.util.ArrayList;
import java.util.List;



public class PatientFakeDaoImpl implements Patient {


    private static List<Patient> patients = new ArrayList<>();
    private static int ID=0;


}

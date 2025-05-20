package br.fai.lds.medlink.dataTransferObject;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicDto {

    private int id;
    private String name;
    private String cpf;
    private Gender gender;
    private LocalDate dataNascimento;
    private String phoneNumber;
    private Address address;

    private String crm;
    private String specialty;
    private String email;
    private boolean active = true;
}

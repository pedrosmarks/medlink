package br.fai.lds.medlink.dataTransferObject;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.Gender;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientDto {


    private int id;
    private String name;
    private String cpf;
    private Gender gender;
    private LocalDate dataNascimento;
    private String phoneNumber;
    private Address address;

    private String email;
    private String plan;
    private String susCard;
    private boolean active = true;

}

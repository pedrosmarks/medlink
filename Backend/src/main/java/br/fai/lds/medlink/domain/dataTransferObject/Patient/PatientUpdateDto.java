package br.fai.lds.medlink.domain.dataTransferObject.Patient;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.enuns.Gender;
import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.util.CpfUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para atualização de dados do paciente.
 * 
 * <p>Permite atualização parcial dos dados do paciente, onde apenas os campos
 * não nulos serão atualizados na entidade existente.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientUpdateDto {
    
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String name;
    
    /**
     * CPF (Cadastro de Pessoa Física) do paciente.
     * <p>
     * Pode ser fornecido no formato XXX.XXX.XXX-XX ou apenas números XXXXXXXXXXX.
     * A validação aceita ambos os formatos e será normalizado internamente.
     * </p>
     */
    @Pattern(regexp = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})$",
             message = "O CPF deve estar no formato XXX.XXX.XXX-XX ou conter apenas 11 dígitos")
    private String cpf;
    
    private Gender gender;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    
    @Pattern(regexp = "^\\(?\\d{2}\\)?[\\s-]?\\d{4,5}[\\s-]?\\d{4}$", message = "Formato de telefone inválido")
    private String phoneNumber;
    
    private Address address;
    
    @Email(message = "O e-mail deve ser válido")
    private String email;
    
    private String avatar;
    private String bloodType;
    private String observations;
    private String plan;
    private String susCard;
    private Boolean active;

    /**
     * Atualiza uma entidade Patient existente com os dados deste DTO.
     * 
     * <p>Apenas os campos não nulos deste DTO serão aplicados à entidade,
     * permitindo atualizações parciais. O CPF é validado antes da atualização.</p>
     *
     * @param entity Entidade Patient a ser atualizada
     * @throws IllegalArgumentException se o CPF não tiver o comprimento correto após normalização
     */
    public void updateEntity(Patient entity) {
        if (this.name != null) entity.setName(this.name);

        if (this.cpf != null) {
            // Valida o CPF antes de atualizar
            if (!CpfUtil.isValidLength(this.cpf)) {
                throw new IllegalArgumentException("CPF deve ter exatamente 11 dígitos");
            }
            entity.setCpf(this.cpf); // Será normalizado no DAO
        }

        if (this.gender != null) entity.setGender(this.gender);
        if (this.birthDate != null) entity.setBirthDate(this.birthDate);
        if (this.phoneNumber != null) entity.setPhoneNumber(this.phoneNumber);
        if (this.address != null) entity.setAddress(this.address);
        if (this.email != null) entity.setEmail(this.email);
        if (this.avatar != null) entity.setAvatar(this.avatar);
        if (this.bloodType != null) entity.setBloodType(this.bloodType);
        if (this.observations != null) entity.setObservations(this.observations);
        if (this.plan != null) entity.setPlan(this.plan);
        if (this.susCard != null) entity.setSusCard(this.susCard);
        if (this.active != null) entity.setActive(this.active);
    }
}
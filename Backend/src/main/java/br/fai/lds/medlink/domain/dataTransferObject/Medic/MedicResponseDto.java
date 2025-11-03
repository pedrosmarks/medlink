package br.fai.lds.medlink.domain.dataTransferObject.Medic;

import br.fai.lds.medlink.domain.Address;
import br.fai.lds.medlink.domain.enuns.Gender;
import br.fai.lds.medlink.domain.Medic;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) para resposta com dados do médico.
 * <p>
 * Esta classe é utilizada para retornar informações do médico em operações
 * de consulta, listagem e outras operações que não devem expor dados sensíveis
 * como senhas. Contém todos os dados públicos do profissional.
 * </p>
 * 
 * @author Sistema MedLink
 * @version 1.0
 * @since 1.0
 * @see Medic
 * @see MedicCreateDto
 */
@Data
public class MedicResponseDto {

    /**
     * Identificador único do médico no sistema.
     */
    private int id;
    
    /**
     * Nome completo do médico.
     */
    private String name;
    
    /**
     * CPF do médico no formato XXX.XXX.XXX-XX.
     */
    private String cpf;
    
    /**
     * Gênero do médico.
     * 
     * @see Gender
     */
    private Gender gender;
    
    /**
     * Data de nascimento do médico.
     */
    private LocalDate birthDate;
    
    /**
     * Número de telefone para contato.
     */
    private String phoneNumber;
    
    /**
     * Endereço completo do médico.
     * 
     * @see Address
     */
    private Address address;
    
    /**
     * Número do CRM (Conselho Regional de Medicina).
     */
    private String crm;
    
    /**
     * Especialidade médica do profissional.
     */
    private String specialty;
    
    /**
     * Endereço de e-mail do médico.
     */
    private String email;
    
    /**
     * Status de ativação do médico no sistema.
     * <p>
     * true = ativo, false = inativo/desabilitado
     * </p>
     */
    private boolean active;

    /**
     * Converte uma entidade Medic em MedicResponseDto.
     * <p>
     * Método estático que cria uma nova instância do DTO a partir
     * de uma entidade Medic, copiando todos os dados relevantes
     * exceto informações sensíveis como senha.
     * </p>
     * 
     * @param entity Entidade Medic a ser convertida
     * @return Nova instância de MedicResponseDto ou null se entity for null
     * @see Medic
     */
    public static MedicResponseDto fromEntity(Medic entity) {
        if (entity == null) {
            return null;
        }
        MedicResponseDto dto = new MedicResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCpf(entity.getCpf());
        dto.setGender(entity.getGender());
        dto.setBirthDate(entity.getBirthDate());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setAddress(entity.getAddress());
        dto.setCrm(entity.getCrm());
        dto.setSpecialty(entity.getSpecialty());
        dto.setEmail(entity.getEmail());
        dto.setActive(entity.isActive());
        return dto;
    }
}

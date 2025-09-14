package br.fai.lds.medlink.util;

/**
 * Utilitário para manipulação de CPF
 */
public class CpfUtil {

    /**
     * Remove a formatação do CPF (pontos e traços)
     * @param cpf CPF formatado (ex: 123.456.789-00)
     * @return CPF apenas com números (ex: 12345678900)
     */
    public static String removeFormatacao(String cpf) {
        if (cpf == null) {
            return null;
        }
        return cpf.replaceAll("[^0-9]", "");
    }

    /**
     * Formata o CPF adicionando pontos e traços
     * @param cpf CPF apenas com números (ex: 12345678900)
     * @return CPF formatado (ex: 123.456.789-00)
     */
    public static String adicionarFormatacao(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }

        return cpf.substring(0, 3) + "." +
               cpf.substring(3, 6) + "." +
               cpf.substring(6, 9) + "-" +
               cpf.substring(9, 11);
    }

    /**
     * Valida se o CPF tem 11 dígitos após remoção da formatação
     * @param cpf CPF para validar
     * @return true se válido
     */
    public static boolean isValidLength(String cpf) {
        if (cpf == null) {
            return false;
        }
        String cpfLimpo = removeFormatacao(cpf);
        return cpfLimpo.length() == 11;
    }
}

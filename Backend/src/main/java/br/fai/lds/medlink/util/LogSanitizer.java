package br.fai.lds.medlink.util;

/**
 * Utilitário para sanitizar inputs antes de fazer log
 * Previne log injection e vazamento de informações sensíveis
 */
public class LogSanitizer {

    private LogSanitizer() {
        // Utility class
    }

    /**
     * Sanitiza string removendo caracteres perigosos para logs
     * @param input String a ser sanitizada
     * @return String sanitizada
     */
    public static String sanitize(String input) {
        if (input == null) {
            return "null";
        }
        
        return input
                .replaceAll("[\r\n\t]", "_") // Remove CRLF e tabs
                .replaceAll("[\\p{Cntrl}]", "") // Remove caracteres de controle
                .trim();
    }

    /**
     * Sanitiza e limita o tamanho da string para logs
     * @param input String a ser sanitizada
     * @param maxLength Tamanho máximo
     * @return String sanitizada e truncada
     */
    public static String sanitizeAndLimit(String input, int maxLength) {
        String sanitized = sanitize(input);
        if (sanitized.length() > maxLength) {
            return sanitized.substring(0, maxLength) + "...";
        }
        return sanitized;
    }

    /**
     * Sanitiza ID numérico
     * @param id ID a ser sanitizado
     * @return String segura do ID
     */
    public static String sanitizeId(Object id) {
        if (id == null) {
            return "null";
        }
        return String.valueOf(id).replaceAll("[^0-9]", "");
    }
}
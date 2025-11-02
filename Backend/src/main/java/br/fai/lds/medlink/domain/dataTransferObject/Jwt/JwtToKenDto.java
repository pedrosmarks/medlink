package br.fai.lds.medlink.domain.dataTransferObject.Jwt;

public class JwtToKenDto {
    private String token;

    public JwtToKenDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

package br.fai.lds.medlink.implementation.service.authentication.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Profile("jwt")
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtRequestFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("=== JWT FILTER DEBUG ===");
        System.out.println("URL: " + request.getMethod() + " " + request.getRequestURI());
        
        final String requestTokenHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + requestTokenHeader);

        String email = null;
        String jwtToken = null;

        if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")){
            try {
                jwtToken = requestTokenHeader.substring(7);
                System.out.println("Token JWT extraído: " + jwtToken.substring(0, Math.min(30, jwtToken.length())) + "...");
                System.out.println("Token completo length: " + jwtToken.length());

                email = jwtService.getEmailFromToken(jwtToken);
                System.out.println("Email extraído do token: " + email);
                System.out.println("Role do token: " + jwtService.getRoleFromToken(jwtToken));
                System.out.println("UserId do token: " + jwtService.getUserIdFromToken(jwtToken));
            } catch (IllegalArgumentException e) {
                System.out.println("Não foi possível obter o token: " + e.getMessage());
            } catch (ExpiredJwtException e){
                System.out.println("O token já expirou: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Erro ao processar token: " + e.getMessage());
            }
        }else {
            System.out.println("Token não encontrado ou não inicia com Bearer. Header: " + requestTokenHeader);
        }

        if (email != null && SecurityContextHolder
                .getContext()
                .getAuthentication() == null) {
            try {
                System.out.println("Carregando UserDetails para email: " + email);
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
                System.out.println("UserDetails carregado com sucesso. Authorities: " + userDetails.getAuthorities());

                if (jwtService.validadeToken(jwtToken,userDetails)) {
                    System.out.println("Token válido! Autenticando usuário...");
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    // Adicionar dados do JWT ao contexto da requisição
                    request.setAttribute("jwt.role", jwtService.getRoleFromToken(jwtToken));
                    request.setAttribute("jwt.userId", jwtService.getUserIdFromToken(jwtToken));
                    request.setAttribute("jwt.fullname", jwtService.getFullnameFromToken(jwtToken));
                    request.setAttribute("jwt.email", jwtService.getEmailFromToken(jwtToken));

                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    System.out.println("Usuário autenticado com sucesso!");
                } else {
                    System.out.println("Token inválido ou expirado!");
                }
            } catch (Exception e) {
                System.out.println("Erro ao autenticar usuário: " + e.getMessage());
                e.printStackTrace();
            }
        } else if (email == null) {
            System.out.println("Email não extraído do token");
        } else {
            System.out.println("Usuário já autenticado no contexto");
        }
        
        System.out.println("Authentication final: " + (SecurityContextHolder.getContext().getAuthentication() != null ? "AUTHENTICATED" : "NOT AUTHENTICATED"));
        System.out.println("=== FIM JWT FILTER ===");
        
        filterChain.doFilter(request, response);
    }
}

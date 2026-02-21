package com.dao;

import com.dto.LoginDTO;
import com.dto.ProfessorDTO;
import com.utils.SenhaUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class LoginDAO extends DAO {

    public LoginDAO() throws SQLException{
        super();
        conn.setAutoCommit(true);
    }

    // Método para realizar o login, comparando os dados originais do banco com os inseridos pelo usuário
    public ProfessorDTO login(LoginDTO credenciais) throws SQLException {

        String sql = """
                    SELECT
                        id,
                        nome,
                        username,
                        email,
                        senha
                    FROM
                        professor
                    WHERE
                        email = ?
                    
                    """;

        String nome, username, email, senhaHash;
        ProfessorDTO professor;
        UUID id;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, credenciais.getEmail());

            try (ResultSet rs = pstmt.executeQuery()) {

                // Se não encontrar retorna null (o login falhou)
                if (!rs.next()) {
                    return null;
                }

                // Caso contrário, busca o hash da senha no banco
                senhaHash = rs.getString("senha");

                // Se a senha não for compatível retorna null (login falhou)
                if (!SenhaUtils.comparar(credenciais.getSenha(), senhaHash)) {
                    return null;
                }

                Object idObject = rs.getObject("id");
                String idString = String.valueOf(idObject);
                id = UUID.fromString(idString);

                nome = rs.getString("nome");
                username = rs.getString("username");

                professor = new ProfessorDTO(id, nome, username, credenciais.getEmail());
            }
        }

        return professor;
    }
}

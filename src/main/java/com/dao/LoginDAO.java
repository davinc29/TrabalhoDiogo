package com.dao;

import com.dto.AlunoViewDTO;
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
    public Integer login(LoginDTO credenciais) throws SQLException {

        String sql = """
                    SELECT
                        senha
                    FROM
                        professor
                    WHERE
                        email = ?
                    
                    """;

        String senhaHash;
        ProfessorDTO professor;
        UUID id;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, credenciais.getEmail());

            try (ResultSet rs = pstmt.executeQuery()) {

                // Se não encontrar retorna null (o login falhou)
                if (!rs.next()) {

                    sql = """
                          SELECT
                            senha
                        FROM
                            aluno
                        WHERE
                            email = ?
                          """;

                    try (PreparedStatement pstmt2 = conn.prepareStatement(sql)) {
                        pstmt2.setString(1, credenciais.getEmail());

                        try (ResultSet rs2 = pstmt2.executeQuery()) {

                            if (!rs2.next()) {
                                return 0;
                            }


                            // Caso contrário, busca o hash da senha no banco
                            senhaHash = rs2.getString("senha");

                            // Se a senha não for compatível retorna null (login falhou)
                            if (!SenhaUtils.comparar(credenciais.getSenha(), senhaHash)) {
                                return 0;
                            }

                            return 1;
                        }
                    }

                } else {
                    // Caso contrário, busca o hash da senha no banco
                    senhaHash = rs.getString("senha");

                    // Se a senha não for compatível retorna null (login falhou)
                    if (!SenhaUtils.comparar(credenciais.getSenha(), senhaHash)) {
                        return 0;
                    }

                    return 2;
                }
            }
        }
    }

    public ProfessorDTO encontrarProfessor(LoginDTO credenciais) throws SQLException{
        String sql = """
                SELECT
                    id,
                    nome,
                    username
                FROM
                    professor
                WHERE
                    email = ?
                """;

        UUID id;
        String temp, nome, username;
        ProfessorDTO professor = null;


        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,credenciais.getEmail());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                temp = String.valueOf(rs.getObject("id"));
                id = UUID.fromString(temp);

                nome = rs.getString("nome");
                username = rs.getString("username");

                professor = new ProfessorDTO(id, nome, username, credenciais.getEmail());
                return professor;
            }

            conn.commit();
        }

        conn.rollback();
        return professor;
    }

    public AlunoViewDTO encontrarAluno(LoginDTO credenciais) throws SQLException{
        String sql = """
                SELECT
                    a.id,
                    a.nome,
                    a.matricula,
                    p.turma_ano
                FROM
                    aluno
                JOIN
                    pre_matricula p
                    ON p.matricula = a.matricula
                WHERE
                    email = ?
                """;

        UUID id;
        String temp, nome, matricula, turmaAno;
        AlunoViewDTO aluno = null;


        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,credenciais.getEmail());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                temp = String.valueOf(rs.getObject("id"));
                id = UUID.fromString(temp);

                nome = rs.getString("nome");
                matricula = rs.getString("matricula");
                turmaAno = rs.getString("turma_ano");

                aluno = new AlunoViewDTO(id, nome, matricula, credenciais.getEmail(), turmaAno);
                return aluno;
            }

            conn.commit();
        }

        conn.rollback();
        return aluno;
    }
}

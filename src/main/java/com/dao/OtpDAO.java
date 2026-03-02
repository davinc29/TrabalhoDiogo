package com.dao;

import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class OtpDAO extends DAO {

    // Tempo de validade do código em minutos
    private static final int MINUTOS_EXPIRACAO = 5;

    private static final SecureRandom random = new SecureRandom();

    public OtpDAO() throws SQLException {
        super();
    }

    public void limparExpirados() throws SQLException {
        String sql = "SELECT limpar_tokens_expirados();";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Limpeza de tokens executada com sucesso.");
        }
        catch (SQLException e) {
            System.err.println("Erro ao limpar tokens: " + e.getMessage());
            throw e;
        }
    }

    public String gerar(String email) throws SQLException {
        // Código de 6 dígitos
        int numero = 100000 + random.nextInt(900000);
        String codigo = String.format("%06d", numero);

        LocalDateTime expiracao = LocalDateTime.now().plusMinutes(MINUTOS_EXPIRACAO);

        // Invalida os códigos anteriores
        String sqlInvalidar = "UPDATE otp_tokens SET usado = TRUE WHERE email = ? AND usado = FALSE";

        try (PreparedStatement pstmt = conn.prepareStatement(sqlInvalidar)) {
            pstmt.setString(1, email);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            conn.rollback();
            throw e;
        }

        String sqlInserir = """
                INSERT INTO
                    otp_tokens (email, codigo, expiracao, usado)
                VALUES
                    (?, ?, ?, FALSE)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sqlInserir)) {
            pstmt.setString(1, email);
            pstmt.setString(2, codigo);
            pstmt.setTimestamp(3, Timestamp.valueOf(expiracao));

            pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }

        return codigo;
    }

    public boolean validar(String email, String codigo) throws SQLException {

        String sqlBuscar = """
                SELECT id FROM otp_tokens
                WHERE email = ?
                AND codigo = ?
                AND usado = FALSE
                AND expiracao > NOW()
                ORDER BY data_criacao DESC
                LIMIT 1
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sqlBuscar)) {
            pstmt.setString(1, email);
            pstmt.setString(2, codigo);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (!rs.next()) {
                    return false;
                }

                int idToken = rs.getInt("id");
                String sqlMarcar = "UPDATE otp_tokens SET usado = TRUE WHERE id = ?";

                try (PreparedStatement psUpdate = conn.prepareStatement(sqlMarcar)) {
                    psUpdate.setInt(1, idToken);
                    psUpdate.executeUpdate();
                    conn.commit();
                }

                return true;
            }

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public UUID buscarIdAlunoPorEmail(String email) throws SQLException {
        String sql = """
                SELECT id
                FROM aluno
                WHERE email = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    conn.commit();
                    return rs.getObject("id", UUID.class);
                }
            }

            conn.rollback();
            return null;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public String verificarTipoUsuarioPorEmail(String email) throws SQLException {
        String sqlAluno = "SELECT id FROM aluno WHERE email = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sqlAluno)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    conn.commit();
                    return "aluno";
                }
            }
        }

        String sqlProfessor = "SELECT id FROM professor WHERE email = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sqlProfessor)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    conn.commit();
                    return "professor";
                }
            }
        }

        conn.rollback();
        return null; // E-mail não encontrado em nenhuma tabela
    }
}

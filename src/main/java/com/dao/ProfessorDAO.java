package com.dao;

import com.dto.ProfessorDTO;
import com.model.Professor;
import com.utils.SenhaUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ProfessorDAO extends DAO {

    public ProfessorDAO() throws SQLException {
        super();
    }

    public void cadastrar(Professor professor) throws SQLException {
        String nome = professor.getNome();
        String usuario = professor.getUsername();
        String email = professor.getEmail();
        String senhaHash = SenhaUtils.hashear(professor.getSenha());

        String sql = """
                INSERT INTO
                    professor (nome, username, email, senha)
                VALUES
                    (?,?,?,?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, usuario);
            pstmt.setString(3, email);
            pstmt.setString(4, senhaHash);

            pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public void atualizar(ProfessorDTO original, ProfessorDTO atualizado) throws SQLException {
        UUID id = original.getId();
        String nome = atualizado.getNome();
        String usuario = atualizado.getUsername();
        String email = atualizado.getEmail();

        if (Objects.equals(nome, original.getNome())
                && Objects.equals(usuario, original.getUsername())
                && Objects.equals(email, original.getEmail())) {
            return;
        }

        if (!Objects.equals(nome, original.getNome())
                && !Objects.equals(usuario, original.getUsername())
                && !Objects.equals(email, original.getEmail())) {

            String sql = """
                    UPDATE professor
                    SET nome = ?, username = ?, email = ?
                    WHERE id = ?
                    """;

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nome);
                pstmt.setString(2, usuario);
                pstmt.setString(3, email);
                pstmt.setObject(4, id);
                pstmt.executeUpdate();
                conn.commit();
                return;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }

        if (!Objects.equals(nome, original.getNome())
                && !Objects.equals(usuario, original.getUsername())) {

            String sql = """
                    UPDATE professor
                    SET nome = ?, username = ?
                    WHERE id = ?
                    """;

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nome);
                pstmt.setString(2, usuario);
                pstmt.setObject(3, id);
                pstmt.executeUpdate();
                conn.commit();
                return;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }

        if (!Objects.equals(nome, original.getNome())
                && !Objects.equals(email, original.getEmail())) {

            String sql = """
                    UPDATE professor
                    SET nome = ?, email = ?
                    WHERE id = ?
                    """;

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nome);
                pstmt.setString(2, email);
                pstmt.setObject(3, id);
                pstmt.executeUpdate();
                conn.commit();
                return;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }

        if (!Objects.equals(usuario, original.getUsername())
                && !Objects.equals(email, original.getEmail())) {

            String sql = """
                    UPDATE professor
                    SET username = ?, email = ?
                    WHERE id = ?
                    """;

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, usuario);
                pstmt.setString(2, email);
                pstmt.setObject(3, id);
                pstmt.executeUpdate();
                conn.commit();
                return;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }

        if (!Objects.equals(nome, original.getNome())) {
            String sql = """
                    UPDATE professor
                    SET nome = ?
                    WHERE id = ?
                    """;

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nome);
                pstmt.setObject(2, id);
                pstmt.executeUpdate();
                conn.commit();
                return;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }

        if (!Objects.equals(usuario, original.getUsername())) {
            String sql = """
                    UPDATE professor
                    SET username = ?
                    WHERE id = ?
                    """;

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, usuario);
                pstmt.setObject(2, id);
                pstmt.executeUpdate();
                conn.commit();
                return;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }

        String sql = """
                UPDATE professor
                SET email = ?
                WHERE id = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setObject(2, id);
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public List<ProfessorDTO> listar() throws SQLException {
        String sql = """
                SELECT
                    id, nome, username, email
                FROM
                    professor
                ORDER BY
                    nome
                """;

        List<ProfessorDTO> professores = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                UUID id = rs.getObject("id", UUID.class);
                String nome = rs.getString("nome");
                String username = rs.getString("username");
                String email = rs.getString("email");

                professores.add(new ProfessorDTO(id, nome, username, email));
            }

            conn.commit();
            return professores;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public void deletar(UUID id) throws SQLException {
        String sql = """
                DELETE FROM
                    professor
                WHERE
                    id = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public ProfessorDTO pesquisarPorId(UUID id) throws SQLException {
        String sql = """
                SELECT
                    nome, username, email
                FROM
                    professor
                WHERE
                    id = ?
                """;

        ProfessorDTO professor = null;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String nome = rs.getString("nome");
                String username = rs.getString("username");
                String email = rs.getString("email");

                professor = new ProfessorDTO(id, nome, username, email);
            }

            conn.commit();
            return professor;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public ProfessorDTO pesquisarPorEmail(String email) throws SQLException {
        String sql = """
                SELECT
                    id, nome, username
                FROM
                    professor
                WHERE
                    email = ?
                """;

        ProfessorDTO professor = null;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String nome = rs.getString("nome");
                String username = rs.getString("username");
                UUID id = rs.getObject("id", UUID.class);

                professor = new ProfessorDTO(id, nome, username, email);
            }

            conn.commit();
            return professor;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public Map<String, UUID> mapNomeId() throws SQLException {
        String sql = """
                SELECT
                    id,
                    nome
                FROM
                    professor
                """;

        Map<String, UUID> mapNomeId = new HashMap<>();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String nome = rs.getString("nome");
                UUID id = rs.getObject("id", UUID.class);
                mapNomeId.put(nome, id);
            }

            return mapNomeId;
        }
    }

    public void recuperarSenhaProfessor(String email, String senhaHash) throws SQLException {
        String sql = "UPDATE professor SET senha = ? WHERE email = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, senhaHash);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public void atualizarSenhaProfessor(String email, String senhaAtual, String senhaNova) throws SQLException {
        String sql = """
                SELECT
                    senha
                FROM
                    professor
                WHERE
                    email = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String senha = rs.getString("senha");

                if (SenhaUtils.comparar(senhaAtual, senha)) {
                    String update = "UPDATE professor SET senha = ? WHERE email = ?";

                    try (PreparedStatement pstmt2 = conn.prepareStatement(update)) {
                        pstmt2.setString(1, SenhaUtils.hashear(senhaNova));
                        pstmt2.setString(2, email);
                        pstmt2.executeUpdate();
                        conn.commit();
                    } catch (SQLException e) {
                        conn.rollback();
                        throw e;
                    }
                }
            }

        } catch (SQLException err) {
            conn.rollback();
            throw err;
        }
    }

    public List<ProfessorDTO> listarProfessores(String nome, String username, String email) throws SQLException {
        String sql = """
                SELECT
                    id,
                    nome,
                    username,
                    email
                FROM
                    professor
                WHERE
                    (? IS NULL OR LOWER(nome) LIKE LOWER(?))
                AND
                    (? IS NULL OR LOWER(username) LIKE LOWER(?))
                AND
                    (? IS NULL OR LOWER(email) LIKE LOWER(?))
                ORDER BY
                    nome
                """;

        List<ProfessorDTO> professores = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, nome);
            pstmt.setString(2, nome == null ? null : "%" + nome + "%");

            pstmt.setObject(3, username);
            pstmt.setString(4, username == null ? null : "%" + username + "%");

            pstmt.setObject(5, email);
            pstmt.setString(6, email == null ? null : "%" + email + "%");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                UUID id = rs.getObject("id", UUID.class);
                String nomeProf = rs.getString("nome");
                String user = rs.getString("username");
                String mail = rs.getString("email");

                professores.add(new ProfessorDTO(id, nomeProf, user, mail));
            }

            conn.commit();
            return professores;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }
}
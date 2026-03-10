package com.dao;

import com.dto.DisciplinaViewDTO;
import com.model.Disciplina;

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

public class DisciplinaDAO extends DAO {

    public DisciplinaDAO() throws SQLException {
        super();
    }

    public void cadastrar(Disciplina disciplina) throws SQLException {
        String nome = disciplina.getNome();
        UUID idProfessor = disciplina.getIdProfessor();

        String sql = """
                INSERT INTO
                    disciplina (nome, id_professor)
                VALUES
                    (?,?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setObject(2, idProfessor);
            pstmt.execute();
            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public void atualizar(Disciplina original, Disciplina atualizado) throws SQLException {
        Integer idDisciplina = original.getIdDisciplina();
        String nome = atualizado.getNome();
        UUID idProfessor = atualizado.getIdProfessor();

        if (Objects.equals(nome, original.getNome()) && Objects.equals(idProfessor, original.getIdProfessor())) {
            return;
        }

        if (!Objects.equals(nome, original.getNome()) && !Objects.equals(idProfessor, original.getIdProfessor())) {
            String sql = """
                    UPDATE disciplina
                    SET nome = ?, id_professor = ?
                    WHERE id = ?
                    """;

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nome);
                pstmt.setObject(2, idProfessor);
                pstmt.setInt(3, idDisciplina);
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
                    UPDATE disciplina
                    SET nome = ?
                    WHERE id = ?
                    """;

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nome);
                pstmt.setInt(2, idDisciplina);
                pstmt.executeUpdate();
                conn.commit();
                return;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }

        String sql = """
                UPDATE disciplina
                SET id_professor = ?
                WHERE id = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, idProfessor);
            pstmt.setInt(2, idDisciplina);
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public List<DisciplinaViewDTO> listar() throws SQLException {
        String sql = """
                SELECT
                    d.id as id,
                    d.nome as nome_disciplina,
                    p.nome as nome_professor,
                    p.email as email_professor
                FROM
                    disciplina d
                JOIN
                    professor p
                    ON p.id = d.id_professor
                ORDER BY
                    d.id
                """;

        List<DisciplinaViewDTO> disciplinas = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String nomeDisciplina = rs.getString("nome_disciplina");
                String nomeProfessor = rs.getString("nome_professor");
                String emailProfessor = rs.getString("email_professor");

                disciplinas.add(new DisciplinaViewDTO(id, nomeDisciplina, nomeProfessor, emailProfessor));
            }

            conn.commit();
            return disciplinas;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public void deletar(Integer id) throws SQLException {
        String sql = """
                DELETE FROM
                    disciplina
                WHERE
                    id = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public void deletarPorIdProfessor(UUID id_prof) throws SQLException {
        String sql = """
                DELETE FROM
                    disciplina
                WHERE
                    id_professor = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id_prof);
            pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public Disciplina pesquisarPorId(Integer id) throws SQLException {
        String sql = """
                SELECT
                    id,
                    nome,
                    id_professor
                FROM
                    disciplina
                WHERE
                    id = ?
                """;

        Disciplina disciplina = null;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer idDisciplina = rs.getInt("id");
                String nome = rs.getString("nome");
                UUID idProfessor = rs.getObject("id_professor", UUID.class);

                disciplina = new Disciplina(idDisciplina, nome, idProfessor);
            }

            conn.commit();
            return disciplina;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public Map<String, Integer> mapNomeId() throws SQLException {
        String sql = """
                SELECT
                    id,
                    nome
                FROM
                    disciplina
                """;

        Map<String, Integer> mapNomeId = new HashMap<>();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String nome = rs.getString("nome");
                Integer id = rs.getInt("id");
                mapNomeId.put(nome, id);
            }

            return mapNomeId;
        }
    }

    public Map<String, Integer> mapNomeIdProfessor(UUID idProfessor) throws SQLException {
        String sql = """
                SELECT
                    d.id,
                    d.nome
                FROM
                    disciplina d
                JOIN
                    professor p
                    ON p.id = d.id_professor
                WHERE
                    p.id = ?
                """;

        Map<String, Integer> mapNomeId = new HashMap<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, idProfessor);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String nome = rs.getString("nome");
                Integer id = rs.getInt("id");
                mapNomeId.put(nome, id);
            }

            return mapNomeId;
        }
    }

    public List<DisciplinaViewDTO> listarDisciplinas(String nomeDisciplina, Integer idDisciplina, String nomeProfessor) throws SQLException {
        String sql = """
                SELECT
                    d.id as id,
                    d.nome as nome_disciplina,
                    p.nome as nome_professor,
                    p.email as email_professor
                FROM
                    disciplina d
                JOIN
                    professor p
                    ON p.id = d.id_professor
                WHERE
                    (? IS NULL OR LOWER(d.nome) LIKE LOWER(?))
                AND
                    (? IS NULL OR d.id = ?)
                AND
                    (? IS NULL OR LOWER(p.nome) LIKE LOWER(?))
                ORDER BY
                    d.id
                """;

        List<DisciplinaViewDTO> disciplinas = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, nomeDisciplina);
            pstmt.setString(2, nomeDisciplina == null ? null : "%" + nomeDisciplina + "%");

            pstmt.setObject(3, idDisciplina);
            pstmt.setObject(4, idDisciplina);

            pstmt.setObject(5, nomeProfessor);
            pstmt.setString(6, nomeProfessor == null ? null : "%" + nomeProfessor + "%");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String nomeDisc = rs.getString("nome_disciplina");
                String nomeProf = rs.getString("nome_professor");
                String emailProf = rs.getString("email_professor");

                disciplinas.add(new DisciplinaViewDTO(id, nomeDisc, nomeProf, emailProf));
            }

            conn.commit();
            return disciplinas;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }
}
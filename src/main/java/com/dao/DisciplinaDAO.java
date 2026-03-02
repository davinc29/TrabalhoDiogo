package com.dao;

import com.dto.DisciplinaViewDTO;
import com.model.Disciplina;
import com.model.Professor;
import com.utils.SenhaUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DisciplinaDAO extends DAO{

    public DisciplinaDAO() throws SQLException {
        super();
    }

    public void cadastrar(Disciplina disciplina) throws SQLException{
        String nome = disciplina.getNome();
        UUID idProfessor = disciplina.getIdProfessor();

        String sql = """
                INSERT INTO
                    disciplina (nome, id_professor)
                VALUES
                    (?,?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,nome);
            pstmt.setObject(2,idProfessor);

            pstmt.execute();

            conn.commit();
        } catch (SQLException e) {
            System.out.println("Erro no cadastro do disciplina!");
            conn.rollback();
            throw e;
        }
    }

    public void atualizar(Disciplina original, Disciplina atualizado) throws SQLException{
        // Dados originais
        Integer idDisciplina = original.getIdDisciplina();
        String nome = atualizado.getNome();
        UUID idProfessor = original.getIdProfessor();

        StringBuilder sql = new StringBuilder("UPDATE disciplina SET");
        List<Object> valores = new ArrayList<>();

        if (!Objects.equals(nome, original.getNome())) {
            sql.append(" nome = ?, ");
            valores.add(nome);
        }
        if (!Objects.equals(idProfessor, original.getIdProfessor())) {
            sql.append(" id_professor = ?, ");
            valores.add(idProfessor);
        }

        if (valores.isEmpty()) {
            return;
        }

        sql.setLength(sql.length() - 2);

        sql.append(" WHERE id = ?");
        valores.add(idDisciplina);

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < valores.size(); i++) {
                pstmt.setObject(i+1, valores.get(i));
            }

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
                    disciplina
                """;

        List<DisciplinaViewDTO> disciplinas = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    Integer id = rs.getInt("id");
                    String nomeDisciplina = rs.getString("nome_disciplina");
                    String nomeProfessor = rs.getString("nome_professor");
                    String emailProfessor = rs.getString("email_professor");

                    DisciplinaViewDTO disciplina = new DisciplinaViewDTO(id, nomeDisciplina, nomeProfessor, emailProfessor);
                    disciplinas.add(disciplina);
                }

                conn.commit();
            }
        }

        conn.rollback();
        return disciplinas;
    }

    public void deletar(Integer id) throws SQLException{
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

    public Disciplina pesquisarPorId(Integer id) throws SQLException{
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

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1,id);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer idDisciplina = rs.getInt("id");
                String nome = rs.getString("nome");
                UUID idProfessor = rs.getObject("id_professor", UUID.class);

                disciplina = new Disciplina(idDisciplina, nome, idProfessor);
                return disciplina;
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }

        conn.commit();
        return disciplina;
    }

    public Map<String, Integer> mapNomeId() throws SQLException{
        String sql = """
                SELECT
                    id,
                    nome
                FROM
                    disciplina
                """;

        Map<String, Integer> mapNomeId = new HashMap<>();

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()) {
                String nome = rs.getString("nome");
                Integer id = rs.getInt("id");

                mapNomeId.put(nome, id);
            }
        }

        return mapNomeId;
    }

    public Map<String, Integer> mapNomeIdProfessor(UUID idProfessor) throws SQLException{
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
            while(rs.next()) {
                String nome = rs.getString("nome");
                Integer id = rs.getInt("id");

                mapNomeId.put(nome, id);
            }
        }

        return mapNomeId;
    }
}

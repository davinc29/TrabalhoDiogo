package com.dao;

import com.dto.ProfessorDTO;
import com.model.Professor;
import com.utils.SenhaUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ProfessorDAO extends DAO{

    public ProfessorDAO() throws SQLException {
        super();
    }

//    CREATE
    public void cadastrar(Professor professor) throws SQLException{
        String nome = professor.getNome();
        String usuario = professor.getUsername();
        String email = professor.getEmail();

        String senha = professor.getSenha();
        String senhaHash = SenhaUtils.hashear(senha);

        String sql = """
                INSERT INTO
                    professor (nome, username, email, senha)
                VALUES
                    (?,?,?,?,?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,nome);
            pstmt.setString(2,usuario);
            pstmt.setString(3,email);
            pstmt.setString(4,senhaHash);

            pstmt.execute();

            conn.commit();
        } catch (SQLException e) {
            System.out.println("Erro no cadastro do professor!");
            conn.rollback();
            throw e;
        }
    }

    public List<ProfessorDTO> listarPorDisciplina(String nomeDisciplina) throws SQLException{
        String sql = """
                SELECT
                    p.*
                FROM
                    professor p
                JOIN
                    disciplina d
                    ON p.id = d.id_professor
                WHERE
                    d.nome = ?
                """;

        List<ProfessorDTO> professores = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,nomeDisciplina);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                Object id = rs.getObject("id");
                String idString = String.valueOf(id);
                UUID idUuid = UUID.fromString(idString);

                String nome = rs.getString("nome");
                String usuario = rs.getString("usuario");
                String email = rs.getString("email");

                ProfessorDTO professor = new ProfessorDTO(idUuid, nome, usuario, email);
            }
        }

        conn.commit();
        return professores;
    }

    public List<ProfessorDTO> listarPorTurma(String turma, Integer ano) throws SQLException{
        String sql = """
                SELECT
                    DISTINCT
                    p.*
                FROM
                    professor p
                JOIN
                    disciplina d
                    ON p.id = d.id_professor
                JOIN
                    boletim b
                    ON d.id = b.id_disciplina
                JOIN
                    aluno a
                    ON b.id_aluno = a.id
                WHERE
                    a.turma_ano = ?
                """;

        List<ProfessorDTO> professores = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,turma+" - "+ano);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object id = rs.getObject("id");
                String idString = String.valueOf(id);
                UUID idUuid = UUID.fromString(idString);

                String nome = rs.getString("nome");
                String usuario = rs.getString("usuario");
                String email = rs.getString("email");

                ProfessorDTO professor = new ProfessorDTO(idUuid, nome, usuario, email);
            }
        }

        conn.commit();
        return professores;
    }

    public void atualizar(ProfessorDTO original, ProfessorDTO atualizado) throws SQLException{
        // Dados originais
        UUID id = original.getId();
        String nome = atualizado.getNome();
        String usuario = atualizado.getUsername();
        String email = atualizado.getEmail();

        StringBuilder sql = new StringBuilder("UPDATE professor SET");
        List<Object> valores = new ArrayList<>();

        if (!Objects.equals(nome, original.getId())) {
            sql.append(" nome = ?, ");
            valores.add(nome);
        }
        if (!Objects.equals(usuario, original.getUsername())) {
            sql.append("usuario = ?, ");
            valores.add(usuario);
        }
        if (!Objects.equals(email, original.getEmail())) {
            sql.append("email = ?, ");
            valores.add(email);
        }

        if (valores.isEmpty()) {
            return;
        }

        sql.setLength(sql.length() - 2);

        sql.append(" WHERE id = ?");
        valores.add(id);

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

    public void deletar(UUID id) throws SQLException{
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
}

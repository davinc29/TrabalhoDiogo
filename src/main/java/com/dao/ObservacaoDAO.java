package com.dao;

import com.dto.ObservacaoViewDTO;
import com.model.Observacao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ObservacaoDAO extends DAO{

    public ObservacaoDAO() throws SQLException {
        super();
    }

    public void cadastrar(Observacao observacao) throws SQLException {
        String textoObsevacao = observacao.getTextoObservacao();
        UUID idAluno = observacao.getIdAluno();
        Integer idDisciplina = observacao.getIdDisciplina();

        String sql = """
                INSERT INTO
                    observacao (texto_observacao, id_aluno, id_disciplina)
                VALUES
                    (?,?,?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,textoObsevacao);
            pstmt.setObject(2,idAluno);
            pstmt.setInt(3,idDisciplina);

            pstmt.execute();

            conn.commit();
        } catch(SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public Observacao pesquisarPorId(Integer id) throws SQLException{
        String sql = """
                SELECT
                    * except (id)
                FROM
                    observacao
                WHERE
                    id = ?
                """;

        Observacao observacao = null;

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1,id);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String textoObservacao = rs.getString("texto_observacao");

                Object temp = rs.getObject("id_aluno");
                UUID idAluno = UUID.fromString(String.valueOf(temp));

                Integer idDisciplina = rs.getInt("id_disciplina");

                observacao = new Observacao(textoObservacao, idAluno, idDisciplina);
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }

        conn.commit();
        return observacao;
    }

    public List<ObservacaoViewDTO> listarPorAluno(UUID idAluno) throws SQLException{

        String sql = """
                SELECT
                    a.nome as nome_aluno,
                    p2.turma_ano as turma_ano,
                    d.nome as nome_disciplina,
                    p.nome as nome_professor,
                    o.texto_observacao as observacao
                FROM
                    observacao o
                JOIN
                    aluno a
                    ON a.id = o.id_aluno
                JOIN
                    disciplina d
                    ON d.id = o.id_disciplina
                JOIN
                    professor p
                    ON p.id = d.id_professor
                JOIN
                    pre_matricula p2
                    ON p2.matricula = a.matricula
                WHERE
                    a.id = ?
                """;

        List<ObservacaoViewDTO> observacoes = new ArrayList<>();

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1,idAluno);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String nomeAluno = rs.getString("nome_aluno");
                String turmaAno = rs.getString("turma_ano");
                String nomeProfessor = rs.getString("nome_professor");
                String nomeDisciplina = rs.getString("nome_disciplina");
                String observacao = rs.getString("observacao");

                ObservacaoViewDTO observacaoView = new ObservacaoViewDTO(nomeAluno,turmaAno, nomeDisciplina, nomeProfessor, observacao);

                observacoes.add(observacaoView);
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }

        conn.commit();
        return observacoes;
    }

    public List<ObservacaoViewDTO> listarPorProfessor(UUID idProfessor) throws SQLException{

        String sql = """
                SELECT
                    a.nome as nome_aluno,
                    p2.turma_ano as turma_ano,
                    d.nome as nome_disciplina,
                    p.nome as nome_professor,
                    o.texto_observacao as observacao
                FROM
                    observacao o
                JOIN
                    aluno a
                    ON a.id = o.id_aluno
                JOIN
                    disciplina d
                    ON d.id = o.id_disciplina
                JOIN
                    professor p
                    ON p.id = d.id_professor
                JOIN
                    pre_matricula p2
                    ON p2.matricula = a.matricula
                WHERE
                    p.id = ?
                """;

        List<ObservacaoViewDTO> observacoes = new ArrayList<>();

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1,idProfessor);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String nomeAluno = rs.getString("nome_aluno");
                String turmaAno = rs.getString("turma_ano");
                String nomeProfessor = rs.getString("nome_professor");
                String nomeDisciplina = rs.getString("nome_disciplina");
                String observacao = rs.getString("observacao");

                ObservacaoViewDTO observacaoView = new ObservacaoViewDTO(nomeAluno, turmaAno, nomeDisciplina, nomeProfessor, observacao);

                observacoes.add(observacaoView);
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }

        conn.commit();
        return observacoes;
    }

    public void atualizar(Observacao original, Observacao atualizado) throws SQLException{
        Integer id = original.getId();
        String textoObservacao = atualizado.getTextoObservacao();

        String temp = String.valueOf(atualizado.getIdAluno());
        UUID idAluno = UUID.fromString(temp);

        Integer idDisciplina = atualizado.getIdDisciplina();

        StringBuilder sql = new StringBuilder("UPDATE observacao SET ");
        List<Object> valores = new ArrayList<>();

        if (!Objects.equals(textoObservacao, original.getTextoObservacao())) {
            sql.append("texto_observacao = ?, ");
            valores.add(textoObservacao);
        }
        if (!Objects.equals(idAluno, original.getIdAluno())) {
            sql.append("id_aluno = ?, ");
            valores.add(idAluno);
        }
        if (!Objects.equals(idDisciplina, original.getIdDisciplina())) {
            sql.append("id_disciplina = ?, ");
            valores.add(idDisciplina);
        }

        if (valores.isEmpty()) {
            return;
        }

        sql.setLength(sql.length() - 2);
        sql.append(" WHERE id = ?");
        valores.add(id);

        try(PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < valores.size(); i++) {
                pstmt.setObject(i + 1, valores.get(i));
            }

            pstmt.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public void deletar(Integer id) throws SQLException{
        String sql = """
                DELETE FROM
                    boletim
                WHERE
                    id = ?
                """;

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1,id);

            pstmt.executeUpdate();

            conn.commit();
        } catch(SQLException e) {
            conn.rollback();
            throw e;
        }
    }
}

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

public class ObservacaoDAO extends DAO {

    public ObservacaoDAO() throws SQLException {
        super();
    }

    public void cadastrar(Observacao observacao) throws SQLException {
        String sql = """
                INSERT INTO
                    observacao (texto_observacao, id_aluno, id_disciplina)
                VALUES
                    (?, ?, ?)
                """;

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, observacao.getTextoObservacao());
            pstmt.setObject(2, observacao.getIdAluno());
            pstmt.setInt(3, observacao.getIdDisciplina());
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    public void atualizar(Observacao original, Observacao atualizado) throws SQLException {
        Integer id = original.getId();
        String texto = atualizado.getTextoObservacao();
        UUID idAluno = atualizado.getIdAluno();
        Integer idDisciplina = atualizado.getIdDisciplina();

        if (Objects.equals(texto, original.getTextoObservacao())
                && Objects.equals(idAluno, original.getIdAluno())
                && Objects.equals(idDisciplina, original.getIdDisciplina())) {
            return;
        }

        String sql = """
                UPDATE
                    observacao
                SET
                    texto_observacao = ?,
                    id_aluno = ?,
                    id_disciplina = ?
                WHERE
                    id = ?
                """;

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, texto);
            pstmt.setObject(2, idAluno);
            pstmt.setInt(3, idDisciplina);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    public void deletar(Integer idObservacao) throws SQLException {
        String sql = """
                DELETE FROM
                    observacao
                WHERE
                    id = ?
                """;

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idObservacao);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    public Observacao pesquisarPorId(Integer idObservacao) throws SQLException {
        String sql = """
                SELECT
                    id,
                    texto_observacao,
                    id_aluno,
                    id_disciplina
                FROM
                    observacao
                WHERE
                    id = ?
                """;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Observacao observacao = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idObservacao);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Integer id = rs.getInt("id");
                String texto = rs.getString("texto_observacao");
                UUID idAluno = rs.getObject("id_aluno", UUID.class);
                Integer idDisciplina = rs.getInt("id_disciplina");

                observacao = new Observacao(id, texto, idAluno, idDisciplina);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }

        return observacao;
    }

    public List<ObservacaoViewDTO> listarPorProfessor(UUID idProfessor) throws SQLException {
        String sql = """
                SELECT
                    o.id,
                    a.nome AS nome_aluno,
                    pm.turma_ano,
                    d.nome AS nome_disciplina,
                    p.nome AS nome_professor,
                    o.texto_observacao AS observacao
                FROM
                    observacao o
                JOIN
                    aluno a
                    ON a.id = o.id_aluno
                JOIN
                    pre_matricula pm
                    ON pm.matricula = a.matricula
                JOIN
                    disciplina d
                    ON d.id = o.id_disciplina
                JOIN
                    professor p
                    ON p.id = d.id_professor
                WHERE
                    p.id = ?
                """;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ObservacaoViewDTO> observacoes = new ArrayList<>();

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, idProfessor);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String nomeAluno = rs.getString("nome_aluno");
                String turmaAno = rs.getString("turma_ano");
                String nomeDisciplina = rs.getString("nome_disciplina");
                String nomeProfessor = rs.getString("nome_professor");
                String observacao = rs.getString("observacao");

                observacoes.add(new ObservacaoViewDTO(
                        id,
                        nomeAluno,
                        turmaAno,
                        nomeDisciplina,
                        nomeProfessor,
                        observacao
                ));
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }

        return observacoes;
    }

    public List<ObservacaoViewDTO> listarPorAluno(UUID idAluno) throws SQLException {
        String sql = """
                SELECT
                    o.id,
                    a.nome AS nome_aluno,
                    pm.turma_ano,
                    d.nome AS nome_disciplina,
                    p.nome AS nome_professor,
                    o.texto_observacao AS observacao
                FROM
                    observacao o
                JOIN
                    aluno a
                    ON a.id = o.id_aluno
                JOIN
                    pre_matricula pm
                    ON pm.matricula = a.matricula
                JOIN
                    disciplina d
                    ON d.id = o.id_disciplina
                JOIN
                    professor p
                    ON p.id = d.id_professor
                WHERE
                    o.id_aluno = ?
                """;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ObservacaoViewDTO> observacoes = new ArrayList<>();

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, idAluno);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String nomeAluno = rs.getString("nome_aluno");
                String turmaAno = rs.getString("turma_ano");
                String nomeDisciplina = rs.getString("nome_disciplina");
                String nomeProfessor = rs.getString("nome_professor");
                String observacao = rs.getString("observacao");

                observacoes.add(new ObservacaoViewDTO(
                        id,
                        nomeAluno,
                        turmaAno,
                        nomeDisciplina,
                        nomeProfessor,
                        observacao
                ));
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }

        return observacoes;
    }

    public List<ObservacaoViewDTO> listarPorAluno(UUID idAluno, Integer idObservacao, String nomeDisciplina, String nomeProfessor, String textoObservacao) throws SQLException {
        String sql = """
                SELECT
                    o.id,
                    a.nome AS nome_aluno,
                    pm.turma_ano,
                    d.nome AS nome_disciplina,
                    p.nome AS nome_professor,
                    o.texto_observacao AS observacao
                FROM
                    observacao o
                JOIN
                    aluno a
                    ON a.id = o.id_aluno
                JOIN
                    pre_matricula pm
                    ON pm.matricula = a.matricula
                JOIN
                    disciplina d
                    ON d.id = o.id_disciplina
                JOIN
                    professor p
                    ON p.id = d.id_professor
                WHERE
                    o.id_aluno = ?
                AND
                    (? IS NULL OR o.id = ?)
                AND
                    (? IS NULL OR LOWER(d.nome) LIKE LOWER(?))
                AND
                    (? IS NULL OR LOWER(p.nome) LIKE LOWER(?))
                AND
                    (? IS NULL OR LOWER(o.texto_observacao) LIKE LOWER(?))
                """;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ObservacaoViewDTO> observacoes = new ArrayList<>();

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, idAluno);

            pstmt.setObject(2, idObservacao);
            pstmt.setObject(3, idObservacao);

            pstmt.setString(4, nomeDisciplina == null || nomeDisciplina.isBlank() ? null : nomeDisciplina);
            pstmt.setString(5, nomeDisciplina == null || nomeDisciplina.isBlank() ? null : "%" + nomeDisciplina + "%");

            pstmt.setString(6, nomeProfessor == null || nomeProfessor.isBlank() ? null : nomeProfessor);
            pstmt.setString(7, nomeProfessor == null || nomeProfessor.isBlank() ? null : "%" + nomeProfessor + "%");

            pstmt.setString(8, textoObservacao == null || textoObservacao.isBlank() ? null : textoObservacao);
            pstmt.setString(9, textoObservacao == null || textoObservacao.isBlank() ? null : "%" + textoObservacao + "%");

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String nomeAluno = rs.getString("nome_aluno");
                String turmaAno = rs.getString("turma_ano");
                String disciplina = rs.getString("nome_disciplina");
                String professor = rs.getString("nome_professor");
                String observacao = rs.getString("observacao");

                observacoes.add(new ObservacaoViewDTO(
                        id,
                        nomeAluno,
                        turmaAno,
                        disciplina,
                        professor,
                        observacao
                ));
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }

        return observacoes;
    }
}
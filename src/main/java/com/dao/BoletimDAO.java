package com.dao;

import com.dto.BoletimViewDTO;
import com.model.Boletim;
import org.apache.commons.lang3.text.WordUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BoletimDAO extends DAO {

    public BoletimDAO() throws SQLException {
        super();
    }

    public void cadastrar(Boletim boletim) throws SQLException {
        Double nota1 = boletim.getNota1();
        Double nota2 = boletim.getNota2();
        UUID idAluno = boletim.getIdAluno();
        Integer idDisciplina = boletim.getIdDisciplina();

        String sql = """
                INSERT INTO
                    boletim (nota1, nota2, id_aluno, id_disciplina)
                VALUES
                    (?,?,?,?)
                """;

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);

            if (nota1 == null) {
                pstmt.setObject(1, null);
            } else {
                pstmt.setDouble(1, nota1);
            }

            if (nota2 == null) {
                pstmt.setObject(2, null);
            } else {
                pstmt.setDouble(2, nota2);
            }

            pstmt.setObject(3, idAluno);
            pstmt.setInt(4, idDisciplina);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    public Boletim pesquisarPorId(Integer id) throws SQLException {
        String sql = """
                SELECT
                    nota1,
                    nota2,
                    media,
                    id_aluno,
                    id_disciplina
                FROM
                    boletim
                WHERE
                    id = ?
                """;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Boletim boletim = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Double nota1 = rs.getBigDecimal("nota1") != null ? rs.getBigDecimal("nota1").doubleValue() : null;
                Double nota2 = rs.getBigDecimal("nota2") != null ? rs.getBigDecimal("nota2").doubleValue() : null;
                Double media = rs.getBigDecimal("media") != null ? rs.getBigDecimal("media").doubleValue() : null;
                UUID idAluno = rs.getObject("id_aluno", UUID.class);
                Integer idDisciplina = rs.getInt("id_disciplina");

                boletim = new Boletim(id, nota1, nota2, media, idAluno, idDisciplina);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }

        return boletim;
    }

    public List<BoletimViewDTO> listarPorAluno(UUID idAluno) throws SQLException {
        String sql = """
                SELECT
                    b.id as id_boletim,
                    a.matricula as matricula,
                    d.nome as nome_disciplina,
                    b.nota1 as nota1,
                    b.nota2 as nota2,
                    b.media as media
                FROM
                    boletim b
                JOIN
                    aluno a
                    ON a.id = b.id_aluno
                JOIN
                    disciplina d
                    ON d.id = b.id_disciplina
                WHERE
                    a.id = ?
                ORDER BY
                    d.nome
                """;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BoletimViewDTO> boletins = new ArrayList<>();

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, idAluno);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer idBoletim = rs.getInt("id_boletim");
                Integer matricula = rs.getInt("matricula");
                String nomeDisciplina = rs.getString("nome_disciplina");
                Double nota1 = rs.getBigDecimal("nota1") != null ? rs.getBigDecimal("nota1").doubleValue() : null;
                Double nota2 = rs.getBigDecimal("nota2") != null ? rs.getBigDecimal("nota2").doubleValue() : null;
                Double media = rs.getBigDecimal("media") != null ? rs.getBigDecimal("media").doubleValue() : null;

                String situacao;
                if (media == null) {
                    situacao = null;
                } else if (media >= 7) {
                    situacao = "Aprovado";
                } else {
                    situacao = "Reprovado";
                }

                boletins.add(new BoletimViewDTO(
                        idBoletim,
                        matricula,
                        nomeDisciplina,
                        nota1,
                        nota2,
                        media,
                        situacao
                ));
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }

        return boletins;
    }

    public List<BoletimViewDTO> listarPorAluno(UUID idAluno, String nomeDisciplinaFiltro, String situacaoFiltro) throws SQLException {
        String sql = """
                SELECT
                    b.id as id_boletim,
                    a.matricula as matricula,
                    d.nome as nome_disciplina,
                    b.nota1 as nota1,
                    b.nota2 as nota2,
                    b.media as media
                FROM
                    boletim b
                JOIN
                    aluno a
                    ON a.id = b.id_aluno
                JOIN
                    disciplina d
                    ON d.id = b.id_disciplina
                WHERE
                    a.id = ?
                AND
                    (? IS NULL OR LOWER(d.nome) LIKE LOWER(?))
                AND
                    (
                        ? IS NULL
                        OR (? = 'Aprovado' AND b.media >= 7)
                        OR (? = 'Reprovado' AND b.media < 7)
                    )
                ORDER BY
                    d.nome
                """;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BoletimViewDTO> boletins = new ArrayList<>();

        try {
            pstmt = conn.prepareStatement(sql);

            situacaoFiltro = WordUtils.capitalizeFully(situacaoFiltro);

            String disciplina = (nomeDisciplinaFiltro == null || nomeDisciplinaFiltro.isBlank()) ? null : nomeDisciplinaFiltro.trim();
            String situacao = (situacaoFiltro == null || situacaoFiltro.isBlank()) ? null : situacaoFiltro.trim();

            pstmt.setObject(1, idAluno);

            pstmt.setString(2, disciplina);
            pstmt.setString(3, disciplina == null ? null : "%" + disciplina + "%");

            pstmt.setString(4, situacao);
            pstmt.setString(5, situacao);
            pstmt.setString(6, situacao);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer idBoletim = rs.getInt("id_boletim");
                Integer matricula = rs.getInt("matricula");
                String nomeDisciplina = rs.getString("nome_disciplina");
                Double nota1 = rs.getBigDecimal("nota1") != null ? rs.getBigDecimal("nota1").doubleValue() : null;
                Double nota2 = rs.getBigDecimal("nota2") != null ? rs.getBigDecimal("nota2").doubleValue() : null;
                Double media = rs.getBigDecimal("media") != null ? rs.getBigDecimal("media").doubleValue() : null;

                String situacaoResultado;
                if (media == null) {
                    situacaoResultado = null;
                } else if (media >= 7) {
                    situacaoResultado = "Aprovado";
                } else {
                    situacaoResultado = "Reprovado";
                }

                boletins.add(new BoletimViewDTO(
                        idBoletim,
                        matricula,
                        nomeDisciplina,
                        nota1,
                        nota2,
                        media,
                        situacaoResultado
                ));
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }

        return boletins;
    }

    public void atualizar(Boletim original, Boletim atualizado) throws SQLException {
        Integer id = original.getId();
        Double nota1 = atualizado.getNota1();
        Double nota2 = atualizado.getNota2();

        if (Objects.equals(nota1, original.getNota1()) && Objects.equals(nota2, original.getNota2())) {
            return;
        }

        if (!Objects.equals(nota1, original.getNota1()) && !Objects.equals(nota2, original.getNota2())) {
            String sql = """
                    UPDATE boletim
                    SET nota1 = ?, nota2 = ?
                    WHERE id = ?
                    """;

            PreparedStatement pstmt = null;

            try {
                pstmt = conn.prepareStatement(sql);
                pstmt.setObject(1, nota1);
                pstmt.setObject(2, nota2);
                pstmt.setInt(3, id);
                pstmt.executeUpdate();
            } finally {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            }
            return;
        }

        if (!Objects.equals(nota1, original.getNota1())) {
            String sql = """
                    UPDATE boletim
                    SET nota1 = ?
                    WHERE id = ?
                    """;

            PreparedStatement pstmt = null;

            try {
                pstmt = conn.prepareStatement(sql);
                pstmt.setObject(1, nota1);
                pstmt.setInt(2, id);
                pstmt.executeUpdate();
            } finally {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            }
            return;
        }

        String sql = """
                UPDATE boletim
                SET nota2 = ?
                WHERE id = ?
                """;

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, nota2);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    public void deletar(Integer id) throws SQLException {
        String sql = """
                DELETE FROM
                    boletim
                WHERE
                    id = ?
                """;

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    public List<String> notasPendentes(UUID idProfessor) throws SQLException {
        String sql = """
                SELECT
                    d.nome as nome_disciplina,
                    p2.turma_ano as turma_ano
                FROM
                    disciplina d
                JOIN
                    professor p
                    ON p.id = d.id_professor
                JOIN
                    boletim b
                    ON b.id_disciplina = d.id
                JOIN
                    aluno a
                    ON a.id = b.id_aluno
                JOIN
                    pre_matricula p2
                    ON p2.matricula = a.matricula
                WHERE
                    p.id = ?
                AND
                    (b.nota1 is null or b.nota2 is null)
                """;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<String> notasPendentes = new ArrayList<>();

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, idProfessor);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String nomeDisciplina = rs.getString("nome_disciplina");
                String turmaAno = rs.getString("turma_ano");
                notasPendentes.add(String.format("%s - Turma: %s", nomeDisciplina, turmaAno));
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }

        return notasPendentes;
    }
}
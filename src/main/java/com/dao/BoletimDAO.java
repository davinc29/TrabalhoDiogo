package com.dao;

import com.dto.BoletimViewDTO;
import com.model.Boletim;
import com.utils.StringUtils;

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

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
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

            pstmt.execute();
            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
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

        Boletim boletim = null;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Double nota1 = rs.getDouble("nota1");
                Double nota2 = rs.getDouble("nota2");
                Double media = rs.getDouble("media");
                UUID idAluno = rs.getObject("id_aluno", UUID.class);
                Integer idDisciplina = rs.getInt("id_disciplina");

                boletim = new Boletim(id, nota1, nota2, media, idAluno, idDisciplina);
            }

            conn.commit();
            return boletim;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
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
                """;

        List<BoletimViewDTO> boletins = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, idAluno);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer idBoletim = rs.getInt("id_boletim");
                Integer matricula = rs.getInt("matricula");
                String nomeDisciplina = rs.getString("nome_disciplina");
                Double nota1 = rs.getDouble("nota1");
                Double nota2 = rs.getDouble("nota2");
                Double media = rs.getDouble("media");

                String situacao;

                if (rs.wasNull()) {
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

            conn.commit();
            return boletins;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public List<BoletimViewDTO> listarPorAluno(UUID idAluno, Integer idBoletimFiltro, Double nota1Filtro, Double nota2Filtro, Double mediaFiltro, String nomeDisciplinaFiltro) throws SQLException {

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
                    (CAST(? AS INTEGER) IS NULL OR b.id = CAST(? AS INTEGER))
                AND
                    (CAST(? AS DOUBLE PRECISION) IS NULL OR b.nota1 = CAST(? AS DOUBLE PRECISION))
                AND
                    (CAST(? AS DOUBLE PRECISION) IS NULL OR b.nota2 = CAST(? AS DOUBLE PRECISION))
                AND
                    (CAST(? AS DOUBLE PRECISION) IS NULL OR b.media = CAST(? AS DOUBLE PRECISION))
                AND
                    (CAST(? AS TEXT) IS NULL OR upper(d.nome) LIKE upper(CAST(? AS TEXT)))
                """;

        List<BoletimViewDTO> boletins = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, idAluno);

            pstmt.setObject(2, idBoletimFiltro);
            pstmt.setObject(3, idBoletimFiltro);

            pstmt.setObject(4, nota1Filtro);
            pstmt.setObject(5, nota1Filtro);

            pstmt.setObject(6, nota2Filtro);
            pstmt.setObject(7, nota2Filtro);

            pstmt.setObject(8, mediaFiltro);
            pstmt.setObject(9, mediaFiltro);

            pstmt.setString(10, nomeDisciplinaFiltro);
            pstmt.setString(11, nomeDisciplinaFiltro == null ? null : StringUtils.formatarLike(nomeDisciplinaFiltro.toUpperCase()));

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer idBoletim = rs.getInt("id_boletim");
                Integer matricula = rs.getInt("matricula");
                String nomeDisciplina = rs.getString("nome_disciplina");
                Double nota1 = rs.getDouble("nota1");
                Double nota2 = rs.getDouble("nota2");
                Double media = rs.getDouble("media");

                String situacao;

                if (rs.wasNull()) {
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

            conn.commit();
            return boletins;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
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

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setObject(1, nota1);
                pstmt.setObject(2, nota2);
                pstmt.setInt(3, id);
                pstmt.executeUpdate();
                conn.commit();
                return;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }

        if (!Objects.equals(nota1, original.getNota1())) {
            String sql = """
                    UPDATE boletim
                    SET nota1 = ?
                    WHERE id = ?
                    """;

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setObject(1, nota1);
                pstmt.setInt(2, id);
                pstmt.executeUpdate();
                conn.commit();
                return;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }

        String sql = """
                UPDATE boletim
                SET nota2 = ?
                WHERE id = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, nota2);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public void deletar(Integer id) throws SQLException {
        String sql = """
                DELETE FROM
                    boletim
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

        List<String> notasPendentes = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, idProfessor);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String nomeDisciplina = rs.getString("nome_disciplina");
                String turmaAno = rs.getString("turma_ano");

                notasPendentes.add(String.format("%s - Turma: %s", nomeDisciplina, turmaAno));
            }

            return notasPendentes;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }
}
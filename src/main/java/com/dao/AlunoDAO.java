package com.dao;

import com.dto.AlunoCadastrarDTO;
import com.dto.AlunoViewDTO;
import com.utils.SenhaUtils;
import com.utils.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AlunoDAO extends DAO {

    public AlunoDAO() throws SQLException {
        super();
    }

    public int cadastrarAluno(AlunoCadastrarDTO aluno) throws SQLException {
        String nome = aluno.getNome();
        Integer matricula = aluno.getMatricula();
        String email = aluno.getEmail();
        String senha = SenhaUtils.hashear(aluno.getSenha());

        String sql = """
            INSERT INTO
                aluno (nome, matricula, senha, email)
            VALUES
                (?,?,?,?)
            """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setInt(2, matricula);
            pstmt.setString(3, senha);
            pstmt.setString(4, email);

            int verificacao = pstmt.executeUpdate();

            if (verificacao > 0) {
                conn.commit();
                return 1;
            } else {
                conn.rollback();
                return 0;
            }

        } catch (SQLException e) {
            String state = e.getSQLState();

            if ("23503".equals(state)) {
                conn.rollback();
                return 2;
            } else if ("23505".equals(state)) {
                conn.rollback();
                return 3;
            } else {
                conn.rollback();
                return 0;
            }
        }
    }

    public boolean atualizarEmailAluno(UUID idAluno, String novoEmail) throws SQLException {
        String sql = """
                UPDATE
                    aluno
                SET
                    email = ?
                WHERE
                    id = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, novoEmail);
            pstmt.setObject(2, idAluno);

            int validar = pstmt.executeUpdate();

            if (validar > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }

        } catch (SQLException e) {
            conn.rollback();
            return false;
        }
    }

    public boolean atualizarNomeAluno(UUID idAluno, String novoNome) throws SQLException {
        String sql = """
                UPDATE
                    aluno
                SET
                    nome = ?
                WHERE
                    id = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, novoNome);
            pstmt.setObject(2, idAluno);

            int validar = pstmt.executeUpdate();

            if (validar > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }

        } catch (SQLException e) {
            conn.rollback();
            return false;
        }
    }

    public boolean atualizarSenhaAluno(UUID idAluno, String novaSenha) throws SQLException {
        String sql = """
                UPDATE
                    aluno
                SET
                    senha = ?
                WHERE
                    id = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String senhaHash = SenhaUtils.hashear(novaSenha);

            pstmt.setString(1, senhaHash);
            pstmt.setObject(2, idAluno);

            int validar = pstmt.executeUpdate();

            if (validar > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }

        } catch (SQLException e) {
            conn.rollback();
            return false;
        }
    }

    public void atualizarSenhaAlunoAluno(String email, String senhaAtual, String senhaNova) throws SQLException {
        String sql = """
                SELECT
                    senha
                FROM
                    aluno
                WHERE
                    email = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String senha = rs.getString("senha");

                if (SenhaUtils.comparar(senhaAtual, senha)) {
                    String update = "UPDATE aluno SET senha = ? WHERE email = ?";

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

    public List<AlunoViewDTO> listarAlunos(String nomeFiltro, Integer matriculaFiltro, String emailFiltro) throws SQLException {
        String sql = """
        SELECT
            a.id as id,
            a.nome as nome,
            a.matricula as matricula,
            a.email as email,
            p.turma_ano as turma_ano
        FROM
            aluno a
        JOIN
            pre_matricula p
            ON p.matricula = a.matricula
        WHERE
            (CAST(? AS TEXT) IS NULL OR upper(a.nome) LIKE ?)
        AND
            (CAST(? AS INTEGER) IS NULL OR a.matricula = ?)
        AND
            (CAST(? AS TEXT) IS NULL OR upper(a.email) LIKE ?)
        """;

        List<AlunoViewDTO> alunos = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, nomeFiltro);
            pstmt.setString(2, nomeFiltro == null ? null : StringUtils.formatarLike(nomeFiltro.toUpperCase()));

            pstmt.setObject(3, matriculaFiltro);
            pstmt.setObject(4, matriculaFiltro);

            pstmt.setObject(5, emailFiltro);
            pstmt.setString(6, emailFiltro == null ? null : StringUtils.formatarLike(emailFiltro.toUpperCase()));

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                UUID idAluno = rs.getObject("id", UUID.class);
                String nome = rs.getString("nome");
                Integer matricula = rs.getInt("matricula");
                String email = rs.getString("email");
                String turmaAno = rs.getString("turma_ano");

                alunos.add(new AlunoViewDTO(idAluno, nome, matricula, email, turmaAno));
            }

            conn.commit();
            return alunos;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public AlunoViewDTO listarAlunoPorId(UUID idAluno) throws SQLException {
        String sql = """
                SELECT
                    a.id as id,
                    a.nome as nome,
                    a.matricula as matricula,
                    a.email as email,
                    p.turma_ano as turma_ano
                FROM
                    aluno a
                JOIN
                    pre_matricula p
                    ON p.matricula = a.matricula
                WHERE
                    a.id = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, idAluno);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                UUID id = rs.getObject("id", UUID.class);
                String nome = rs.getString("nome");
                Integer matricula = rs.getInt("matricula");
                String email = rs.getString("email");
                String turmaAno = rs.getString("turma_ano");

                AlunoViewDTO aluno = new AlunoViewDTO(id, nome, matricula, email, turmaAno);
                conn.commit();
                return aluno;
            } else {
                conn.rollback();
                return null;
            }

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public boolean deletarAlunoPorId(UUID idAluno) throws SQLException {
        String sql = """
                DELETE FROM
                    aluno
                WHERE
                    id = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, idAluno);

            int validar = pstmt.executeUpdate();

            if (validar > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }

        } catch (SQLException e) {
            conn.rollback();
            return false;
        }
    }

    public List<AlunoViewDTO> listarAlunosPorTurmaAno(String turmaAno) throws SQLException {
        String sql = """
                SELECT
                    a.id as id,
                    a.nome as nome,
                    a.matricula as matricula,
                    a.email as email,
                    p.turma_ano as turma_ano
                FROM
                    aluno a
                JOIN
                    pre_matricula p
                    ON p.matricula = a.matricula
                WHERE
                    p.turma_ano = ?
                """;

        List<AlunoViewDTO> alunos = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, turmaAno);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                UUID idAluno = rs.getObject("id", UUID.class);
                String nome = rs.getString("nome");
                Integer matricula = rs.getInt("matricula");
                String email = rs.getString("email");
                String turmaAnoBanco = rs.getString("turma_ano");

                alunos.add(new AlunoViewDTO(idAluno, nome, matricula, email, turmaAnoBanco));
            }

            conn.commit();
            return alunos;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public List<AlunoViewDTO> listarAlunosPorParteNome(String parteNome) throws SQLException {
        String sql = """
                SELECT
                    a.id as id,
                    a.nome as nome,
                    a.matricula as matricula,
                    a.email as email,
                    p.turma_ano as turma_ano
                FROM
                    aluno a
                JOIN
                    pre_matricula p
                    ON p.matricula = a.matricula
                WHERE
                    a.nome ILIKE ?
                """;

        List<AlunoViewDTO> alunos = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + parteNome + "%");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                UUID idAluno = rs.getObject("id", UUID.class);
                String nome = rs.getString("nome");
                Integer matricula = rs.getInt("matricula");
                String email = rs.getString("email");
                String turmaAno = rs.getString("turma_ano");

                alunos.add(new AlunoViewDTO(idAluno, nome, matricula, email, turmaAno));
            }

            conn.commit();
            return alunos;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }
}
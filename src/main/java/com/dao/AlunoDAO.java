package com.dao;

import com.dto.AlunoCadastrarDTO;
import com.dto.AlunoViewDTO;
import com.utils.SenhaUtils;
import com.utils.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AlunoDAO extends DAO {

    public AlunoDAO() throws SQLException {
        super();
    }

    //Cadastrar aluno
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

            if ("23503".equals(state)) { // FK (ex: matrícula não existe na pré-matrícula)
                conn.rollback();
                return 2;

            } else if ("23505".equals(state)) { // unique
                conn.rollback();
                return 3;

            } else {
                conn.rollback();
                return 0;
            }
        }
    }

    //Atualizar email do aluno
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

    //Atualizar senha do aluno
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

            int validadr = pstmt.executeUpdate();

            if (validadr > 0) {
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

    public void atualizarSenhaAlunoAluno(String email, String senhaAtual, String senhaNova) throws SQLException{
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
                    String senhaHash = SenhaUtils.hashear(senhaNova);

                    try (PreparedStatement pstmt2 = conn.prepareStatement(update)) {
                        pstmt2.setString(1, senhaHash);
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

    //Listar Todos os Alunos
    public List<AlunoViewDTO> listarAlunos(String nomeFiltro, Integer matriculaFiltro, String turmaAnoFiltro) throws SQLException {
        StringBuilder sql = new StringBuilder("""
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
                    ON p.matricula = a.matricula WHERE 1=1
                """);

        List<Object> valores = new ArrayList<>();

        if (nomeFiltro != null) {
            sql.append("""
                       AND upper(a.nome) LIKE ?
                    """);
            valores.add(StringUtils.formatarLike(nomeFiltro.toUpperCase()));
        }
        if (matriculaFiltro != null) {
            sql.append("""
                    AND a.matricula = ?
                    """);
            valores.add(matriculaFiltro);
        }
        if (turmaAnoFiltro != null) {
            sql.append("""
                    AND p.turma_ano LIKE ?
                    """);
            valores.add(StringUtils.formatarLike(turmaAnoFiltro.toUpperCase()));
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < valores.size(); i++){
                pstmt.setObject(i+1, valores.get(i));
            }

            ResultSet rs = pstmt.executeQuery();

            List<AlunoViewDTO> alunos = new java.util.ArrayList<>();

            while (rs.next()) {
                UUID idAluno = rs.getObject("id", java.util.UUID.class);
                String nome = rs.getString("nome");
                Integer matricula = rs.getInt("matricula");
                String email = rs.getString("email");
                String turmaAno = rs.getString("turma_ano");

                AlunoViewDTO aluno = new AlunoViewDTO(idAluno, nome, matricula, email, turmaAno);
                alunos.add(aluno);





            }

            conn.commit();
            return alunos;

        } catch (SQLException e) {
            conn.rollback();

            //Erro ao listar os alunos
            throw new RuntimeException(e);
        }
    }

    //Listar 1 aluno por ID
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
                    id = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, idAluno);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                UUID id = rs.getObject("id", java.util.UUID.class);
                String nome = rs.getString("nome");
                Integer matricula = rs.getInt("matricula");
                String email = rs.getString("email");
                String turmaAno = rs.getString("turma_ano");

                AlunoViewDTO aluno = new AlunoViewDTO(id, nome, matricula, email, turmaAno);
                conn.commit();
                return aluno;
            } else {
                conn.rollback();
                return null; // Retorna null se o aluno não for encontrado
            }

        } catch (SQLException e) {
            conn.rollback();
            throw new RuntimeException(e);
        }
    }

    //Deletar aluno por ID
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

    //Listar alunos por turma_ano
    public List<AlunoViewDTO> listarAlunosPorTurmaAno(String turmaAno) throws SQLException {
        String sql = """
                SELECT
                    a.id as id,
                    a.nome as nome,
                    a.matricula as matricula,
                    a.email as email,
                    p.turma_ano as turma_ano
                FROM
                    aluno
                JOIN
                    pre_matricula p
                    ON p.matricula = a.matricula
                WHERE
                    p.turma_ano = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, turmaAno);

            ResultSet rs = pstmt.executeQuery();

            List<AlunoViewDTO> alunos = new java.util.ArrayList<>();

            while (rs.next()) {
                UUID idAluno = rs.getObject("id", java.util.UUID.class);
                String nome = rs.getString("nome");
                Integer matricula = rs.getInt("matricula");
                String email = rs.getString("email");
                String turmaAnoBanco = rs.getString("turma_ano");

                AlunoViewDTO aluno = new AlunoViewDTO(idAluno, nome, matricula, email, turmaAnoBanco);
                alunos.add(aluno);
            }

            conn.commit();
            return alunos;

        } catch (SQLException e) {
            conn.rollback();
            throw new RuntimeException(e);
        }
    }

    //Listar por parte do nome
    public List<AlunoViewDTO> listarAlunosPorParteNome(String parteNome) throws SQLException {
        String sql = """
                SELECT
                    a.id as id,
                    a.nome as nome,
                    a.matricula as matricula,
                    a.email as email,
                    p.turma_ano as turma_ano
                FROM
                    aluno
                JOIN
                    pre_matricula p
                    ON p.matricula = a.matricula
                WHERE
                    nome ILIKE ?
            """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + parteNome + "%");

            ResultSet rs = pstmt.executeQuery();

            List<AlunoViewDTO> alunos = new java.util.ArrayList<>();

            while (rs.next()) {
                UUID idAluno = rs.getObject("id", java.util.UUID.class);
                String nome = rs.getString("nome");
                Integer matricula = rs.getInt("matricula");
                String email = rs.getString("email");
                String turmaAno = rs.getString("turma_ano");

                AlunoViewDTO aluno = new AlunoViewDTO(idAluno, nome, matricula, email, turmaAno);
                alunos.add(aluno);
            }

            conn.commit();
            return alunos;

        } catch (SQLException e) {
            conn.rollback();
            throw new RuntimeException(e);
        }
    }
}

package com.dao;

import com.dto.AlunoCadastrarDTO;
import com.dto.AlunoViewDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class AlunoDAO extends DAO {

    public AlunoDAO() throws Exception {
        super();
    }


    //Cadastrar aluno
    public int cadastrarAluno() throws SQLException {

        AlunoCadastrarDTO aluno = new AlunoCadastrarDTO();

        String nome = aluno.getNome();
        String matricula = String.valueOf(aluno.getMatricula());
        String email = aluno.getEmail();
        String senha = aluno.getSenha();

        String turmaAno = String.valueOf(java.time.Year.now());


        String sql = """
                INSERT INTO
                    aluno (nome, matricula, senha, email, turma_ano)
                VALUES
                    (?,?,?,?,?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, matricula);
            pstmt.setString(3, senha);
            pstmt.setString(4, email);
            pstmt.setString(5, turmaAno);


            int verificacao = pstmt.executeUpdate();

            if (verificacao > 0) {
                conn.commit();
                // Se a inserção foi bem-sucedida, commit e retorna 1

                return 1;
            } else {
                conn.rollback();
                // Se a inserção falhou, rollback e retorna 0

                return 0;
            }

        } catch (SQLException e) {

            String state = e.getSQLState();

            if ("23503".equals(state)) {
                conn.rollback();
                // Erro 2: Matricula não existe na Pré-matricula

                return 2;

            } else if ("23505".equals(state)) {
                conn.rollback();
                // Erro 3: Matricula já existe na tabela Aluno

                return 3;

            } else {
                conn.rollback();
                // Para outros erros, apenas retorna 0

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
            pstmt.setString(1, novaSenha);
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

    //Listar Todos os Alunos
    public List<AlunoViewDTO> listarAlunos() throws SQLException {
        String sql = """
                SELECT
                    id, nome, matricula, email, turma_ano
                FROM
                    aluno
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            List<AlunoViewDTO> alunos = new java.util.ArrayList<>();

            while (rs.next()) {
                UUID idAluno = rs.getObject("id", java.util.UUID.class);
                String nome = rs.getString("nome");
                Long matricula = rs.getLong("matricula");
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
                    id, nome, matricula, email, turma_ano
                FROM
                    aluno
                WHERE
                    id = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, idAluno);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                UUID id = rs.getObject("id", java.util.UUID.class);
                String nome = rs.getString("nome");
                Long matricula = rs.getLong("matricula");
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
                    id, nome, matricula, email, turma_ano
                FROM
                    aluno
                WHERE
                    turma_ano = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, turmaAno);

            ResultSet rs = pstmt.executeQuery();

            List<AlunoViewDTO> alunos = new java.util.ArrayList<>();

            while (rs.next()) {
                UUID idAluno = rs.getObject("id", java.util.UUID.class);
                String nome = rs.getString("nome");
                Long matricula = rs.getLong("matricula");
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
                id, nome, matricula, email, turma_ano
            FROM
                aluno
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
                Long matricula = rs.getLong("matricula");
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

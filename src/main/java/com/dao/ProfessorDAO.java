package com.dao;

import com.dto.ProfessorDTO;
import com.model.Professor;
import com.utils.SenhaUtils;
import com.utils.StringUtils;

import java.sql.*;
import java.util.*;

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
                    (?,?,?,?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,nome);
            pstmt.setString(2,usuario);
            pstmt.setString(3,email);
            pstmt.setString(4,senhaHash);

            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            System.out.println("Erro no cadastro do professor!");
            conn.rollback();
            throw e;
        }
    }

    public void atualizar(ProfessorDTO original, ProfessorDTO atualizado) throws SQLException{
        UUID id = original.getId();
        String nome = atualizado.getNome();
        String usuario = atualizado.getUsername();
        String email = atualizado.getEmail();

        StringBuilder sql = new StringBuilder("UPDATE professor SET ");
        List<Object> valores = new ArrayList<>();

        if (!Objects.equals(nome, original.getNome())) {
            sql.append("nome = ?, ");
            valores.add(nome);
        }
        if (!Objects.equals(usuario, original.getUsername())) {
            sql.append("username = ?, ");
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
                pstmt.setObject(i + 1, valores.get(i));
            }

            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public List<ProfessorDTO> listar(String nomeFiltro, String usernameFiltro, String emailFiltro) throws SQLException {
        StringBuilder sql = new StringBuilder("""
                SELECT
                    id, nome, username, email
                FROM
                    professor
                WHERE
                    1=1
                """);

        List<ProfessorDTO> professores = new ArrayList<>();
        List<Object> valores = new ArrayList<>();

        if (nomeFiltro != null) {
            sql.append("""
                       AND upper(nome) LIKE ?
                    """);
            valores.add(StringUtils.formatarLike(nomeFiltro.toUpperCase()));
        }
        if (usernameFiltro != null) {
            sql.append("""
                    AND upper(username) LIKE ?
                    """);
            valores.add(StringUtils.formatarLike(usernameFiltro.toUpperCase()));
        }
        if (emailFiltro != null) {
            sql.append("""
                    AND upper(email) LIKE ?
                    """);
            valores.add(StringUtils.formatarLike(emailFiltro.toUpperCase()));
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < valores.size(); i++) {
                pstmt.setObject(i+1, valores.get(i));
            }

            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    UUID id = rs.getObject("id", UUID.class);
                    String nome = rs.getString("nome");
                    String username = rs.getString("username");
                    String email = rs.getString("email");

                    ProfessorDTO professor = new ProfessorDTO(id, nome, username, email);
                    professores.add(professor);
                }

                conn.commit();
            }
        }

        conn.rollback();
        return professores;
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

    public ProfessorDTO pesquisarPorId(UUID id) throws SQLException{
        String sql = """
                SELECT
                    nome, username, email
                FROM
                    professor
                WHERE
                    id = ?
                """;

        ProfessorDTO professor = null;

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1,id);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String nome = rs.getString("nome");
                String username = rs.getString("username");
                String email = rs.getString("email");

                professor = new ProfessorDTO(id, nome, username, email);
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }

        conn.commit();
        return professor;
    }

    public ProfessorDTO pesquisarPorEmail(String email) throws SQLException{
        String sql = """
                SELECT
                    id, nome, username
                FROM
                    professor
                WHERE
                    email = ?
                """;

        ProfessorDTO professor = null;

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String nome = rs.getString("nome");
                String username = rs.getString("username");
                UUID id = rs.getObject("id", UUID.class);

                professor = new ProfessorDTO(id, nome, username, email);
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }

        conn.commit();
        return professor;
    }

    public Map<String, UUID> mapNomeId () throws SQLException {
        String sql = """
                SELECT
                    id,
                    nome
                FROM
                    professor
                """;

        Map<String, UUID> mapNomeId = new HashMap<>();

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()) {
                String nome = rs.getString("nome");
                UUID id = rs.getObject("id", UUID.class);

                mapNomeId.put(nome, id);
            }
        }

        return mapNomeId;
    }

    public void recuperarSenhaProfessor(String email, String novaSenha)
            throws SQLException {
        String sql = "UPDATE professor SET senha = ? WHERE email = ?";

        String senhaHash = SenhaUtils.hashear(novaSenha);

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try {
                pstmt.setString(1, senhaHash);
                pstmt.setString(2, email);
                pstmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public void atualizarSenhaProfessor(String email, String senhaAtual, String senhaNova) throws SQLException{
        String sql = """
                SELECT
                    senha
                FROM
                    professor
                WHERE
                    email = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String senha = rs.getString("senha");

                if (SenhaUtils.comparar(senhaAtual, senha)) {
                    String update = "UPDATE professor SET senha = ? WHERE email = ?";
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
}

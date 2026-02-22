package com.dao;

import com.dto.ProfessorDTO;
import com.model.Observacao;
import com.model.Professor;
import com.utils.SenhaUtils;

import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public List<ProfessorDTO> listar() throws SQLException {
        String sql = """
                SELECT
                    * except(senha)
                FROM
                    professor
                """;

        List<ProfessorDTO> professores = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
                    * except (id, senha)
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
}

package com.davinc.sistemaodettediogo.model.aluno;

import com.davinc.sistemaodettediogo.dao.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class AlunoDAO {

    public boolean CadastrarAluno(Aluno aluno) {
        Conexao conexao = new Conexao();
        Connection conn = conexao.conectar();

        try {
            String sql = "INSERT INTO aluno(nome_completo, matricula, email, senha) VALUES (?, ?, ?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, aluno.getNomeCompleto());
            if (aluno.getMatricula() != null) {
                pstmt.setLong(2, aluno.getMatricula());
            } else {
                pstmt.setNull(2, Types.VARCHAR);
            }
            pstmt.setString(3, aluno.getEmail());
            pstmt.setString(4, aluno.getSenha());

            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            conexao.desconectar(conn);
        }
    }
}

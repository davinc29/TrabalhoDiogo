package com.dao;

import com.dto.AdminDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDAO extends DAO {

    public AdminDAO() throws Exception {
        super();
    }

    //Cadastrar Admins
    public boolean cadastrarAdmin() {
        AdminDTO admin = new AdminDTO();

        String email = admin.getEmail();
        String senha = admin.getSenha();

        String sql = "INSERT INTO admin (email, senha) VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, senha);

            int verificacao = pstmt.executeUpdate();

            if (verificacao > 0) {
                conn.commit();

                // Se a inserção foi bem-sucedida, commit e retorna true
                return true;

            } else {
                conn.rollback();

                // Se a inserção falhou, rollback e retorna false
                return false;
            }

        } catch (Exception e) {
            //Erro: faz rollback e retorna false
            return false;
        }

    }

    public boolean logarAdmin(AdminDTO admin) throws Exception {
        String sql = "SELECT * FROM admin WHERE email = ? AND senha = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, admin.getEmail());
            pstmt.setString(2, admin.getSenha());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return true; // Login bem-sucedido
            } else {
                return false; // Login falhou
            }
        } catch (Exception e) {
            throw e; // Exeção lançada
        }

    }



}

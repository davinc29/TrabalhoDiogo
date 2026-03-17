package com.dao;

import com.dto.AdminDTO;
import com.utils.SenhaUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDAO extends DAO {

    public AdminDAO() throws Exception {
        super();
    }

    public boolean logarAdmin(AdminDTO adminDTO) throws Exception {

        String sql = """
        SELECT
            senha
        FROM
            admin
        WHERE
            email = ?
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, adminDTO.getEmail());

            try (ResultSet rs = pstmt.executeQuery()) {

                if (!rs.next()) {
                    conn.rollback();
                    return false;
                }

                String senhaHash = rs.getString("senha");

                boolean ok = SenhaUtils.comparar(adminDTO.getSenha(), senhaHash);

                if (ok) {
                    conn.commit();
                    return true;
                }

                conn.rollback();
                return false;
            }
        }
    }



}

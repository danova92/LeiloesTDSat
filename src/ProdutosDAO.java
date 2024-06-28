
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutosDAO {

    Connection conn;
    PreparedStatement st;
    ResultSet rs;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();

    public int cadastrarProduto(ProdutosDTO produto) {

        conn = new conectaDAO().connectDB();
        if (conn == null) {
            return -1;
        }

        int status;

        try {

            st = conn.prepareStatement("INSERT INTO produtos (nome, valor, status) VALUES (?,?,?)");
            st.setString(1, produto.getNome());
            st.setInt(2, produto.getValor());
            st.setString(3, produto.getStatus());
            status = st.executeUpdate();
            return status;
        } catch (SQLException ex) {
            return ex.getErrorCode();
        }
    }

    public ArrayList<ProdutosDTO> listarProdutos(String filtro) {
        conn = new conectaDAO().connectDB();
        try {
            String sqlFiltro = "SELECT * FROM produtos";

            if (!filtro.isEmpty()) {
                sqlFiltro += " WHERE nome LIKE ?";
            }

            st = conn.prepareStatement(sqlFiltro);

            if (!filtro.isEmpty()) {
                st.setString(1, "%" + filtro + "%");
            }

            rs = st.executeQuery();

            while (rs.next()) {
                ProdutosDTO produtoEncontrado = new ProdutosDTO();
                produtoEncontrado.setId(rs.getInt("id"));
                produtoEncontrado.setNome(rs.getString("nome"));
                produtoEncontrado.setValor(rs.getInt("valor"));
                produtoEncontrado.setStatus(rs.getString("status"));

                listagem.add(produtoEncontrado);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return listagem;
    }

    public void desconectar() {
        try {
            this.conn.close();
        } catch (SQLException ex) {
            System.out.print("erro ao desconectar " + ex.getMessage());
        }
    }

}

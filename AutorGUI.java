package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




/**
 * A classe AutorGUI representa uma interface gráfica de usuário para registrar
 * informações do autor.
 * Ela estende a classe JFrame e fornece um formulário para os usuários
 * inserirem detalhes do livro, como código do livro,
 * nome do livro, email e categoria. Também exibe uma tabela para mostrar os
 * dados registrados.
 * 
 * by. Alexandre Mello
 *
 */
public class AutorGUI extends JFrame {
    private JTextField bookCodeField;
    private JTextField bookNameField;
    private JTextField bookDataField;
    private JTextField horaField;
    private String category;
    private String data;
    private String hora;
    private JComboBox<String> categoryDropdown;
    private JButton submitButton;
    private JButton clearButton;
    private JTable table;
    private DefaultTableModel tableModel;
    

    /**
     * Construtor para a classe AutorGUI. Inicializa a interface gráfica de usuário.
     */
    public AutorGUI() {
        setLayout(null);

        JLabel bookCodeLabel = new JLabel("Nome: ");
        bookCodeLabel.setBounds(200, 20, 100, 25);
        add(bookCodeLabel);

        bookCodeField = new JTextField();
        bookCodeField.setBounds(200, 50, 200, 25);
        add(bookCodeField);

        JLabel bookNameLabel = new JLabel("Telefone: ");
        bookNameLabel.setBounds(200, 80, 100, 25);
        add(bookNameLabel);

        bookNameField = new JTextField();
        bookNameField.setBounds(200, 110, 200, 25);
        add(bookNameField);


        JLabel dataLabel = new JLabel("Data: ");
        dataLabel.setBounds(200, 140, 100, 25);
        add(dataLabel);

        bookDataField = new JTextField();
        bookDataField.setBounds(200, 170, 200, 25);
        add(bookDataField);


        JLabel horaLabel = new JLabel("Hora: ");
        horaLabel.setBounds(200, 200, 100, 25);
        add(horaLabel);

        horaField = new JTextField();
        horaField.setBounds(200, 230, 200, 25);
        add(horaField);

        JLabel categoryLabel = new JLabel("Tipo de Serviço: ");
        categoryLabel.setBounds(200, 260, 100, 25);
        add(categoryLabel);

        String[] categories = { "Escova", "Mechas", "Unha de fibra", "Pé + Mão simples","Pé", "Mão", "Unha de Gel", "Spa dos pés", "Sobrancelha", "Coloração", "Hidratação", };
        categoryDropdown = new JComboBox<>(categories);
        categoryDropdown.setBounds(200, 290, 200, 25);
        add(categoryDropdown);

        submitButton = new JButton("Enviar");
        submitButton.setBounds(200, 330, 100, 25);
        add(submitButton);

        clearButton = new JButton("Deletar");
        clearButton.setBounds(330, 330, 100, 25);
        add(clearButton);

        String[] columnNames = { "Nome", "Telefone", "Data", "Hora",  "Serviço" };
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);

        
        sorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(2, SortOrder.ASCENDING)));

        
        table.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(15, 400, 560, 200);
        add(scrollPane);

        clearButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                String nome = (String) model.getValueAt(selectedRow, 0); // assume que a coluna 0 é a chave primária
                model.removeRow(selectedRow);
        
                // conexão com o banco de dados
                String url = "jdbc:postgresql://localhost:5432/aplicacao";
                String user = "postgres";
                String password = "X@nde17930";
        
                try {
                    Connection conn = DriverManager.getConnection(url, user, password);
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM login WHERE nome = ?");
                    stmt.setString(1, nome);
                    stmt.executeUpdate();
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        bookCodeField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (Character.isDigit(c)) {
                    e.consume(); // Ignora a tecla se for um dígito
                
                    // Exibe uma mensagem de aviso
                    JOptionPane.showMessageDialog(null, "Por favor, digite apenas letras no campo Código.");
                }
                
            }
            @Override
            public void keyPressed(KeyEvent e) {
                // Não é necessário para o seu caso, pode deixar vazio
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Não é necessário para o seu caso, pode deixar vazio
            }
        });

        submitButton.addActionListener(e -> {
            String bookCode = bookCodeField.getText();
            String bookName = bookNameField.getText();
            category = (String) categoryDropdown.getSelectedItem();
            String data = bookDataField.getText();
            String hora = horaField.getText();

            if (bookCode.isEmpty() || bookName.isEmpty() || hora.isEmpty() || data.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos.");
            } else if (!isNumeroValido(bookName)) {
                JOptionPane.showMessageDialog(null, "Por favor, digite um número válido.");
                return;
            } else {
                if (!isValidHora(hora)) {
                    JOptionPane.showMessageDialog(null, "Por favor, digite uma hora válida.");
                } else {
                    if (!isValidData(data)) {
                        JOptionPane.showMessageDialog(null, "Por favor, digite uma data válida.");
                        return;
                    } else
                JOptionPane.showMessageDialog(null, "Dados cadastrados com sucesso!");
                Object[] rowData = { bookCode, bookName, data, hora, category};
                setBookCodeField(bookCodeField);
                setBookNameField(bookNameField);
                setCategory(category);
                setData(data);
                setHora(hora);
                carregarDadosDaTabela();
                try {
                    cadastroUsuario();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                tableModel.addRow(rowData);
            }
        }});


        setTitle("Cadastro dos serviços");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public Connection getConnection() throws SQLException {
        try {
            // Load the PostgreSQL JDBC driver
            System.out.println("Carregando driver.");
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver carregado com sucesso.");
        } catch (ClassNotFoundException e) {
            throw new SQLException(e);
        }
    
        Connection conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432/aplicacao", "postgres","X@nde17930");
        return conexao;
    }
    
    public void cadastroUsuario() throws SQLException {
        Connection conexao = getConnection();
    
        // Create a PreparedStatement
        System.out.println("prepare statement.");
        PreparedStatement stmt = conexao.prepareStatement("INSERT INTO login (nome, telefone, data, hora, servico) VALUES (?,?,?,?,?)");
    
        // Set the parameters
        stmt.setString(1, getBookCodeField().getText());
        stmt.setString(2, getBookNameField().getText());
        stmt.setString(3, getData());
        stmt.setString(4, getHora());
        stmt.setString(5, getCategory());
    
        // Execute the query
        stmt.executeUpdate();
    
        // Close the resources
        stmt.close();
        conexao.close();
        System.out.println("RODOU");
    }

    /**
     * pode usar pro telefone depois
     * Verifica se o email é válido.
     * 
     * @param email O email a ser verificado.
    */


    private boolean isValidHora(String hora) {
        String horaRegex = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";
        return hora.matches(horaRegex);
    }
    
    private boolean isValidData(String data) {
        String dataRegex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])$";
        return data.matches(dataRegex);
    }
    

    /**
     * Método principal. Cria uma instância da classe AutorGUI.
     * 
     * @param args Argumentos de linha de comando
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AutorGUI();
        });
    }

    public void updateTableData(Object tableData) {
    }


private boolean isNumeroValido(String bookName) {
    // Regular expression pattern for number validation
    String regex = "^[0-9]{11}$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(bookName);
    return matcher.matches();
    }
    

    public JTextField getBookCodeField() {
        return bookCodeField;
    }

    public void setBookCodeField(JTextField bookCodeField) {
        this.bookCodeField = bookCodeField;
    }

    public JTextField getBookNameField() {
        return bookNameField;
    }

    public void setBookNameField(JTextField bookNameField) {
        this.bookNameField = bookNameField;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



public JTextField getBookDataField() {
    return bookDataField;
}

public void setBookDataField(JTextField bookDataField) {
    this.bookDataField = bookDataField;
}

public JTextField getHoraField() {
    return horaField;
}

public void setHoraField(JTextField horaField) {
    this.horaField = horaField;
}

public JComboBox<String> getCategoryDropdown() {
    return categoryDropdown;
}

public void setCategoryDropdown(JComboBox<String> categoryDropdown) {
    this.categoryDropdown = categoryDropdown;
}

public JButton getSubmitButton() {
    return submitButton;
}

public void setSubmitButton(JButton submitButton) {
    this.submitButton = submitButton;
}

public JButton getClearButton() {
    return clearButton;
}

public void setClearButton(JButton clearButton) {
    this.clearButton = clearButton;
}

public void carregarDadosDaTabela() {
    tableModel.setRowCount(0);
    try {
        Connection conexao = getConnection();
        Statement stmt = conexao.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM login");
        System.out.println();
        while (resultSet.next()) {
            String nome = resultSet.getString("nome");
            String telefone = resultSet.getString("telefone");
            String data = resultSet.getString("data");
            String hora = resultSet.getString("hora");
            String servico = resultSet.getString("servico");

            Object[] rowData = { nome, telefone, data, hora, servico };
            tableModel.addRow(rowData);
        }

        conexao.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}







}
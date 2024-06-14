import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import View.AutorGUI;
import View.LoginGUI;


/**
 * A classe Controller é responsável por coordenar as interações entre as classes Model, LoginGUI e AutorGUI.
 * Ela define os listeners de ação para os eventos de usuário e atualiza a interface do usuário com base nos dados do modelo.
 * 
 * 
 * 
 * by. Alexandre Mello
 * 
 * 
 * 
 /* */
public class Controller {
    private Model model;
    private LoginGUI loginGUI;
    private AutorGUI autorGUI;
    private JTextField bookCodeField;
    private JTextField bookNameField;
    private JTextField bookDataField;
    private JTextField emailField;

    /**
     * Construtor para a classe Controller.
     * @param model A instância do modelo de dados.
     * @param loginGUI A instância da interface gráfica de usuário de login.
     * @param autorGUI A instância da interface gráfica de usuário do autor.
     */
    public Controller(Model model, LoginGUI loginGUI, AutorGUI autorGUI) {
        this.model = model;
        this.loginGUI = loginGUI;
        this.autorGUI = autorGUI;

        autorGUI.updateTableData(model.getTableData());

        loginGUI.setLoginActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String username = loginGUI.getName();
                String password = loginGUI.getPassword();

                if (username.equals("user") && password.equals("12345")) {
                    loginGUI.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Login feito com sucesso!");
                    loginGUI.openRegistrationScreen();
                } else {
                    loginGUI.displayErrorMessage("Login ou senha errados. Tente novamente.");
                }
            }
        });

    }

}
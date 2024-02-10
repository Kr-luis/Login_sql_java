import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class Registro extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField passwordUsuario;
    private JPanel Registro;
    private JButton verificarConexionButton;

    public Registro() {
        super("Registro_yaya");
        setContentPane(Registro);
        verificarConexionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validaciones();
            }
        });
    }


    public void validaciones() {
        // Obtener los datos del formulario
        String usuario = txtUsuario.getText();
        String contraseña = new String(passwordUsuario.getPassword());

        // Verificar la autenticación
        if (autenticarUsuario(usuario, contraseña)) {
            // Si la autenticación es exitosa, permitir el acceso
            JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso.");
            Login login = new Login();  //pasamos al siguiente formulario
            login.saltar();
            dispose();
        } else {
            // Si la autenticación falla, mostrar un mensaje de error
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos. Inténtalo de nuevo.");
            txtUsuario.setText("");
            passwordUsuario.setText("");
        }
    }

    public boolean autenticarUsuario(String nombre, String contraseña) {


                    // Crear una instancia de la clase ManejadorBaseDeDatos
                    ManejadorBaseDeDatos manejadorBD = new ManejadorBaseDeDatos();

                    // Llamar al método conexionBase() para obtener la conexión
                    Connection conexion = manejadorBD.conexionBase();


        if (conexion != null) {
            try {
                // Prepa'rar la consulta SQL para verificar la autenticación
                String sql = "SELECT * FROM usuarios WHERE nombre = ? AND contraseña = ?";
                try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                    // Establecer los valores de los parámetros en la consulta
                    pstmt.setString(1, nombre);
                    pstmt.setString(2, contraseña);

                    // Ejecutar la consulta para obtener el resultado
                                        System.out.println("Consulta SQL: " + pstmt.toString()); // Imprimir la consulta SQL
                    ResultSet resultSet = pstmt.executeQuery();
                    return resultSet.next();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Imprimir el seguimiento de la pila para diagnóstico
                JOptionPane.showMessageDialog(null, "Error al ejecutar la consulta: " + e.getMessage());
            } finally {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;


    }
}



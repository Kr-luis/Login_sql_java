import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txt_Pass;
    private JComboBox Cb_rol;
    private JPanel JPanel1;
    private JButton insertButton;
    private JTextField txtcodigo;
    private JTextField txtcedula;
    private JButton mostrarUsuariosButton;

    public Login() {
        super("LOGUEO");
        setContentPane(JPanel1);
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ingreso_formulario();
            }
        });
        mostrarUsuariosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarUsuarios();
            }
        });
    }

    public void Ingreso_formulario() {
        // Obtener los datos del formulario
        String usuario = txtUsuario.getText();
        String contraseña = new String(txt_Pass.getPassword());
        String rol = (String) Cb_rol.getSelectedItem(); // Obtener el rol seleccionado
        String codigo = txtcodigo.getText(); // Obtener el código
        String cedula = txtcedula.getText(); // Obtener la cédula

        // Llamar a insertarDatos() con los datos obtenidos del formulario
        insertarDatos(usuario, contraseña, rol, codigo, cedula);
    }

    // Método para insertar datos en la base de datos
    public void insertarDatos(String nombre, String contraseña, String rol, String codigo, String cedula) {
                            // Crear una instancia de la clase ManejadorBaseDeDatos
                            ManejadorBaseDeDatos manejadorBD = new ManejadorBaseDeDatos();

                            // Llamar al método conexionBase() para obtener la conexión
                            Connection conexion = manejadorBD.conexionBase();

        // Ahora puedes usar la conexión en tu clase

        if (conexion != null) {
            // La conexión se ha establecido correctamente
            try {
                // Preparar la consulta SQL para insertar los datos en la tabla
                String sql = "INSERT INTO usuarios (nombre, contraseña, rol, codigo, cedula) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                    // Establecer los valores de los parámetros en la consulta
                    pstmt.setString(1, nombre);
                    pstmt.setString(2, contraseña);
                    pstmt.setString(3, rol);
                    pstmt.setString(4, codigo); // Agregar el código como parámetro
                    pstmt.setString(5, cedula); // Agregar la cédula como parámetro
                    // Ejecutar la consulta para insertar los datos
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Datos insertados correctamente.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al insertar datos en la base de datos: " + e.getMessage());
            } finally {
                try {
                    // Cerrar la conexión
                    conexion.close();
                } catch (SQLException e) {
                    // Manejar errores al cerrar la conexión
                    e.printStackTrace();
                }
            }
        }
    }


    public void mostrarUsuarios() {
        // Crear una instancia de la clase ManejadorBaseDeDatos
        ManejadorBaseDeDatos manejadorBD = new ManejadorBaseDeDatos();

        // Obtener la conexión a la base de datos
        try (Connection conexion = manejadorBD.conexionBase()) {
            // Verificar si la conexión es nula
            if (conexion == null) {
                JOptionPane.showMessageDialog(null, "No se pudo establecer la conexión a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Salir del método si no se pudo establecer la conexión
            }

            // Preparar la consulta SQL para obtener todos los usuarios
            String sql = "SELECT nombre, rol FROM usuarios";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                // Ejecutar la consulta para obtener el resultado
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    // StringBuilder para almacenar los usuarios
                    StringBuilder usuarios = new StringBuilder();

                    // Iterar a través de los resultados y mostrarlos en la consola
                    while (resultSet.next()) {
                        String nombre = resultSet.getString("nombre");
                        String rol = resultSet.getString("rol");
                        usuarios.append("Nombre: ").append(nombre).append(", Rol: ").append(rol).append("\n");
                    }

                    // Mostrar los usuarios en un JOptionPane
                    JOptionPane.showMessageDialog(null, usuarios.toString(), "Usuarios", JOptionPane.PLAIN_MESSAGE);
                }
            }
        } catch (SQLException e) {
            // Capturar cualquier excepción SQL
            JOptionPane.showMessageDialog(null, "Error al recuperar usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Imprimir la traza de la pila para diagnóstico
        }
    }

    // Método para establecer a otro formulario

    public void saltar(){
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
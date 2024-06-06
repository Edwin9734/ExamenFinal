package org.example.Vista;
import org.example.Modelo.Contacto;
import org.example.Servicio.Agenda;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Main {
    private static Agenda agenda = new Agenda();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Agenda de Contactos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setSize(600, 400);
        frame.setSize (900, 800);

        // Crear panel principal con fondo azul marino
        JPanel panel = new JPanel();
//        panel.setBackground(new Color(0, 0, 128));
//        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(255, 10, 80));
        panel.setLayout(new BorderLayout());



        // Área de texto para mostrar contactos
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
      //  textArea.setBackground(new Color(0, 0, 64));
        textArea.setBackground(new Color(255, 40, 64));
        textArea.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
       // buttonPanel.setBackground(new Color(0, 0, 128));
        buttonPanel.setBackground(new Color(195, 0, 128));
        buttonPanel.setLayout(new GridLayout(7, 1, 10, 10)); // Cambiado a 7 filas para añadir el nuevo botón

        // Botón para agregar contacto
        JButton addButton = new JButton("NUEVO CONTACTO");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = JOptionPane.showInputDialog(frame, "Nombre:");
                if (nombre == null || nombre.trim().isEmpty()) {
                    return;
                }
                long telefono;
                try {
                    telefono = Long.parseLong(JOptionPane.showInputDialog(frame, "Teléfono:"));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Número de teléfono inválido.");
                    return;
                }
                String correo = JOptionPane.showInputDialog(frame, "Correo Electrónico:");
                if (correo == null || correo.trim().isEmpty()) {
                    return;
                }
                LocalDate fechaNacimiento;
                try {
                    String fecha = JOptionPane.showInputDialog(frame, "Fecha de Nacimiento (AAAA-MM-DD):");
                    if (fecha == null || fecha.trim().isEmpty()) {
                        return;
                    }
                    fechaNacimiento = LocalDate.parse(fecha, DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(frame, "Formato de fecha inválido.");
                    return;
                }
                agenda.agregarContacto(nombre, telefono, correo, fechaNacimiento);
                textArea.append("Contacto agregado: " + nombre + "\n");
            }
        });

        // Botón para eliminar contacto
        JButton deleteButton = new JButton("ELIMINAR");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = JOptionPane.showInputDialog(frame, "Nombre del contacto a eliminar:");
                if (nombre == null || nombre.trim().isEmpty()) {
                    return;
                }
                agenda.eliminarContacto(nombre);
                textArea.append("Contacto eliminado: " + nombre + "\n");
            }
        });

        // Botón para buscar contacto
        JButton searchButton = new JButton("BUSQUEDA");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String criterio = JOptionPane.showInputDialog(frame, "Buscar por (nombre, telefono, correo):");
                if (criterio == null || criterio.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Debe ingresar un criterio de búsqueda válido.");
                    return;
                }
                String valor = JOptionPane.showInputDialog(frame, "Ingrese el valor de búsqueda:");
                if (valor == null || valor.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Debe ingresar un valor de búsqueda válido.");
                    return;
                }
                Contacto contacto = null;

                try {
                    contacto = agenda.buscarContacto(criterio, valor);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Formato de número de teléfono inválido.");
                    return;
                }

                if (contacto != null) {
                    textArea.append("Contacto encontrado: " + contacto.getNombre() + ", Teléfono: " + contacto.getTelefono() +
                            ", Correo: " + contacto.getCorreoElectronico() + ", Fecha de Nacimiento: " + contacto.getFechaNacimiento() + "\n");
                } else {
                    textArea.append("Contacto no encontrado\n");
                }
            }
        });

        // Botón para buscar contacto por múltiples criterios
        JButton multiSearchButton = new JButton("REVISION DE EXISTENCIA");
        multiSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = JOptionPane.showInputDialog(frame, "Nombre (opcional):");
                String telefonoStr = JOptionPane.showInputDialog(frame, "Teléfono (opcional):");
                String correo = JOptionPane.showInputDialog(frame, "Correo Electrónico (opcional):");

                long telefono = 0;
                if (telefonoStr != null && !telefonoStr.trim().isEmpty()) {
                    try {
                        telefono = Long.parseLong(telefonoStr);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Número de teléfono inválido.");
                        return;
                    }
                }

                Contacto criterio = new Contacto(
                        nombre != null && !nombre.trim().isEmpty() ? nombre : null,
                        telefono,
                        correo != null && !correo.trim().isEmpty() ? correo : null,
                        null // Ignoramos la fecha de nacimiento en esta búsqueda
                );

                Contacto resultado = agenda.buscar(criterio);
                if (resultado != null) {
                    textArea.append("Contacto encontrado: " + resultado.getNombre() + ", Teléfono: " + resultado.getTelefono() +
                            ", Correo: " + resultado.getCorreoElectronico() + ", Fecha de Nacimiento: " + resultado.getFechaNacimiento() + "\n");
                } else {
                    textArea.append("Contacto no encontrado\n");
                }
            }
        });

        // Botón para cargar agenda desde archivo
        JButton loadButton = new JButton("IMPORTAR AGENDA");
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        agenda.cargarAgenda(fileChooser.getSelectedFile().getAbsolutePath());
                        textArea.append("Agenda cargada desde archivo\n");
                        agenda.mostrarContactos(textArea);
                    } catch (IOException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Botón para guardar agenda en archivo
        JButton saveButton = new JButton("EXPORTAR LA AGENDA");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        agenda.guardarAgenda(fileChooser.getSelectedFile().getAbsolutePath());
                        textArea.append("Agenda guardada en archivo\n");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Botón para mostrar todos los contactos
        JButton showButton = new JButton("AGENDA");
        showButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agenda.mostrarContactos(textArea);
            }
        });

        // Añadir botones al panel de botones
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(multiSearchButton); // Añadir el nuevo botón aquí
        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(showButton);

        // Añadir el panel de botones y el área de texto al panel principal
        panel.add(buttonPanel, BorderLayout.WEST);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Añadir el panel principal al frame
        frame.add(panel);
        frame.setVisible(true);
    }
}
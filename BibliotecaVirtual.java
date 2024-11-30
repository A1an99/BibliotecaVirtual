import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

class Libro {
    private String titulo;
    private String autor;
    private boolean disponible;

    public Libro(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
        this.disponible = true;
    }

    public String getTitulo() {
        return titulo;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void prestar() {
        disponible = false;
    }

    public void devolver() {
        disponible = true;
    }

    @Override
    public String toString() {
        return titulo + " por " + autor + " - " + (disponible ? "Disponible" : "Prestado");
    }
}

class Biblioteca {
    private ArrayList<Libro> librosDisponibles;
    private Queue<String> colaSolicitudes;

    public Biblioteca() {
        librosDisponibles = new ArrayList<>();
        colaSolicitudes = new LinkedList<>();
    }

    public void agregarLibro(Libro libro) {
        librosDisponibles.add(libro);
    }

    public void solicitarLibro(String titulo, String usuario) {
        for (Libro libro : librosDisponibles) {
            if (libro.getTitulo().equalsIgnoreCase(titulo)) {
                if (libro.isDisponible()) {
                    libro.prestar();
                    JOptionPane.showMessageDialog(null, "El libro \"" + titulo + "\" ha sido prestado a " + usuario + ".");
                } else {
                    colaSolicitudes.add(usuario);
                    JOptionPane.showMessageDialog(null, "El libro \"" + titulo + "\" no está disponible. " + usuario + " ha sido agregado a la cola de espera.");
                }
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "El libro \"" + titulo + "\" no se encuentra en la biblioteca.");
    }

    public void devolverLibro(String titulo) {
        for (Libro libro : librosDisponibles) {
            if (libro.getTitulo().equalsIgnoreCase(titulo)) {
                if (!libro.isDisponible()) {
                    libro.devolver();
                    JOptionPane.showMessageDialog(null, "El libro \"" + titulo + "\" ha sido devuelto.");

                    if (!colaSolicitudes.isEmpty()) {
                        String siguienteUsuario = colaSolicitudes.poll();
                        libro.prestar();
                        JOptionPane.showMessageDialog(null, "El libro \"" + titulo + "\" ha sido prestado a " + siguienteUsuario + " de la cola de espera.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "El libro \"" + titulo + "\" ya está disponible en la biblioteca.");
                }
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "El libro \"" + titulo + "\" no se encuentra en la biblioteca.");
    }

    public ArrayList<Libro> getLibrosDisponibles() {
        return librosDisponibles;
    }
}

public class BibliotecaVirtual extends JFrame {
    private Biblioteca biblioteca;
    private JList<String> listaLibros;
    private DefaultListModel<String> listModel;
    private JTextField textoTitulo;
    private JTextField textoUsuario;

    public BibliotecaVirtual() {
        biblioteca = new Biblioteca();
        biblioteca.agregarLibro(new Libro("\n No puedes hacerme daño", "David Goggins"));
        biblioteca.agregarLibro(new Libro( "\n El hombre en busca de sentido", "Viktor Frankl"));
        biblioteca.agregarLibro(new Libro("\n La sabiduría de los psicópatas", "Kevin Dutton"));
        biblioteca.agregarLibro(new Libro("\n Don Quijote de la Mancha", "Miguel de Cervantes"));
        biblioteca.agregarLibro(new Libro("\n American Sniper", "Chris Kyle y Jim DeFelice"));
        biblioteca.agregarLibro(new Libro("\n Ted Bundy", "Jack Maxwell"));
        biblioteca.agregarLibro(new Libro("\n Tus zonas erróneas", "Wayne Dyer"));
        biblioteca.agregarLibro(new Libro("\n Cosmos ", "Carl Sagan"));
        biblioteca.agregarLibro(new Libro("\n codigo ", "Charles Petzold "));
        biblioteca.agregarLibro(new Libro("\n El libro negro de las computadoras", "A.David Garza Marín"));



        setTitle("Sistema de Biblioteca");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(Color.BLACK);

        listModel = new DefaultListModel<>();
        actualizarListaLibros();

        listaLibros = new JList<>(listModel);
        listaLibros.setBackground(Color.white);
        listaLibros.setForeground(Color.BLACK);
        listaLibros.setFont(new Font("Arial", Font.PLAIN, 17));  // Cambiar el tamaño de la letra

        JScrollPane scrollPane = new JScrollPane(listaLibros);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.setBackground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 25);

        JLabel labelTitulo = new JLabel("Título del libro:");
        labelTitulo.setForeground(Color.BLACK);
        labelTitulo.setFont(font);

        textoTitulo = new JTextField();
        textoTitulo.setFont(font);

        JLabel labelUsuario = new JLabel("Nombre del usuario:");
        labelUsuario.setForeground(Color.BLACK);
        labelUsuario.setFont(font);

        textoUsuario = new JTextField();
        textoUsuario.setFont(font);

        JButton botonSolicitar = new JButton("Solicitar libro");
        botonSolicitar.setFont(font);
        botonSolicitar.setBackground(Color.GRAY);
        botonSolicitar.setForeground(Color.WHITE);
        botonSolicitar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titulo = textoTitulo.getText();
                String usuario = textoUsuario.getText();
                if (!titulo.isEmpty() && !usuario.isEmpty()) {
                    biblioteca.solicitarLibro(titulo, usuario);
                    actualizarListaLibros();
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese el título y el nombre del usuario.");
                }
            }
        });

        JButton botonDevolver = new JButton("Devolver libro");
        botonDevolver.setFont(font);
        botonDevolver.setBackground(Color.GRAY);
        botonDevolver.setForeground(Color.WHITE);
        botonDevolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titulo = textoTitulo.getText();
                if (!titulo.isEmpty()) {
                    biblioteca.devolverLibro(titulo);
                    actualizarListaLibros();
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese el título del libro a devolver.");
                }
            }
        });

        panel.add(labelTitulo);
        panel.add(textoTitulo);
        panel.add(labelUsuario);
        panel.add(textoUsuario);
        panel.add(botonSolicitar);
        panel.add(botonDevolver);

        add(panel, BorderLayout.SOUTH);
    }

    private void actualizarListaLibros() {
        listModel.clear();
        for (Libro libro : biblioteca.getLibrosDisponibles()) {
            listModel.addElement(libro.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BibliotecaVirtual().setVisible(true);
            }
        });
    }
}

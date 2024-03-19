import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.prefs.Preferences;

public class Application extends Thread {
    private static final String WIDTH_KEY = "width";
    private static final String HEIGHT_KEY = "height";
    private static final String POS_X = "x";
    private static final String POS_Y = "y";
    private JFrame frame;
    private Container cp;
    private static JTextArea posta1, posta2, posta3;
    private Preferences preferences;
    private final String MESSAGGE_IN_A_BOTTLE = "MESSAGGE_IN_A_BOTTLE";
    private final static String MESSAGGE_READ = "MESSAGGE_READ";
    private AtomicBoolean postaInUso = new AtomicBoolean(false);

    public Application() {
        super();
        frame = new JFrame();
        cp = frame.getContentPane();
        cp.setLayout(new FlowLayout(FlowLayout.CENTER));
        frame.setTitle("Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        preferences = Preferences.userNodeForPackage(Application.class);
        int width = preferences.getInt(WIDTH_KEY, 300);
        int height = preferences.getInt(HEIGHT_KEY, 400);
        int posx = preferences.getInt(POS_X, 100);
        int posy = preferences.getInt(POS_Y, 100);

        frame.setSize(900, 300);
        frame.setResizable(false);
        frame.setLocation(posx, posy);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveUserDimensions();
                System.exit(0);
            }
        });
        this.setupApp();
    }

    private void setupApp() {
        //impostazione layout delle JTextArea
        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        posta1 = new JTextArea(7, 25);
        posta1.setText("Posta1");
        posta1.setBorder(border);
        posta1.setEditable(false);
        posta1.setBackground(new Color(255, 0, 0));

        posta2 = new JTextArea(7, 25);
        posta2.setBorder(border);
        posta2.setText("Posta2");
        posta2.setEditable(false);
        posta2.setBackground(new Color(255, 0, 0));

        posta3 = new JTextArea(7, 25);
        posta3.setBorder(border);
        posta3.setText("Posta3");
        posta3.setEditable(false);
        posta3.setBackground(new Color(255, 0, 0));

        //gestione degli eventi quando viene cliccato il bottone
        JButton startSimuleButton = new JButton("Start Simulation");
        startSimuleButton.addActionListener(e -> {
            Thread simulationThread = new Thread(() -> {
                try {
                    Random random = new Random();
                    for (int i = 0; i < 10; i++) {
                        int postaIndex = random.nextInt(3) + 1; // numero casuale che rappresenta le caselle.

                        // viene scelta casualmente una delle 3 caselle e viene assegnata a una nuova
                        // JTextArea così modifichiamo la JTextarea corrente subito.
                        JTextArea targetPosta = null;
                        switch (postaIndex) {
                            case 1:
                                targetPosta = posta1;
                                break;
                            case 2:
                                targetPosta = posta2;
                                break;
                            case 3:
                                targetPosta = posta3;
                                break;
                            default:
                                break;
                        }
                        // controlla se la casella è vuota e aggiorna la casella a rosso.
                        //utilizziamo l'AtomicBoolean "postaInUso"  per assicurarci che un thread alla volta possa impostare il messaggio.
                        if (!postaInUso.getAndSet(true)) {
                            targetPosta.setText(MESSAGGE_IN_A_BOTTLE);
                            targetPosta.setBackground(new Color(255, 0, 0));
                        }
                        try {
                            Thread.sleep(1000); 
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        // aggiorna la casella a verde se il messaggio è stato inviato.
                        if (targetPosta.getText().equals(MESSAGGE_IN_A_BOTTLE)) {
                            targetPosta.setText(MESSAGGE_READ);
                            targetPosta.setBackground(new Color(0, 255, 0));
                        }
                        postaInUso.set(false); // impostiamo il valore inziale a false.
                    }
                } finally {
                    JOptionPane.showMessageDialog(frame, "Simulazione terminata");
                }
            });
            simulationThread.start();
        });

        cp.add(posta1);
        cp.add(posta2);
        cp.add(posta3);
        cp.add(startSimuleButton);
    }

    public void saveUserDimensions() {
        preferences.putInt(WIDTH_KEY, frame.getWidth());
        preferences.putInt(HEIGHT_KEY, frame.getHeight());
        preferences.putInt(POS_X, frame.getX());
        preferences.putInt(POS_Y, frame.getY());
    }

    public void startApp(boolean packElements) {
        if (packElements)
            frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Application().startApp(false);

            // Creazione thread che leggono i messaggi.
            for (int i = 0; i < 3; i++) {
                Thread readerThread = new Thread(new Reader("ciao" + (i + 1)));
                readerThread.start();
            }
        });
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("metodo non implementato");
    }

    // Classe interna per la lettura dei messaggi.
    static class Reader implements Runnable {
        private String name;

        public Reader(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            while (true) {
                // garantiamo che un thread alla volta possa accedere e modificare le risorse
                // condivise all'interno della classe Application.
                synchronized (Application.class) {
                    JTextArea targetPosta = null;
                    // Scegli casualmente una delle caselle di posta
                    Random random = new Random();
                    int postaIndex = random.nextInt(3) + 1;

                    switch (postaIndex) {
                        case 1:
                            targetPosta = posta1;
                            break;
                        case 2:
                            targetPosta = posta2;
                            break;
                        case 3:
                            targetPosta = posta3;
                            break;
                        default:
                            break;
                    }
                    // Se la casella contiene un messaggio da leggere
                    if (targetPosta.getText().equals(MESSAGGE_READ)) {
                        targetPosta.setText("Nome lettore: \n" + name + "\n  messaggio: \n" + targetPosta.getText());

                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class AudioPlayerWithFileSelection extends JFrame implements ActionListener {
    Clip clip;
    boolean isPlaying = false;
    File currentAudioFile;

    JLabel fileLabel;
    JButton selectButton, playButton, pauseButton, stopButton;

    public AudioPlayerWithFileSelection() {
        super("Audio Player");
        setLayout(new GridLayout(0, 1)); 

        fileLabel = new JLabel("No file selected.");
        add(fileLabel);

        selectButton = createButton("Select File");
        selectButton.addActionListener(this);
        add(selectButton);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3)); 
        buttonPanel.add(playButton = createButton("Play"));
        buttonPanel.add(pauseButton = createButton("Pause"));
        buttonPanel.add(stopButton = createButton("Stop"));

        playButton.setEnabled(false);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);

        add(buttonPanel);

        setSize(350, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        return button;
    }

    public void play() {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(currentAudioFile);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioStream);

            clip.start();
            isPlaying = true;
            playButton.setEnabled(false); // Disable 'Play' while playing
            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
        } catch (Exception e) {
            System.out.println("Error playing audio file: " + e.getMessage());
        }
    }

    public void pause() {
        if (isPlaying) {
            clip.stop();
            isPlaying = false;
            pauseButton.setEnabled(false);
            playButton.setEnabled(true);
        }
    }

    public void stop() {
        if (isPlaying) {
            clip.stop();
            clip.setFramePosition(0);
            isPlaying = false;
            stopButton.setEnabled(false);
            playButton.setEnabled(true);
            pauseButton.setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == selectButton) {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                currentAudioFile = fileChooser.getSelectedFile();
                fileLabel.setText("File: " + currentAudioFile.getName());
                playButton.setEnabled(true);
                pauseButton.setEnabled(false);
                stopButton.setEnabled(false);
            }
        } else if (e.getSource() == playButton) {
            play();
        } else if (e.getSource() == pauseButton) {
            pause();
        } else if (e.getSource() == stopButton) {
            stop();
        }
    }

    public static void main(String[] args) {
        new AudioPlayerWithFileSelection();
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


class Surface extends JPanel implements ActionListener {        //iç kısım
    private static final int width = 1000;
    private static final int height = 1000;
    private double angle; // İlk açı
    private double v0; // İlk hız
    private double g = 9.8; // Yerçekimi ivmesi
    private double timeStep = 0.16; // Zaman adımı
    private Timer timer;
    private double vx, vy;
    private double x, y; // Topun konumu
    private double t; // Geçen süre
    private Image ball;


    public Surface() {
        initSurface();
        initTimer();
        loadImage();
    }
    private void loadImage(){
        ball = new ImageIcon("images/ball.png").getImage();

    }
    private void initSurface() {
        while(true){
            try {
                angle = Double.parseDouble(JOptionPane.showInputDialog("Lütfen açıyı girin :"));
                v0 = Double.parseDouble(JOptionPane.showInputDialog("Lütfen hızı girin :"));
                vx = v0 * Math.cos(Math.toRadians(angle));
                vy = -v0 * Math.sin(Math.toRadians(angle));
                x = 50;
                y = height-75;
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Hatalı giriş! Lütfen sayısal değerleri doğru bir şekilde girin.");
            }
        }
    }
    private void initTimer() {
        timer = new Timer(16, this);
        timer.start();
    }
    private void updatePosition() {
        t += timeStep;
        x += vx * timeStep;
        y += vy * timeStep + 0.5 * g * Math.pow(timeStep,2);
        vy+= g * timeStep; // yer çekimi etkisi
        if (x <= 0 || x >= getWidth()-50) {
            vx = -vx; // Hızı tersine çevir
        }

        // Topun tavana çarptığını kontrol et
        if (y <= 0) {
            y = 50; // Topun alt kenarıyla çakışmasını önle
            vy = -vy; // Hızı tersine çevir
        }

        if (y >= height-50) { //top zemine ulaştı mı?
            y = height-50; // topun konumunu zemine eşitle
            timer.stop();
            JOptionPane.showMessageDialog(this, "Top " + (float)(t/6.25) + " saniyede yere düştü");

            // Programı kapat
            System.exit(1);
        }
    }

    public void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(ball,(int) x,(int) y,50,50,null);
        g2d.setColor(Color.orange);
        g2d.fillRect(0, height - 25 ,getWidth(), height);
        updatePosition();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}

public class serbestAtis extends JFrame {   //Dış çerçeve
    public serbestAtis() {

        initUI();
    }

    private void initUI() {

        final Surface surface = new Surface();
        add(surface);

        setTitle("Serbest Atış");
        setSize(1000,1050);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                serbestAtis atis = new serbestAtis();
                atis.setVisible(true);
            }
        });
    }
}
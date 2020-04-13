import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LatihanGUI6 extends JFrame implements ActionListener, MouseListener, MouseMotionListener {

    private JButton loadBtn, convertBtn, quantBtn;
    private ImagePanel myPanel, dstPanel;
    private JFileChooser jfc;
    private BufferedImage image, image2;

    Histogram hIntBefore, hIntAfter;
    HistogramGUI hGIntBefore, hGIntAfter;

    JButton brightChange, constrastChange;
    JSlider brightnessSlider, constrastSlider;
    JLabel constrastLabel;
    JLabel brightnessLabel;

    JButton hequal;
    CDF cdfBefore, cdfAfter;
    HistogramGUI cdfGintBefore, cdfGIntAfter;

    public LatihanGUI6() {
        super("Latihan Pixel");
        this.setLayout(new FlowLayout());

        JPanel commandPanel = new JPanel();
        commandPanel.setPreferredSize(new Dimension(200, 500));
        commandPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        loadBtn = new JButton("Load Image");
        loadBtn.addActionListener(this);
        commandPanel.add(loadBtn);

        convertBtn = new JButton("Convert to Gray");
        convertBtn.addActionListener(this);
        commandPanel.add(convertBtn);

        quantBtn = new JButton("Quant");
        quantBtn.addActionListener(this);
        commandPanel.add(quantBtn);

        JPanel brightnessPanel = new JPanel();
        brightnessPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        brightnessPanel.setPreferredSize(new Dimension(200, 100));

        brightnessPanel.add(new JLabel("Brightness : "));
        brightnessLabel = new JLabel("0");
        brightnessPanel.add(brightnessLabel);

        brightnessSlider = new JSlider();
        brightnessSlider.setValue(0);
        brightnessSlider.setMaximum(-100);
        brightnessSlider.setMaximum(100);
        brightnessSlider.setPaintTicks(true);
        brightnessSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                int val = brightnessSlider.getValue();
                brightnessLabel.setText("" + val);
            }
        });

        brightnessPanel.add(brightnessSlider);

        brightChange = new JButton("Apply");
        brightChange.addActionListener(this);
        brightnessPanel.add(brightChange);
        commandPanel.add(brightnessPanel);

        JPanel contrastPanel = new JPanel();
        contrastPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        contrastPanel.setPreferredSize(new Dimension(200, 100));

        contrastPanel.add(new JLabel("Contrast : "));
        constrastLabel = new JLabel("1.0");
        contrastPanel.add(constrastLabel);

        constrastSlider = new JSlider();
        constrastSlider.setValue(100);
        constrastSlider.setMaximum(0);
        constrastSlider.setMaximum(200);
        constrastSlider.setPaintTicks(true);
        constrastSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                int val = constrastSlider.getValue();
                constrastLabel.setText("" + (val / 100f));
            }
        });

        contrastPanel.add(constrastSlider);

        constrastChange = new JButton("Apply");
        constrastChange.addActionListener(this);
        contrastPanel.add(constrastChange);
        commandPanel.add(contrastPanel);

        hequal = new JButton("Hist. Equalization");
        hequal.addActionListener(this);
        commandPanel.add(hequal);
        this.add(commandPanel);

        jfc = new JFileChooser();
        myPanel = new ImagePanel();
        myPanel.setPreferredSize(new Dimension(500, 500));
        myPanel.setBackground(Color.white);
        myPanel.addMouseListener(this);
        myPanel.addMouseMotionListener(this);
        this.add(myPanel);

        dstPanel = new ImagePanel();
        dstPanel.setPreferredSize(new Dimension(500, 500));
        dstPanel.setBackground(Color.white);
        this.add(dstPanel);

        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int xPos = this.getLocationOnScreen().x + this.getWidth();
        int yPos = this.getLocationOnScreen().y;
        hGIntBefore = new HistogramGUI("H Before ");
        hGIntBefore.setLocation(xPos, yPos);

        hGIntAfter = new HistogramGUI("H After ");
        hGIntAfter.setLocation(xPos, yPos + hGIntBefore.getHeight());

        cdfGintBefore = new HistogramGUI("CDF Before ");
        cdfGintBefore.setLocation(xPos + hGIntBefore.getWidth(), yPos);

        cdfGIntAfter = new HistogramGUI("CDF After ");
        cdfGIntAfter.setLocation(xPos + hGIntAfter.getWidth(), yPos + hGIntBefore.getHeight());
    }

    public static void main(String arg[]) {
        new LatihanGUI6();
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(loadBtn)) {
            jfc.showOpenDialog(this);
            if (jfc.getSelectedFile() != null) {
                try {
                    image = ImageIO.read(jfc.getSelectedFile());
                    myPanel.setImage(image);
                    myPanel.repaint();

                    hIntBefore = new Histogram(image);
                    hGIntBefore.setHistogramData(hIntBefore.getHistogram(Histogram.INTENSITY_CHANNEL));
                    cdfBefore = new CDF(hIntBefore);
                    cdfGintBefore.setHistogramData(cdfBefore.getCdfInt());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        if (src.equals(convertBtn)) {
            if (image != null) {
                image2 = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
                for (int y = 0; y < image.getHeight(); y++) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        int rgb = image.getRGB(x, y);
                        Color c = new Color(rgb);
                        int gray = c.getRed() + c.getGreen() + c.getBlue();
                        gray = gray / 3;
                        gray = gray > 255 ? 255 : gray;
                        //comment page 3
                        Color newC = new Color(gray, gray, gray);
                        image2.setRGB(x, y, newC.getRGB());
                    }
                }

                hIntAfter = new Histogram(image2);
                hGIntAfter.setHistogramData(hIntAfter.getHistogram(Histogram.INTENSITY_CHANNEL));
                cdfAfter = new CDF(hIntAfter);
                cdfGIntAfter.setHistogramData(cdfAfter.getCdfInt());
                dstPanel.setImage(image2);
                dstPanel.repaint();
            }
        }

        if (src.equals(quantBtn)) {
            if (image != null) {
                image2 = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
                for (int y = 0; y < image.getHeight(); y++) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        int rgb = image.getRGB(x, y);
                        Color c = new Color(rgb);
                        int r = c.getRed();
                        int g = c.getGreen();
                        int b = c.getBlue();

                        int deg = 8;
                        int range = 256 / deg;
                        r = (int) (Math.ceil(r / range) * range);
                        g = (int) (Math.ceil(g / range) * range);
                        b = (int) (Math.ceil(b / range) * range);

                        Color newC = new Color(r, g, b);
                        image2.setRGB(x, y, newC.getRGB());
                    }
                }

                hIntAfter = new Histogram(image2);
                hGIntAfter.setHistogramData(hIntAfter.getHistogram(Histogram.INTENSITY_CHANNEL));
                cdfAfter = new CDF(hIntAfter);
                cdfGIntAfter.setHistogramData(cdfAfter.getCdfInt());
                dstPanel.setImage(image2);
                dstPanel.repaint();
            }
        }

        if (src.equals(brightChange)) {
            if (image != null) {
                image2 = this.changeBrightness(image, brightnessSlider.getValue());
                hIntAfter = new Histogram(image2);
                hGIntAfter.setHistogramData(hIntAfter.getHistogram(Histogram.INTENSITY_CHANNEL));
                cdfAfter = new CDF(hIntAfter);
                cdfGIntAfter.setHistogramData(cdfAfter.getCdfInt());
                dstPanel.setImage(image2);
                dstPanel.repaint();
            }
        }

        if (src.equals(constrastChange)) {
            if (image != null) {
                image2 = this.changeContrast(image, constrastSlider.getValue() / 100f);
                hIntAfter = new Histogram(image2);
                hGIntAfter.setHistogramData(hIntAfter.getHistogram(Histogram.INTENSITY_CHANNEL));
                cdfAfter = new CDF(hIntAfter);
                cdfGIntAfter.setHistogramData(cdfAfter.getCdfInt());
                dstPanel.setImage(image2);
                dstPanel.repaint();
            }
        }

        if (src.equals(hequal)) {
            if (image != null) {
                image2 = equalizeImage(image);
                hIntAfter = new Histogram(image2);
                hGIntAfter.setHistogramData(hIntAfter.getHistogram(Histogram.INTENSITY_CHANNEL));
                cdfAfter = new CDF(hIntAfter);
                cdfGIntAfter.setHistogramData(cdfAfter.getCdfInt());
                dstPanel.setImage(image2);
                dstPanel.repaint();
            }
        }
    }

    private BufferedImage equalizeImage(BufferedImage srcImage) {
        BufferedImage dstImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), srcImage.getType());
        Histogram h = new Histogram(srcImage);
        CDF cdf = new CDF(h);

        int lutRed[] = new int[256];
        int[] cdfRed = cdf.getCdfRed();
        int lutGreen[] = new int[256];
        int[] cdfGreen = cdf.getCdfGreen();
        int lutBlue[] = new int[256];
        int[] cdfBlue = cdf.getCdfBlue();

        double imageSize = srcImage.getWidth() * srcImage.getHeight();
        for (int i = 0; i < 256; i++) {
            lutRed[i] = (int) Math.min(255, ((cdfRed[i] * 256) / imageSize));
            lutGreen[i] = (int) Math.min(255, ((cdfGreen[i] * 256) / imageSize));
            lutBlue[i] = (int) Math.min(255, ((cdfBlue[i] * 256) / imageSize));
        }
        for (int y = 0; y < srcImage.getHeight(); y++) {
            for (int x = 0; x < srcImage.getWidth(); x++) {
                int rgb = srcImage.getRGB(x, y);
                Color c = new Color(rgb);

                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();

                int gray = (red + green + blue) / 3;
                red = lutRed[red];
                green = lutGreen[green];
                blue = lutBlue[blue];
                rgb = new Color(red, green, blue).getRGB();
                dstImage.setRGB(x, y, rgb);
            }
        }
        return dstImage;
    }

    private BufferedImage changeBrightness(BufferedImage srcImage, int value) {
        BufferedImage dstImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), srcImage.getType());
        for (int y = 0; y < srcImage.getHeight(); y++) {
            for (int x = 0; x < srcImage.getWidth(); x++) {
                int rgb = srcImage.getRGB(x, y);
                Color c = new Color(rgb);
                int red = c.getRed() + value;
                int green = c.getGreen() + value;
                int blue = c.getBlue() + value;

                red = Math.min(255, Math.max(0, red));
                green = Math.min(255, Math.max(0, green));
                blue = Math.min(255, Math.max(0, blue));
                Color gc = new Color(red, green, blue);
                dstImage.setRGB(x, y, gc.getRGB());
            }
        }
        return dstImage;
    }

    private BufferedImage changeContrast(BufferedImage srcImage, float value) {
        BufferedImage dstImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), srcImage.getType());
        for (int y = 0; y < srcImage.getHeight(); y++) {
            for (int x = 0; x < srcImage.getWidth(); x++) {
                int rgb = srcImage.getRGB(x, y);
                Color c = new Color(rgb);

                int red = (int) (c.getRed() * value);
                int green = (int) (c.getGreen() * value);
                int blue = (int) (c.getBlue() * value);

                red = red > 255 ? 255 : red;
                green = green > 255 ? 255 : green;
                blue = blue > 255 ? 255 : blue;

                Color gc = new Color(red, green, blue);
                dstImage.setRGB(x, y, gc.getRGB());
            }
        }
        return dstImage;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        Object src = e.getSource();
        if (src.equals(myPanel)) {
            Point p = e.getPoint();
            int size = 20;
            int counter = 0;
            boolean stepUp = true;
            for (int i = -size; i <= size; i++) {
                int start = -counter;
                int stop = counter;
                for (int j = start; j <= stop; j++) {
                    int nx = p.x + j;
                    int ny = p.y + i;

                    if ((nx >= 0 && nx <= image.getWidth()) && (ny >= 0 && ny <= image.getHeight())) {
                        Color c1 = new Color(image.getRGB(nx, ny));
                        Color c2 = new Color(255, 128, 128, 128);

                        int nRed = (int) (c1.getRed() + (c2.getRed() * (c2.getAlpha() / 255f)));
                        int nGreen = (int) (c1.getGreen() + (c2.getGreen() * (c2.getAlpha() / 255f)));
                        int nBlue = (int) (c1.getBlue() + (c2.getBlue() * (c2.getAlpha() / 255f)));

                        nRed = nRed > 255 ? 255 : nRed;
                        nGreen = nGreen > 255 ? 255 : nGreen;
                        nBlue = nRed > 255 ? 255 : nBlue;
                        image.setRGB(nx, ny, new Color(nRed, nGreen, nBlue).getRGB());
                    }
                }

                if (stepUp) {
                    counter++;
                    if (counter >= size)
                        stepUp = false;
                } else {
                    counter--;
                }
            }
            myPanel.repaint();
        }
    }

    public void mouseMoved(MouseEvent e) {

    }
}

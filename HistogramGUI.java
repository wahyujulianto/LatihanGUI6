import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class HistogramGUI extends JFrame{
    private int histogramData[];
    private int maxValue = 1;
    private HistogramPanel hPanel;


    public HistogramGUI(String label){
        super(label);
        this.setResizable(false);
        histogramData = new int[256];
        this.setLayout(new FlowLayout());
        hPanel = new HistogramPanel();
        this.add(hPanel);
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    class HistogramPanel extends JPanel{

        public HistogramPanel(){
            super();
            setPreferredSize(new Dimension(256,200));
        }

        public void paint(Graphics g){
            super.paint(g);
            g.setColor(Color.white);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.BLACK);
            for(int i=0;i<histogramData.length;i++){
                g.drawLine(i, 200, i, (int)(200-((histogramData[i]/(maxValue*1.0)) * 200)));
            }
        }

    }
    public void setHistogramData(int data[]){
        this.histogramData=data;
        maxValue = 1;
        for(int i=0;i<data.length;i++){
            if(data[i]>maxValue)
                maxValue = data[i];
        }
        repaint();
    }

    public void setHistogramData(BufferedImage bim,int channel){
        for(int i=0;i<histogramData.length;i++){
            histogramData[i]=0;

        }

        if(channel <=3){
            for(int y=0;y<bim.getHeight();y++){
                for(int x=0;y<bim.getWidth();x++){
                    int rgb = bim.getRGB(x, y);
                    int vc[] = { rgb & 0xff , (rgb >> 8 ) & 0xff,(rgb >> 16) & 0xff};

                    if(channel == 3){
                        int index = (vc[0]+vc[1]+vc[2])/3;
                        histogramData[Math.min(255, index)]++;
                    } else{
                        histogramData[vc[channel]]++;

                    }
                }

            }

            setHistogramData(histogramData);
        }
    }

}


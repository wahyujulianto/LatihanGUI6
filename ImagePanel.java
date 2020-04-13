import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{
    BufferedImage image;
    public ImagePanel(){
        super();
    }

    public void paint(Graphics g){
        super.paint(g);
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        if(image!=null){
            g.drawImage(image, 0, 0, null);
        }


    }

    public synchronized BufferedImage getImage(){
        return image;
    }

    public synchronized void setImage(BufferedImage image){
        this.image=image;
    }

    public int getRGB(int x, int y){
        if(image!=null){
            return image.getRGB(x, y);
        }
        return 0;
    }

}

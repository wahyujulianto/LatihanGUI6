import java.awt.image.BufferedImage;

public class Histogram {
    public static final int RED_CHANNEL = 0;
    public static final int GREEN_CHANNEL = 1;
    public static final int BLUE_CHANNEL = 2;
    public static final int INTENSITY_CHANNEL = 3;


    private int hRed[] = new int[256];
    private int hGreen[] = new int[256];
    private int hBlue[] = new int[256];
    private int hInt[] = new int[256];

    public Histogram(BufferedImage bf) {
        updateHistogram(bf);
    }


    public void updateHistogram(BufferedImage bf){
        hRed = new int[256];
        hGreen = new int[256];
        hBlue = new int[256];
        hInt = new int[256];

        int rgbArray[] = new int[bf.getWidth()* bf.getHeight()];
        rgbArray =  bf.getRGB(0,0,bf.getWidth(), bf.getHeight(),rgbArray,0,bf.getWidth());

        for(int i = 0 ;i <rgbArray.length;i++){
            int vc[] = { rgbArray[i] & 0xff,(rgbArray[i]>>8)& 0xff,(rgbArray[i]>>16)&0xff};
            hRed[vc[0]]++;
            hGreen[vc[1]]++;
            hBlue[vc[2]]++;
            int index = Math.min(255,(vc[0]+vc[1]+vc[2]/3) );
            hInt[index]++;
        };

    }


    public int[] getHistogram(int channel){
        switch (channel) {
            case RED_CHANNEL:
                return  hRed;
            case GREEN_CHANNEL:
                return hGreen;
            case BLUE_CHANNEL:
                return hBlue;
            case INTENSITY_CHANNEL:
                return  hInt;
            default:
                return  null;
        }
    }

}


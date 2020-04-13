public class CDF {
    Histogram hist;
    private int cdfRed[] = new int[256];
    private int cdfGreen[] = new int[256];
    private int cdfBlue[] = new int[256];
    private int cdfInt[] = new int[256];

    public CDF(Histogram hist){
        this.hist = hist;
        calculateCDF(hist);
    }

    private void calculateCDF(Histogram hist){
        cdfRed = new int[256];
        cdfGreen = new int[256];
        cdfBlue = new int[256];
        cdfInt = new int[256];

        int[] hRed = hist.getHistogram(0);
        int[] hGreen = hist.getHistogram(1);
        int[] hBlue = hist.getHistogram(2);
        int[] hInt = hist.getHistogram(3);
        cdfRed[0] = hRed[0];
        cdfGreen[0] = hGreen[0];
        cdfBlue[0] = hBlue[0];
        cdfInt[0] = hInt[0];

        for(int i=1;i<256;i++) {
            cdfRed[i] = cdfRed[i - 1] + hRed[i];
            cdfGreen[i] = cdfGreen[i - 1] + hGreen[i];
            cdfBlue[i] = cdfBlue[i - 1] + hBlue[i];
            cdfInt[i] = cdfInt[i - 1] + hInt[i];
        }
    }

    public Histogram getHist() {
        return hist;
    }

    public int[] getCdfRed() {
        return cdfRed;
    }

    public int[] getCdfGreen() {
        return cdfGreen;
    }

    public int[] getCdfBlue() {
        return cdfBlue;
    }

    public int[] getCdfInt() {
        return cdfInt;
    }
}

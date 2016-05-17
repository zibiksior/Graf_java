import org.apache.commons.math3.analysis.interpolation.BicubicInterpolatingFunction;
import org.apache.commons.math3.analysis.interpolation.LoessInterpolator;
import org.apache.commons.math3.analysis.interpolation.SmoothingPolynomialBicubicSplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.exception.util.ExceptionContext;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StrokeMap;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * Created by Zbigniew on 16/12/15.
 */
public class LineChart_AWT extends JFrame {

    private static int ITERACJE=150, PUNKTY=50;
    private int[][] newTwoD;
    private double[][] dane = new double[6][PUNKTY];
    private double[] noweDane = new double[PUNKTY];
    private double[] x = new double[PUNKTY];

    public LineChart_AWT(String applicationTitle , String chartTitle )
    {
        super(applicationTitle);
        XYDataset dataset = createDataset();
        JFreeChart lineChart = ChartFactory.createXYLineChart(
                chartTitle,
                "Punkty","Moc sygnału",
                dataset,
                PlotOrientation.VERTICAL,true,true,false);

        //final XYDataset dataset2 = MovingAverage.createMovingAverage(
          //      dataset, "-MAVG",15,0);

        XYPlot xyPlot = lineChart.getXYPlot();
        //xyPlot.setDataset(1, dataset2);
        xyPlot.setRenderer(new XYSplineRenderer());
        //CategoryAxis axis = lineChart.getCategoryPlot().getDomainAxis();
        //axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        //axis.setLowerMargin(50);
        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 1000 , 700) );
        setContentPane( chartPanel );

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private XYDataset createDataset( )
    {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("Moc");
        XYSeries series2 = new XYSeries("Moc AVG");
        //dataset.addValue( 15 , "schools" , "1970" );
        for (int i=0;i<PUNKTY;i++) {
            try {
                int pkt=(i+1);
                FileInputStream fis = new FileInputStream("/Users/Zbigniew/Documents/Praca Inżynierska/Kalibracja moc 7/Beacon_Kalibracja_"+pkt+".dat");
                ObjectInputStream iis = new ObjectInputStream(fis);
                newTwoD = (int[][]) iis.readObject();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            liczSrednia(i);

        }

        for (int k = 0; k < PUNKTY; k++) {
            x[k]=k;
            //dane[1][k]=dane[1][k]*(-1);
        }
        LoessInterpolator loessInterpolator = new LoessInterpolator();
        //loessInterpolator.interpolate(x,dane[1]);
        noweDane = loessInterpolator.smooth(x,dane[1]);
try{
    FileOutputStream fis = new FileOutputStream("/Users/Zbigniew/Desktop/Beacon_Kalibracja_smooth.dat");
    ObjectOutputStream iis = new ObjectOutputStream(fis);
    iis.writeObject(noweDane);
}
catch (Exception e){
    System.out.println(e.toString());
}

        //noweDane = polynomialSplineFunction.getKnots();
        for (int k = 0; k < noweDane.length; k++) {
            series1.add(k, dane[1][k]);
            series2.add(k, noweDane[k]);
            //dataset.addValue(noweDane[k],"Moc sygnału", String.valueOf(k));
        }

        dataset.addSeries(series1);
        dataset.addSeries(series2);
        return dataset;
    }

    private void liczSrednia(int punkt){
        int sum = 0; //average will have decimal point
        for (int j=0;j<3;j++) {
            for (int i = 0; i < ITERACJE; i++) {
                //parse string to double, note that this might fail if you encounter a non-numeric string
                //Note that we could also do Integer.valueOf( args[i] ) but this is more flexible
                sum += newTwoD[j][i];
            }
            double average = sum/ITERACJE;
            sum=0;
            dane[j][punkt]=average;
        }



    }


}

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;

import static java.lang.Math.floor;
import static java.lang.Math.pow;

public class controller implements Initializable {

    public Canvas canvas;
    public TextField alfa;
    public TextField r;
    public GraphicsContext g;
    @FXML
    public LineChart lineChart;
    @FXML
    public CategoryAxis xAxis;
    @FXML
    public NumberAxis yAxis;
    private Stage stage;
    List<Double> dataX =  new ArrayList<Double>();
    List<Double> dataY =  new ArrayList<Double>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    public void spirala(){
        XYChart.Series series = new XYChart.Series();
        series.setName("Data Chart");
        for(int i=0; i<dataX.size();i++){
            series.getData().add(new XYChart.Data<>(String.valueOf(dataX.get(i)),dataY.get(i)));

        }
        lineChart.getData().addAll(series);
    }
    @FXML
    public void loadData(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setInitialDirectory(new File("C://Users//jkacz//Desktop//materialy_app//materialy_app//wygladzanie i wizualizacja danych//wygladzanie i wizualizacja danych"));
        File file = fileChooser.showOpenDialog(stage);

        int i=0;
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(file != null){
            String params = scanner.next();
            xAxis.setLabel(params);
            params = scanner.next();
            yAxis.setLabel(params);
            while(scanner.hasNext()){
                dataX.add(Double.parseDouble(scanner.next()));
                dataY.add(Double.parseDouble(scanner.next()));
                i++;
            }

        }
        yAxis.setAutoRanging(false);
        List<Double> sorted = new ArrayList<>(dataY);
        Collections.sort(sorted);
        yAxis.setLowerBound(sorted.get(0));
        yAxis.setUpperBound(sorted.get(dataY.size()-1));

    }

    @FXML
    public void expo(){
        double alf = Double.parseDouble(alfa.getText());
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Exponential " + '\u03B1' + ": " + alf);
        double[] exp = new double[dataY.size()];
        for(int i=0;i<dataX.size();i++){
            if(i==0){
               exp[i]=dataY.get(i);
            }
            else{
                exp[i] = alf * dataY.get(i-1) + (1-alf)*exp[i-1];
            }
            double x=dataX.get(i);
            double y=exp[i];
            series1.getData().add(new XYChart.Data<>(String.valueOf(x),y));
        }
        lineChart.getData().addAll(series1);
    }
    @FXML
    public void mnk(){
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("MNK");
        List<Double> sorted1 = new ArrayList<>(dataX);
        Collections.sort(sorted1);
        int n = (int) floor(sorted1.get(dataX.size()-1));
        double[] reg = new double[n];
        double xy=0,xi=0,yi=0,x2=0;
        double a,b;
        for(int i=0;i<dataX.size();i++){
            xy += dataX.get(i)*dataY.get(i);
            xi += dataX.get(i);
            yi += dataY.get(i);
            x2 += pow(dataX.get(i),2);
        }
        double xsr = xi/dataX.size();
        double ysr = yi/dataY.size();
        double l=0,m=0;
        for(int i=0;i<dataX.size();i++){
            l += (dataX.get(i) - xsr)*(dataY.get(i) - ysr);
            m += (dataX.get(i) - xsr)*(dataX.get(i) - xsr);
        }
        a = l/m;
        b = ysr - a * xsr;
        System.out.println(n + " " + a + " " + b);

        for(int i=0;i<dataX.size()-1;i++){

            reg[i] = a*dataX.get(i) + b;
            double y = reg[i];
            series2.getData().add(new XYChart.Data<>(String.valueOf(dataX.get(i)),y));
        }
        yAxis.setAutoRanging(false);
        List<Double> sorted = new ArrayList<>(dataY);
        Collections.sort(sorted);


        lineChart.getData().addAll(series2);
    }
}

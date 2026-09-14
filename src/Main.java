import java.util.Scanner;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Main {
  static double mass = 2000;
  static double mu = 0.01;
  static double dragCoeff = 0.30;
  static double areaFront = 2.5;
  static double airDensity = 1.2;
  static double gravity = 10;
  static double tireRadius = 0.4;
  static double drivetrainEff = 0.9;
  static double theta = 0;
  static double gearRatio;
  
  public static void main (String[] args) {
    theta = 0; // degrees
    System.out.println("Enter Gear Ratio: ");
    Scanner scanner = new Scanner(System.in);
    gearRatio = scanner.nextDouble();
    XYSeries series = new XYSeries(" ");
    
    double velocity = 0;
    double dt = 0.01;
    series.add(0, 0);
    for (int i = 1; i < 12000; i++) {
     double wheelSpeed = (velocity*60)/(Math.PI*tireRadius);
     double motorSpeed = gearRatio*wheelSpeed;
     double motorTorque = getTorque(motorSpeed);
     double forwardTireForce = motorTorque*gearRatio*drivetrainEff/tireRadius;
     
     double rollingResist = mass*mu*gravity;
     double aeroResist = 0.5*airDensity*areaFront*velocity*velocity*dragCoeff;
     double gradResist = mass*gravity* Math.sin(Math.toRadians(theta));
     double totalForce = forwardTireForce-rollingResist-aeroResist-gradResist;
     double acceleration = totalForce/mass;
     velocity = velocity + (acceleration)*dt;
     series.add(dt*i,velocity);
    }
    
    XYSeriesCollection dataset = new XYSeriesCollection();
    dataset.addSeries(series);
    ChartPanel chartPanel = new ChartPanel(ChartFactory.createXYLineChart("","Time","Speed",dataset));
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(chartPanel);
    frame.pack();
    frame.setVisible(true);
    System.out.println("Time taken to reach X Velocity: ");
    double vf = scanner.nextDouble();
    for (int i = 0; i < series.getItemCount(); i++) {
      if (series.getY(i).doubleValue() >= vf) {
        System.out.println(series.getX(i).doubleValue() + " seconds");
        break;
      }
    }
    scanner.close();
  }
  
  public static double getTorque (double rpm) {
    if (rpm < 0) {
      System.out.println("Error RPM");
      throw new RuntimeException();
    }
    if (rpm < 4000) {
      return 250.0;
    }
    if (rpm <= 10000) {
      return 250.0*(4000.0/rpm);
    }
    if (rpm > 10000) {
      return 0.0;
    }
    return -1.0;
  }
}

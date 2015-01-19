/*	Ofek Gila
	April 12th, 2014
	PlanckLaw.java
	This program will calculate planck's law of black-body radiation

	This program:
		*	Returns brightness at given wavelength and given temperature
		*	Calculates the peak brightness and wavelength at given temperature
		*	Calculates visible light efficiency of light spectrum from given file
		*	Writes onto file spectrum of a given temperature
*/

import java.awt.*;			// Imports
import javax.swing.*;
import java.util.Scanner;
import java.math.BigDecimal;
import java.io.*;

public class PlanckLaw	{

	BigDecimal T, h, c, k, v, e, pi, b, l;
	Scanner input;
	public PlanckLaw()	{		
		OG = new OGraph("Light Spectrum Differences by Temperatures", "Wavelength (nm)", "Relative Brightness");
		startup();	
	}
	public PlanckLaw(BigDecimal temperature)	{
		OG = new OGraph("Light Spectrum Differences by Temperatures", "Wavelength (nm)", "Relative Brightness");
		T = new BigDecimal(temperature.toString());	// in kelvin
		startup();
	}
	public static void main(String[] args) {
		PlanckLaw PL = new PlanckLaw(new BigDecimal("4500"));
		//PL.createGraph();
		//PL.getEfficiency(30000);
		//PL.getEfficiency(2200);
		PL.getEfficiency("MercuryVaporLamp Spectrum.txt");
		//PL.saveBBRFile(3000, "LightningPoints.txt");
		/*System.out.print("temperature:\t");
		PlanckLaw PL = new PlanckLaw(new BigDecimal(new Scanner(System.in).nextDouble()));

		while (true)	{
			System.out.print("wave length (um):\t");
			String radiated = new Scanner(System.in).nextLine();
			System.out.println(PL.getPowerRadiated(radiated));
		}
		//PL.();*/
	}
	OGraph OG;	
	public void addDataSet()	{
		OG.newDataSet();
	}
	public void createGraph()	{
		JFrame frame = new JFrame("OGraph");	// ask why I extend JApplet or implement all of those things ^_^
		frame.setContentPane(OG);
		frame.setSize(520, 600);		// Sets size of frame
		OG.tellDimensions(500, 500);
		frame.setResizable(true);						// Makes it so you can't resize the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		OG.drawLines = true;
		OG.autoUpdate = true;
		OG.drawVerticalTicks(0.05);
		OG.drawMajorVerticalTicks(0.2);
		OG.drawMajorVerticalTickLabels("nm", 1000);
		OG.shadeVertical(0.38, 0.45, new Color(238, 130, 238));
		OG.shadeVertical(0.45, 0.495, new Color(0, 0, 255));
		OG.shadeVertical(0.495, 0.57, new Color(0, 128, 0));
		OG.shadeVertical(0.57, 0.59, new Color(255, 255, 0));
		OG.shadeVertical(0.59, 0.62, new Color(255, 165, 0));
		OG.shadeVertical(0.62, 0.75, new Color(255, 0, 0));
		//OG.drawVerticalTicks(0.1);
	//	OG.addVerticalLine(0.38); OG.addVerticalLine(0.45);
		for (double i = 0.01; i < 1.5; i+=0.01)
			OG.addPoint(i, getPowerRadiated(i).doubleValue());
		//OG.addVerticalLine(getPeak().doubleValue() * 1E6);
		OG.newDataSet();
		T = new BigDecimal("6000");
		for (double i = 0.01; i < 1.5; i+=0.01)
			OG.addPoint(i, getPowerRadiated(i).doubleValue());
		//OG.addVerticalLine(getPeak().doubleValue() * 1E6);
		OG.newDataSet();
		T = new BigDecimal("7500");
		for (double i = 0.01; i < 1.5; i+=0.01)
			OG.addPoint(i, getPowerRadiated(i).doubleValue());
		//OG.addVerticalLine(getPeak().doubleValue() * 1E6);
		/*OG.newDataSet();
		T = new BigDecimal("6000");
		for (double i = 0.01; i < 1.5; i+=0.01)
			OG.addPoint(i, getPowerRadiated(i).doubleValue());
		OG.addVerticalLine(getPeak().doubleValue() * 1E6);*/
		
		//OG.renderGraph();
	}
	public void setTemperature(double t)	{
		T = new BigDecimal(t);
	}
	public double[][] getGraph(double t, double startat, double endat, double increment)	{
		T = new BigDecimal(t);
		double[][] points = new double[(int)((endat - startat) / increment)][2];
		int pointon = 0;
		for (double i = startat; i <= endat; i+=increment)	{
			points[pointon][0] = i;
			points[pointon][1] = getPowerRadiated(i).doubleValue();
			pointon++;
		}
		return points;
	}
	public double[] getGraph(Object t, double startat, double endat, double increment)	{
		T = new BigDecimal(t.toString());
		double[] points = new double[(int)((endat - startat) / increment)];
		int pointon = 0;
		for (double i = startat; i <= endat; i+=increment)	{
			points[pointon] = getPowerRadiated(i).doubleValue();
			pointon++;
		}
		return points;
	}
	public void startup()	{	// this method calculates for constants
		h = new BigDecimal("6.62606957E-34");
		c = new BigDecimal("299792458");
		k = new BigDecimal("1.3806488E-23");
		e = new BigDecimal("2.71828182845904523536028747135266249775724709369995");
		pi = new BigDecimal("3.14159265358979323846264338327950288419716939937510");
		b = new BigDecimal("2.8977721E-3");
		//System.out.println("h: " + h.toString());
	}
	/*public BigDecimal getPowerRadiated(String frequency)	{	// this calculates power radiated using planck's law
		v = new BigDecimal(frequency);
		return leftCalc().multiply(rightCalc());
	}
	public BigDecimal leftCalc()	{
		BigDecimal leftSide = new BigDecimal("2");
		leftSide = leftSide.multiply(h);
		leftSide = leftSide.multiply(v.pow(3));
		leftSide = leftSide.divide(c.pow(2), 50, BigDecimal.ROUND_HALF_EVEN);
		return leftSide;

	}
	public BigDecimal rightCalc()	{
		BigDecimal rightSide = new BigDecimal("1");
		BigDecimal power = new BigDecimal(h.toString());
		power = power.multiply(v);
		power = power.divide(k.multiply(T), 50, BigDecimal.ROUND_HALF_EVEN);
		//System.out.println(power.toString());
		rightSide = rightSide.divide(BigDecimalPower(e, power).subtract(new BigDecimal("1")), 50, BigDecimal.ROUND_HALF_EVEN);
		return rightSide;
	}*/
	public BigDecimal getPowerRadiated (String wavelength)	{
		l = new BigDecimal(wavelength);
		l = l.multiply(new BigDecimal(1E-6));
		return topCalc().divide(bottomCalc(), 50, BigDecimal.ROUND_HALF_EVEN);
	}
	public BigDecimal getPowerRadiated (double wavelength)	{
		l = new BigDecimal(wavelength);
		l = l.multiply(new BigDecimal(1E-6));
		return topCalc().divide(bottomCalc(), 50, BigDecimal.ROUND_HALF_EVEN);
	}
	public BigDecimal topCalc()	{
		BigDecimal topCalc = new BigDecimal("2");
		topCalc = topCalc.multiply(pi);
		topCalc = topCalc.multiply(h);
		topCalc = topCalc.multiply(BigDecimalPower(c, 2));
		return topCalc;
	}
	public BigDecimal bottomCalc()	{
		BigDecimal bottomCalc = new BigDecimal("1");
		bottomCalc = bottomCalc.multiply(BigDecimalPower(l, 5));
		BigDecimal power = new BigDecimal("1");
		power = power.multiply(h.multiply(c));
		power = power.divide(l.multiply(k).multiply(T), 50, BigDecimal.ROUND_HALF_EVEN);
		bottomCalc = bottomCalc.multiply(BigDecimalPower(e, power).subtract(new BigDecimal("1")));
		return bottomCalc;
	}
	public BigDecimal BigDecimalPower(BigDecimal n1, BigDecimal n2)	{
		return 	BigFunctions.exp( BigFunctions.ln(n1, 50).multiply(n2), 50);
	}
	public BigDecimal BigDecimalPower(BigDecimal n1, int pow)	{
		BigDecimal n2 = new BigDecimal(pow);
		return 	BigFunctions.exp( BigFunctions.ln(n1, 50).multiply(n2), 50);
	}
	public BigDecimal getPeak()	{
		return b.divide(T, 50, BigDecimal.ROUND_HALF_EVEN);
	}
	public BigDecimal getPeak(double temperature)	{
		return b.divide(new BigDecimal(temperature), 50, BigDecimal.ROUND_HALF_EVEN);
	}
	public BigDecimal getBrightnessAtPeak(double temperature)	{
		T = new BigDecimal(temperature);
		BigDecimal peak = new BigDecimal(getPeak().toString());
		return getPowerRadiated(peak.doubleValue() * 1E6);
	}
	public BigDecimal getBrightnessAtPeak()	{
		BigDecimal peak = new BigDecimal(getPeak().toString());
		return getPowerRadiated(peak.doubleValue() * 1E6);
	}
	public PrintWriter output;
	public void saveBBRFile(String filelocation)	{
		try {
			output = new PrintWriter(new File(filelocation));
		}
		catch (IOException e)	{
			System.err.println("ERROR: Cannot open file Mars2Save.txt");
			System.exit(99);
		}
		for (double d = 0.01; d <= 1.5; d+=0.01)
			output.print(d + " " + getPowerRadiated(d).toString());	
		output.close();
	}
	public void saveBBRFile(double temperature, String filelocation)	{
		T = new BigDecimal(temperature);
		try {
			output = new PrintWriter(new File(filelocation));
		}
		catch (IOException e)	{
			System.err.println("ERROR: Cannot open file Mars2Save.txt");
			System.exit(99);
		}
		for (double d = 0.01; d <= 1.5; d+=0.01)
			output.println(d + " " + getPowerRadiated(d).toString());	
		output.close();
	}
	public void getEfficiency(double temperature)	{
		T = new BigDecimal(temperature);
		BigDecimal Sum = new BigDecimal("0");
		BigDecimal Visible = new BigDecimal("0");
		double added = 1;
		boolean addedOnce = false;
		for (double i = 0.001; i <= 4; i += 0.001)	{
			added = getPowerRadiated(i).doubleValue();
			//if (added > 0) addedOnce = true;
			Sum = Sum.add(new BigDecimal(added));
			if (i >= 0.39 && i <= 0.7) Visible = Visible.add(new BigDecimal(added));
			//if (!(addedOnce)) added = 1;
		}
		System.out.println(Visible.divide(Sum, 50, BigDecimal.ROUND_HALF_EVEN).toString());
	}
	public void getEfficiency(String filename)	{
		try {
			input = new Scanner(new File(filename));// tries to open the file
		}
		catch (FileNotFoundException e)	{
			System.err.println("ERROR: Cannot open file " + filename);
			System.exit(97);
		}
		BigDecimal Sum = new BigDecimal("0");
		BigDecimal Visible = new BigDecimal("0");
		double added = 1;
		double i;
		while(input.hasNext())	{
			i = input.nextDouble();
			added = input.nextDouble();
			//if (added > 0) addedOnce = true;
			Sum = Sum.add(new BigDecimal(added));
			if (i >= 0.39 && i <= 0.7) Visible = Visible.add(new BigDecimal(added));
			//if (!(addedOnce)) added = 1;
		}
		System.out.println(Visible.divide(Sum, 50, BigDecimal.ROUND_HALF_EVEN).toString());
	}
}
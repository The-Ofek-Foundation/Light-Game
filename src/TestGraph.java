/*	Ofek Gila
	April 21st, 2014
	TestGraph.java
	This program tests out graph making
*/
import java.awt.*;			// Imports
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Scanner;
import java.net.MalformedURLException;
import javax.sound.sampled.*;
import java.io.*;			// classes File, IOException
import javax.imageio.*;	// class ImageIO
import java.lang.Integer;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;

public class TestGraph	extends JApplet{			// I'm pretty sure I copied down one of your online codes for key and focus listeners for their methods
	
	public JFrame frame;
	public OGraph OG;
	
	public static void main(String[] args) {	// when I made snake.java, and I copied snake.java to have all the implements for this code, so don't
		TestGraph GUIT = new TestGraph();
		GUIT.run();
	}
	public void run(){
		frame = new JFrame("OGraph");	// ask why I extend JApplet or implement all of those things ^_^
		OG = new OGraph("Title", "XLabel", "YLabel");
		frame.setContentPane(OG);
		frame.setSize(width(500), height(200));		// Sets size of frame
		OG.tellDimensions(500, 200);
		frame.setResizable(true);						// Makes it so you can't resize the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		OG.drawLines = true;
		OG.addPoint(10, 10); OG.addPoint(20, 20); OG.addPoint(0, 5); OG.addPoint(7,15);
		OG.renderGraph();
	}
	public int width(int w) {
		return w + 12;
	}
	public int height(int h) {
		return h + 31;
	}
	public void init()	{
		setContentPane(	new OGraph("Title"));
	}
}
class OGraph extends JPanel	{
	public double[][] points;
	public double[] horizontalLines;
	public double[] verticalLines;
	public double[][] verticalShades;
	public Color[] verticalShadeColors;
	public boolean drawLines;
	public int width, height;
	public String title;
	public String labelx, labely;
	public JLabel TitleLabel;
	public double maxX, maxY;
	public double XIncrement, YIncrement;
	public double XStart, YStart;
	public double XEnd, YEnd;
	public int lineon;
	public int numberofdatasets;
	public boolean dataGiven;
	public boolean autoUpdate;
	public boolean automaticXScale, automaticYScale;
	public boolean showVerticalLines, showHorizontalLines;
	private Graphics g;
	public int tempY, tempX;
	public double VerticalTickIncrement;
	public boolean drawVerticalTicks;
	public double MajorVerticalTickIncrement;
	public boolean drawMajorVerticalTicks;
	public boolean drawMajorVerticalTickLabels;
	public boolean showShades;
	public String MajorVerticalTickUnit;
	public double MajorVerticalTickConversionValue;
	
	OGraph()	{
		points = new double[0][3];
		TitleLabel = new JLabel();
		TitleLabel.setFont(new Font("Arial", Font.BOLD, 22));
		horizontalLines = new double[0];
		verticalLines = new double[0];
		verticalShades = new double[0][2];
		verticalShadeColors = new Color[0];
		drawLines = drawVerticalTicks = false;
		showHorizontalLines = showVerticalLines = showShades = true;
		width = height = 0;
		maxX = maxY = 0;
		XStart = YStart = 0;
		tempY = tempX = 1;
		automaticXScale = automaticYScale = true;
		lineon = 0;
		numberofdatasets = 1;
		dataGiven = false;
		autoUpdate = false;
		add(TitleLabel);
	}
	OGraph(String t)	{
		title = t;
		TitleLabel = new JLabel();
		TitleLabel.setFont(new Font("Arial", Font.BOLD, 22));
		horizontalLines = new double[0];
		verticalLines = new double[0];
		verticalShades = new double[0][2];
		verticalShadeColors = new Color[0];
		points = new double[0][3];
		drawLines = drawVerticalTicks = false;
		showHorizontalLines = showVerticalLines = showShades = true;
		width = height = 0;
		maxX = maxY = 0;
		XStart = YStart = 0;
		tempY = tempX = 1;
		automaticXScale = automaticYScale = true;
		lineon = 0;
		numberofdatasets = 1;
		dataGiven = false;
		autoUpdate = false;
		add(TitleLabel);
	}
	OGraph(String t, String lx, String ly)	{
		title = t;
		labelx = lx;
		labely = ly;
		TitleLabel = new JLabel();
		TitleLabel.setFont(new Font("Arial", Font.BOLD, 22));
		horizontalLines = new double[0];
		verticalLines = new double[0];
		verticalShades = new double[0][2];
		verticalShadeColors = new Color[0];
		points = new double[0][3];
		drawLines = drawVerticalTicks = false;
		showHorizontalLines = showVerticalLines = showShades = true;
		width = height = 0;
		maxX = maxY = 0;
		XStart = YStart = 0;
		tempY = tempX = 1;
		automaticXScale = automaticYScale = true;
		lineon = 0;
		numberofdatasets = 1;
		dataGiven = false;
		autoUpdate = false;
		add(TitleLabel);
	}
	public void drawVerticalTicks(double increment)	{
		VerticalTickIncrement = increment;
		drawVerticalTicks = true;
		if (autoUpdate)	repaint();
	}
	public void drawMajorVerticalTickLabels(String unit, double conversionratio)	{
		drawMajorVerticalTickLabels = true;
		MajorVerticalTickUnit = unit;
		MajorVerticalTickConversionValue = conversionratio;
		if (autoUpdate)	repaint();
	}
	public void drawMajorVerticalTicks(double Increment)	{
		MajorVerticalTickIncrement = Increment;
		drawMajorVerticalTicks = true;
		if (autoUpdate)	repaint();
	}
	public void showVerticalTicks(boolean b)	{
		drawVerticalTicks = b;
		if (autoUpdate)	repaint();
	}
	public void showMajorVerticalTicks(boolean b)	{
		drawMajorVerticalTicks = b;
		if (autoUpdate)	repaint();
	}
	public void showMajorVerticalTickLabels(boolean b)	{
		drawMajorVerticalTickLabels = b;
		if (autoUpdate)	repaint();
	}
	public void setXStart(double xs)	{
		XStart = xs;
	}
	public void setXEnd(double xe)	{
		int w;
		if (width / 20 < 20) w = 20;
		else                        w = width/20;
		int linewidth = width - 2 * w;
		automaticXScale = false;
		XIncrement = linewidth / xe;
		//System.out.println("XIncrement " + XIncrement);
		XEnd = xe;
		maxX = XEnd;
	}
	public void setYStart(double ys)	{
		YStart = ys;
	}
	public void setYEnd(double ye)	{
		int h;
		if (height / 20 < 20) h = 20;
		else                         h = height / 20;
		int lineheight = height - 2 * h;
		automaticYScale = false;
		YIncrement = lineheight / ye;
		YEnd = ye;
		maxY = YEnd;
	}
	public void setXLabel(String xl)	{
		labelx = xl;
	}
	public void setYLabel(String yl)	{
		labely = yl;
	}
	public void setTitle(String t)	{
		title = t;
	}
	public void tellDimensions(int w, int h)	{
		width = w;
		height = h;
	}
	public void removeAllPoints()	{
		points = new double[0][3];
		if (autoUpdate) repaint();
	}
	public void addPoint(double x, double y)	{
		//System.out.println(x + " " + y);
		double[][] pointsCopy = new double[points.length][3];
		for (int i = 0; i < points.length; i++)	{
			pointsCopy[i][0] = points[i][0];
			pointsCopy[i][1] = points[i][1];
			pointsCopy[i][2] = points[i][2];
		}
		points = new double[points.length+1][3];
		int stopon = 0;
		for (int i = 0; i < pointsCopy.length; i++)	{
			if (x < pointsCopy[i][0]) break;
			stopon = i + 1;
			points[i][0] = pointsCopy[i][0];
			points[i][1] = pointsCopy[i][1];
			points[i][2] = pointsCopy[i][2];
		}
		for (int i = stopon; i < pointsCopy.length; i++)	{
			points[i+1][0] = pointsCopy[i][0];
			points[i+1][1] = pointsCopy[i][1];
			points[i+1][2] = pointsCopy[i][2];
		}
		points[stopon][0] = x;
		points[stopon][1] = y;
		points[stopon][2] = lineon;
		if (x > maxX) maxX = x;
		if (y > maxY)	maxY = y;
		if (maxX > XEnd) XEnd = maxX;
		if (maxY > YEnd) YEnd = maxY;
		if (autoUpdate) repaint();
	}
	public void addHorizontalLine(double y)	{
		double[] horizontalLineCopy = new double[horizontalLines.length + 1];
		for (int i = 0; i < horizontalLines.length; i++)
			horizontalLineCopy[i] = horizontalLines[i];
		horizontalLines = new double[horizontalLineCopy.length];
		for (int i = 0; i < horizontalLineCopy.length - 1; i++)
			horizontalLines[i] = horizontalLineCopy[i];
		horizontalLines[horizontalLines.length - 1] = y;
	}
	public void addVerticalLine(double x)	{
		double[] verticalLineCopy = new double[verticalLines.length + 1];
		for (int i = 0; i < verticalLines.length; i++)
			verticalLineCopy[i] = verticalLines[i];
		verticalLines = new double[verticalLineCopy.length];
		for (int i = 0; i < verticalLineCopy.length - 1; i++)
			verticalLines[i] = verticalLineCopy[i];
		verticalLines[verticalLines.length - 1] = x;
	}
	public void removeAllVerticalLines()	{
		verticalLines = new double[0];
		if (autoUpdate) repaint();

	}
	public void removeAllHorizontalLines()	{
		horizontalLines = new double[0];
		if (autoUpdate) repaint();
	}
	public void enableVerticalLines(boolean b)	{
		showVerticalLines = b;
		if (autoUpdate) repaint();
	}
	public void enableShades(boolean b)	{
		showShades = b;
		if (autoUpdate) repaint();
	}
	public void enableHorizontalLines(boolean b)	{
		showHorizontalLines = b;
		if (autoUpdate) repaint();
	}
	public void setYAdjustable(boolean b)	{
		automaticYScale = b;
	}
	public void setXAdjustable(boolean b)	{
		automaticXScale = b;
	}
	public void shadeVertical(double start, double end, Color c)	{
		if (start > end) return;
		double[][] verticalShadeCopy = new double[verticalShades.length + 1][2];
		for (int i = 0; i < verticalShades.length; i++)	{
			verticalShadeCopy[i][0] = verticalShades[i][0];
			verticalShadeCopy[i][1] = verticalShades[i][1];
		}
		verticalShades = new double[verticalShadeCopy.length][2];
		for (int i = 0; i < verticalShadeCopy.length - 1; i++)	{
			verticalShades[i][0] = verticalShadeCopy[i][0];
			verticalShades[i][1] = verticalShadeCopy[i][1];
		}
		verticalShades[verticalShades.length - 1][0] = start;
		verticalShades[verticalShades.length - 1][1] = end - start;

		Color[] verticalShadeColorCopy = new Color[verticalShadeColors.length + 1];
		for (int i = 0; i < verticalShadeColors.length; i++)
			verticalShadeColorCopy[i] = verticalShadeColors[i];
		verticalShadeColors = new Color[verticalShadeColorCopy.length];
		for (int i = 0; i < verticalShadeColorCopy.length - 1; i++)
			verticalShadeColors[i] = verticalShadeColorCopy[i];
		verticalShadeColors[verticalShadeColors.length - 1] = c;
		if (autoUpdate)	repaint();
	}
	public void newDataSet()	{
		numberofdatasets++;
		lineon = numberofdatasets - 1;
	}
	public void setDataSet(int lo)	{
		lineon = lo;
	}
	public void renderGraph()	{
		dataGiven = true;
		repaint();
	}
	public void paintComponent(Graphics a)	{
		g = a;
		super.paintComponent(a);
		if (dataGiven || autoUpdate)
			drawGraph();
	}
	public void drawGraph()	{
		if (showShades)	drawShades();
		g.setFont(new Font("Arial", Font.BOLD, 15));
		if (automaticXScale)	getXIncrement();
		if (automaticYScale)	getYIncrement();
		TitleLabel.setText(title);
		label_line(5, 25, 90 * java.lang.Math.PI/180, labely);
		g.drawString(labelx, 25, height - 5);
		drawAxes();
		drawPoints();
		drawLines();
		if (drawVerticalTicks) drawVerticalTicks();
		if (drawMajorVerticalTicks) drawMajorVerticalTicks();
		if (drawMajorVerticalTickLabels) drawMajorVerticalTickLabels();
	}
	public void drawVerticalTicks()	{
		for (double d = XStart; d < XEnd; d += VerticalTickIncrement)
			g.drawLine(getX(d), getY(0), getX(d), getY(maxY/ 50));
	}
	public void drawMajorVerticalTicks()	{
		for (double d = XStart; d < XEnd; d += MajorVerticalTickIncrement)
			g.drawLine(getX(d), getY(0), getX(d), getY(maxY/ 25));
	}
	public void drawMajorVerticalTickLabels()	{
		g.setFont(new Font("Arial", Font.PLAIN, 10));
		String label;
		for (double d = XStart + MajorVerticalTickIncrement; d < XEnd; d += MajorVerticalTickIncrement)	{
			label = convertX(d) + MajorVerticalTickUnit;
			g.drawString(label, getX(d) - label.length() * 3, getY(maxY / -30));
		}
	}
	public int convertX(double d)	{
		return (int)(d * MajorVerticalTickConversionValue + 0.5);
	}
	public void drawShades()	{
		for (int i = 0; i < verticalShades.length; i++)	{
			g.setColor(verticalShadeColors[i]);
			if (automaticYScale)	
				if (maxY != 0)	g.fillRect(getX(verticalShades[i][0]), getY(maxY), getX(verticalShades[i][1]), getY(0) - getY(maxY));
				else 				g.fillRect(getX(verticalShades[i][0]), getY(tempY), getX(verticalShades[i][1]), getY(0) - getY(tempY));
			else 							g.fillRect(getX(verticalShades[i][0]), getY(YEnd), getX(verticalShades[i][1]), getY(0) - getY(YEnd));
		}
		g.setColor(Color.black);
	}
	public void drawLines()	{
		if (showHorizontalLines)
		for (int i = 0; i < horizontalLines.length; i++)
			g.drawLine(getX(0), getY(horizontalLines[i]), getX(maxX), getY(horizontalLines[i]));
		if (showVerticalLines)
		for (int i = 0; i < verticalLines.length; i++)
			g.drawLine(getX(verticalLines[i]), getY(0), getX(verticalLines[i]), getY(maxY));
	}
	public void drawAxes()	{
		int w, h;
		if (width / 20 < 20) w = 20;
		else                        w = width/20;
		if (height / 20 < 20) h = 20;
		else                         h = height / 20;
		g.drawLine(w, h, w, height - h);	//	y
		g.drawLine(w, height - h, width - w, height - h); //x
	}
	public int pointsOfDataSet(int ds)	{
		int numberofpoints = 0;
		for (int i = 0; i < points.length; i++)
			if (points[i][2] == ds) numberofpoints++;
		return numberofpoints;
	}
	public double[][] getPointsOfDataSet(int ds)	{
		double[][] p = new double[pointsOfDataSet(ds)][2];
		int count = 0;
		for (int i = 0; i < points.length; i++)
			if (points[i][2] == ds) {
				p[count][0] = points[i][0];
				p[count][1] = points[i][1];
				count++;
			}
		return p;
	}
	public void drawPoints()	{
		if (!(drawLines))
			for (int a = 0; a < numberofdatasets; a++)
				for (int i = 0; i < pointsOfDataSet(a); i++)	{
					double[][] points = new double[pointsOfDataSet(a)][2];
					points = getPointsOfDataSet(a);
					g.drawLine(getX(points[i][0]), getY(points[i][1]), getX(points[i][0]), getY(points[i][1]));
				}
		else
			for (int a = 0; a < numberofdatasets; a++)
				for (int i = 0; i < pointsOfDataSet(a) - 1; i++)	{
					double[][] points = new double[pointsOfDataSet(a)][2];
					points = getPointsOfDataSet(a);
					//System.out.println(getX(points[i][0]) + " " + getY(points[i][1]));
					g.drawLine(getX(points[i][0]), getY(points[i][1]), getX(points[i+1][0]), getY(points[i+1][1]));
				}
	}
	public int getX(double x)	{
		x -= XStart;
		int w;
		if (width / 20 < 20) w = 20;
		else                        w = width/20;
		return (int)(w + x * XIncrement + 0.5);
	}
	public int getY(double y)	{
		y -= YStart;
		int h;
		if (height / 20 < 20) h = 20;
		else                         h = height / 20;
		return height - (int)(h + y * YIncrement + 0.5);
	}
	public double getXIncrement()	{
		int w;
		if (width / 20 < 20) w = 20;
		else                        w = width/20;
		double linewidth = width - 2 * w;
		XIncrement = linewidth / (maxX - XStart);
		//System.out.println("XIncrement " + XIncrement + " maxX " + maxX + " linewidth " + linewidth);
		return XIncrement;
	}
	public double getYIncrement()	{
		int h;
		if (height / 20 < 20) h = 20;
		else                         h = height / 20;
		double lineheight = height - 2 * h;
		if (maxY == 0)	YIncrement = lineheight / (tempY - YStart);
		else 				YIncrement = lineheight / (maxY - YStart);
		return YIncrement;
	}
	private void label_line(double x, double y, double theta, String label) {

     Graphics2D g2D = (Graphics2D)g;

    // Create a rotation transformation for the font.
    AffineTransform fontAT = new AffineTransform();

    // get the current font
    Font theFont = g2D.getFont();

    // Derive a new font using a rotatation transform
    fontAT.rotate(theta);
    Font theDerivedFont = theFont.deriveFont(fontAT);

    // set the derived font in the Graphics2D context
    g2D.setFont(theDerivedFont);

    // Render a string using the derived font
    g2D.drawString(label, (int)x, (int)y);

    // put the original font back
    g2D.setFont(theFont);
}
}
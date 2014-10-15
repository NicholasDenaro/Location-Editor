package denaro.nick.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WallBrowser extends JPanel implements ActionListener
{
	public WallBrowser()
	{
		super();
		init();
	}
	
	private void init()
	{
		this.setBackground(Color.pink);
		this.setSize(new Dimension(200,400));
		this.setPreferredSize(new Dimension(200,400));
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc=new GridBagConstraints();
		chooser=new JComboBox<String>();
		chooser.setPreferredSize(new Dimension(200,30));
		chooser.addActionListener(this);
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.gridwidth=1;
		gbc.gridheight=1;
		gbc.weightx=1;
		gbc.weighty=0;
		
		this.add(chooser,gbc);
		
		gbc.fill=GridBagConstraints.BOTH;
		gbc.gridx=0;
		gbc.gridy=1;
		gbc.gridwidth=1;
		gbc.gridheight=1;
		gbc.weightx=1;
		gbc.weighty=1;
		
		JPanel p=new JPanel()
		{
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				Graphics2D g2=(Graphics2D)g;
				g.setColor(Color.black);
				g2.fill(getWallShape());
			}
		};
		p.setBackground(Color.green);
		
		this.add(p,gbc);
		
		try
		{
			this.loadShapes(chooser);
		}
		catch(ClassNotFoundException|IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Shape getWallShape()
	{
		return(shape);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==chooser)
		{
			shape=shapelist.get(chooser.getSelectedItem());
			repaint();
		}
	}
	
	private void loadShapes(JComboBox<String> box) throws IOException, ClassNotFoundException
	{
		shapelist=new HashMap<String,Shape>();
		shapelist.put("Rect",new Rectangle(0,0,16,16));
		chooser.addItem("Rect");
		shapelist.put("Circle",new Ellipse2D.Double(0,0,16,16));
		chooser.addItem("Circle");
		
	}
	
	private Shape shape;
	private JComboBox<String> chooser;
	private HashMap<String,Shape> shapelist;
	
}

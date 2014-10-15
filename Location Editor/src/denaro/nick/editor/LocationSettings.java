package denaro.nick.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LocationSettings extends JPanel implements KeyListener
{
	public LocationSettings(Editor editor)
	{
		this.editor=editor;
		init();
		gridWidth=16;
		gridHeight=16;
	}
	
	private void init()
	{
		setBackground(Color.PINK.darker().darker());
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.anchor=GridBagConstraints.LINE_START;
		gbc.insets=new Insets(0,5,10,5);
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.gridwidth=1;
		gbc.gridheight=1;
		gbc.weightx=0;
		gbc.weighty=1;
		gbc.fill=GridBagConstraints.HORIZONTAL;
		
		add(new JLabel("Name:"),gbc);
		
		gbc.gridx=1;
		gbc.weightx=1;
		gbc.gridwidth=2;
		add(nameField=new JTextField(""),gbc);
		nameField.addKeyListener(this);
		nameField.setName("nameField");
		
		gbc.gridy=1;
		
		gbc.gridx=0;
		gbc.weightx=0;
		gbc.gridwidth=1;
		add(new JLabel("Loc width:"),gbc);
		
		gbc.gridx=1;
		gbc.weightx=1;
		gbc.gridwidth=2;
		JTextField locwidth;
		add(locwidth=new JTextField("300"),gbc);
		locwidth.addKeyListener(this);
		locwidth.setName("locwidth");
		
		gbc.gridy=2;
		
		gbc.gridx=0;
		gbc.weightx=0;
		gbc.gridwidth=1;
		add(new JLabel("Loc height:"),gbc);
		
		gbc.gridx=1;
		gbc.weightx=1;
		gbc.gridwidth=2;
		JTextField locheight;
		add(locheight=new JTextField("300"),gbc);
		locheight.addKeyListener(this);
		locheight.setName("locheight");
		
		gbc.gridy=3;
		
		gbc.gridx=0;
		gbc.weightx=0;
		gbc.gridwidth=1;
		add(new JLabel("Grid width:"),gbc);
		
		gbc.gridx=1;
		gbc.weightx=1;
		gbc.gridwidth=2;
		JTextField gridwidth;
		add(gridwidth=new JTextField("16"),gbc);
		gridwidth.addKeyListener(this);
		gridwidth.setName("gridwidth");
		
		gbc.gridy=4;
		
		gbc.gridx=0;
		gbc.weightx=0;
		gbc.gridwidth=1;
		add(new JLabel("Grid height:"),gbc);
		
		gbc.gridx=1;
		gbc.weightx=1;
		gbc.gridwidth=2;
		JTextField gridheight;
		add(gridheight=new JTextField("16"),gbc);
		gridheight.addKeyListener(this);
		gridheight.setName("gridheight");
	}
	
	public int gridWidth()
	{
		return(gridWidth);
	}
	
	public int gridHeight()
	{
		return(gridHeight);
	}
	
	public int locWidth()
	{
		return(this.getWidth());
	}
	
	public int locHeight()
	{
		return(this.getHeight());
	}
	
	public void changeLocationName(String name)
	{
		System.out.println("name: "+name);
		nameField.setText(name);
		repaint();
	}
	
	@Override
	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent event)
	{
		if(event.getKeyCode()==KeyEvent.VK_ENTER)
		{
			if(nameField.hasFocus())
			{
				if(!nameField.getText().isEmpty())
				{
					editor.setLocationName(nameField.getText());
				}
			}
			else if(((JTextField)event.getSource()).getName().equals("locwidth"))
			{
				JTextField lwidth=(JTextField)event.getSource();
				int locwidth=new Integer(lwidth.getText());
				editor.setLocationWidth(locwidth);
			}
			else if(((JTextField)event.getSource()).getName().equals("locheight"))
			{
				JTextField lheight=(JTextField)event.getSource();
				int locheight=new Integer(lheight.getText());
				editor.setLocationHeight(locheight);
			}
			else if(((JTextField)event.getSource()).getName().equals("gridwidth"))
			{
				JTextField gwidth=(JTextField)event.getSource();
				gridWidth=new Integer(gwidth.getText());
				editor.setLocationGridSize(new Dimension(gridWidth,gridHeight));
			}
			else if(((JTextField)event.getSource()).getName().equals("gridheight"))
			{
				JTextField gheight=(JTextField)event.getSource();
				gridHeight=new Integer(gheight.getText());
				editor.setLocationGridSize(new Dimension(gridWidth,gridHeight));
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
	private Editor editor;
	private int gridWidth;
	private int gridHeight;
	private JTextField nameField;
	
}

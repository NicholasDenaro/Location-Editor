package denaro.nick.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TileBrowser extends JPanel implements ActionListener, KeyListener, MouseListener
{
	public TileBrowser()
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
		gbc.gridwidth=2;
		gbc.gridheight=1;
		gbc.weightx=1;
		gbc.weighty=0;
		
		this.add(chooser,gbc);
		
		gbc.gridx=0;
		gbc.gridy=1;
		gbc.gridwidth=1;
		gbc.weightx=0;
		this.add(new JLabel("Depth: "),gbc);
		gbc.gridx=1;
		gbc.gridwidth=1;
		gbc.weightx=1;
		gbc.fill=GridBagConstraints.HORIZONTAL;
		this.add(fieldDepth=new JTextField("0"),gbc);
		fieldDepth.setName("fieldDepth");
		fieldDepth.addKeyListener(this);
		
		gbc.gridx=0;
		gbc.gridy=2;
		gbc.gridwidth=1;
		gbc.weightx=0;
		this.add(new JLabel("Width: "),gbc);
		gbc.gridx=1;
		gbc.gridwidth=1;
		gbc.weightx=1;
		gbc.fill=GridBagConstraints.HORIZONTAL;
		this.add(tileWidth=new JTextField("16"),gbc);
		tileWidth.setName("tilewidth");
		tileWidth.addKeyListener(this);
		
		gbc.gridx=0;
		gbc.gridy=3;
		gbc.gridwidth=1;
		gbc.weightx=0;
		this.add(new JLabel("Width: "),gbc);
		gbc.gridx=1;
		gbc.gridwidth=1;
		gbc.weightx=1;
		gbc.fill=GridBagConstraints.HORIZONTAL;
		this.add(tileHeight=new JTextField("16"),gbc);
		tileHeight.setName("tileheight");
		tileHeight.addKeyListener(this);
		
		gbc.gridx=0;
		gbc.gridy=4;
		gbc.gridwidth=2;
		gbc.weightx=1;
		gbc.weighty=1;
		
		JPanel p=new JPanel()
		{
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				g.drawImage(getTile(),0,0,null);
				g.drawRect(selected.x*tileSize.width,selected.y*tileSize.height,tileSize.width,tileSize.height);
			}
		};
		p.setBackground(Color.green);
		p.addMouseListener(this);
		
		gbc.fill=GridBagConstraints.BOTH;
		
		this.add(p,gbc);
		
		try
		{
			loadTiles(chooser);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	private void loadTiles(JComboBox<String> box) throws IOException, ClassNotFoundException
	{
		JFileChooser chooser=new JFileChooser(".");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.showOpenDialog(null);
		File dir=chooser.getSelectedFile();
		FilenameFilter filter=new FilenameFilter(){

			@Override
			public boolean accept(File dir,String name)
			{
				return(name.endsWith(".png")||name.endsWith(".bmp")||name.endsWith(".jpg"));
			}
			
		};
		if(dir!=null)
		{
			tileList=new HashMap<String,BufferedImage>();
			for(String fname:dir.list(filter))
			{
				File f=new File(fname);
				String name=f.getName().substring(0,f.getName().lastIndexOf('.'));
				box.addItem(name);
				BufferedImage e=ImageIO.read(f);
				tileList.put(name,e);
			}
		}
	}
	
	public int getDepth()
	{
		return(new Integer(fieldDepth.getText()));
	}
	
	public BufferedImage getTile()
	{
		String name=(String)chooser.getSelectedItem();
		if(tileList.get(name)==null)
			return(null);
		BufferedImage img=tileList.get(name);
		return(img);
	}
	
	public BufferedImage getSelectedTile()
	{
		String name=(String)chooser.getSelectedItem();
		if(tileList.get(name)==null)
			return(null);
		BufferedImage img=tileList.get(name);
		return(img.getSubimage(selected.x*tileSize.width,selected.y*tileSize.height,tileSize.width,tileSize.height));
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==chooser)
		{
			BufferedImage img=this.getTile();
			if(img==null)
				return;
			if(tileWidth!=null)
				tileWidth.setText(""+img.getWidth());
			if(tileHeight!=null)
				tileHeight.setText(""+img.getHeight());
			tileSize=new Dimension(img.getWidth(),img.getHeight());
			repaint();
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode()==KeyEvent.VK_ENTER)
		{
			JTextField field=(JTextField)e.getSource();
			if(field.getName().equals("tilewidth"))
			{
				tileSize.width=new Integer(field.getText());
			}
			else if(field.getName().equals("tileheight"))
			{
				tileSize.height=new Integer(field.getText());
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		selected.x=e.getX()/tileSize.width;
		selected.y=e.getY()/tileSize.height;
		JPanel panel=(JPanel)e.getSource();
		panel.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
	private Dimension tileSize=new Dimension(16,16);
	private Point selected=new Point(0,0);
	private JComboBox<String> chooser;
	private HashMap<String,BufferedImage> tileList;
	private JTextField fieldDepth;
	
	private JTextField tileWidth;
	private JTextField tileHeight;
	
}

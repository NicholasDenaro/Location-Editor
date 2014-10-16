package denaro.nick.editor;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import denaro.nick.core.GameMap;
import denaro.nick.core.Location;
import denaro.nick.core.LocationAddEntityException;
import denaro.nick.core.entity.Entity;

public class Editor extends JFrame implements ChangeListener
{
	public Editor()
	{
		super("Location Editor");
		init();
	}
	
	private void init()
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		GridBagConstraints gbc=new GridBagConstraints();
		Container c=this.getContentPane();
		c.setLayout(new GridBagLayout());
		c.setBackground(Color.blue);
		c.setSize(new Dimension(640,480));
		c.setPreferredSize(new Dimension(640,480));
		
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.gridwidth=2;
		gbc.gridheight=1;
		
		c.add(new EditorMenuBar(this),gbc);
		
		gbc.fill=GridBagConstraints.VERTICAL;
		gbc.gridx=0;
		gbc.gridy=1;
		gbc.gridwidth=1;
		gbc.gridheight=1;
		
		settingsTabs=new JTabbedPane();
		
		settingsTabs.add("Entities",entBrowser=new EntityBrowser());
		settingsTabs.add("Tiles",tileBrowser=new TileBrowser());
		settingsTabs.add("Walls",wallBrowser=new WallBrowser());
		settingsTabs.add("Settings",settings=new LocationSettings(this));
		
		c.add(settingsTabs,gbc);
		

		gbc.gridx=1;
		gbc.gridy=1;
		gbc.gridwidth=1;
		gbc.gridheight=1;
		gbc.fill=GridBagConstraints.BOTH;
		gbc.weightx=1;
		gbc.weighty=1;
		
		locationTabs=new JTabbedPane();
		locationTabs.addChangeListener(this);
		settingsTabs.addChangeListener(this);
		
		c.add(locationTabs,gbc);
		
		pack();
	}
	
	public void newLocationPanel(LocationPanel panel)
	{
		settings.changeLocationName(panel.getName());
		
		locationTabs.add(panel.getName(),panel);
	}
	
	public Tab getSettingsTab()
	{
		int index=settingsTabs.getSelectedIndex();
		switch(settingsTabs.getTitleAt(index))
		{
			case "Entities":
				return(Tab.ENTITY);
			case "Tiles":
				return(Tab.TILES);
			case "Walls":
				return(Tab.WALLS);
			case "Settings":
				return(Tab.SETTINGS);
			default:
				return(null);
		}
	}
	
	public Shape getWallShape()
	{
		return(wallBrowser.getWallShape());
	}
	
	public int getTileDepth()
	{
		return(tileBrowser.getDepth());
	}
	
	public BufferedImage getSelectedTile()
	{
		return(tileBrowser.getSelectedTile());
	}
	
	private LocationPanel getCurrentLocationPanel()
	{
		return (LocationPanel)locationTabs.getComponentAt(locationTabs.getSelectedIndex());
	}
	
	public Entity getSelectedEntity()
	{
		return(entBrowser.getSelectedEntity());
	}
	
	public Point getGridSize()
	{
		return(new Point(settings.gridWidth(),settings.gridHeight()));
	}
	
	public Point getLocationSize()
	{
		return(new Point(settings.locWidth(),settings.locHeight()));
	}
	
	public void setLocationGridSize(Dimension size)
	{
		LocationPanel panel=getCurrentLocationPanel();
		panel.redrawGrid(size);
		panel.repaint();
	}
	
	public void setLocationWidth(int width)
	{
		LocationPanel panel=getCurrentLocationPanel();
		panel.changeWidth(width);
	}
	
	public void setLocationHeight(int height)
	{
		LocationPanel panel=getCurrentLocationPanel();
		panel.changeHeight(height);
	}
	
	public void setLocationName(String name)
	{
		locationTabs.getComponentAt(locationTabs.getSelectedIndex()).setName(name);
		locationTabs.setTitleAt(locationTabs.getSelectedIndex(),name);
	}
	
	public void saveLocation() throws FileNotFoundException, IOException
	{
		LocationPanel panel=getCurrentLocationPanel();
		Location loc=panel.location;
		
		for(Entity entity:loc.entityList())
		{
			System.out.println("saving entity.id() "+entity.id());
			if(entity.id()==0)
			{
				//this is the wall! (hopefully)
				entity.mask(new Area(panel.getWalls()));
			}
		}
		JFileChooser chooser=new JFileChooser(".");
		chooser.showOpenDialog(this);
		File file=chooser.getSelectedFile();
		ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(file));
		out.writeObject(loc);
		out.close();
	}
	
	public void loadLocation() throws IOException, ClassNotFoundException
	{
		JFileChooser chooser=new JFileChooser(".");
		chooser.showOpenDialog(this);
		File file=chooser.getSelectedFile();
		
		ObjectInputStream in=new ObjectInputStream(new FileInputStream(file));
		Location loc=(Location)in.readObject();
		
		LocationPanel panel=new LocationPanel(this);
		panel.setName(file.getName().substring(0,file.getName().indexOf('.')));
		panel.location=loc;
		
		for(Entity entity:loc.entityList())
		{
			System.out.println("entity.id() "+entity.id());
			if(entity.id()==0)
			{
				//this is the wall! (hopefully)
				panel.setWalls(new Area(entity.mask()));
			}
		}
		this.newLocationPanel(panel);
		in.close();
	}
	
	@Override
	public void stateChanged(ChangeEvent event)
	{
		if(event.getSource()==locationTabs)
		{
			int index=locationTabs.getSelectedIndex();
			if(index!=locationTabsSelectedIndex)
			{
				settings.changeLocationName(locationTabs.getTitleAt(index));
			}
		}
		this.getCurrentLocationPanel().repaint();
	}
	
	private JTabbedPane locationTabs;
	private JTabbedPane settingsTabs;
	private EntityBrowser entBrowser;
	private TileBrowser tileBrowser;
	private WallBrowser wallBrowser;
	private LocationSettings settings;
	
	private int locationTabsSelectedIndex=-1;
	
	public enum Tab{ENTITY,TILES,WALLS,SETTINGS}
}

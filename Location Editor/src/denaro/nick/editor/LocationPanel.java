package denaro.nick.editor;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.JPanel;

import denaro.nick.core.GameMap;
import denaro.nick.core.Location;
import denaro.nick.core.LocationAddEntityException;
import denaro.nick.core.Sprite;
import denaro.nick.core.entity.Entity;
import denaro.nick.editor.Editor.Tab;

public class LocationPanel extends JPanel implements MouseListener, MouseMotionListener
{
	public LocationPanel(Editor editor)
	{
		this.editor=editor;
		init();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		grid=null;
		
		this.setPreferredSize(new Dimension(300,300));
		
		location=new Location();
	}
	
	private void init()
	{
		this.setBackground(Color.red);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		//super.paintComponent(g);
		g.setColor(this.getBackground());
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		if(location==null)
			return;
		
		if(grid==null)
		{
			redrawGrid(new Dimension(16,16));
		}
		
		HashMap<Integer,BufferedImage> bgs=location.backgroundLayers();
		HashMap<Integer,ArrayList<Entity>> ents=location.entityListByDepth();
		TreeSet<Integer> sortedkeys=new TreeSet<Integer>(bgs.keySet());
		
		sortedkeys.addAll(ents.keySet());
		
		Iterator<Integer> it=sortedkeys.iterator();
		while(it.hasNext())
		{
			int index=it.next();
			if(bgs.containsKey(index))
			{
				System.out.println("bg: "+index);
				g.drawImage(bgs.get(index),0,0,null);
			}
			if(ents.containsKey(index))
			{
				for(Entity entity:ents.get(index))
				{
					g.drawImage(entity.image(),(int)entity.x(),(int)entity.y(),null);
				}
			}
		}
		
		if(editor.getSettingsTab()==Tab.WALLS)
		{
			Graphics2D g2=(Graphics2D)g;
			g.setColor(Color.black);
			g2.fill(walls);
		}
		
		g.drawImage(grid,0,0,null);
	}
	
	public void redrawGrid()
	{
		redrawGrid(gridsize);
	}
	
	public void changeWidth(int width)
	{
		setPreferredSize(new Dimension(width,getHeight()));
		setSize(new Dimension(width,getHeight()));
		setMinimumSize(new Dimension(width,getHeight()));
		Iterator<Integer> it=location.backgroundLayers().keySet().iterator();
		while(it.hasNext())
		{
			int index=it.next();
			BufferedImage img=location.backgroundLayers().get(index);
			BufferedImage newimg=new BufferedImage(width,getHeight(),BufferedImage.TYPE_INT_ARGB);
			Graphics2D g=newimg.createGraphics();
			g.drawImage(img,0,0,null);
			g.dispose();
			location.backgroundLayers().put(index,newimg);
		}
		redrawGrid();
		getParent().doLayout();
	}
	
	public void changeHeight(int height)
	{
		setPreferredSize(new Dimension(getWidth(),height));
		setSize(new Dimension(getWidth(),height));
		setMinimumSize(new Dimension(getWidth(),height));
		Iterator<Integer> it=location.backgroundLayers().keySet().iterator();
		while(it.hasNext())
		{
			int index=it.next();
			BufferedImage img=location.backgroundLayers().get(index);
			BufferedImage newimg=new BufferedImage(getWidth(),height,BufferedImage.TYPE_INT_ARGB);
			Graphics2D g=newimg.createGraphics();
			g.drawImage(img,0,0,null);
			g.dispose();
			location.backgroundLayers().put(index,newimg);
		}
		redrawGrid();
		getParent().doLayout();
	}
	
	public void setWalls(Area area)
	{
		walls=area;
	}
	
	public Area getWalls()
	{
		return(walls);
	}
	
	public void redrawGrid(Dimension size)
	{
		Dimension locSize=this.getSize();
		grid=new BufferedImage(locSize.width,locSize.height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g=grid.createGraphics();
		if(size.width>1)
			for(int i=size.width-1;i<grid.getWidth();i+=size.width)
			{
				g.drawLine(i,0,i,grid.getHeight());
			}
		if(size.height>1)
			for(int a=size.height-1;a<grid.getHeight();a+=size.height)
			{
				g.drawLine(0,a,grid.getWidth(),a);
			}
		g.dispose();
		this.gridsize=size;
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent event)
	{
		if(editor.getSettingsTab()==Tab.ENTITY)
		{
			try
			{
				if(event.getButton()==MouseEvent.BUTTON1)
				{
					Entity e=editor.getSelectedEntity();
					location.addEntityUnprotected(e);
					e.move(event.getX()/gridsize.width*gridsize.width,event.getY()/gridsize.height*gridsize.height);
					repaint();
				}
				else if(event.getButton()==MouseEvent.BUTTON3)
				{
					Entity e=editor.getSelectedEntity();
					location.removeEntityUnprotected(e);
					repaint();
				}
			}
			catch(LocationAddEntityException e)
			{
				e.printStackTrace();
			}
		}
		else if(editor.getSettingsTab()==Tab.TILES)
		{
			
			int depth=editor.getTileDepth();
			HashMap<Integer,BufferedImage> layers=location.backgroundLayers();
			if(!layers.containsKey(depth))
			{
				BufferedImage img=new BufferedImage(this.getWidth(),this.getHeight(),BufferedImage.TYPE_INT_ARGB);
				layers.put(depth,img);
				Graphics2D g=img.createGraphics();
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
				g.setColor(new Color(0,0,0,0));
				g.fillRect(0,0,img.getWidth(),img.getHeight());
				g.dispose();
			}
			BufferedImage img=layers.get(depth);
			Graphics2D g=img.createGraphics();
			if(event.getButton()==MouseEvent.BUTTON1)
			{
				g.drawImage(editor.getSelectedTile(),event.getX()/gridsize.width*gridsize.width,event.getY()/gridsize.height*gridsize.height,null);
			}
			else if(event.getButton()==MouseEvent.BUTTON3)
			{
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
				g.setColor(new Color(0,0,0,0));
				g.fillRect(event.getX()/gridsize.width*gridsize.width,event.getY()/gridsize.height*gridsize.height,gridsize.width,gridsize.height);
			}
			g.dispose();
			
			this.repaint();
		}
		else if(editor.getSettingsTab()==Tab.WALLS)
		{
			Area area=new Area(editor.getWallShape());
			area.transform(AffineTransform.getTranslateInstance(event.getX()/gridsize.width*gridsize.width,event.getY()/gridsize.height*gridsize.height));
			if(event.getModifiersEx()==MouseEvent.BUTTON1_DOWN_MASK)
			{
				walls.add(area);
			}
			else if(event.getModifiersEx()==MouseEvent.BUTTON3_DOWN_MASK)
			{
				walls.subtract(area);
			}
			repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent event)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent event)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseDragged(MouseEvent event)
	{
		if(editor.getSettingsTab()==Tab.TILES)
		{
			int depth=editor.getTileDepth();
			HashMap<Integer,BufferedImage> layers=location.backgroundLayers();
			if(!layers.containsKey(depth))
			{
				BufferedImage img=new BufferedImage(this.getWidth(),this.getHeight(),BufferedImage.TYPE_INT_ARGB);
				layers.put(depth,img);
				Graphics2D g=img.createGraphics();
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
				g.setColor(new Color(0,0,0,0));
				g.fillRect(0,0,img.getWidth(),img.getHeight());
				g.dispose();
				
			}
			BufferedImage img=layers.get(depth);
			Graphics2D g=img.createGraphics();
			if(event.getModifiersEx()==MouseEvent.BUTTON1_DOWN_MASK)
			{
				g.drawImage(editor.getSelectedTile(),event.getX()/gridsize.width*gridsize.width,event.getY()/gridsize.height*gridsize.height,null);
			}
			else if(event.getModifiersEx()==MouseEvent.BUTTON3_DOWN_MASK)
			{
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
				g.setColor(new Color(0,0,0,0));
				g.fillRect(event.getX()/gridsize.width*gridsize.width,event.getY()/gridsize.height*gridsize.height,gridsize.width,gridsize.height);
			}
			g.dispose();
			
			this.repaint();
		}
		else if(editor.getSettingsTab()==Tab.WALLS)
		{
			Area area=new Area(editor.getWallShape());
			area.transform(AffineTransform.getTranslateInstance(event.getX()/gridsize.width*gridsize.width,event.getY()/gridsize.height*gridsize.height));
			if(event.getModifiersEx()==MouseEvent.BUTTON1_DOWN_MASK)
			{
				walls.add(area);
			}
			else if(event.getModifiersEx()==MouseEvent.BUTTON3_DOWN_MASK)
			{
				walls.subtract(area);
			}
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
	public Location location;
	private Area walls=new Area();
	private Editor editor;
	private BufferedImage grid;
	private Dimension gridsize;
}

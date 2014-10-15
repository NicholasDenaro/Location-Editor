package denaro.nick.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import denaro.nick.core.entity.Entity;


public class EntityBrowser extends JPanel
{
	public EntityBrowser()
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
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.gridwidth=1;
		gbc.gridheight=1;
		gbc.weightx=1;
		gbc.weighty=0;
		
		this.add(chooser,gbc);
		
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
				g.drawImage(getSelectedEntity().image(),0,0,null);
			}
		};
		p.setBackground(Color.green);
		
		gbc.fill=GridBagConstraints.BOTH;
		
		this.add(p,gbc);
		
		try
		{
			loadEntities(chooser);
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
	
	private void loadEntities(JComboBox<String> box) throws IOException, ClassNotFoundException
	{
		JFileChooser chooser=new JFileChooser(".");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.showOpenDialog(null);
		File dir=chooser.getSelectedFile();
		FilenameFilter filter=new FilenameFilter(){

			@Override
			public boolean accept(File dir,String name)
			{
				if(name.endsWith(".ent"))
					return(true);
				return(false);
			}
			
		};
		if(dir!=null)
		{
			entityList=new HashMap<String,Entity>();
			for(String fname:dir.list(filter))
			{
				File f=new File(fname);
				String name=f.getName().substring(0,f.getName().lastIndexOf('.'));
				box.addItem(name);
				ObjectInputStream in=new ObjectInputStream(new FileInputStream(f));
				Entity e=(Entity)in.readObject();
				in.close();
				entityList.put(name,e);
			}
		}
	}
	
	public Entity getSelectedEntity()
	{
		String name=(String)chooser.getSelectedItem();
		
		return(entityList.get(name));
	}
	
	private JComboBox<String> chooser;
	private HashMap<String,Entity> entityList;
}

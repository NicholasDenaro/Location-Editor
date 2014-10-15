package denaro.nick.editor;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import denaro.nick.core.GameMap;
import denaro.nick.core.Sprite;
import denaro.nick.core.entity.Entity;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		new Sprite("Player","Player.png",16,16,new Point(0,0));
		
		Entity player=new Entity(Sprite.sprite("Player"),0,0)
		{
			@Override
			public void tick()
			{
				this.moveDelta(1,0);
			}
		};
		Entity wall=new Entity(null,0,0)
		{
			@Override
			public void tick()
			{
				
			}
		};
		
		GameMap<Entity> entityMap=new GameMap<Entity>();
		entityMap.add(wall);
		entityMap.add(player);
		
		saveEntity("player",player);
		saveEntity("wall",wall);
		
		Editor editor=new Editor();
		editor.setVisible(true);
		
	}
	
	protected static void saveEntity(String name,Entity e) throws FileNotFoundException, IOException
	{
		File f=new File(name+".ent");
		ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(f));
		out.writeObject(e);
		out.close();
	}
	
	protected static Entity loadEntity(String fname) throws ClassNotFoundException, IOException
	{
		File f=new File(fname);
		String name=f.getName().substring(0,f.getName().lastIndexOf('.'));
		ObjectInputStream in=new ObjectInputStream(new FileInputStream(f));
		Entity e=(Entity)in.readObject();
		in.close();
		return(e);
	}
	
	private BufferedImage breakingthings;
}

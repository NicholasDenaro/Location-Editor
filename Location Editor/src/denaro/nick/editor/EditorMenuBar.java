package denaro.nick.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

public class EditorMenuBar extends JMenuBar implements ActionListener
{
	public EditorMenuBar(Editor editor)
	{
		this.editor=editor;
		init();
	}
	
	private void init()
	{
		JMenu filemenu=new JMenu("File");
		
		JMenuItem fmnew=new JMenuItem("New");
		filemenu.add(fmnew);
		fmnew.addActionListener(this);
		JMenuItem fmopen=new JMenuItem("Open");
		filemenu.add(fmopen);
		fmopen.addActionListener(this);
		JMenuItem fmsave=new JMenuItem("Save");
		filemenu.add(fmsave);
		fmsave.addActionListener(this);
		
		
		this.add(filemenu);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		if(((JMenuItem)event.getSource()).getText().equals("New"))
		{
			System.out.println("Creating new location...");
			String name=JOptionPane.showInputDialog("Enter the name");
			LocationPanel panel=new LocationPanel(editor);
			panel.setName(name);
			editor.newLocationPanel(panel);
		}
		else if(((JMenuItem)event.getSource()).getText().equals("Open"))
		{
			System.out.println("Opening location...");
			try
			{
				editor.loadLocation();
			}
			catch(ClassNotFoundException|IOException e)
			{
				e.printStackTrace();
			}
		}
		else if(((JMenuItem)event.getSource()).getText().equals("Save"))
		{
			System.out.println("Saving location...");
			try
			{
				editor.saveLocation();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private Editor editor;
}

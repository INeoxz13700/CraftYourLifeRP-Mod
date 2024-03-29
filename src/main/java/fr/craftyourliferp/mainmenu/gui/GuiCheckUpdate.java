package fr.craftyourliferp.mainmenu.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIImage;
import fr.craftyourliferp.guicomponents.UIRect;
import fr.craftyourliferp.guicomponents.GraphicObject;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UIButton.Type;
import fr.craftyourliferp.ingame.gui.GuiBase;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiCheckUpdate extends GuiBase 
{

	private String displayMessage = "";
	
	private String displayedMessage = "";

	
	private boolean checking = false;
	
	private int state;
	
	private int ticks = 0;
	
	private boolean stopUpdate = false;
	
	public static boolean checkSuccess = false;
	
	

	@Override
	public void initGui()
	{
		super.initGui();
		setWindowSize(width, height);
		setWindowPosition(0, 0);
		
		checkUpdate();
	}
	
	@Override
	public void initializeComponent() 
	{ 
		//this.addComponent(new UIImage(new ResourceLocation("craftyourliferp","gui/mainmenu/background_noel.png")).setPosition(0, 0, width, height));
		this.addComponent(new UIImage(new ResourceLocation("craftyourliferp","gui/mainmenu/background.png")).setPosition(0, 0, width, height));
		//this.addComponent(new UIImage(new ResourceLocation("craftyourliferp","gui/mainmenu/background_halloween.png")).setPosition(0, 0, width, height));

		this.addComponent(new UIRect(new UIColor(0,0,0,150)).setPosition(0, 0, width, height));
		this.addComponent(new UIRect(new UIColor(0,0,0,200)).setPosition(0, (height-20)/2, width, 25));
		GraphicObject obj = this.addComponent(new UIButton(Type.SQUARE,new UIRect(new UIColor(0,0,0,200)),"Réessayer",new UIRect(new UIColor(0,0,0,240)),new UIButton.CallBackObject()
		{
			@Override
			public void call()
			{
				checkUpdate();
			}
			
		}).setPosition((width-80)/2, (int)(height*0.8), 80, 20));
		super.initializeComponent();
		
		
	}
	
	private void checkUpdate()
	{
		Thread threadUpdate = new Thread() {
		      public void run() {
		    	  if(checking) return;
		    	  stopUpdate = false;
		    	  checking = true;
		    	  while(true)
		    	  {
		    		  
		    		 if(checkSuccess)
		    		 {
		    			 return;
		    		 }
		    		  
		    		 try {
			    		GraphicObject obj = getComponent(3);
			    		obj.setVisible(false);
			    		getVersion();
			    		displayMessage = "En attente d'une réponse du serveur";
						Thread.sleep(2000);
			    		displayMessage = "Vérification de la version";
						Thread.sleep(2000);
			    		if(!CraftYourLifeRPMod.VERSION.equalsIgnoreCase(CraftYourLifeRPMod.getClientData().version))
			    		{
			    			displayMessage = "Vous avez une version antérieur du jeu, relancez votre lanceur";
			    			stopUpdate = true;
			    			checking = false;
			    			return;
			    		}
			    		displayMessage = "Chargement";
						Thread.sleep(1000);
						checkSuccess = true;
						return;
		    		 } catch (InterruptedException e) {
						e.printStackTrace();
		    		 }
		    	  }
		      }
		};
		threadUpdate.start();
	}
	
	public void drawScreen(int x, int y, float partialTicks)
	{
		super.drawScreen(x, y, partialTicks);
		
		GuiUtils.renderCenteredText(displayedMessage, this.width/2, this.height / 2,1.1f);
	}
	
	@Override
	public void updateScreen()
	{
		if(checkSuccess) Minecraft.getMinecraft().displayGuiScreen(new GuiCustomMainMenu());
		
		if(stopUpdate)
		{
			this.getComponent(3).setVisible(true);
			displayedMessage = displayMessage;
			return;
		}
		
		if(ticks++ % 20 == 0)
		{
			if(state == 0)
			{
				displayedMessage = displayMessage;
			}
			if(state == 1)
			{
				displayedMessage = displayMessage + ".";
			}
			else if(state == 2)
			{
				displayedMessage = displayMessage + "..";
			}
			else if(state == 3)
			{
				displayedMessage = displayMessage + "...";
			}
			
			if(state == 3)
			{
				state = 0;
			}
			state++;
			
		}
	}
	
	public boolean getVersion()
	{
		 URL url;
			try {
				url = new URL(CraftYourLifeRPMod.apiIp +  "/version.txt");
		        BufferedReader in;
				try {
					HttpURLConnection con = (HttpURLConnection)url.openConnection();
					in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			        String inputLine;
			        while ((inputLine = in.readLine()) != null)
			        {
			            CraftYourLifeRPMod.getClientData().version = inputLine;
			        }
			        in.close();
			        con.disconnect();
			        return true;
				} catch (IOException e) {
					e.printStackTrace();
					displayMessage = "Communication avec le serveur impossible";
					stopUpdate = true;
					return false;
				}
				finally {
					
				}
			} catch (MalformedURLException e) {
				displayMessage = "Communication avec le serveur impossible";
				stopUpdate = true;
				e.printStackTrace();
				return false;
			}
	}
	
}

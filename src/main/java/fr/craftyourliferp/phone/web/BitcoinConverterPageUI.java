package fr.craftyourliferp.phone.web;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.guicomponents.GraphicObject;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UIButton.Type;
import fr.craftyourliferp.phone.Tor.WebPage;
import fr.craftyourliferp.phone.web.BMGPageUI.State;
import fr.craftyourliferp.phone.web.page.BitcoinConverterPage;
import fr.craftyourliferp.phone.web.page.WebPageData;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIDropdown;
import fr.craftyourliferp.guicomponents.UIImage;
import fr.craftyourliferp.guicomponents.UIRect;
import fr.craftyourliferp.guicomponents.UIText;
import fr.craftyourliferp.guicomponents.UITextField;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.network.PacketBitcoinPage;
import fr.craftyourliferp.utils.GuiUtils;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class BitcoinConverterPageUI extends WebPageUI {

	private String dropdownPreviousElement;
	
	public BitcoinConverterPageUI() {
		super(new BitcoinConverterPage(Minecraft.getMinecraft().thePlayer), "https://www.bitcoinconvert.onion", "Bitcoin Converter", null);
	}
	
	@Override
	public void initializeComponent()
	{
		this.addComponent(new UIRect(new UIColor(84, 151, 214))).setZIndex(999).setPosition(getWindowPosX(), getWindowPosY(), getWindowWidth(), 20);
		
		

		
		super.initializeComponent();
	}
	
	@Override
	public void back()
	{
		super.back();

		WebPage page = (WebPage)parent;
		page.openPage(null);
	}
	
	public void updateScrollviewContents()
	{
		contentRect.childs.clear();
		super.updateScrollviewContents();
		
		this.addToContainer(new UIText("Convertir : ",new UIColor(80,80,80),0.8f));
		spacing = 10;
		this.addToContainer(new UIDropdown(10,Arrays.asList(new String[] { "Euro -> Bitcoin", "Bitcoin -> Euro" }),new UIColor(84, 151, 214)).setPosition(0, 0, 100, 15));
		this.addToContainer(new UIText("Entrez une valeur : ",new UIColor(80,80,80),0.8f));
		UITextField convertField = (UITextField)this.addToContainer(new UITextField(new UIRect(new UIColor(255,255,255)), 0.8f, UITextField.Type.TEXT).setTextColor(new UIColor(80,80,80)).setPosition(0, 0, 100, 15));
		spacing = 10;
		this.addToContainer(new UIText(" = ",new UIColor(80,80,80),0.8f));
		UITextField resultField = (UITextField)this.addToContainer(new UITextField(new UIRect(new UIColor(255,255,255)), 0.8f, UITextField.Type.NUMBER).setTextColor(new UIColor(80,80,80)).setEnabled(false).setPosition(0, 0, 100, 15));
		spacing = 20;
		this.addToContainer(new UIButton(Type.SQUARE, new UIRect(new UIColor(84, 151, 214)), "Convertir", new UIRect(new UIColor(84, 151, 240)), new UIButton.CallBackObject()
		{
			@Override
			public void call()
			{
				convert();
			}
			
		}).setPosition(0,0,100,15));
		
		spacing = 5;
		this.addToContainer(new UIText("§lCours du bitcoin : ",new UIColor(80,80,80),0.8f));

		this.addToContainer(new BitcoinDetailsContainer().setPosition(0, 0, 100, 30));
		
		BitcoinConverterPage page = (BitcoinConverterPage) getWebPageData();
		
		spacing = 20;
		addToContainer(new UIText("§lVotre solde :",new UIColor(80,80,80),0.8f));
		spacing = 5;
		addToContainer(new UIText("Euro : " + ServerUtils.getMoneyDisplay("%.2f",page.playerData.getPlayer()) ,new UIColor(80,80,80),0.8f));
		addToContainer(new UIText("BTC : " + ServerUtils.getMoneyDisplay("%f",page.playerData.bitcoin),new UIColor(80,80,80),0.8f));

		
		convertField.setContourColor(new UIColor(80,80,80));
		resultField.setContourColor(new UIColor(80,80,80));
	
		 contentRect.setHeight(contentRect.getHeight() + 30);
	}
	
	public void convert()
	{
		 UIDropdown convertDropdown =  (UIDropdown) contentRect.childs.get(1);
		 UITextField textField =  (UITextField) contentRect.childs.get(3);

		float value;
		try
		{
			 value = convertValueToFloat();

		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
		 
		if(convertDropdown.getSelectedElement().equalsIgnoreCase("Euro -> Bitcoin"))
		{
			CraftYourLifeRPMod.packetHandler.sendToServer(PacketBitcoinPage.convertMoneyToBitcoin((byte)0, value));
		}
		else
		{
			CraftYourLifeRPMod.packetHandler.sendToServer(PacketBitcoinPage.convertMoneyToBitcoin((byte)1, value));
		}
	}

	
	private float convertValueToFloat() throws Exception
	{
		 UITextField textField =  (UITextField) contentRect.childs.get(3);
		 return Float.parseFloat(textField.getText().replace(" ", "").replace(",", ".").replace("Euro", "").replace("BTC", ""));
	}
	
	 public GraphicObject addToContainer(GraphicObject object)
	 {
		 if(object == null)  return null;
		
		 
		 contentRect.addChild(object);
		 		 		 
		 object.localPosX = (contentRect.getWidth() - object.getWidth()) / 2;
		 object.localPosY = contentRect.getHeight() + 30;
		 
		 if(object.localPosY + object.getHeight() > contentRect.getHeight())
		 {
			 contentRect.setHeight(contentRect.getHeight() + object.getHeight() + spacing);
		 }
		 
		 return object;
	 }
	
	 
	 @Override
	 public void updateScreen()
	 {
		 super.updateScreen();

		 if(getGuiTicks() % 20 == 0)
		 {
			 UITextField textField = (UITextField) contentRect.childs.get(3);
			 
			 
			 UIDropdown convertDropdown =  (UIDropdown) contentRect.childs.get(1);
			 
			 if(!convertDropdown.getSelectedElement().equalsIgnoreCase(dropdownPreviousElement))
			 {
				 dropdownPreviousElement = convertDropdown.getSelectedElement();
				 textField.setText("");
				 textField = (UITextField) contentRect.childs.get(5);
				 textField.setText("");
			 }
	
			 if(!textField.isFocused() && !textField.getText().isEmpty())
			 {
				 if(convertDropdown.getSelectedElement().equalsIgnoreCase("Euro -> Bitcoin"))
				 {
					if(!textField.getText().contains("Euro"))
					{
						textField.setText(textField.getText() + " Euro");
					}
				 }
				 else
				 {
					if(!textField.getText().contains("BTC"))
					{
						textField.setText(textField.getText() + " BTC");
					}
				 }
			 }
			 
			 if(!textField.getText().isEmpty())
			 {
				 
				 float value;
				 try
				 {
					 value = Float.parseFloat(textField.getText().replace(" Euro", "").replace(" BTC", "").replace(",", "."));
				 }
				 catch(Exception e)
				 {
					 textField = (UITextField) contentRect.childs.get(5);
					 textField.setText("nombre à virgule demandé");
					 return;
				 }
				 
	
				 textField = (UITextField) contentRect.childs.get(5);
				 if(convertDropdown.getSelectedElement().equalsIgnoreCase("Euro -> Bitcoin"))
				 {
					 float bitcoinPrice = ((BitcoinConverterPage)getWebPageData()).getBitcoinInEuro();

					 bitcoinPrice = (float) Math.round(bitcoinPrice*100) / 100;
					 
					 float convertValue = value / bitcoinPrice;
					 
					 textField.setText(ServerUtils.getMoneyDisplay("%f",convertValue).replace(".", ",") + " BTC");

				 }
				 else
				 {
					 float bitcoinPrice = ((BitcoinConverterPage)getWebPageData()).getBitcoinInEuro();

					 bitcoinPrice = (float) Math.round(bitcoinPrice*100) / 100;
					 
					 float convertValue = value * bitcoinPrice;
					 textField.setText(ServerUtils.getMoneyDisplay("%f",convertValue).replace(".", ",")  + " Euro");
				 }

			 }
			 else
			 {
				 textField.setText("");
				 textField = (UITextField) contentRect.childs.get(5);
				 textField.setText("");
			 }
			 
			 UIText euroTxt = (UIText) contentRect.childs.get(10);
			 UIText bictoinTxt = (UIText) contentRect.childs.get(11);
			 
			 BitcoinConverterPage page = (BitcoinConverterPage)getWebPageData();
			 
			 //euroTxt.setText("Euro : " + ServerUtils.getMoneyDisplay("%.2f",(float)page.tempData.serverData.money));
			 euroTxt.setText("Euro : " + ServerUtils.getMoneyDisplay("%.2f",(float)page.playerData.serverData.money));
			 bictoinTxt.setText("BTC : " + ServerUtils.getMoneyDisplay("%f",(float)page.playerData.bitcoin));
		 }
	 }
	 
	
	@Override
	public void drawScreen(int x, int y, float pt)
	{		
		super.drawScreen(x, y, pt);
		
		if(scrollBarVertical.isVisible())
		{
			getComponent(0).setWidth(getWindowWidth()-5);
		}
		else
		{
			getComponent(0).setWidth(getWindowWidth());
		}

		GraphicObject header = getComponent(0);
		GL11.glPushMatrix();
		GL11.glTranslatef(0, 0, 999);
		
		GuiUtils.renderText("Convertisseur bitcoin", header.getX() + 5, header.getY() + 7,GuiUtils.gameColor,0.8f);
		
		GL11.glPopMatrix();
	}
	
	@Override
	public void onPageOpen()
	{
		PacketBitcoinPage page = new PacketBitcoinPage();
		page.action = 0;
		page.pageId = getWebPageData().pageId();
		CraftYourLifeRPMod.packetHandler.sendToServer(page);
	}
	
	public void updatePageData()
	{
		
	}
	
	
	public class BitcoinDetailsContainer extends GraphicObject
	{
		
		
		public UIImage bitcoinIco;
		
		public BitcoinDetailsContainer()
		{
			bitcoinIco = new UIImage(new ResourceLocation("craftyourliferp","gui/phone/bitcoin.png"));
		}
		
		public GraphicObject setPosition(int x, int y, int width, int height)
		{
			super.setPosition(x, y, width, height);
			bitcoinIco.setPosition((x + (width - 15) / 2)-25, y + (height - 15) / 2, 15, 15);
			return this;
		}
		
		
		public void draw(int x, int y)
		{
			super.draw(x, y);
			bitcoinIco.draw(x, y);
			
			String displayText = " = " + ((BitcoinConverterPage)getWebPageData()).getBitcoinInEuro() + " €";
			GuiUtils.renderText(displayText, posX + 32, posY + 11, 5263440);
		}
	}
	

}
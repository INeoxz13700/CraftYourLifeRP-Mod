package fr.craftyourliferp.models; 

import saracalia.svm.util.tmt.ModelRendererTurbo;
import saracalia.svm.util.tmt.ModelVehicle;

public class ModelCrowbar extends ModelVehicle
{
	int textureX = 64;
	int textureY = 64;

	public ModelCrowbar() 
	{
		this.bodyModel = new ModelRendererTurbo[10];
		bodyModel[0] = new ModelRendererTurbo(this, 1, 1, textureX, textureY);
		bodyModel[1] = new ModelRendererTurbo(this, 17, 1, textureX, textureY); 
		bodyModel[2] = new ModelRendererTurbo(this, 33, 1, textureX, textureY); 
		bodyModel[3] = new ModelRendererTurbo(this, 41, 1, textureX, textureY); 
		bodyModel[4] = new ModelRendererTurbo(this, 49, 1, textureX, textureY); 
		bodyModel[5] = new ModelRendererTurbo(this, 57, 1, textureX, textureY); 
		bodyModel[6] = new ModelRendererTurbo(this, 17, 9, textureX, textureY); 
		bodyModel[7] = new ModelRendererTurbo(this, 25, 9, textureX, textureY); 
		bodyModel[8] = new ModelRendererTurbo(this, 33, 9, textureX, textureY); 
		bodyModel[9] = new ModelRendererTurbo(this, 41, 9, textureX, textureY); 

		bodyModel[0].addShapeBox(-1F, -12F, -1F, 2, 25, 2, 0F,-0.25F, 0F, -0.5F, -0.25F, 0F, -0.25F, 0F, 0F, -0.5F, -0.25F, 0F, -0.25F, -0.25F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, -0.25F, 0F, -0.25F); // Box 0
		bodyModel[0].setRotationPoint(0F, 0F, 0F);

		bodyModel[1].addShapeBox(-1F, -13F, -1F, 2, 1, 2, 0F,1.75F, 4.25F, 0.5F, -2.5F, 3.25F, 0.25F, -2.5F, 3.25F, 0.25F, 1.75F, 4.25F, 0.5F, -0.25F, 0F, -0.5F, -0.25F, 0F, -0.25F, 0F, 0F, -0.5F, -0.25F, 0F, -0.25F); // Box 1
		bodyModel[1].setRotationPoint(0F, 0F, 0F);

		bodyModel[2].addShapeBox(-3F, -18F, -1F, 1, 1, 1, 0F,-0.5F, 0.75F, 0.5F, 0.25F, 0.75F, 0.5F, 0.5F, 0.75F, 1.5F, -0.75F, 0.75F, 1.5F, -0.25F, -0.25F, 0.5F, 0.5F, 0.75F, 0.25F, 0.5F, 0.75F, 1.25F, -0.25F, -0.25F, 1.5F); // Box 2
		bodyModel[2].setRotationPoint(0F, 0F, 0F);

		bodyModel[3].addShapeBox(0F, -20F, 0F, 1, 1, 1, 0F,1.75F, 0F, 1.5F, 0.75F, 0.25F, 1.75F, 0.75F, 0.25F, 0.75F, 1.75F, 0F, 0.5F, 1F, -0.5F, 1.75F, 0.75F, -0.5F, 1.75F, 0.75F, -0.5F, 0.75F, 1F, -0.5F, 0.75F); // Box 3
		bodyModel[3].setRotationPoint(0F, 0F, 0F);

		bodyModel[4].addShapeBox(-2F, -20F, 0F, 1, 1, 1, 0F,-0.25F, 0F, 1.5F, 0F, -0.5F, 1.75F, 0F, -0.5F, 0.75F, -0.25F, 0F, 0.5F, 0.5F, 0.25F, 1.5F, -0.75F, 0.25F, 1.5F, -0.5F, 0.25F, 0.5F, 0.25F, 0.25F, 0.5F); // Box 4
		bodyModel[4].setRotationPoint(0F, 0F, 0F);

		bodyModel[5].addShapeBox(2F, -20F, -2F, 1, 1, 1, 0F,0.25F, 0.25F, -0.25F, 2F, 0F, -0.25F, 2F, 0F, 1F, 0.25F, 0.25F, 1F, 0.25F, -0.5F, -0.25F, 1.75F, -0.5F, -0.25F, 1.75F, -0.5F, 0.75F, 0.25F, -0.5F, 1F); // Box 5
		bodyModel[5].setRotationPoint(0F, 0F, 0F);

		bodyModel[6].addShapeBox(2F, -20F, 0F, 1, 1, 1, 0F,0.25F, 0.25F, -0.25F, 2F, 0F, -0.25F, 2F, 0F, 0.75F, 0.25F, 0.25F, 0.75F, 0.25F, -0.5F, -0.25F, 1.75F, -0.5F, -0.25F, 1.75F, -0.5F, 0.75F, 0.25F, -0.5F, 0.75F); // Box 6
		bodyModel[6].setRotationPoint(0F, 0F, 0F);

		bodyModel[7].addShapeBox(-1F, 13F, -1F, 1, 4, 1, 0F,-0.25F, 0F, -0.25F, 1F, 0F, -0.25F, 1F, 0F, 0.75F, -0.25F, 0F, 0.75F, -0.25F, 0F, 0F, 0.25F, 0F, 0F, 0.25F, 0F, 1F, -0.25F, 0F, 1F); // Box 7
		bodyModel[7].setRotationPoint(0F, 0F, 0F);

		bodyModel[8].addShapeBox(-1F, 17F, -1F, 1, 2, 2, 0F,-0.25F, 0F, 0F, 0.25F, 0F, 0F, 0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.5F, 0.5F, 0F, -0.5F, 0.5F, 0F, -0.25F, 0F, 0F); // Box 8
		bodyModel[8].setRotationPoint(0F, 0F, 0F);

		bodyModel[9].addShapeBox(-1F, 19F, -1F, 1, 1, 2, 0F,-0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, -0.25F, 0F, 0F, 0F, 0F, 0F, -1F, 0F, 0F, -1F, 0F, 0F, 0F, 0F, 0F); // Box 9
		bodyModel[9].setRotationPoint(0F, 0F, 0F);
	}
}
package mods.magico13.ExtraIndustrial.core;

import net.minecraftforge.client.MinecraftForgeClient;

public class ExtraIndustrialReference {

	
	/* Strings (including file locations) */
	public static String modTextures = "magico13/ExtraIndustrial";
	
	
	// Item PNGs
	public static String tool0PNG = "/magico13/ExtraIndustrial/sprites/tools/tool_0.png";
	// Block PNGs
	public static String storagePNG = "/magico13/ExtraIndustrial/sprites/storage/storage.png";
	public static String generatorPNG = "/magico13/ExtraIndustrial/sprites/generator/generator.png";
	public static String machinePNG = "/magico13/ExtraIndustrial/sprites/machine/machine.png";
	public static String superconductorPNG = "/magico13/ExtraIndustrial/sprites/cable/superconductor.png";
	// GUIs
	private static String guiBase = "/mods/magico13/ExtraIndustrial/textures/gui/";
	public static String portableCrafterGUIPNG = guiBase + "crafting.png";
	public static String ingotCompactorGUIPNG = guiBase + "ingotCompactorGUI.png";
	public static String industrialCrafterGUIPNG = guiBase + "industrialCrafterGUI.png";
	public static String fusionReactorCoreGUIPNG = guiBase + "fusionGUI.png";
	
	
	/* Integers (including GUI IDs) */
	// GUI IDs
	public static final int craftingGUI = 0;
	public static final int ingotCompactorGUI = 1;
	public static final int industrialCrafterGUI = 2;
	public static final int fusionReactorCoreGUI = 3;
}

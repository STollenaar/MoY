package MoY.tollenaar.stephen.Quests;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import MoY.tollenaar.stephen.MistsOfYsir.MoY;
import MoY.tollenaar.stephen.PlayerInfo.Playerstats;
import MoY.tollenaar.stephen.Travel.HarborWaitLocations;
import MoY.tollenaar.stephen.Travel.TripLocations;

public class Quest {

	protected MoY plugin;

	public Quest(MoY instance) {
		this.plugin = instance;

		ids.put("0", "Air");
		ids.put("1", "Stone");
		ids.put("1:1", "Granite");
		ids.put("1:2", "Polished Granite");
		ids.put("1:3", "Diorite");
		ids.put("1:4", "Polished Diorite");
		ids.put("1:5", "Andesite");
		ids.put("1:6", "Polished Andesite");
		ids.put("2", "Grass Block");
		ids.put("3", "Dirt");
		ids.put("3:1", "Coarse Dirt");
		ids.put("3:2", "Podzol");
		ids.put("4", "Cobblestone");
		ids.put("5", "Oak Wood Planks");
		ids.put("5:1", "Spruce Wood Planks");
		ids.put("5:2", "Birch Wood Planks");
		ids.put("5:3", "Jungle Wood Planks");
		ids.put("5:4", "Acacia Wood Planks");
		ids.put("5:5", "Dark Oak Wood Planks");
		ids.put("6", "Oak Sapling");
		ids.put("6:1", "Spruce Sapling");
		ids.put("6:2", "Birch Sapling");
		ids.put("6:3", "Jungle Sapling");
		ids.put("6:4", "Acacia Sapling");
		ids.put("6:5", "Dark Oak Sapling");
		ids.put("7", "Bedrock");
		ids.put("8", "Water (No Spread)");
		ids.put("9", "Water");
		ids.put("10", "Lava (No Spread)");
		ids.put("11", "Lava");
		ids.put("12", "Sand");
		ids.put("13", "Gravel");
		ids.put("14", "Gold Ore");
		ids.put("15", "Iron Ore");
		ids.put("16", "Coal Ore");
		ids.put("17", "Oak Wood");
		ids.put("17:1", "Spruce Wood");
		ids.put("17:2", "Birch Wood");
		ids.put("17:3", "Jungle Wood");
		ids.put("18", "Oak Leaves");
		ids.put("18:1", "Spruce Leaves");
		ids.put("18:2", "Birch Leaves");
		ids.put("18:3", "Jungle Leaves");
		ids.put("19", "Sponge");
		ids.put("20", "Glass");
		ids.put("21", "Lapis Lazuli Ore");
		ids.put("22", "Lapis Lazuli Block");
		ids.put("23", "Dispenser");
		ids.put("24", "Sandstone");
		ids.put("24:1", "Chiseled Sandstone");
		ids.put("24:2", "Smooth Sandstone");
		ids.put("25", "Note Block");
		ids.put("26", "Bed");
		ids.put("27", "Powered Rail");
		ids.put("28", "Detector Rail");
		ids.put("29", "Sticky Piston");
		ids.put("30", "Web");
		ids.put("31", "Shrub");
		ids.put("31:1", "Grass");
		ids.put("31:2", "Fern");
		ids.put("32", "Dead Bush");
		ids.put("33", "Piston");
		ids.put("34", "Piston (Head)");
		ids.put("35", "Wool");
		ids.put("35:1", "Orange Wool");
		ids.put("35:2", "Magenta Wool");
		ids.put("35:3", "Light Blue Wool");
		ids.put("35:4", "Yellow Wool");
		ids.put("35:5", "Lime Wool");
		ids.put("35:6", "Pink Wool");
		ids.put("35:7", "Gray Wool");
		ids.put("35:8", "Light Gray Wool");
		ids.put("35:9", "Cyan Wool");
		ids.put("35:10", "Purple Wool");
		ids.put("35:11", "Blue Wool");
		ids.put("35:12", "Brown Wool");
		ids.put("35:13", "Green Wool");
		ids.put("35:14", "Red Wool");
		ids.put("35:15", "Black Wool");
		ids.put("37", "Dandelion");
		ids.put("38", "Rose");
		ids.put("39", "Brown Mushroom");
		ids.put("40", "Red Mushroom");
		ids.put("41", "Gold Block");
		ids.put("42", "Iron Block");
		ids.put("43", "Stone Slab (Double)");
		ids.put("43:1", "Sandstone Slab (Double)");
		ids.put("43:2", "Wooden Slab (Double)");
		ids.put("43:3", "Cobblestone Slab (Double)");
		ids.put("43:4", "Brick Slab (Double)");
		ids.put("43:5", "Stone Brick Slab (Double)");
		ids.put("44", "Stone Slab");
		ids.put("44:1", "Sandstone Slab");
		ids.put("44:2", "Wooden Slab");
		ids.put("44:3", "Cobblestone Slab");
		ids.put("44:4", "Brick Slab");
		ids.put("44:5", "Stone Brick Slab");
		ids.put("44:6", "Nether Brick Slab");
		ids.put("44:7", "Quartz Slab");
		ids.put("45", "Brick");
		ids.put("46", "TNT");
		ids.put("47", "Bookcase");
		ids.put("48", "Moss Stone");
		ids.put("49", "Obsidian");
		ids.put("50", "Torch");
		ids.put("51", "Fire");
		ids.put("52", "Mob Spawner");
		ids.put("53", "Oak Wood Stairs");
		ids.put("54", "Chest");
		ids.put("55", "Redstone Wire");
		ids.put("56", "Diamond Ore");
		ids.put("57", "Diamond Block");
		ids.put("58", "Crafting Table");
		ids.put("59", "Wheat (Crop)");
		ids.put("60", "Farmland");
		ids.put("61", "Furnace");
		ids.put("62", "Furnace (Smelting)");
		ids.put("63", "Sign (Block)");
		ids.put("64", "Wood Door (Block)");
		ids.put("65", "Ladder");
		ids.put("66", "Rails");
		ids.put("67", "Stone Stairs");
		ids.put("68", "Sign (Wall Block)");
		ids.put("69", "Lever");
		ids.put("70", "Pressure Plate");
		ids.put("71", "Iron Door (Block)");
		ids.put("72", "Pressure Plate");
		ids.put("73", "Redstone Ore");
		ids.put("74", "Redstone Ore (Glowing)");
		ids.put("75", "Redstone Torch (Off)");
		ids.put("76", "Redstone Torch");
		ids.put("77", "Button");
		ids.put("78", "Snow");
		ids.put("79", "Ice");
		ids.put("80", "Snow Block");
		ids.put("81", "Cactus");
		ids.put("82", "Clay Block");
		ids.put("83", "Sugar Cane (Block)");
		ids.put("84", "Jukebox");
		ids.put("85", "Fence");
		ids.put("86", "Pumpkin");
		ids.put("87", "Netherrack");
		ids.put("88", "Soul Sand");
		ids.put("89", "Glowstone");
		ids.put("90", "Portal");
		ids.put("91", "Jack-O-Lantern");
		ids.put("92", "Cake (Block)");
		ids.put("93", "Redstone Repeater (Block Off)");
		ids.put("94", "Redstone Repeater (Block On)");
		ids.put("95", "Stained Glass");
		ids.put("96", "Trapdoor");
		ids.put("97", "Silverfish Stone");
		ids.put("98", "Stone Bricks");
		ids.put("98:1", "Mossy Stone Bricks");
		ids.put("98:2", "Cracked Stone Bricks");
		ids.put("99", "Brown Mushroom (Block)");
		ids.put("100", "Red Mushroom (Block)");
		ids.put("101", "Iron Bars");
		ids.put("102", "Glass Pane");
		ids.put("103", "Melon (Block)");
		ids.put("104", "Pumpkin Vine");
		ids.put("105", "Melon Vine");
		ids.put("106", "Vines");
		ids.put("107", "Fence Gate");
		ids.put("108", "Brick Stairs");
		ids.put("109", "Stone Brick Stairs");
		ids.put("110", "Mycelium");
		ids.put("111", "Lily Pad");
		ids.put("112", "Nether Brick");
		ids.put("113", "Nether Brick Fence");
		ids.put("114", "Nether Brick Stairs");
		ids.put("115", "Nether Wart");
		ids.put("116", "Enchantment Table");
		ids.put("117", "Brewing Stand (Block)");
		ids.put("118", "Cauldron (Block)");
		ids.put("119", "End Portal");
		ids.put("120", "End Portal Frame");
		ids.put("121", "End Stone");
		ids.put("122", "Dragon Egg");
		ids.put("123", "Redstone Lamp (Inactive)");
		ids.put("124", "Redstone Lamp (Active)");
		ids.put("125", "Double Wood Slab");
		ids.put("126", "Oak Wood Slab");
		ids.put("126:1", "Spruce Wood Slab");
		ids.put("126:2", "Birch Slab");
		ids.put("126:3", "Jungle Slab");
		ids.put("126:4", "Acacia Wood Slab");
		ids.put("126:5", "Dark Oak Wood Slab");
		ids.put("127", "Cocoa Plant");
		ids.put("128", "Sandstone Stairs");
		ids.put("129", "Emerald Ore");
		ids.put("130", "Ender Chest");
		ids.put("131", "Tripwire Hook");
		ids.put("132", "Tripwire");
		ids.put("133", "Emerald Block");
		ids.put("134", "Spruce Wood Stairs");
		ids.put("135", "Birch Wood Stairs");
		ids.put("136", "Jungle Wood Stairs");
		ids.put("137", "Command Block");
		ids.put("138", "Beacon Block");
		ids.put("139", "Cobblestone Wall");
		ids.put("139:1", "Mossy Cobblestone Wall");
		ids.put("140", "Flower Pot");
		ids.put("141", "Carrots");
		ids.put("142", "Potatoes");
		ids.put("143", "Button");
		ids.put("144", "Head");
		ids.put("145", "Anvil");
		ids.put("146", "Trapped Chest");
		ids.put("147", "Weighted Pressure Plate (Light)");
		ids.put("148", "Weighted Pressure Plate (Heavy)");
		ids.put("149", "Redstone Comparator (inactive)");
		ids.put("150", "Redstone Comparator (active)");
		ids.put("151", "Daylight Sensor");
		ids.put("152", "Redstone Block");
		ids.put("153", "Nether Quartz Ore");
		ids.put("154", "Hopper");
		ids.put("155", "Quartz Block");
		ids.put("155:1", "Chiseled Quartz Block");
		ids.put("155:2", "Pillar Quartz Block");
		ids.put("156", "Quartz Stairs");
		ids.put("157", "Activator Rail");
		ids.put("158", "Dropper");
		ids.put("159", "Stained Clay");
		ids.put("160", "Stained Glass Pane");
		ids.put("161:0", "Acacia Leaves");
		ids.put("161:1", "Dark Oak Leaves");
		ids.put("162:0", "Acacia Wood");
		ids.put("162:1", "Dark Oak Wood");
		ids.put("163", "Acacia Wood Stairs");
		ids.put("164", "Dark Oak Wood Stairs");
		ids.put("165", "Slime Block");
		ids.put("166", "Barrier");
		ids.put("167", "Iron Trapdoor");
		ids.put("168", "Prismarine");
		ids.put("169", "Sea Lantern");
		ids.put("170", "Hay Block");
		ids.put("171", "Carpet");
		ids.put("172", "Hardened Clay");
		ids.put("173", "Block of Coal");
		ids.put("174", "Packed Ice");
		ids.put("175:0", "Sunflower");
		ids.put("175:1", "Lilac");
		ids.put("175:2", "Double Tallgrass");
		ids.put("175:3", "Large Fern");
		ids.put("175:4", "Rose Bush");
		ids.put("175:5", "Peony");
		ids.put("256", "Iron Shovel");
		ids.put("257", "Iron Pickaxe");
		ids.put("258", "Iron Axe");
		ids.put("259", "Flint and Steel");
		ids.put("260", "Apple");
		ids.put("261", "Bow");
		ids.put("262", "Arrow");
		ids.put("263", "Coal");
		ids.put("263:1", "Charcoal");
		ids.put("264", "Diamond");
		ids.put("265", "Iron Ingot");
		ids.put("266", "Gold Ingot");
		ids.put("267", "Iron Sword");
		ids.put("268", "Wooden Sword");
		ids.put("269", "Wooden Shovel");
		ids.put("270", "Wooden Pickaxe");
		ids.put("271", "Wooden Axe");
		ids.put("272", "Stone Sword");
		ids.put("273", "Stone Shovel");
		ids.put("274", "Stone Pickaxe");
		ids.put("275", "Stone Axe");
		ids.put("276", "Diamond Sword");
		ids.put("277", "Diamond Shovel");
		ids.put("278", "Diamond Pickaxe");
		ids.put("279", "Diamond Axe");
		ids.put("280", "Stick");
		ids.put("281", "Bowl");
		ids.put("282", "Mushroom Stew");
		ids.put("283", "Gold Sword");
		ids.put("284", "Gold Shovel");
		ids.put("285", "Gold Pickaxe");
		ids.put("286", "Gold Axe");
		ids.put("287", "String");
		ids.put("288", "Feather");
		ids.put("289", "Gunpowder");
		ids.put("290", "Wooden Hoe");
		ids.put("291", "Stone Hoe");
		ids.put("292", "Iron Hoe");
		ids.put("293", "Diamond Hoe");
		ids.put("294", "Gold Hoe");
		ids.put("295", "Seeds");
		ids.put("296", "Wheat");
		ids.put("297", "Bread");
		ids.put("298", "Leather Helmet");
		ids.put("299", "Leather Chestplate");
		ids.put("300", "Leather Leggings");
		ids.put("301", "Leather Boots");
		ids.put("302", "Chainmail Helmet");
		ids.put("303", "Chainmail Chestplate");
		ids.put("304", "Chainmail Leggings");
		ids.put("305", "Chainmail Boots");
		ids.put("306", "Iron Helmet");
		ids.put("307", "Iron Chestplate");
		ids.put("308", "Iron Leggings");
		ids.put("309", "Iron Boots");
		ids.put("310", "Diamond Helmet");
		ids.put("311", "Diamond Chestplate");
		ids.put("312", "Diamond Leggings");
		ids.put("313", "Diamond Boots");
		ids.put("314", "Gold Helmet");
		ids.put("315", "Gold Chestplate");
		ids.put("316", "Gold Leggings");
		ids.put("317", "Gold Boots");
		ids.put("318", "Flint");
		ids.put("319", "Raw Porkchop");
		ids.put("320", "Cooked Porkchop");
		ids.put("321", "Painting");
		ids.put("322", "Gold Apple");
		ids.put("323", "Sign");
		ids.put("324", "Wooden Door");
		ids.put("325", "Bucket");
		ids.put("326", "Water Bucket");
		ids.put("327", "Lava Bucket");
		ids.put("328", "Minecart");
		ids.put("329", "Saddle");
		ids.put("330", "Iron Door");
		ids.put("331", "Redstone");
		ids.put("332", "Snowball");
		ids.put("333", "Boat");
		ids.put("334", "Leather");
		ids.put("335", "Milk Bucket");
		ids.put("336", "Brick");
		ids.put("337", "Clay");
		ids.put("338", "Sugar Cane");
		ids.put("339", "Paper");
		ids.put("340", "Book");
		ids.put("341", "Slime Ball");
		ids.put("342", "Storage Minecart");
		ids.put("343", "Powered Minecart");
		ids.put("344", "Egg");
		ids.put("345", "Compass");
		ids.put("346", "Fishing Rod");
		ids.put("347", "Watch");
		ids.put("348", "Glowstone Dust");
		ids.put("349", "Raw Fish");
		ids.put("349:1", "Raw Salmon");
		ids.put("349:2", "Clownfish");
		ids.put("349:3", "Pufferfish");
		ids.put("350", "Cooked Fish");
		ids.put("350:1", "Cooked Salmon");
		ids.put("351", "Ink Sack [Black Dye]");
		ids.put("351:1", "Rose Red [Red Dye]");
		ids.put("351:2", "Cactus Green [Green Dye]");
		ids.put("351:3", "Cocoa Bean [Brown Dye]");
		ids.put("351:4", "Lapis Lazuli [Blue Dye]");
		ids.put("351:5", "Purple Dye");
		ids.put("351:6", "Cyan Dye");
		ids.put("351:7", "Light Gray Dye");
		ids.put("351:8", "Gray Dye");
		ids.put("351:9", "Pink Dye");
		ids.put("351:10", "Lime Dye");
		ids.put("351:11", "Dandelion Yellow [Yellow Dye]");
		ids.put("351:12", "Light Blue Dye");
		ids.put("351:13", "Magenta Dye");
		ids.put("351:14", "Orange Dye");
		ids.put("351:15", "Bone Meal [White Dye]");
		ids.put("352", "Bone");
		ids.put("353", "Sugar");
		ids.put("354", "Cake");
		ids.put("355", "Bed");
		ids.put("356", "Redstone Repeater");
		ids.put("357", "Cookie");
		ids.put("358", "Map");
		ids.put("359", "Shears");
		ids.put("360", "Melon");
		ids.put("361", "Pumpkin Seeds");
		ids.put("362", "Melon Seeds");
		ids.put("363", "Raw Beef");
		ids.put("364", "Steak");
		ids.put("365", "Raw Chicken");
		ids.put("366", "Roast Chicken");
		ids.put("367", "Rotten Flesh");
		ids.put("368", "Ender Pearl");
		ids.put("369", "Blaze Rod");
		ids.put("370", "Ghast Tear");
		ids.put("371", "Gold Nugget");
		ids.put("372", "Nether Wart");
		ids.put("373", "Water Bottle");
		ids.put("373:16", "Awkward Potion");
		ids.put("373:32", "Thick Potion");
		ids.put("373:64", "Mundane Potion");
		ids.put("373:8193", "Regeneration Potion (0:45)");
		ids.put("373:8194", "Swiftness Potion (3:00)");
		ids.put("373:8195", "Fire Resistance Potion (3:00)");
		ids.put("373:8196", "Poison Potion (0:45)");
		ids.put("373:8197", "Healing Potion");
		ids.put("373:8200", "Weakness Potion (1:30)");
		ids.put("373:8201", "Strength Potion (3:00)");
		ids.put("373:8202", "Slowness Potion (1:30)");
		ids.put("373:8204", "Harming Potion");
		ids.put("373:8225", "Regeneration Potion II (0:22)");
		ids.put("373:8226", "Swiftness Potion II (1:30)");
		ids.put("373:8228", "Poison Potion II (0:22)");
		ids.put("373:8229", "Healing Potion II");
		ids.put("373:8230", "Night Vision Potion (3:00)");
		ids.put("373:8233", "Strength Potion II (1:30)");
		ids.put("373:8236", "Harming Potion II");
		ids.put("373:8237", "Water Breathing Potion (3:00)");
		ids.put("373:8238", "Invisibility Potion (3:00)");
		ids.put("373:8257", "Regeneration Potion (2:00)");
		ids.put("373:8258", "Swiftness Potion (8:00)");
		ids.put("373:8259", "Fire Resistance Potion (8:00)");
		ids.put("373:8260", "Poison Potion (2:00)");
		ids.put("373:8262", "Night Vision Potion (8:00)");
		ids.put("373:8264", "Weakness Potion (4:00)");
		ids.put("373:8265", "Strength Potion (8:00)");
		ids.put("373:8266", "Slowness Potion (4:00)");
		ids.put("373:8269", "Water Breathing Potion (8:00)");
		ids.put("373:8270", "Invisibility Potion (8:00)");
		ids.put("373:16378", "Fire Resistance Splash (2:15)");
		ids.put("373:16385", "Regeneration Splash (0:33)");
		ids.put("373:16386", "Swiftness Splash (2:15)");
		ids.put("373:16388", "Poison Splash (0:33)");
		ids.put("373:16389", "Healing Splash");
		ids.put("373:16392", "Weakness Splash (1:07)");
		ids.put("373:16393", "Strength Splash (2:15)");
		ids.put("373:16394", "Slowness Splash (1:07)");
		ids.put("373:16396", "Harming Splash");
		ids.put("373:16418", "Swiftness Splash II (1:07)");
		ids.put("373:16420", "Poison Splash II (0:16)");
		ids.put("373:16421", "Healing Splash II");
		ids.put("373:16422", "Night Vision Splash (2:15)");
		ids.put("373:16425", "Strength Splash II (1:07)");
		ids.put("373:16428", "Harming Splash II");
		ids.put("373:16429", "Water Breathing Splash (2:15)");
		ids.put("373:16430", "Invisibility Splash (2:15)");
		ids.put("373:16449", "Regeneration Splash (1:30)");
		ids.put("373:16450", "Swiftness Splash (6:00)");
		ids.put("373:16451", "Fire Resistance Splash (6:00)");
		ids.put("373:16452", "Poison Splash (1:30)");
		ids.put("373:16454", "Night Vision Splash (6:00)");
		ids.put("373:16456", "Weakness Splash (3:00)");
		ids.put("373:16457", "Strength Splash (6:00)");
		ids.put("373:16458", "Slowness Splash (3:00)");
		ids.put("373:16461", "Water Breathing Splash (6:00)");
		ids.put("373:16462", "Invisibility Splash (6:00)");
		ids.put("373:16471", "Regeneration Splash II (0:16)");
		ids.put("374", "Glass Bottle");
		ids.put("375", "Spider Eye");
		ids.put("376", "Fermented Spider Eye");
		ids.put("377", "Blaze Powder");
		ids.put("378", "Magma Cream");
		ids.put("379", "Brewing Stand");
		ids.put("380", "Cauldron");
		ids.put("381", "Eye of Ender");
		ids.put("382", "Glistering Melon");
		ids.put("383", "Spawn Egg");
		ids.put("383:50", "Spawn Creeper");
		ids.put("383:51", "Spawn Skeleton");
		ids.put("383:52", "Spawn Spider");
		ids.put("383:54", "Spawn Zombie");
		ids.put("383:55", "Spawn Slime");
		ids.put("383:56", "Spawn Ghast");
		ids.put("383:57", "Spawn Pigman");
		ids.put("383:58", "Spawn Enderman");
		ids.put("383:59", "Spawn Cave Spider");
		ids.put("383:60", "Spawn Silverfish");
		ids.put("383:61", "Spawn Blaze");
		ids.put("383:62", "Spawn Magma Cube");
		ids.put("383:65", "Spawn Bat");
		ids.put("383:66", "Spawn Witch");
		ids.put("383:67", "Spawn Endermite");
		ids.put("383:68", "Spawn Guardian");
		ids.put("383:90", "Spawn Pig");
		ids.put("383:91", "Spawn Sheep");
		ids.put("383:92", "Spawn Cow");
		ids.put("383:93", "Spawn Chicken");
		ids.put("383:94", "Spawn Squid");
		ids.put("383:95", "Spawn Wolf");
		ids.put("383:96", "Spawn Mooshroom");
		ids.put("383:98", "Spawn Ocelot");
		ids.put("383:100", "Spawn Horse");
		ids.put("383:120", "Spawn Villager");
		ids.put("384", "Bottle o' Enchanting");
		ids.put("385", "Fire Charge");
		ids.put("386", "Book and Quill");
		ids.put("387", "Written Book");
		ids.put("388", "Emerald");
		ids.put("389", "Item Frame");
		ids.put("390", "Flower Pot");
		ids.put("391", "Carrot");
		ids.put("392", "Potato");
		ids.put("393", "Baked Potato");
		ids.put("394", "Poisonous Potato");
		ids.put("395", "Empty Map");
		ids.put("396", "Golden Carrot");
		ids.put("397", "Mob Head");
		ids.put("398", "Carrot on a Stick");
		ids.put("399", "Nether Star");
		ids.put("400", "Pumpkin Pie");
		ids.put("401", "Firework Rocket");
		ids.put("402", "Firework Star");
		ids.put("403", "Enchanted Book");
		ids.put("404", "Redstone Comparator");
		ids.put("405", "Nether Brick");
		ids.put("406", "Nether Quartz");
		ids.put("407", "Minecart with TNT");
		ids.put("408", "Minecart with Hopper");
		ids.put("409", "Prismarine Shard");
		ids.put("410", "Prismarine Crystals");
		ids.put("417", "Iron Horse Armor");
		ids.put("418", "Gold Horse Armor");
		ids.put("419", "Diamond Horse Armor");
		ids.put("420", "Lead");
		ids.put("421", "Name Tag");
		ids.put("422", "Minecart with Command Block");
		ids.put("2256", "Music Disk (13)");
		ids.put("2257", "Music Disk (Cat)");
		ids.put("2258", "Music Disk (Blocks)");
		ids.put("2259", "Music Disk (Chirp)");
		ids.put("2260", "Music Disk (Far)");
		ids.put("2261", "Music Disk (Mall)");
		ids.put("2262", "Music Disk (Mellohi)");
		ids.put("2263", "Music Disk (Stal)");
		ids.put("2264", "Music Disk (Strad)");
		ids.put("2265", "Music Disk (Ward)");
		ids.put("2266", "Music Disk (11)");
		ids.put("2267", "Music Disk (wait)");
	}

	protected HashMap<String, String> ids = new HashMap<String, String>();

	private HashMap<Integer, QuestKill> killquests = new HashMap<Integer, QuestKill>();

	private HashMap<Integer, QuestHarvest> harvestquests = new HashMap<Integer, QuestHarvest>();

	private HashMap<Integer, QuestTalkto> talktoquests = new HashMap<Integer, QuestTalkto>();

	private HashMap<Integer, Warps> warplist = new HashMap<Integer, Warps>();

	private HashMap<Integer, TripLocations> triplocations = new HashMap<Integer, TripLocations>();

	private HashMap<Integer, HarborWaitLocations> harborlocation = new HashMap<Integer, HarborWaitLocations>();

	public HashMap<UUID, HashMap<String, HashMap<Integer, Integer>>> progress = new HashMap<UUID, HashMap<String, HashMap<Integer, Integer>>>();

	public int returnProgress(UUID playeruuid, String type, int number) {
		return progress.get(playeruuid).get(type).get(number);
	}

	public void removetrip(int number) {
		triplocations.remove(number);
		plugin.database.deletetrip(number);
	}

	public void removeharbor(int number) {
		harborlocation.remove(number);
		plugin.database.deleteharbor(number);
	}

	public void removekill(int number) {
		killquests.remove(number);
		plugin.database.deletekill(number);
	}

	public void removeharvest(int number) {
		harvestquests.remove(number);
		plugin.database.deleteharvest(number);
	}

	public void removetalkto(int number) {
		talktoquests.remove(number);
		plugin.database.deletetalkto(number);
	}

	public void removewarp(int number) {
		Warps.allwarps.remove(warplist.get(number));
		warplist.remove(number);
		plugin.database.deletewarp(number);
	}

	public int createnewtrip() {
		int i = 0;
		for (; i <= triplocations.size(); i++) {
			if (triplocations.get(i) == null) {
				break;
			}
		}
		TripLocations trip = new TripLocations(i);
		triplocations.put(i, trip);
		return i;
	}

	public int createnewharbor() {
		int i = 0;
		for (; i <= harborlocation.size(); i++) {
			if (harborlocation.get(i) == null) {
				break;
			}
		}
		HarborWaitLocations trip = new HarborWaitLocations(i);
		harborlocation.put(i, trip);
		return i;
	}

	public int createnewkill() {
		int i = 0;
		for (; i <= killquests.size(); i++) {
			if (killquests.get(i) == null) {
				break;
			}
		}

		QuestKill kill = new QuestKill(i);

		killquests.put(i, kill);

		return i;
	}

	public int createnewhar() {
		int i = 0;
		for (; i <= harvestquests.size(); i++) {
			if (harvestquests.get(i) == null) {
				break;
			}
		}

		QuestHarvest kill = new QuestHarvest(i, plugin);

		harvestquests.put(i, kill);

		return i;
	}

	public int createnewtalk() {
		int i = 0;
		for (; i <= talktoquests.size(); i++) {
			if (talktoquests.get(i) == null) {
				break;
			}
		}

		QuestTalkto talk = new QuestTalkto(i);
		talktoquests.put(i, talk);
		return i;
	}

	public int createnewwarp(Location l) {
		int i = 0;
		for (; i <= warplist.size(); i++) {
			if (warplist.get(i) == null) {
				break;
			}
		}
		Warps warp = new Warps(i, l);

		warplist.put(i, warp);
		return i;
	}

	public QuestKill returnkill(int questnumber) {
		if (killquests.get(questnumber) != null) {
			return killquests.get(questnumber);
		} else {
			return null;
		}
	}

	public QuestHarvest returnharvest(int questnumber) {
		if (harvestquests.get(questnumber) != null) {
			return harvestquests.get(questnumber);
		} else {
			return null;
		}
	}

	public QuestTalkto returntalkto(int questnumber) {
		if (talktoquests.get(questnumber) != null) {
			return talktoquests.get(questnumber);
		} else {
			return null;
		}
	}

	public Warps returnwarp(int number) {
		if (warplist.get(number) != null) {
			return warplist.get(number);
		} else {
			return null;
		}
	}

	public TripLocations returntrip(int number) {
		return triplocations.get(number);
	}

	public HarborWaitLocations returnharbor(int number) {
		return harborlocation.get(number);
	}

	public void loadkill(int number, String name, String reward, String delay,
			int minlvl, String message, String prereq, int count, String monster) {
		QuestKill kill = new QuestKill(number);

		kill.setName(name);
		kill.setReward(reward);
		kill.setDelay(delay);
		kill.setMinlvl(minlvl);
		kill.setPrereq(prereq);
		kill.setCount(count);
		kill.setMonster(monster);
		kill.setMessage(message);

		killquests.put(number, kill);
	}

	public void loadharvest(int number, String name, String reward,
			String delay, int minlvl, String message, String prereq, int count,
			String itemid) {
		QuestHarvest kill = new QuestHarvest(number, plugin);

		kill.setName(name);
		kill.setReward(reward);
		kill.setDelay(delay);
		kill.setMinlvl(minlvl);
		kill.setPrereq(prereq);
		kill.setCount(count);
		kill.setMessage(message);
		kill.setItem(itemid);

		harvestquests.put(number, kill);
	}

	public void loadtalkto(int number, String name, int id, String message,
			String delay, int minlvl, String prereq, String reward) {
		QuestTalkto temp = new QuestTalkto(number);
		temp.setName(name);
		temp.setNpcid(id);
		temp.setDelay(delay);
		temp.setMessage(message);
		temp.setMinlvl(minlvl);
		temp.setReward(reward);
		temp.setPrereq(prereq);

		talktoquests.put(number, temp);
	}

	public void loadwarps(int number, String name, Location start,
			double costs, String type) {
		Warps warp = new Warps(number, start);
		warp.setCosts(costs);
		warp.setName(name);
		warp.SetType(type);

		warplist.put(number, warp);
	}

	public void loadhardbor(int id, String type, Location loc) {
		HarborWaitLocations h = new HarborWaitLocations(id);
		h.setLocation(loc);
		h.setType(type);
		harborlocation.put(id, h);
	}

	public void loadtrip(int id, String type, Location loc) {
		TripLocations t = new TripLocations(id);
		t.setLocation(loc);
		t.setType(type);
		triplocations.put(id, t);
	}

	public void allkill(Player player, HashSet<Integer> quests, UUID npcuuid) {
		int rowcount = 0;
		if (quests != null) {
			rowcount = quests.size();
			if (rowcount % 9 == 0) {
				rowcount++;
			}
			while (rowcount % 9 != 0) {
				rowcount++;
			}
		}
		if (rowcount == 0) {
			rowcount = 9;
		}

		Inventory inv = Bukkit.createInventory(null, rowcount, "AllKill");
		if (quests != null) {
			for (Integer i : quests) {
				if (killquests.get(i) != null) {
					QuestKill kill = killquests.get(i);
					ItemStack title = new ItemStack(Material.BOOK);
					{
						ArrayList<String> lore = new ArrayList<String>();
						lore.add(Integer.toString(kill.getQuestnumber()));
						ItemMeta meta = title.getItemMeta();
						meta.setLore(lore);
						meta.setDisplayName(kill.getName());
						title.setItemMeta(meta);
					}
					inv.addItem(title);
				}
			}
		}

		Wool wool = new Wool(DyeColor.LIME);
		ItemStack create = wool.toItemStack();
		{
			ItemMeta meta = create.getItemMeta();
			meta.setDisplayName("Create New");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(npcuuid.toString());
			meta.setLore(lore);
			create.setItemMeta(meta);
		}

		inv.setItem(inv.getSize() - 1, create);
		player.openInventory(inv);

	}

	public void allharvest(Player player, HashSet<Integer> quests, UUID npcuuid) {
		int rowcount = 0;
		if (quests != null) {
			rowcount = quests.size();
			if (rowcount % 9 == 0) {
				rowcount++;
			}
			while (rowcount % 9 != 0) {
				rowcount++;
			}
		}
		if (rowcount == 0) {
			rowcount = 9;
		}

		Inventory inv = Bukkit.createInventory(null, rowcount, "AllHarvest");
		if (quests != null) {
			for (Integer i : quests) {
				if (harvestquests.get(i) != null) {
					QuestHarvest kill = harvestquests.get(i);
					ItemStack title = new ItemStack(Material.BOOK);
					{
						ArrayList<String> lore = new ArrayList<String>();
						lore.add(Integer.toString(kill.getQuestnumber()));
						ItemMeta meta = title.getItemMeta();
						meta.setLore(lore);
						meta.setDisplayName(kill.getName());
						title.setItemMeta(meta);
					}
					inv.addItem(title);
				}
			}
		}

		Wool wool = new Wool(DyeColor.LIME);
		ItemStack create = wool.toItemStack();
		{
			ItemMeta meta = create.getItemMeta();
			meta.setDisplayName("Create New");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(npcuuid.toString());
			meta.setLore(lore);
			create.setItemMeta(meta);
		}

		inv.setItem(inv.getSize() - 1, create);
		player.openInventory(inv);

	}

	public void alltalkto(Player player, HashSet<Integer> quests, UUID npcuuid) {
		int rowcount = 0;
		if (quests != null) {
			rowcount = quests.size();
			if (rowcount % 9 == 0) {
				rowcount++;
			}
			while (rowcount % 9 != 0) {
				rowcount++;
			}
		}
		if (rowcount == 0) {
			rowcount = 9;
		}

		Inventory inv = Bukkit.createInventory(null, rowcount, "AllTalkTo");
		if (quests != null) {
			for (Integer i : quests) {
				if (talktoquests.get(i) != null) {
					QuestTalkto kill = talktoquests.get(i);
					ItemStack title = new ItemStack(Material.BOOK);
					{
						ArrayList<String> lore = new ArrayList<String>();
						lore.add(Integer.toString(kill.getQuestnumber()));
						ItemMeta meta = title.getItemMeta();
						meta.setLore(lore);
						meta.setDisplayName(kill.getName());
						title.setItemMeta(meta);
					}
					inv.addItem(title);
				}
			}
		}

		Wool wool = new Wool(DyeColor.LIME);
		ItemStack create = wool.toItemStack();
		{
			ItemMeta meta = create.getItemMeta();
			meta.setDisplayName("Create New");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(npcuuid.toString());
			meta.setLore(lore);
			create.setItemMeta(meta);
		}

		inv.setItem(inv.getSize() - 1, create);
		player.openInventory(inv);

	}

	public void allwarps(Player player, Integer quests, UUID npcuuid) {
		int rowcount = 9;
		if (quests != null) {
			// rowcount = quests.size();
			if (rowcount % 9 == 0) {
				rowcount++;
			}
			while (rowcount % 9 != 0) {
				rowcount++;
			}
		}
		if (rowcount == 0) {
			rowcount = 9;
		}

		Inventory inv = Bukkit.createInventory(null, rowcount, "AllWarps");
		Warps kill = warplist.get(quests);
		if (kill != null) {
			ItemStack warp = new ItemStack(Material.BOAT);
			{
				ItemMeta meta = warp.getItemMeta();
				ArrayList<String> lore = new ArrayList<String>();
				lore.add(Integer.toString(kill.getWarpid()));
				meta.setLore(lore);
				meta.setDisplayName(kill.getName());
				warp.setItemMeta(meta);
				inv.addItem(warp);
			}
		}
		Wool wool = new Wool(DyeColor.LIME);
		ItemStack create = wool.toItemStack();
		{
			ItemMeta meta = create.getItemMeta();
			meta.setDisplayName("Create New");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(npcuuid.toString());
			meta.setLore(lore);
			create.setItemMeta(meta);
		}

		inv.setItem(inv.getSize() - 1, create);
		player.openInventory(inv);

	}

	public void AddActiveQuest(Player player, int number, String quetstype) {
		if (Playerstats.activequests.get(player.getUniqueId()) == null) {
			HashMap<String, ArrayList<Integer>> types = new HashMap<String, ArrayList<Integer>>();
			ArrayList<Integer> numbers = new ArrayList<Integer>();
			numbers.add(number);
			types.put(quetstype, numbers);
			Playerstats.activequests.put(player.getUniqueId(), types);
		} else {
			HashMap<String, ArrayList<Integer>> types = Playerstats.activequests
					.get(player.getUniqueId());
			if (types.get(quetstype) == null) {
				ArrayList<Integer> numbers = new ArrayList<Integer>();
				numbers.add(number);
				types.put(quetstype, numbers);
				Playerstats.activequests.put(player.getUniqueId(), types);
			} else {
				ArrayList<Integer> numbers = types.get(quetstype);
				numbers.add(number);
				types.put(quetstype, numbers);
				Playerstats.activequests.put(player.getUniqueId(), types);
			}
		}
	}

	public void AddCompletedQuest(Player player, int number, String questtype,
			String npcname) {
		ArrayList<Integer> numbers = Playerstats.activequests.get(
				player.getUniqueId()).get(questtype);
		for (int i = 0; i < numbers.size(); i++) {
			if (numbers.get(i) == number) {
				numbers.remove(i);
				break;
			}
		}
		if (questtype.equals("kill") || questtype.equals("harvest")) {
			progress.get(player.getUniqueId()).get(questtype).remove(number);
		}

		if (Playerstats.completedquests.get(player.getUniqueId()) != null) {
			if (Playerstats.completedquests.get(player.getUniqueId()).get(
					questtype) != null) {
				Playerstats.completedquests.get(player.getUniqueId())
						.get(questtype).add(number);
			} else {
				HashMap<String, ArrayList<Integer>> types = Playerstats.completedquests
						.get(player.getUniqueId());
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp.add(number);
				types.put(questtype, temp);
				Playerstats.completedquests.put(player.getUniqueId(), types);
			}
		} else {
			HashMap<String, ArrayList<Integer>> types = new HashMap<String, ArrayList<Integer>>();
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.add(number);
			types.put(questtype, temp);
			Playerstats.completedquests.put(player.getUniqueId(), types);
		}
		String name = returnname(questtype, number);
		String message = plugin.fw.GetUtilityLine("QuestComplete")
				.replace("%questname%", name).replace("%npc%", npcname);
		player.sendMessage(ChatColor.BLUE + "[" + ChatColor.BLUE + "YQuest"
				+ ChatColor.BLUE + "] " + ChatColor.GRAY + message);

	}

	public void ActiveQuest(Player player) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		if (Playerstats.activequests.get(player.getUniqueId()) != null) {
			for (String type : Playerstats.activequests.get(
					player.getUniqueId()).keySet()) {
				for (int number : Playerstats.activequests.get(
						player.getUniqueId()).get(type)) {
					switch (type) {
					case "kill":
						QuestKill kill = returnkill(number);
						ItemStack kil = new ItemStack(Material.STONE_SWORD);
						{
							ItemMeta meta = kil.getItemMeta();
							meta.setDisplayName(kill.getName());
							ArrayList<String> lore = new ArrayList<String>();
							lore.add("Kill: " + kill.getMonster());
							lore.add("Progress: "
									+ progress.get(player.getUniqueId())
											.get(type).get(number) + "/"
									+ kill.getCount());
							meta.setLore(lore);
							kil.setItemMeta(meta);
							items.add(kil);
						}
						break;
					case "harvest":
						QuestHarvest harvest = returnharvest(number);
						ItemStack har = new ItemStack(Material.STONE_PICKAXE);
						{
							ItemMeta meta = har.getItemMeta();
							meta.setDisplayName(harvest.getName());
							ArrayList<String> lore = new ArrayList<String>();
							lore.add("get: " + ids.get(harvest.getItemId()));
							lore.add("Progress: "
									+ progress.get(player.getUniqueId())
											.get(type).get(number) + "/"
									+ harvest.getCount());
							meta.setLore(lore);
							har.setItemMeta(meta);
							items.add(har);
						}
						break;
					case "talkto":
						QuestTalkto talk = returntalkto(number);
						ItemStack tal = new ItemStack(Material.FEATHER);
						{
							ItemMeta meta = tal.getItemMeta();
							meta.setDisplayName(talk.getName());
							ArrayList<String> lore = new ArrayList<String>();
							NPCRegistry reg = CitizensAPI.getNPCRegistry();
							NPC npc = reg
									.getByUniqueId(plugin.questers.uniquenpcid
											.get(talk.getNpcid()));
							lore.add("Talk to: " + npc.getName());
							meta.setLore(lore);
							tal.setItemMeta(meta);
							items.add(tal);
						}
						break;
					}
				}
			}
		}
		int rowcount = items.size();
		if (rowcount % 9 == 0) {
			rowcount++;
		}
		while (rowcount % 9 != 0) {
			rowcount++;
		}
		if (rowcount == 0) {
			rowcount = 9;
		}
		Inventory inv = Bukkit.createInventory(null, rowcount, "Active Quests");
		for (ItemStack item : items) {
			inv.addItem(item);
		}
		player.openInventory(inv);
	}

	public void CompletedQuest(Player player) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		if (Playerstats.completedquests.get(player.getUniqueId()) != null) {
			for (String type : Playerstats.completedquests.get(
					player.getUniqueId()).keySet()) {
				for (int number : Playerstats.completedquests.get(
						player.getUniqueId()).get(type)) {
					switch (type) {
					case "kill":
						QuestKill kill = returnkill(number);
						ItemStack kil = new ItemStack(Material.IRON_SWORD);
						{
							ItemMeta meta = kil.getItemMeta();
							meta.setDisplayName(kill.getName());
							kil.setItemMeta(meta);
							items.add(kil);
						}
						break;
					case "harvest":
						QuestHarvest harvest = returnharvest(number);
						ItemStack har = new ItemStack(Material.IRON_PICKAXE);
						{
							ItemMeta meta = har.getItemMeta();
							meta.setDisplayName(harvest.getName());
							har.setItemMeta(meta);
							items.add(har);
						}
						break;
					case "talkto":
						QuestTalkto talk = returntalkto(number);
						ItemStack tal = new ItemStack(Material.FEATHER);
						{
							ItemMeta meta = tal.getItemMeta();
							meta.setDisplayName(talk.getName());
							tal.setItemMeta(meta);
							items.add(tal);
						}
						break;
					}
				}
			}
		}
		int rowcount = items.size();
		if (rowcount % 9 == 0) {
			rowcount++;
		}
		while (rowcount % 9 != 0) {
			rowcount++;
		}
		if (rowcount == 0) {
			rowcount = 9;
		}
		Inventory inv = Bukkit.createInventory(null, rowcount,
				"Completed Quests");
		for (ItemStack item : items) {
			inv.addItem(item);
		}
		player.openInventory(inv);
	}

	public void RewardedQuest(Player player) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		if (Playerstats.rewardedlist.get(player.getUniqueId()) != null) {
			for (String type : Playerstats.rewardedlist.get(
					player.getUniqueId()).keySet()) {
				for (int number : Playerstats.rewardedlist
						.get(player.getUniqueId()).get(type).keySet()) {
					long dated = Playerstats.rewardedlist
							.get(player.getUniqueId()).get(type).get(number);
					switch (type) {
					case "kill":
						QuestKill kill = returnkill(number);
						if (kill != null) {
							ItemStack kil = new ItemStack(Material.GOLD_SWORD);
							{
								ItemMeta meta = kil.getItemMeta();
								meta.setDisplayName(kill.getName());
								ArrayList<String> lore = new ArrayList<String>();
								lore.add("Rewarded on " + new Timestamp(dated));
								meta.setLore(lore);
								kil.setItemMeta(meta);
								items.add(kil);
							}
						} else {
							Playerstats.rewardedlist.get(player.getUniqueId())
									.get(type).remove(number);
							plugin.database.deletecomkill(Integer
									.toString(number), player.getUniqueId()
									.toString());
						}
						break;
					case "harvest":
						QuestHarvest harvest = returnharvest(number);
						if (harvest != null) {
							ItemStack har = new ItemStack(Material.GOLD_PICKAXE);
							{
								ItemMeta meta = har.getItemMeta();
								meta.setDisplayName(harvest.getName());
								ArrayList<String> lore = new ArrayList<String>();
								lore.add("Rewarded on " + new Timestamp(dated));
								meta.setLore(lore);
								har.setItemMeta(meta);
								items.add(har);
							}
						} else {
							Playerstats.rewardedlist.get(player.getUniqueId())
									.get(type).remove(number);
							plugin.database.deletecomhar(Integer
									.toString(number), player.getUniqueId()
									.toString());
						}
						break;
					case "talkto":
						QuestTalkto talk = returntalkto(number);
						if (talk != null) {
							ItemStack tal = new ItemStack(Material.FEATHER);
							{
								ItemMeta meta = tal.getItemMeta();
								meta.setDisplayName(talk.getName());
								ArrayList<String> lore = new ArrayList<String>();
								lore.add("Rewarded on " + new Timestamp(dated));
								meta.setLore(lore);
								tal.setItemMeta(meta);
								items.add(tal);
							}
							break;
						} else {
							Playerstats.rewardedlist.get(player.getUniqueId())
									.get(type).remove(number);
							plugin.database.deletecomtalk(Integer
									.toString(number), player.getUniqueId()
									.toString());

						}
					}
				}
			}
		}
		int rowcount = items.size();
		if (rowcount % 9 == 0) {
			rowcount++;
		}
		while (rowcount % 9 != 0) {
			rowcount++;
		}
		if (rowcount == 0) {
			rowcount = 9;
		}
		Inventory inv = Bukkit.createInventory(null, rowcount,
				"Rewarded Quests");
		for (ItemStack item : items) {
			inv.addItem(item);
		}
		player.openInventory(inv);
	}

	public Set<Integer> AllKillId() {
		return killquests.keySet();
	}

	public Set<Integer> AllHarvestId() {
		return harvestquests.keySet();
	}

	public Set<Integer> AllTalkToId() {
		return talktoquests.keySet();
	}

	public Set<Integer> AllWarpId() {
		return warplist.keySet();
	}

	public Set<Integer> AllHarbors() {
		return harborlocation.keySet();
	}

	public Set<Integer> AllTrips() {
		return triplocations.keySet();
	}

	private String returnname(String type, int number) {
		switch (type) {
		case "kill":
			return returnkill(number).getName();
		case "harvest":
			return returnharvest(number).getName();
		case "talkto":
			return returntalkto(number).getName();
		default:
			return "null";
		}
	}
}

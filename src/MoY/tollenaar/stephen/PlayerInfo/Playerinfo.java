package MoY.tollenaar.stephen.PlayerInfo;

import java.util.ArrayList;
import java.util.HashMap;






import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import MoY.tollenaar.stephen.MistsOfYsir.MoY;


public class Playerinfo implements Listener{
	private MoY plugin;
	
	
	private static HashMap<String, String> pinventory = new HashMap<String, String>(); 
	private static HashMap<String, Integer> temperus = new HashMap<String, Integer>();  
	
	private HashSet<Material> tools = new HashSet<Material>();
	private HashSet<Material> axes = new HashSet<Material>();
	private HashSet<Material> pickaxes = new HashSet<Material>();
	
	
	private static HashMap<UUID, Playerstats> players = new HashMap<UUID, Playerstats>();
	
	public void playerstatsinv(Player player){
		Inventory stats = Bukkit.createInventory(null, 18, player.getName() + " stats");
		
		Playerstats p = getplayer(player.getUniqueId());
		
		//general stats
		ItemStack onlineplayer = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		
		{
			SkullMeta skull = (SkullMeta) onlineplayer.getItemMeta();
			skull.setDisplayName(ChatColor.GREEN + player.getName());
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(player.getName());
			skull.setLore(lore);
			skull.setOwner(player.getName());
			ItemMeta temp = onlineplayer.getItemMeta();
			temp.setDisplayName("Name");
			ArrayList<String> t = new ArrayList<String>();
			t.add(player.getName());
			temp.setLore(t);
			onlineplayer.setItemMeta(temp);
		}
		ItemStack lvl = new ItemStack(Material.EXP_BOTTLE);
		{
			ItemMeta temp = lvl.getItemMeta();
			temp.setDisplayName("Level");
			ArrayList<String> t = new ArrayList<String>();
			t.add(Integer.toString(p.getLevel()));
			t.add(Integer.toString(p.getLevelprog()) + "/" + Integer.toString(p.getLevel() * 15));
			temp.setLore(t);
			lvl.setItemMeta(temp);
		}
		ItemStack ability = new ItemStack(Material.NETHER_STAR);
		{
			ItemMeta temp = ability.getItemMeta();
			temp.setDisplayName("Ability points");
			ArrayList<String> t = new ArrayList<String>();
			t.add(Integer.toString(p.getAbility()));
			t.add("With this you can higher your");
			t.add("strength and dex by clicking on it");
			temp.setLore(t);
			ability.setItemMeta(temp);
		}
		ItemStack strenght = new ItemStack(Material.DIAMOND_CHESTPLATE);
		{
			ItemMeta temp = strenght.getItemMeta();
			temp.setDisplayName("Strength");
			ArrayList<String> t = new ArrayList<String>();
			t.add(Integer.toString(p.getStrength()));
			t.add("This allows you to hold");
			t.add("better armor and better weapons");
			temp.setLore(t);
			strenght.setItemMeta(temp);
		}
		ItemStack dex = new ItemStack(Material.BOW);
		{
			ItemMeta temp = dex.getItemMeta();
			temp.setDisplayName("Dex");
			ArrayList<String> t = new ArrayList<String>();
			t.add(Integer.toString(p.getDex()));
			t.add("This allows you to hold better bows");
			temp.setLore(t);
			dex.setItemMeta(temp);
		}
		//skill stats
		ItemStack mining = new ItemStack(Material.DIAMOND_PICKAXE);
		{
			ItemMeta temp = mining.getItemMeta();
			temp.setDisplayName("Mining level");
			ArrayList<String> t = new ArrayList<String>();
			t.add(Integer.toString(p.getMining()));
			t.add(Integer.toString(p.getMiningprog()) + "/" + Integer.toString(p.getMining() * 10));
			t.add("This allows you to mine more valuable ores");
			temp.setLore(t);
			mining.setItemMeta(temp);
		}
		ItemStack wood = new ItemStack(Material.DIAMOND_AXE);
		{
			ItemMeta temp = wood.getItemMeta();
			temp.setDisplayName("Woodcutting level");
			ArrayList<String> t = new ArrayList<String>();
			t.add(Integer.toString(p.getWoodcutting()));
			t.add(Integer.toString(p.getWoodcuttingprog()) + "/" + Integer.toString(p.getWoodcutting() * 10));
			t.add("This allows you to cut bigger trees");
			temp.setLore(t);
			wood.setItemMeta(temp);
		}
		ItemStack fishing = new ItemStack(Material.FISHING_ROD);
		{
			ItemMeta temp = fishing.getItemMeta();
			temp.setDisplayName("Fishing level");
			ArrayList<String> t = new ArrayList<String>();
			t.add(Integer.toString(p.getFishing()));
			t.add(Integer.toString(p.getFishingprog()) + "/" + Integer.toString(p.getFishing() * 10));
			t.add("This gives better fishing results");
			temp.setLore(t);
			fishing.setItemMeta(temp);
		}
		ItemStack smelting = new ItemStack(Material.IRON_INGOT);
		{
			ItemMeta temp = smelting.getItemMeta();
			temp.setDisplayName("Smelting level");
			ArrayList<String> t = new ArrayList<String>();
			t.add(Integer.toString(p.getSmelting()));
			t.add(Integer.toString(p.getSmeltingprog()) + "/" + Integer.toString(p.getSmelting() * 10));
			t.add("This increases the chance of");
			t.add("dropped ignots instead of ores");
			temp.setLore(t);
			smelting.setItemMeta(temp);
		}
		ItemStack cooking = new ItemStack(Material.COOKED_BEEF);
		{
			ItemMeta temp  = cooking.getItemMeta();
			temp.setDisplayName("Cooking level");
			ArrayList<String> t = new ArrayList<String>();
			t.add(Integer.toString(p.getCooking()));
			t.add(Integer.toString(p.getCookingprog()) + "/" + Integer.toString(p.getCooking() * 10));
			t.add("This decreases the chance of burned food");
			temp.setLore(t);
			cooking.setItemMeta(temp);
		}
		
		//filling inv
		//general stats
		stats.setItem(2, onlineplayer);
		stats.setItem(3, lvl);
		stats.setItem(4, ability);
		stats.setItem(5, strenght);
		stats.setItem(6, dex);
		//skill stats
		stats.setItem(11, mining);
		stats.setItem(12, wood);
		stats.setItem(13, fishing);
		stats.setItem(14, smelting);
		stats.setItem(15, cooking);
		pinventory.put(player.getName(), player.getName() + " stats");
		
		player.openInventory(stats);
	}
	
	
	
	@EventHandler(ignoreCancelled = true)
	public void onInventoryclickstatsup(InventoryClickEvent event){
		Player player = (Player) event.getWhoClicked();
		Playerstats p = getplayer(player.getUniqueId());
		if(pinventory.get(player.getName()) != null){
			if(pinventory.get(player.getName()).equals(event.getInventory().getName())){
		
			if(event.getCurrentItem() != null){
				ItemStack item = event.getCurrentItem();
				if(item.getItemMeta() != null){
					if(item.getItemMeta().getDisplayName() != null){
						if(item.getItemMeta().getDisplayName().equals("Strength")){
							if(p.getAbility() >= 1 && p.getStrength() < 90){
								p.setAbility(p.getAbility()-1);;
								p.setStrength(p.getStrength()+1);;
								player.closeInventory();
								playerstatsinv(player);
							}
						}
						if(item.getItemMeta().getDisplayName().equals("Dex")){
							if(p.getAbility() >= 1 && p.getDex()< 50){
								p.setAbility(p.getAbility()-1);;
								p.setDex(p.getDex()+1);
								player.closeInventory();
								playerstatsinv(player);
							}
						}
					}
				}
				player.closeInventory();
				playerstatsinv(player);
				event.setCancelled(true);
			}
			}
		}
	}
		
	
	
	public Playerstats createplayer(UUID player){
		Playerstats p  = new Playerstats(player);
		players.put(player, p);
		return p;
	}
	
	@EventHandler
	public void onplayerjoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		if(getplayer(player.getUniqueId()) == null){
			createplayer(player.getUniqueId());
		}
	}
	
	@EventHandler
	public void onplayeritemhold(PlayerItemHeldEvent event){
		Player player = event.getPlayer();
		Playerstats p = getplayer(player.getUniqueId());
		if(player.getLocation().getWorld().getName().equals("MMOTESTWORLD")){
		ArrayList<ItemStack>pweapons = helditems("weapon", p.getStrength(), "strength");
		ArrayList<ItemStack>pbows = helditems("bow", p.getDex(), "dex");
		
		ArrayList<ItemStack>weapons = helditems("weapon", 90, "strength");
		ArrayList<ItemStack>bows = helditems("bow", 90, "dex");
		Inventory inventory = player.getInventory();
		
		ItemStack item = inventory.getItem(event.getNewSlot());
		if(item == null){
			item = new ItemStack(Material.AIR);
		}
		Material mat = item.getType();
		ArrayList<Material> weapontype = new ArrayList<Material>();
		ArrayList<Material> bowtype = new ArrayList<Material>();
			for(ItemStack weapon : weapons){
				weapontype.add(weapon.getType());
			}
			for(ItemStack bow : bows){
				bowtype.add(bow.getType());
			}
			
			ArrayList<Material> pweapontype = new ArrayList<Material>();
			ArrayList<Material> pbowtype = new ArrayList<Material>();
		
			for(ItemStack pweapon : pweapons){
				pweapontype.add(pweapon.getType());
			}
			for(ItemStack pbow : pbows){
				pbowtype.add(pbow.getType());
			}

			ArrayList<Material> axesskill = heldtools("axes", p.getWoodcutting(), "skill");
			ArrayList<Material> axesstrength = heldtools("axes", p.getStrength(), "strength");
			ArrayList<Material> pickaxesskill = heldtools("pickaxes", p.getMining(), "skill");
			ArrayList<Material> pickaxesstrength = heldtools("pickaxes", p.getStrength(), "strength");
			
			ArrayList<Material> allaxesstrength = heldtools("axes", 50, "strength");
			if(weapontype.contains(item.getType()) || bowtype.contains(item.getType())){
			
			
			
			boolean pass = true;
	if(weapontype.contains(item.getType())){
		
	if(!pweapontype.contains(item.getType())){
		pass = false;
	}

	else
		if(item.containsEnchantment(Enchantment.DAMAGE_ALL)){
			int playerlvl = p.getStrength();
			HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = itemenchat(mat);
			HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
			Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
			int current = 0;
			ArrayList<Integer> itemlvl = new ArrayList<Integer>();
			itemlvl.addAll(sortedtlist.keySet());
			for(int lvl : itemlvl){
				if(playerlvl <= lvl){
					current = lvl;
					break;
				}
			}
			HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
	int enchantlvl = enchant.get(Enchantment.DAMAGE_ALL);
	if(item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) > enchantlvl){
		pass = false;
	}
}

	if(pass == false){
	event.setCancelled(true);
		player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength is to low, it needs to be at least level "+ neededlvl(item, item.getEnchantments(), p.getStrength()) +". Use more ability points on strength to use this weapon.");
	
	}
	}else if(bowtype.contains(item.getType())){
		
	if(!pbowtype.contains(item.getType())){
		pass = false;
	}
		else
			if(item.containsEnchantment(Enchantment.ARROW_DAMAGE)){
				int playerlvl = p.getDex();
				HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = itemenchat(mat);
				HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
				Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
				int current = 0;
				ArrayList<Integer> itemlvl = new ArrayList<Integer>();
				itemlvl.addAll(sortedtlist.keySet());
				for(int lvl : itemlvl){
					if(playerlvl <= lvl){
						current = lvl;
						break;
					}
				}
				HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
		int enchantlvl = enchant.get(Enchantment.ARROW_DAMAGE);
		if(item.getEnchantmentLevel(Enchantment.ARROW_DAMAGE) > enchantlvl){
			pass = false;
		}
}
	
		if(pass == false){
		event.setCancelled(true);
		player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your dex is to low, it needs to be at least level "+ neededlvl(item, item.getEnchantments(), p.getDex()) +". Use more ability points on dex to use this weapon.");
	}
	}
		}else if(tools.contains(item.getType())){
			boolean pass = true;
			if(allaxesstrength.contains(item.getType())){
				if(axesskill.contains(item.getType()) || axesstrength.contains(item.getType())){
				if(item.containsEnchantment(Enchantment.DAMAGE_ALL)){
					int playerlvl = p.getStrength();
					HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = toolsencahtstrength(mat);
					HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
					Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
					int current = 0;
					ArrayList<Integer> itemlvl = new ArrayList<Integer>();
					itemlvl.addAll(sortedtlist.keySet());
					for(int lvl : itemlvl){
						if(playerlvl <= lvl){
							current = lvl;
							break;
						}
					}
					HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
			int enchantlvl = enchant.get(Enchantment.DAMAGE_ALL);
			if(item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) > enchantlvl){
				pass = false;
				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength is to low, it needs to be at least level "+ neededstrength(item, item.getEnchantments(), p.getStrength()) +". Use more ability points on strength to use this weapon.");

			}
				}else if(item.containsEnchantment(Enchantment.DIG_SPEED)){
					int playerlvl = p.getWoodcutting();
					HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = toolsencahtskill(mat);
					HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
					Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
					int current = 0;
					ArrayList<Integer> itemlvl = new ArrayList<Integer>();
					itemlvl.addAll(sortedtlist.keySet());
					for(int lvl : itemlvl){
						if(playerlvl <= lvl){
							current= lvl;
							break;
						}
					}
					HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
			int enchantlvl = enchant.get(Enchantment.DIG_SPEED);
			if(item.getEnchantmentLevel(Enchantment.DIG_SPEED) > enchantlvl){
				pass = false;
				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your woodcutting is to low, it needs to be at least level "+ neededskillvl(item, item.getEnchantments(), p.getWoodcutting()) +".");

			}
				}
				}else{
					pass = false;
					player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength or woodcutting is to low, your strength needs to be at least level "+ neededstrength(item, item.getEnchantments(), p.getStrength()) +" or your woodcutting needs to be at least level " + neededskillvl(item, item.getEnchantments(), p.getWoodcutting()) + ". Use more ability points on strength to use this weapon or chop down some more trees.");

				}
			}else{
				if(pickaxesskill.contains(item.getType()) || pickaxesstrength.contains(item.getType())){
				if(item.containsEnchantment(Enchantment.DAMAGE_ALL)){
					int playerlvl = p.getStrength();
					HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = toolsencahtstrength(mat);
					HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
					Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
					int current = 0;
					ArrayList<Integer> itemlvl = new ArrayList<Integer>();
					itemlvl.addAll(sortedtlist.keySet());
					for(int lvl : itemlvl){
						if(playerlvl <= lvl){
							current = lvl;
							break;
						}
					}
					HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
			int enchantlvl = enchant.get(Enchantment.DAMAGE_ALL);
			if(item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) > enchantlvl){
				pass = false;
				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength is to low, it needs to be at least level "+ neededstrength(item, item.getEnchantments(), p.getStrength()) +". Use more ability points on strength to use this weapon.");

			}
				}else if(item.containsEnchantment(Enchantment.DIG_SPEED)){
					int playerlvl = p.getMining();
					HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = toolsencahtskill(mat);
					HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
					Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
					int current = 0;
					ArrayList<Integer> itemlvl = new ArrayList<Integer>();
					itemlvl.addAll(sortedtlist.keySet());
					for(int lvl : itemlvl){
						if(playerlvl <= lvl){
							current = lvl;
							break;
						}
					}
					HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
			int enchantlvl = enchant.get(Enchantment.DIG_SPEED);
			if(item.getEnchantmentLevel(Enchantment.DIG_SPEED) > enchantlvl){
				pass = false;
				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your mining is to low, it needs to be at least level "+ neededskillvl(item, item.getEnchantments(), p.getMining()) +".");

			}
				}
				}else{
					pass = false;
					player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength or mining is to low, your strength needs to be at least "+ neededstrength(item, item.getEnchantments(), p.getStrength()) +" or your mining needs to be at least level" + neededskillvl(item, item.getEnchantments(), p.getMining()) + ". Use more ability points on strength to use this weapon or mine some more ore.");

				}
			}
				
			if(pass == false){
							event.setCancelled(true);
			}
		}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true)
	public void onplayeritemfrominv(InventoryClickEvent event){
		Player player = (Player) event.getWhoClicked();
		Playerstats p = getplayer(player.getUniqueId());
		if(player.getLocation().getWorld().getName().equals("MMOTESTWORLD")){
		if(event.getAction() != InventoryAction.DROP_ALL_SLOT){
		 
		 
		 ArrayList<ItemStack>pweapons = helditems("weapon", p.getStrength(), "strength");
		ArrayList<ItemStack>parmors = helditems("armor", p.getStrength(), "strength");
		ArrayList<ItemStack>pbows = helditems("bow", p.getDex(), "dex");
		
		ArrayList<ItemStack>weapons = helditems("weapon", 90, "strength");
		ArrayList<ItemStack>armors = helditems("armor", 90, "strength");
		ArrayList<ItemStack>bows = helditems("bow", 90, "dex");
		ItemStack item = event.getCursor();
		Material mat = item.getType();
		ArrayList<Material> weapontype = new ArrayList<Material>();
		ArrayList<Material> bowtype = new ArrayList<Material>();
		ArrayList<Material> armortype = new ArrayList<Material>();
		
		ArrayList<Material> axesskill = heldtools("axes", p.getWoodcutting(), "skill");
		ArrayList<Material> axesstrength = heldtools("axes", p.getStrength(), "strength");
		ArrayList<Material> pickaxesskill = heldtools("pickaxes", p.getMining(), "skill");
		ArrayList<Material> pickaxesstrength = heldtools("pickaxes", p.getStrength(), "strength");
		
		ArrayList<Material> allaxesstrength = heldtools("axes", 50, "strength");
		for(ItemStack weapon : weapons){
			weapontype.add(weapon.getType());
		}
		for(ItemStack bow : bows){
			bowtype.add(bow.getType());
		}
		for(ItemStack armor : armors){
			armortype.add(armor.getType());
		}
		ArrayList<Material> pweapontype = new ArrayList<Material>();
		ArrayList<Material> pbowtype = new ArrayList<Material>();
		ArrayList<Material> parmortype = new ArrayList<Material>();
	
		for(ItemStack pweapon : pweapons){
			pweapontype.add(pweapon.getType());
		}
		for(ItemStack pbow : pbows){
			pbowtype.add(pbow.getType());
		}
		for(ItemStack parmor : parmors){
			parmortype.add(parmor.getType());
		}
		
		
		int press = -1;
		if(temperus.get(player.getName()) != null){
			press = temperus.get(player.getName());
		}else{
			for(int x = 0; x <= player.getInventory().getSize(); x++){
				if((player.getInventory().getItem(x) == null || player.getInventory().getItem(x).getType() == Material.AIR) && player.getInventory().getHeldItemSlot() != x){
					press = x;
					break;
				}
			}
		}
		
	
		
	if(event.getAction() == InventoryAction.PICKUP_ALL || event.getAction() == InventoryAction.PICKUP_HALF){
				if(!event.getSlotType().equals(SlotType.ARMOR)){
		 press = event.getSlot();
		 temperus.put(player.getName(), press);
		}else{
		press = -1;	
		temperus.put(player.getName(), press);
		}
			}
		
		if(event.isShiftClick()){
			item = event.getCurrentItem();
			mat = item.getType();
						if(armortype.contains(item.getType())){
							boolean pass = true;
						if(!parmortype.contains(item.getType())){
							
						pass = false;
					}else
						if(item.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)){
							int playerlvl = p.getStrength();
							HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = itemenchat(mat);
							HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
							Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
							int current = 0;
							ArrayList<Integer> itemlvl = new ArrayList<Integer>();
							itemlvl.addAll(sortedtlist.keySet());
							for(int lvl : itemlvl){
								if(playerlvl <= lvl){
									current = lvl;
									break;
								}
							}
							HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
							int enchantlvl = enchant.get(Enchantment.PROTECTION_ENVIRONMENTAL);
							if(item.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) > enchantlvl){
								pass = false;
							}
					}
						
						if(pass == false){
						event.setCursor(new ItemStack(Material.AIR));
						event.setCancelled(true);
						player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " For safety measures this action is disabled. This is for the fact that your level is to low for this item. It needs to be at least level " + neededlvl(item, item.getEnchantments(), p.getStrength()));
						}
					}
						
						if(bowtype.contains(item.getType()) || weapontype.contains(item.getType())){
				
								boolean pass = true;
						if(weapontype.contains(item.getType())){
							
						if(!pweapontype.contains(item.getType())){
							pass = false;
						}

						else
							if(item.containsEnchantment(Enchantment.DAMAGE_ALL)){
								int playerlvl = p.getStrength();
								HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = itemenchat(mat);
								HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
								Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
								int current = 0;
								ArrayList<Integer> itemlvl = new ArrayList<Integer>();
								itemlvl.addAll(sortedtlist.keySet());
								for(int lvl : itemlvl){
									if(playerlvl <= lvl){
										current = lvl;
										break;
									}
								}
								HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
						int enchantlvl = enchant.get(Enchantment.DAMAGE_ALL);
						if(item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) > enchantlvl){
							pass = false;
						}
				}
					
					if(pass == false){
						event.setCursor(new ItemStack(Material.AIR));
						event.setCancelled(true);
						player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength is to low, it needs to be at least level "+ neededlvl(item, item.getEnchantments(), p.getStrength()) +". Use more ability points on strength to use this weapon.");
						
						}
						}else if(bowtype.contains(item.getType())){
							
						if(!pbowtype.contains(item.getType())){
							pass = false;
						}
							else
								if(item.containsEnchantment(Enchantment.ARROW_DAMAGE)){
									int playerlvl = p.getDex();
									HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = itemenchat(mat);
									HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
									Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
									int current = 0;
									ArrayList<Integer> itemlvl = new ArrayList<Integer>();
									itemlvl.addAll(sortedtlist.keySet());
									for(int lvl : itemlvl){
										if(playerlvl <= lvl){
											current = lvl;
											break;
										}
									}
									HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
							int enchantlvl = enchant.get(Enchantment.ARROW_DAMAGE);
							if(item.getEnchantmentLevel(Enchantment.ARROW_DAMAGE) > enchantlvl){
								pass = false;
							}
					}else if(tools.contains(item.getType())){
						if(allaxesstrength.contains(item.getType())){
							if(axesskill.contains(item.getType()) || axesstrength.contains(item.getType())){
							if(item.containsEnchantment(Enchantment.DAMAGE_ALL)){
								int playerlvl = p.getStrength();
								HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = toolsencahtstrength(mat);
								HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
								Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
								int current = 0;
								ArrayList<Integer> itemlvl = new ArrayList<Integer>();
								itemlvl.addAll(sortedtlist.keySet());
								for(int lvl : itemlvl){
									if(playerlvl <= lvl){
										current = lvl;
										break;
									}
								}
								HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
						int enchantlvl = enchant.get(Enchantment.DAMAGE_ALL);
						if(item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) > enchantlvl){
							pass = false;
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength is to low, it needs to be at least level "+ neededstrength(item, item.getEnchantments(), p.getStrength()) +". Use more ability points on strength to use this weapon.");

						}
							}else if(item.containsEnchantment(Enchantment.DIG_SPEED)){
								int playerlvl = p.getWoodcutting();
								HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = toolsencahtskill(mat);
								HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
								Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
								int current = 0;
								ArrayList<Integer> itemlvl = new ArrayList<Integer>();
								itemlvl.addAll(sortedtlist.keySet());
								for(int lvl : itemlvl){
									if(playerlvl <= lvl){
										current = lvl;
										break;
									}
								}
								HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
						int enchantlvl = enchant.get(Enchantment.DIG_SPEED);
						if(item.getEnchantmentLevel(Enchantment.DIG_SPEED) > enchantlvl){
							pass = false;
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your woodcutting is to low, it needs to be at least level "+ neededskillvl(item, item.getEnchantments(), p.getWoodcutting()) +".");

						}
							}
							}else{
								pass = false;
								player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength or woodcutting is to low, your strength needs to be at least level "+ neededstrength(item, item.getEnchantments(), p.getStrength()) +" or your woodcutting needs to be at least level " + neededskillvl(item, item.getEnchantments(), p.getWoodcutting()) + ". Use more ability points on strength to use this weapon or chop down some more trees.");

							}
						}else{
							if(pickaxesskill.contains(item.getType()) || pickaxesstrength.contains(item.getType())){
							if(item.containsEnchantment(Enchantment.DAMAGE_ALL)){
								int playerlvl = p.getStrength();
								HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = toolsencahtstrength(mat);
								HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
								Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
								int current = 0;
								ArrayList<Integer> itemlvl = new ArrayList<Integer>();
								itemlvl.addAll(sortedtlist.keySet());
								for(int lvl : itemlvl){
									if(playerlvl <= lvl){
										current = lvl;
										break;
									}
								}
								HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
						int enchantlvl = enchant.get(Enchantment.DAMAGE_ALL);
						if(item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) > enchantlvl){
							pass = false;
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength is to low, it needs to be at least level "+ neededstrength(item, item.getEnchantments(), p.getStrength()) +". Use more ability points on strength to use this weapon.");

						}
							}else if(item.containsEnchantment(Enchantment.DIG_SPEED)){
								int playerlvl = p.getMining();
								HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = toolsencahtskill(mat);
								HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
								Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
								int current = 0;
								ArrayList<Integer> itemlvl = new ArrayList<Integer>();
								itemlvl.addAll(sortedtlist.keySet());
								for(int lvl : itemlvl){
									if(playerlvl <= lvl){
										current = lvl;
										break;
									}
								}
								HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
						int enchantlvl = enchant.get(Enchantment.DIG_SPEED);
						if(item.getEnchantmentLevel(Enchantment.DIG_SPEED) > enchantlvl){
							pass = false;
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your mining is to low, it needs to be at least level "+ neededskillvl(item, item.getEnchantments(), p.getMining()) +".");

						}
							}
							}else{
								pass = false;
								player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength or mining is to low, your strength needs to be at least "+ neededstrength(item, item.getEnchantments(), p.getStrength()) +" or your mining needs to be at least level" + neededskillvl(item, item.getEnchantments(), p.getMining()) + ". Use more ability points on strength to use this weapon or mine some more ore.");

							}
						}
							
					}
						if(pass == false){
							event.setCursor(new ItemStack(Material.AIR));
							event.setCancelled(true);
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your dex is to low, it needs to be at least level "+ neededlvl(item, item.getEnchantments(), p.getDex()) +". Use more ability points on dex to use this weapon.");
						}
						}
							
					}
		}
		if(event.getAction() == InventoryAction.PLACE_ALL || event.getAction() == InventoryAction.PLACE_ONE || event.getAction() == InventoryAction.SWAP_WITH_CURSOR){
			
		if(weapontype.contains(item.getType()) || bowtype.contains(item.getType()) || armortype.contains(item.getType())){
			
			
			
			if(armortype.contains(item.getType())){
				boolean pass = true;
				if(event.getSlotType().equals(SlotType.ARMOR)){
					if(!parmortype.contains(item.getType())){
						pass = false;
					}
		
			else
				if(item.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)){
					int playerlvl = p.getStrength();
					HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = itemenchat(mat);
					HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
					Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
					int current = 0;
					ArrayList<Integer> itemlvl = new ArrayList<Integer>();
					itemlvl.addAll(sortedtlist.keySet());
					for(int lvl : itemlvl){
						if(playerlvl <= lvl){
							current = lvl;
							break;
						}
					}
					HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
					int enchantlvl = enchant.get(Enchantment.PROTECTION_ENVIRONMENTAL);
					if(item.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) > enchantlvl){
						pass = false;
					}
			}
				}
				if(pass == false){
					event.setCursor(new ItemStack(Material.AIR));
					event.setCancelled(true);
					if(press != -1){
						player.getInventory().setItem(press, item);
					}else{
						player.getInventory().addItem(item);
					}
					player.updateInventory();
					player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength is to low, it needs to be at least level "+ neededlvl(item, item.getEnchantments(), p.getStrength()) +". Use more ability points on strength to use this weapon.");
				}
			}
			
			
			
			if(bowtype.contains(item.getType()) || weapontype.contains(item.getType())){
	
				if(event.getSlotType().equals(SlotType.QUICKBAR) && event.getSlot() == player.getInventory().getHeldItemSlot()){
					boolean pass = true;
			if(weapontype.contains(item.getType())){
				
			if(!pweapontype.contains(item.getType())){
				pass = false;
			}

			else
				if(item.containsEnchantment(Enchantment.DAMAGE_ALL)){
					int playerlvl = p.getStrength();
					HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = itemenchat(mat);
					HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
					Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
					int current =0;
					ArrayList<Integer> itemlvl = new ArrayList<Integer>();
					itemlvl.addAll(sortedtlist.keySet());
					for(int lvl : itemlvl){
						if(playerlvl <= lvl){
							current = lvl;
							break;
						}
					}
					HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
			int enchantlvl = enchant.get(Enchantment.DAMAGE_ALL);
			if(item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) > enchantlvl){
				pass = false;
			}
	}
		
		if(pass == false){
			event.setCursor(new ItemStack(Material.AIR));
			event.setCancelled(true);
			if(press != -1){
				player.getInventory().setItem(press, item);
			}else{
				player.getInventory().addItem(item);
			}
			player.updateInventory();
			player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength is to low, it needs to be at least level "+ neededlvl(item, item.getEnchantments(), p.getStrength()) +". Use more ability points on strength to use this weapon.");
			
			}
			}else if(bowtype.contains(item.getType())){
				
			if(!pbowtype.contains(item.getType())){
				pass = false;
			}
				else
					if(item.containsEnchantment(Enchantment.ARROW_DAMAGE)){	
						int playerlvl = p.getDex();
						HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = itemenchat(mat);
						HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
						Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
						int current = 0;
						ArrayList<Integer> itemlvl = new ArrayList<Integer>();
						itemlvl.addAll(sortedtlist.keySet());
						for(int lvl : itemlvl){
							if(playerlvl <= lvl){
								current = lvl;
								break;
							}
						}
						HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
				int enchantlvl = enchant.get(Enchantment.ARROW_DAMAGE);
				if(item.getEnchantmentLevel(Enchantment.ARROW_DAMAGE) > enchantlvl){
					pass = false;
				}
		}
			
			if(pass == false){
				event.setCursor(new ItemStack(Material.AIR));
				event.setCancelled(true);
				if(press != -1){
					player.getInventory().setItem(press, item);
				}else{
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your dex is to low, it needs to be at least level "+ neededlvl(item, item.getEnchantments(), p.getDex()) +". Use more ability points on dex to use this weapon.");
			}
			}
				
		}
		}
		}else if(tools.contains(item.getType())){
			boolean pass = true;
			if(allaxesstrength.contains(item.getType())){
				if(axesskill.contains(item.getType()) || axesstrength.contains(item.getType())){
				if(item.containsEnchantment(Enchantment.DAMAGE_ALL)){
					int playerlvl = p.getStrength();
					HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = toolsencahtstrength(mat);
					HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
					Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
					int current = 0;
					ArrayList<Integer> itemlvl = new ArrayList<Integer>();
					itemlvl.addAll(sortedtlist.keySet());
					for(int lvl : itemlvl){
						if(playerlvl <= lvl){
							current = lvl;
							break;
						}
					}
					HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
			int enchantlvl = enchant.get(Enchantment.DAMAGE_ALL);
			if(item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) > enchantlvl){
				pass = false;
				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength is to low, it needs to be at least level "+ neededstrength(item, item.getEnchantments(), p.getStrength()) +". Use more ability points on strength to use this weapon.");

			}
				}else if(item.containsEnchantment(Enchantment.DIG_SPEED)){
					int playerlvl = p.getWoodcutting();
					HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = toolsencahtskill(mat);
					HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
					Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
					int current = 0;
					ArrayList<Integer> itemlvl = new ArrayList<Integer>();
					itemlvl.addAll(sortedtlist.keySet());
					for(int lvl : itemlvl){
						if(playerlvl <= lvl){
							current = lvl;
							break;
						}
					}
					HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
			int enchantlvl = enchant.get(Enchantment.DIG_SPEED);
			if(item.getEnchantmentLevel(Enchantment.DIG_SPEED) > enchantlvl){
				pass = false;
				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your woodcutting is to low, it needs to be at least level "+ neededskillvl(item, item.getEnchantments(), p.getWoodcutting()) +".");

			}
				}
				}else{
					pass = false;
					player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength or woodcutting is to low, your strength needs to be at least level "+ neededstrength(item, item.getEnchantments(), p.getStrength()) +" or your woodcutting needs to be at least level " + neededskillvl(item, item.getEnchantments(), p.getWoodcutting()) + ". Use more ability points on strength to use this weapon or chop down some more trees.");

				}
			}else{
				if(pickaxesskill.contains(item.getType()) || pickaxesstrength.contains(item.getType())){
				if(item.containsEnchantment(Enchantment.DAMAGE_ALL)){
					int playerlvl = p.getStrength();
					HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = toolsencahtstrength(mat);
					HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
					Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
					int current = 0;
					ArrayList<Integer> itemlvl = new ArrayList<Integer>();
					itemlvl.addAll(sortedtlist.keySet());
					for(int lvl : itemlvl){
						if(playerlvl <= lvl){
							current = lvl;
							break;
						}
					}
					HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
			int enchantlvl = enchant.get(Enchantment.DAMAGE_ALL);
			if(item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) > enchantlvl){
				pass = false;
				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength is to low, it needs to be at least level "+ neededstrength(item, item.getEnchantments(), p.getStrength()) +". Use more ability points on strength to use this weapon.");

			}
				}else if(item.containsEnchantment(Enchantment.DIG_SPEED)){
					int playerlvl = p.getMining();
					HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = toolsencahtskill(mat);
					HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
					Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
					int current = 0;
					ArrayList<Integer> itemlvl = new ArrayList<Integer>();
					itemlvl.addAll(sortedtlist.keySet());
					for(int lvl : itemlvl){
						if(playerlvl <= lvl){
							current = lvl;
							break;
						}
					}
					HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
			int enchantlvl = enchant.get(Enchantment.DIG_SPEED);
			if(item.getEnchantmentLevel(Enchantment.DIG_SPEED) > enchantlvl){
				pass = false;
				player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your mining is to low, it needs to be at least level "+ neededskillvl(item, item.getEnchantments(), p.getMining()) +".");

			}
				}
				}else{
					pass = false;
					player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength or mining is to low, your strength needs to be at least "+ neededstrength(item, item.getEnchantments(), p.getStrength()) +" or your mining needs to be at least level" + neededskillvl(item, item.getEnchantments(), p.getMining()) + ". Use more ability points on strength to use this weapon or mine some more ore.");

				}
			}
		
		
				
		if(pass == false){
			event.setCursor(new ItemStack(Material.AIR));
			event.setCancelled(true);
			if(press != -1){
				player.getInventory().setItem(press, item);
			}else{
				player.getInventory().addItem(item);
			}
			player.updateInventory();
			
			}
		}
		}
		}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onplayerrightarmor(PlayerInteractEvent event){
		Player player = event.getPlayer();
		Playerstats p = getplayer(player.getUniqueId());
		if(player.getLocation().getWorld().getName().equals("MMOTESTWORLD")){
		ArrayList<ItemStack> parmors = helditems("armors", p.getStrength(), "strength");
		ArrayList<ItemStack> armors = helditems("armors", 90, "strength");
		ArrayList<Material> armortype = new ArrayList<Material>();
		for(ItemStack armor : armors){
			armortype.add(armor.getType());
		}
		ArrayList<Material> parmortype = new ArrayList<Material>();
		for(ItemStack parmor : parmors){
			parmortype.add(parmor.getType());
		}
		
		ItemStack item = player.getItemInHand();
		Material mat = item.getType();
		if(armortype.contains(item.getType())){
			boolean pass = true;
			if(!parmortype.contains(item.getType())){
				
			pass = false;
		}else
			if(item.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)){
				int playerlvl = p.getStrength();
				HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = itemenchat(mat);
				HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
				Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
				int current = 0;
				ArrayList<Integer> itemlvl = new ArrayList<Integer>();
				itemlvl.addAll(sortedtlist.keySet());
				for(int lvl : itemlvl){
					if(playerlvl <= lvl){
						current = lvl;
						break;
					}
				}
				HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
				int enchantlvl = enchant.get(Enchantment.PROTECTION_ENVIRONMENTAL);
				if(item.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) > enchantlvl){
					pass = false;
				}
		}
			
			if(pass == false){

			event.setCancelled(true);
			player.updateInventory();
			player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength is to low, it needs to be at least level "+ neededlvl(item, item.getEnchantments(), p.getStrength()) +". Use more ability points on strength to use this weapon.");
			}
		}
	}
	}
	
	@EventHandler
	public void onplayerpickup(PlayerPickupItemEvent event){
		 Player player = event.getPlayer();
		 Playerstats p = getplayer(player.getUniqueId());
			if(player.getLocation().getWorld().getName().equals("MMOTESTWORLD")){
			 ArrayList<ItemStack>pweapons = helditems("weapon", p.getStrength(), "strength");
			 ArrayList<ItemStack>pbows = helditems("bow", p.getDex(), "dex");
			
			ArrayList<ItemStack>weapons = helditems("weapon", 90, "strength");
			ArrayList<ItemStack>bows = helditems("bow", 90, "dex");
			
			ArrayList<Material> weapontype = new ArrayList<Material>();
			ArrayList<Material> bowtype = new ArrayList<Material>();
			for(ItemStack weapon : weapons){
				weapontype.add(weapon.getType());
			}
			for(ItemStack bow : bows){
				bowtype.add(bow.getType());
			}
			
			ArrayList<Material> pweapontype = new ArrayList<Material>();
			ArrayList<Material> pbowtype = new ArrayList<Material>();
			
			ArrayList<Material> axesskill = heldtools("axes", p.getWoodcutting(), "skill");
			ArrayList<Material> axesstrength = heldtools("axes", p.getStrength(), "strength");
			ArrayList<Material> pickaxesskill = heldtools("pickaxes", p.getMining(), "skill");
			ArrayList<Material> pickaxesstrength = heldtools("pickaxes", p.getStrength(), "strength");
			
			ArrayList<Material> allaxesstrength = heldtools("axes", 50, "strength");
			for(ItemStack weapon : pweapons){
				pweapontype.add(weapon.getType());
			}
			for(ItemStack bow : pbows){
				pbowtype.add(bow.getType());
			}
			
			 ItemStack item = event.getItem().getItemStack();
			 Material mat = item.getType();
			if(weapontype.contains(item.getType()) || bowtype.contains(item.getType())){
				if(!pweapontype.contains(item.getType()) || !pbowtype.contains(item.getType())){
					if(player.getInventory().getItemInHand().getType() == Material.AIR){
						
					 int slot  = player.getInventory().getHeldItemSlot();
						for(int x = 0; x <= player.getInventory().getSize(); x++){
							if((player.getInventory().getItem(x) == null || player.getInventory().getItem(x).getType() == Material.AIR) && x != slot){
								player.getInventory().setItem(x, item);
								event.getItem().remove();
								event.setCancelled(true);
								break;
							}
						}
					}
				}else if(item.containsEnchantment(Enchantment.ARROW_DAMAGE) || item.containsEnchantment(Enchantment.DAMAGE_ALL)){
					boolean pass = true;
					if(item.containsEnchantment(Enchantment.ARROW_DAMAGE)){
						int playerlvl = p.getDex();
						HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = itemenchat(mat);
						HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
						Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
						int current = 0;
						ArrayList<Integer> itemlvl = new ArrayList<Integer>();
						itemlvl.addAll(sortedtlist.keySet());
						for(int lvl : itemlvl){
							if(playerlvl <= lvl){
								current = lvl;
								break;
							}
						}
						HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
						int enchantlvl = enchant.get(Enchantment.ARROW_DAMAGE);
						if(item.getEnchantmentLevel(Enchantment.ARROW_DAMAGE) > enchantlvl){
							pass = false;
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your dex is to low, it needs to be at least level "+ neededlvl(item, item.getEnchantments(), p.getDex()) +". Use more ability points on dex to use this weapon.");
						}
					}else if(item.containsEnchantment(Enchantment.DAMAGE_ALL)){
						int playerlvl = p.getStrength();
						HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = itemenchat(mat);
						HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
						Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
						int current = 0;
						ArrayList<Integer> itemlvl = new ArrayList<Integer>();
						itemlvl.addAll(sortedtlist.keySet());
						for(int lvl : itemlvl){
							if(playerlvl <= lvl){
								current = lvl;
								break;
							}
						}
						HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
						int enchantlvl = enchant.get(Enchantment.DAMAGE_ALL);
						if(item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) > enchantlvl){
							pass = false;
							player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength is to low, it needs to be at least level "+ neededlvl(item, item.getEnchantments(), p.getStrength()) +". Use more ability points on strength to use this weapon.");

						}
					}
					
					if(pass == false){
						 int slot  = player.getInventory().getHeldItemSlot();
							for(int x = 0; x <= player.getInventory().getSize(); x++){
								if((player.getInventory().getItem(x) == null || player.getInventory().getItem(x).getType() == Material.AIR) && x != slot){
									player.getInventory().setItem(x, item);
									event.getItem().remove();
									event.setCancelled(true);
									break;
								}
							}
					}
				}
			}else if(tools.contains(item.getType())){
				boolean pass = true;
				if(allaxesstrength.contains(item.getType())){
					if(axesskill.contains(item.getType()) || axesstrength.contains(item.getType())){
					if(item.containsEnchantment(Enchantment.DAMAGE_ALL)){
						int playerlvl = p.getStrength();
						HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = toolsencahtstrength(mat);
						HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
						Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
						int current = 0;
						ArrayList<Integer> itemlvl = new ArrayList<Integer>();
						itemlvl.addAll(sortedtlist.keySet());
						for(int lvl : itemlvl){
							if(playerlvl <= lvl){
								current = lvl;
								break;
							}
						}
						HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
				int enchantlvl = enchant.get(Enchantment.DAMAGE_ALL);
				if(item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) > enchantlvl){
					pass = false;
					player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength is to low, it needs to be at least level "+ neededstrength(item, item.getEnchantments(), p.getStrength()) +". Use more ability points on strength to use this weapon.");

				}
					}else if(item.containsEnchantment(Enchantment.DIG_SPEED)){
						int playerlvl = p.getWoodcutting();
						HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = toolsencahtskill(mat);
						HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
						Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
						int current = 0;
						ArrayList<Integer> itemlvl = new ArrayList<Integer>();
						itemlvl.addAll(sortedtlist.keySet());
						for(int lvl : itemlvl){
							if(playerlvl <= lvl){
								current = lvl;
								break;
							}
						}
						HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
				int enchantlvl = enchant.get(Enchantment.DIG_SPEED);
				if(item.getEnchantmentLevel(Enchantment.DIG_SPEED) > enchantlvl){
					pass = false;
					player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your woodcutting is to low, it needs to be at least level "+ neededskillvl(item, item.getEnchantments(), p.getWoodcutting()) +".");

				}
					}
					}else{
						pass = false;
						player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength or woodcutting is to low, your strength needs to be at least level "+ neededstrength(item, item.getEnchantments(), p.getStrength()) +" or your woodcutting needs to be at least level " + neededskillvl(item, item.getEnchantments(), p.getWoodcutting()) + ". Use more ability points on strength to use this weapon or chop down some more trees.");

					}
				}else{
					if(pickaxesskill.contains(item.getType()) || pickaxesstrength.contains(item.getType())){
					if(item.containsEnchantment(Enchantment.DAMAGE_ALL)){
						int playerlvl = p.getStrength();
						HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = toolsencahtstrength(mat);
						HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
						Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
						int current = 0;
						ArrayList<Integer> itemlvl = new ArrayList<Integer>();
						itemlvl.addAll(sortedtlist.keySet());
						for(int lvl : itemlvl){
							if(playerlvl <= lvl){
								current = lvl;
								break;
							}
						}
						HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
				int enchantlvl = enchant.get(Enchantment.DAMAGE_ALL);
				if(item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) > enchantlvl){
					pass = false;
					player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength is to low, it needs to be at least level "+ neededstrength(item, item.getEnchantments(), p.getStrength()) +". Use more ability points on strength to use this weapon.");

				}
					}else if(item.containsEnchantment(Enchantment.DIG_SPEED)){
						int playerlvl = p.getMining();
						HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = toolsencahtskill(mat);
						HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
						Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
						int current = 0;
						ArrayList<Integer> itemlvl = new ArrayList<Integer>();
						itemlvl.addAll(sortedtlist.keySet());
						for(int lvl : itemlvl){
							if(playerlvl <= lvl){
								current = lvl;
								break;
							}
						}
						HashMap<Enchantment, Integer> enchant = iteminfo.get(current);
				int enchantlvl = enchant.get(Enchantment.DIG_SPEED);
				if(item.getEnchantmentLevel(Enchantment.DIG_SPEED) > enchantlvl){
					pass = false;
					player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your mining is to low, it needs to be at least level "+ neededskillvl(item, item.getEnchantments(), p.getMining()) +".");

				}
					}
					}else{
						pass = false;
						player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YSkill" + ChatColor.DARK_PURPLE + "]" + ChatColor.AQUA + " Sorry your strength or mining is to low, your strength needs to be at least "+ neededstrength(item, item.getEnchantments(), p.getStrength()) +" or your mining needs to be at least level" + neededskillvl(item, item.getEnchantments(), p.getMining()) + ". Use more ability points on strength to use this weapon or mine some more ore.");

					}
				}
					
				if(pass == false){
					 int slot  = player.getInventory().getHeldItemSlot();
						for(int x = 0; x <= player.getInventory().getSize(); x++){
							if((player.getInventory().getItem(x) == null || player.getInventory().getItem(x).getType() == Material.AIR) && x != slot){
								player.getInventory().setItem(x, item);
								event.getItem().remove();
								event.setCancelled(true);
								break;
							}
						}
				}
			}
			}
		}
	
	@EventHandler(ignoreCancelled = true)
	public void antianvil(InventoryClickEvent event){
		InventoryView view = event.getView();
		int rawslot = event.getRawSlot();
		if(rawslot == view.convertSlot(rawslot)){
			if(rawslot == 2){
				ItemStack item = event.getCurrentItem();
				if(item.getType() == Material.PUMPKIN_SEEDS){
				if(item != null){
					ItemMeta meta = item.getItemMeta();
					if(meta != null){
						if(meta.hasDisplayName()){
							if(meta.getDisplayName().equalsIgnoreCase(plugin.getConfig().getString("convertedchainname"))){
								event.setCancelled(true);
							}
						}
					}
				}
			}
			}
		}
	}
	
	
	public ArrayList<ItemStack> helditems(String type, int plvl, String weapontype){
		ArrayList<ItemStack> item = new ArrayList<ItemStack>();
		if(weapontype.equals("strength")){
			if(type.equals("weapon")){
				for(int lvl = 1; lvl < plvl; lvl = lvl + 5){
					if(lvl == 1){
						item.add(new ItemStack(Material.WOOD_SWORD));
					}else if(lvl == 10){
						item.add(new ItemStack(Material.STONE_SWORD));
					}else if(lvl == 25){
						item.add(new ItemStack(Material.GOLD_SWORD));
					}else if(lvl == 45){
						item.add(new ItemStack(Material.IRON_SWORD));
					}else if(lvl == 65){
						item.add(new ItemStack(Material.DIAMOND_SWORD));
					}
					if(lvl == 6){
						lvl = 5;
					}
			
			}
				if(plvl == 1){
					item.add(new ItemStack(Material.WOOD_SWORD));
				}
		}else{
			for(int lvl = 1; lvl < plvl ; lvl = lvl + 5){
				if(lvl == 1){
					item.add(new ItemStack(Material.LEATHER_CHESTPLATE));
					item.add(new ItemStack(Material.LEATHER_LEGGINGS));
					item.add(new ItemStack(Material.LEATHER_BOOTS));
					item.add(new ItemStack(Material.LEATHER_HELMET));
				}else if(lvl == 15){
					item.add(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
					item.add(new ItemStack(Material.CHAINMAIL_LEGGINGS));
					item.add(new ItemStack(Material.CHAINMAIL_BOOTS));
					item.add(new ItemStack(Material.CHAINMAIL_HELMET));
				}else if(lvl == 30){
					item.add(new ItemStack(Material.GOLD_CHESTPLATE));
					item.add(new ItemStack(Material.GOLD_LEGGINGS));
					item.add(new ItemStack(Material.GOLD_BOOTS));
					item.add(new ItemStack(Material.GOLD_HELMET));
				}else if(lvl == 50){
					item.add(new ItemStack(Material.IRON_CHESTPLATE));
					item.add(new ItemStack(Material.IRON_LEGGINGS));
					item.add(new ItemStack(Material.IRON_BOOTS));
					item.add(new ItemStack(Material.IRON_HELMET));
				}else if(lvl == 70){
					item.add(new ItemStack(Material.DIAMOND_CHESTPLATE));
					item.add(new ItemStack(Material.DIAMOND_LEGGINGS));
					item.add(new ItemStack(Material.DIAMOND_BOOTS));
					item.add(new ItemStack(Material.DIAMOND_HELMET));
				}
				if(lvl == 6){
					lvl = 5;
				}
			}
			if(plvl == 1){
				item.add(new ItemStack(Material.LEATHER_CHESTPLATE));
				item.add(new ItemStack(Material.LEATHER_LEGGINGS));
				item.add(new ItemStack(Material.LEATHER_BOOTS));
				item.add(new ItemStack(Material.LEATHER_HELMET));
			}
		}
		}else{
			item.add(new ItemStack(Material.BOW));
		}
		return item;
	}
	
	private ArrayList<Material> heldtools(String type, int playerlvl, String skilltype){
		ArrayList<Material> toollist = new ArrayList<Material>();
		if(type.equals("axes")){
			if(skilltype.equals("skill")){
			for(int i = 1; i <= playerlvl; i = i + 5){
				if( i == 6){
					i = 5;
				}
				if(i == 1){
					toollist.add(Material.WOOD_AXE);
				}
				if( i == 10){
					toollist.add(Material.STONE_AXE);
				}
				if( i== 15){
					toollist.add(Material.GOLD_AXE);
				}
				if(i == 20){
					toollist.add(Material.IRON_AXE);
				}
				if(i == 25){
					toollist.add(Material.DIAMOND_AXE);
				}
					
				}
			}else{
				for(int i = 1; i <= playerlvl; i = i + 5){
					if( i == 6){
						i = 5;
					}
					if(i == 1){
						toollist.add(Material.WOOD_AXE);
					}
					if( i == 10){
						toollist.add(Material.STONE_AXE);
					}
					if( i== 20){
						toollist.add(Material.GOLD_AXE);
					}
					if(i == 40){
						toollist.add(Material.IRON_AXE);
					}
					if(i == 50){
						toollist.add(Material.DIAMOND_AXE);
					}
						
					}
			}
		}else{
			if(skilltype.equals("skill")){
				for(int i = 1; i <= playerlvl; i = i + 5){
					if( i == 6){
						i = 5;
					}
					if(i == 1){
						toollist.add(Material.WOOD_PICKAXE);
					}
					if( i == 10){
						toollist.add(Material.STONE_PICKAXE);
					}
					if( i== 15){
						toollist.add(Material.GOLD_PICKAXE);
					}
					if(i == 20){
						toollist.add(Material.IRON_PICKAXE);
					}
					if(i == 25){
						toollist.add(Material.DIAMOND_PICKAXE);
					}
						
					}
				}else{
					for(int i = 1; i <= playerlvl; i = i + 5){
						if( i == 6){
							i = 5;
						}
						if(i == 1){
							toollist.add(Material.WOOD_PICKAXE);
						}
						if( i == 10){
							toollist.add(Material.STONE_PICKAXE);
						}
						if( i== 20){
							toollist.add(Material.GOLD_PICKAXE);
						}
						if(i == 40){
							toollist.add(Material.IRON_PICKAXE);
						}
						if(i == 50){
							toollist.add(Material.DIAMOND_PICKAXE);
						}
							
						}
				}
		}
		return toollist;
	}
	
	private int neededlvl(ItemStack item, Map<Enchantment, Integer> ent, int playerlvl){
		Material mat = item.getType();
		if(ent != null && (ent.get(Enchantment.PROTECTION_ENVIRONMENTAL) != null || ent.get(Enchantment.DAMAGE_ALL) != null || ent.get(Enchantment.ARROW_DAMAGE) != null )){
			
			Enchantment encht = null;
			ArrayList<Enchantment> ee = new ArrayList<Enchantment>();
			ee.addAll(ent.keySet());
			for(Enchantment e : ee){
				if(e.equals(Enchantment.PROTECTION_ENVIRONMENTAL) || e.equals(Enchantment.DAMAGE_ALL) || e.equals(Enchantment.ARROW_DAMAGE)){
						encht = e;
						break;
					}
			}
			HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = itemenchat(mat);
			HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
			Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
			ArrayList<Integer> itemlvl = new ArrayList<Integer>();
			itemlvl.addAll(sortedtlist.keySet());
			for(int lvl : itemlvl){
				int enchtlvl = sortedtlist.get(lvl).get(encht);
				if(item.getEnchantmentLevel(encht) == enchtlvl){
					return lvl;
				}
			}
		}else{
			HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = itemenchat(mat);
			HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
			Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
			
			ArrayList<Integer> itemlvl = new ArrayList<Integer>();
			itemlvl.addAll(sortedtlist.keySet());
			for(int lvl : itemlvl){
				if(playerlvl <= lvl){
					return lvl;
				}
		}
		}
		return 0;
		
	}
	
	private int neededskillvl(ItemStack item, Map<Enchantment, Integer> ent, int playerlvl){
		Material mat = item.getType();
		if(ent != null && ent.get(Enchantment.DIG_SPEED) != null ){
			
			Enchantment encht = null;
			ArrayList<Enchantment> ee = new ArrayList<Enchantment>();
			ee.addAll(ent.keySet());
			for(Enchantment e : ee){
				if(e.equals(Enchantment.DIG_SPEED)){
						encht = e;
						break;
					}
			}
			HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = toolsencahtskill(mat);
			HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
			Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
			ArrayList<Integer> itemlvl = new ArrayList<Integer>();
			itemlvl.addAll(sortedtlist.keySet());
			for(int lvl : itemlvl){
				int enchtlvl = sortedtlist.get(lvl).get(encht);
				if(item.getEnchantmentLevel(encht) == enchtlvl){
					return lvl;
				}
			}
		}else{
			HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = toolsencahtskill(mat);
			HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
			Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
			
			ArrayList<Integer> itemlvl = new ArrayList<Integer>();
			itemlvl.addAll(sortedtlist.keySet());
			for(int lvl : itemlvl){
				if(playerlvl <= lvl){
					return lvl;
				}
		}
		}
		return 0;
		
	}
	private int neededstrength(ItemStack item, Map<Enchantment, Integer> ent, int playerlvl){
		Material mat = item.getType();
		if(ent != null && ent.get(Enchantment.DAMAGE_ALL) != null ){
			
			Enchantment encht = null;
			ArrayList<Enchantment> ee = new ArrayList<Enchantment>();
			ee.addAll(ent.keySet());
			for(Enchantment e : ee){
				if(e.equals(Enchantment.DAMAGE_ALL)){
						encht = e;
						break;
					}
			}
			HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = toolsencahtstrength(mat);
			HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
			Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
			ArrayList<Integer> itemlvl = new ArrayList<Integer>();
			itemlvl.addAll(sortedtlist.keySet());
			for(int lvl : itemlvl){
				int enchtlvl = sortedtlist.get(lvl).get(encht);
				if(item.getEnchantmentLevel(encht) == enchtlvl){
					return lvl;
				}
			}
		}else{
			HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemneeds = toolsencahtstrength(mat);
			HashMap<Integer, HashMap<Enchantment, Integer>> iteminfo = itemneeds.get(mat);
			Map<Integer, HashMap<Enchantment, Integer>> sortedtlist = new TreeMap<Integer, HashMap<Enchantment,Integer>>(iteminfo);
			
			ArrayList<Integer> itemlvl = new ArrayList<Integer>();
			itemlvl.addAll(sortedtlist.keySet());
			for(int lvl : itemlvl){
				if(playerlvl <= lvl){
					return lvl;
				}
		}
		}
		return 0;
		
	}
	
	private HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemenchat(Material mat){
		HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> itemenc = new HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>>();
		HashMap<Integer, HashMap<Enchantment, Integer>> lvlencht = new HashMap<Integer, HashMap<Enchantment, Integer>>();
		
		if(mat == Material.LEATHER_HELMET || mat == Material.LEATHER_CHESTPLATE || mat == Material.LEATHER_LEGGINGS || mat == Material.LEATHER_BOOTS){
			HashMap<Enchantment, Integer> enchantm = new HashMap<Enchantment, Integer>();
			enchantm.put(Enchantment.PROTECTION_ENVIRONMENTAL, 0);
			lvlencht.put(1, enchantm);
			HashMap<Enchantment, Integer> enchantml = new HashMap<Enchantment, Integer>();
			enchantml.put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			lvlencht.put(5, enchantml);
			HashMap<Enchantment, Integer> enchantme = new HashMap<Enchantment, Integer>();
			enchantme.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			lvlencht.put(10, enchantme);
			HashMap<Enchantment, Integer> enchantmh = new HashMap<Enchantment, Integer>();
			enchantmh.put(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
			lvlencht.put(15, enchantmh);
			HashMap<Enchantment, Integer> enchanth = new HashMap<Enchantment, Integer>();
			enchanth.put(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
			lvlencht.put(20, enchanth);
			itemenc.put(mat, lvlencht);
		}else if(mat == Material.CHAINMAIL_HELMET || mat == Material.CHAINMAIL_CHESTPLATE || mat == Material.CHAINMAIL_LEGGINGS || mat == Material.CHAINMAIL_BOOTS){
			HashMap<Enchantment, Integer> enchantm = new HashMap<Enchantment, Integer>();
			enchantm.put(Enchantment.PROTECTION_ENVIRONMENTAL, 0);
			lvlencht.put(15, enchantm);
			HashMap<Enchantment, Integer> enchantml = new HashMap<Enchantment, Integer>();
			enchantml.put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			lvlencht.put(20, enchantml);
			HashMap<Enchantment, Integer> enchantme = new HashMap<Enchantment, Integer>();
			enchantme.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			lvlencht.put(25, enchantme);
			HashMap<Enchantment, Integer> enchantmh = new HashMap<Enchantment, Integer>();
			enchantmh.put(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
			lvlencht.put(30, enchantmh);
			HashMap<Enchantment, Integer> enchanth = new HashMap<Enchantment, Integer>();
			enchanth.put(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
			lvlencht.put(35, enchanth);
			itemenc.put(mat, lvlencht);
		}else if(mat == Material.GOLD_HELMET || mat == Material.GOLD_CHESTPLATE || mat == Material.GOLD_LEGGINGS || mat == Material.GOLD_BOOTS){
			HashMap<Enchantment, Integer> enchantm = new HashMap<Enchantment, Integer>();
			enchantm.put(Enchantment.PROTECTION_ENVIRONMENTAL, 0);
			lvlencht.put(30, enchantm);
			HashMap<Enchantment, Integer> enchantml = new HashMap<Enchantment, Integer>();
			enchantml.put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			lvlencht.put(35, enchantml);
			HashMap<Enchantment, Integer> enchantme = new HashMap<Enchantment, Integer>();
			enchantme.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			lvlencht.put(40, enchantme);
			HashMap<Enchantment, Integer> enchantmh = new HashMap<Enchantment, Integer>();
			enchantmh.put(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
			lvlencht.put(45, enchantmh);
			HashMap<Enchantment, Integer> enchanth = new HashMap<Enchantment, Integer>();
			enchanth.put(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
			lvlencht.put(50, enchanth);
			itemenc.put(mat, lvlencht);
		}else if(mat == Material.IRON_HELMET || mat == Material.IRON_CHESTPLATE || mat == Material.IRON_LEGGINGS || mat == Material.IRON_BOOTS){
			HashMap<Enchantment, Integer> enchantm = new HashMap<Enchantment, Integer>();
			enchantm.put(Enchantment.PROTECTION_ENVIRONMENTAL, 0);
			lvlencht.put(50, enchantm);
			HashMap<Enchantment, Integer> enchantml = new HashMap<Enchantment, Integer>();
			enchantml.put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			lvlencht.put(55, enchantml);
			HashMap<Enchantment, Integer> enchantme = new HashMap<Enchantment, Integer>();
			enchantme.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			lvlencht.put(60, enchantme);
			HashMap<Enchantment, Integer> enchantmh = new HashMap<Enchantment, Integer>();
			enchantmh.put(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
			lvlencht.put(65, enchantmh);
			HashMap<Enchantment, Integer> enchanth = new HashMap<Enchantment, Integer>();
			enchanth.put(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
			lvlencht.put(70, enchanth);
			itemenc.put(mat, lvlencht);
		}else if(mat == Material.DIAMOND_HELMET || mat == Material.DIAMOND_CHESTPLATE || mat == Material.DIAMOND_LEGGINGS || mat == Material.DIAMOND_BOOTS){
			HashMap<Enchantment, Integer> enchantm = new HashMap<Enchantment, Integer>();
			enchantm.put(Enchantment.PROTECTION_ENVIRONMENTAL, 0);
			lvlencht.put(70, enchantm);
			HashMap<Enchantment, Integer> enchantml = new HashMap<Enchantment, Integer>();
			enchantml.put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			lvlencht.put(75, enchantml);
			HashMap<Enchantment, Integer> enchantme = new HashMap<Enchantment, Integer>();
			enchantme.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			lvlencht.put(80, enchantme);
			HashMap<Enchantment, Integer> enchantmh = new HashMap<Enchantment, Integer>();
			enchantmh.put(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
			lvlencht.put(85, enchantmh);
			HashMap<Enchantment, Integer> enchanth = new HashMap<Enchantment, Integer>();
			enchanth.put(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
			lvlencht.put(90, enchanth);
			itemenc.put(mat, lvlencht);
			
			//swords
		}else if(mat == Material.WOOD_SWORD){
			HashMap<Enchantment, Integer> enchantm = new HashMap<Enchantment, Integer>();
			enchantm.put(Enchantment.DAMAGE_ALL, 0);
			lvlencht.put(1, enchantm);
			HashMap<Enchantment, Integer> enchantml = new HashMap<Enchantment, Integer>();
			enchantml.put(Enchantment.DAMAGE_ALL, 1);
			lvlencht.put(5, enchantml);
			HashMap<Enchantment, Integer> enchantme = new HashMap<Enchantment, Integer>();
			enchantme.put(Enchantment.DAMAGE_ALL, 2);
			lvlencht.put(10, enchantme);
			HashMap<Enchantment, Integer> enchantmh = new HashMap<Enchantment, Integer>();
			enchantmh.put(Enchantment.DAMAGE_ALL, 3);
			lvlencht.put(15, enchantmh);
			HashMap<Enchantment, Integer> enchanth = new HashMap<Enchantment, Integer>();
			enchanth.put(Enchantment.DAMAGE_ALL, 4);
			lvlencht.put(20, enchanth);
			HashMap<Enchantment, Integer> enchanthm = new HashMap<Enchantment, Integer>();
			enchanthm.put(Enchantment.DAMAGE_ALL, 5);
			lvlencht.put(25, enchanthm);
			itemenc.put(mat, lvlencht);
		}else if(mat == Material.STONE_SWORD){
			HashMap<Enchantment, Integer> enchantm = new HashMap<Enchantment, Integer>();
			enchantm.put(Enchantment.DAMAGE_ALL, 0);
			lvlencht.put(10, enchantm);
			HashMap<Enchantment, Integer> enchantml = new HashMap<Enchantment, Integer>();
			enchantml.put(Enchantment.DAMAGE_ALL, 1);
			lvlencht.put(15, enchantml);
			HashMap<Enchantment, Integer> enchantme = new HashMap<Enchantment, Integer>();
			enchantme.put(Enchantment.DAMAGE_ALL, 2);
			lvlencht.put(20, enchantme);
			HashMap<Enchantment, Integer> enchantmh = new HashMap<Enchantment, Integer>();
			enchantmh.put(Enchantment.DAMAGE_ALL, 3);
			lvlencht.put(25, enchantmh);
			HashMap<Enchantment, Integer> enchanth = new HashMap<Enchantment, Integer>();
			enchanth.put(Enchantment.DAMAGE_ALL, 4);
			lvlencht.put(30, enchanth);
			HashMap<Enchantment, Integer> enchanthm = new HashMap<Enchantment, Integer>();
			enchanthm.put(Enchantment.DAMAGE_ALL, 5);
			lvlencht.put(35, enchanthm);
			itemenc.put(mat, lvlencht);
		}else if(mat == Material.GOLD_SWORD){
			HashMap<Enchantment, Integer> enchantm = new HashMap<Enchantment, Integer>();
			enchantm.put(Enchantment.DAMAGE_ALL, 0);
			lvlencht.put(25, enchantm);
			HashMap<Enchantment, Integer> enchantml = new HashMap<Enchantment, Integer>();
			enchantml.put(Enchantment.DAMAGE_ALL, 1);
			lvlencht.put(30, enchantml);
			HashMap<Enchantment, Integer> enchantme = new HashMap<Enchantment, Integer>();
			enchantme.put(Enchantment.DAMAGE_ALL, 2);
			lvlencht.put(35, enchantme);
			HashMap<Enchantment, Integer> enchantmh = new HashMap<Enchantment, Integer>();
			enchantmh.put(Enchantment.DAMAGE_ALL, 3);
			lvlencht.put(40, enchantmh);
			HashMap<Enchantment, Integer> enchanth = new HashMap<Enchantment, Integer>();
			enchanth.put(Enchantment.DAMAGE_ALL, 4);
			lvlencht.put(45, enchanth);
			HashMap<Enchantment, Integer> enchanthm = new HashMap<Enchantment, Integer>();
			enchanthm.put(Enchantment.DAMAGE_ALL, 5);
			lvlencht.put(50, enchanthm);
			itemenc.put(mat, lvlencht);
		}else if(mat == Material.IRON_SWORD){
			HashMap<Enchantment, Integer> enchantm = new HashMap<Enchantment, Integer>();
			enchantm.put(Enchantment.DAMAGE_ALL, 0);
			lvlencht.put(45, enchantm);
			HashMap<Enchantment, Integer> enchantml = new HashMap<Enchantment, Integer>();
			enchantml.put(Enchantment.DAMAGE_ALL, 1);
			lvlencht.put(50, enchantml);
			HashMap<Enchantment, Integer> enchantme = new HashMap<Enchantment, Integer>();
			enchantme.put(Enchantment.DAMAGE_ALL, 2);
			lvlencht.put(55, enchantme);
			HashMap<Enchantment, Integer> enchantmh = new HashMap<Enchantment, Integer>();
			enchantmh.put(Enchantment.DAMAGE_ALL, 3);
			lvlencht.put(60, enchantmh);
			HashMap<Enchantment, Integer> enchanth = new HashMap<Enchantment, Integer>();
			enchanth.put(Enchantment.DAMAGE_ALL, 4);
			lvlencht.put(65, enchanth);
			HashMap<Enchantment, Integer> enchanthm = new HashMap<Enchantment, Integer>();
			enchanthm.put(Enchantment.DAMAGE_ALL, 5);
			lvlencht.put(70, enchanthm);
			itemenc.put(mat, lvlencht);
		}else if(mat == Material.DIAMOND_SWORD){
			HashMap<Enchantment, Integer> enchantm = new HashMap<Enchantment, Integer>();
			enchantm.put(Enchantment.DAMAGE_ALL, 0);
			lvlencht.put(65, enchantm);
			HashMap<Enchantment, Integer> enchantml = new HashMap<Enchantment, Integer>();
			enchantml.put(Enchantment.DAMAGE_ALL, 1);
			lvlencht.put(70, enchantml);
			HashMap<Enchantment, Integer> enchantme = new HashMap<Enchantment, Integer>();
			enchantme.put(Enchantment.DAMAGE_ALL, 2);
			lvlencht.put(75, enchantme);
			HashMap<Enchantment, Integer> enchantmh = new HashMap<Enchantment, Integer>();
			enchantmh.put(Enchantment.DAMAGE_ALL, 3);
			lvlencht.put(80, enchantmh);
			HashMap<Enchantment, Integer> enchanth = new HashMap<Enchantment, Integer>();
			enchanth.put(Enchantment.DAMAGE_ALL, 4);
			lvlencht.put(85, enchanth);
			HashMap<Enchantment, Integer> enchanthm = new HashMap<Enchantment, Integer>();
			enchanthm.put(Enchantment.DAMAGE_ALL, 5);
			lvlencht.put(90, enchanthm);
			itemenc.put(mat, lvlencht);
		}else if(mat == Material.BOW){	
			HashMap<Enchantment, Integer> enchantm = new HashMap<Enchantment, Integer>();
			enchantm.put(Enchantment.ARROW_DAMAGE, 0);
			lvlencht.put(1, enchantm);
			HashMap<Enchantment, Integer> enchantml = new HashMap<Enchantment, Integer>();
			enchantml.put(Enchantment.ARROW_DAMAGE, 1);
			lvlencht.put(10, enchantml);
			HashMap<Enchantment, Integer> enchantme = new HashMap<Enchantment, Integer>();
			enchantme.put(Enchantment.ARROW_DAMAGE, 2);
			lvlencht.put(20, enchantme);
			HashMap<Enchantment, Integer> enchantmh = new HashMap<Enchantment, Integer>();
			enchantmh.put(Enchantment.ARROW_DAMAGE, 3);
			lvlencht.put(30, enchantmh);
			HashMap<Enchantment, Integer> enchanth = new HashMap<Enchantment, Integer>();
			enchanth.put(Enchantment.ARROW_DAMAGE, 4);
			lvlencht.put(40, enchanth);
			HashMap<Enchantment, Integer> enchanthm = new HashMap<Enchantment, Integer>();
			enchanthm.put(Enchantment.ARROW_DAMAGE, 5);
			lvlencht.put(50, enchanthm);
			itemenc.put(mat, lvlencht);
		}
		
		
		return itemenc;
	}
	
	private HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> toolsencahtskill(Material mat){
		HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> tools = new HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>>();
		if(mat == Material.WOOD_AXE || mat == Material.WOOD_PICKAXE){
			HashMap<Integer, HashMap<Enchantment, Integer>> lvlinfo = new HashMap<Integer, HashMap<Enchantment, Integer>>();
			HashMap<Enchantment, Integer> enctlvl = new HashMap<Enchantment, Integer>();
			enctlvl.put(Enchantment.DIG_SPEED, 0);
			lvlinfo.put(1, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 1);
			lvlinfo.put(5, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 2);
			lvlinfo.put(10, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 3);
			lvlinfo.put(15, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 4);
			lvlinfo.put(20, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 5);
			lvlinfo.put(25, enctlvl);
			tools.put(mat, lvlinfo);
			return tools;
		}else if(mat == Material.STONE_AXE || mat == Material.STONE_PICKAXE){
			HashMap<Integer, HashMap<Enchantment, Integer>> lvlinfo = new HashMap<Integer, HashMap<Enchantment, Integer>>();
			HashMap<Enchantment, Integer> enctlvl = new HashMap<Enchantment, Integer>();
			enctlvl.put(Enchantment.DIG_SPEED, 0);
			lvlinfo.put(10, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 1);
			lvlinfo.put(15, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 2);
			lvlinfo.put(20, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 3);
			lvlinfo.put(25, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 4);
			lvlinfo.put(30, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 5);
			lvlinfo.put(35, enctlvl);
			tools.put(mat, lvlinfo);
			return tools;
		}else if(mat == Material.GOLD_AXE || mat == Material.GOLD_PICKAXE){
			HashMap<Integer, HashMap<Enchantment, Integer>> lvlinfo = new HashMap<Integer, HashMap<Enchantment, Integer>>();
			HashMap<Enchantment, Integer> enctlvl = new HashMap<Enchantment, Integer>();
			enctlvl.put(Enchantment.DIG_SPEED, 0);
			lvlinfo.put(15, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 1);
			lvlinfo.put(20, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 2);
			lvlinfo.put(25, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 3);
			lvlinfo.put(30, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 4);
			lvlinfo.put(35, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 5);
			lvlinfo.put(40, enctlvl);
			tools.put(mat, lvlinfo);
			return tools;
		}else if(mat == Material.IRON_AXE || mat == Material.IRON_PICKAXE){
			HashMap<Integer, HashMap<Enchantment, Integer>> lvlinfo = new HashMap<Integer, HashMap<Enchantment, Integer>>();
			HashMap<Enchantment, Integer> enctlvl = new HashMap<Enchantment, Integer>();
			enctlvl.put(Enchantment.DIG_SPEED, 0);
			lvlinfo.put(20, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 1);
			lvlinfo.put(25, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 2);
			lvlinfo.put(30, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 3);
			lvlinfo.put(35, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 4);
			lvlinfo.put(40, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 5);
			lvlinfo.put(45, enctlvl);
			tools.put(mat, lvlinfo);
			return tools;
		}else{
			HashMap<Integer, HashMap<Enchantment, Integer>> lvlinfo = new HashMap<Integer, HashMap<Enchantment, Integer>>();
			HashMap<Enchantment, Integer> enctlvl = new HashMap<Enchantment, Integer>();
			enctlvl.put(Enchantment.DIG_SPEED, 0);
			lvlinfo.put(25, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 1);
			lvlinfo.put(30, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 2);
			lvlinfo.put(35, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 3);
			lvlinfo.put(40, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 4);
			lvlinfo.put(45, enctlvl);
			enctlvl.put(Enchantment.DIG_SPEED, 5);
			lvlinfo.put(50, enctlvl);
			tools.put(mat, lvlinfo);
			return tools;
		}
	
	}
	
	private HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> toolsencahtstrength(Material mat){
		HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>> tools = new HashMap<Material, HashMap<Integer, HashMap<Enchantment, Integer>>>();
		if(mat == Material.WOOD_AXE || mat == Material.WOOD_PICKAXE){
			HashMap<Integer, HashMap<Enchantment, Integer>> lvlinfo = new HashMap<Integer, HashMap<Enchantment, Integer>>();
			HashMap<Enchantment, Integer> enctlvl = new HashMap<Enchantment, Integer>();
			enctlvl.put(Enchantment.DAMAGE_ALL, 0);
			lvlinfo.put(1, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 1);
			lvlinfo.put(5, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 2);
			lvlinfo.put(10, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 3);
			lvlinfo.put(15, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 4);
			lvlinfo.put(20, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 5);
			lvlinfo.put(25, enctlvl);
			tools.put(mat, lvlinfo);
			return tools;
		}else if(mat == Material.STONE_AXE || mat == Material.STONE_PICKAXE){
			HashMap<Integer, HashMap<Enchantment, Integer>> lvlinfo = new HashMap<Integer, HashMap<Enchantment, Integer>>();
			HashMap<Enchantment, Integer> enctlvl = new HashMap<Enchantment, Integer>();
			enctlvl.put(Enchantment.DAMAGE_ALL, 0);
			lvlinfo.put(10, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 1);
			lvlinfo.put(15, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 2);
			lvlinfo.put(20, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 3);
			lvlinfo.put(25, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 4);
			lvlinfo.put(30, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 5);
			lvlinfo.put(35, enctlvl);
			tools.put(mat, lvlinfo);
			return tools;
		}else if(mat == Material.GOLD_AXE || mat == Material.GOLD_PICKAXE){
			HashMap<Integer, HashMap<Enchantment, Integer>> lvlinfo = new HashMap<Integer, HashMap<Enchantment, Integer>>();
			HashMap<Enchantment, Integer> enctlvl = new HashMap<Enchantment, Integer>();
			enctlvl.put(Enchantment.DAMAGE_ALL, 0);
			lvlinfo.put(20, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 1);
			lvlinfo.put(25, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 2);
			lvlinfo.put(30, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 3);
			lvlinfo.put(35, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 4);
			lvlinfo.put(40, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 5);
			lvlinfo.put(45, enctlvl);
			tools.put(mat, lvlinfo);
			return tools;
		}else if(mat == Material.IRON_AXE || mat == Material.IRON_PICKAXE){
			HashMap<Integer, HashMap<Enchantment, Integer>> lvlinfo = new HashMap<Integer, HashMap<Enchantment, Integer>>();
			HashMap<Enchantment, Integer> enctlvl = new HashMap<Enchantment, Integer>();
			enctlvl.put(Enchantment.DAMAGE_ALL, 0);
			lvlinfo.put(40, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 1);
			lvlinfo.put(45, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 2);
			lvlinfo.put(50, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 3);
			lvlinfo.put(60, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 4);
			lvlinfo.put(65, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 5);
			lvlinfo.put(70, enctlvl);
			tools.put(mat, lvlinfo);
			return tools;
		}else{
			HashMap<Integer, HashMap<Enchantment, Integer>> lvlinfo = new HashMap<Integer, HashMap<Enchantment, Integer>>();
			HashMap<Enchantment, Integer> enctlvl = new HashMap<Enchantment, Integer>();
			enctlvl.put(Enchantment.DAMAGE_ALL, 0);
			lvlinfo.put(55, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 1);
			lvlinfo.put(60, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 2);
			lvlinfo.put(65, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 3);
			lvlinfo.put(70, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 4);
			lvlinfo.put(75, enctlvl);
			enctlvl.put(Enchantment.DAMAGE_ALL, 5);
			lvlinfo.put(80, enctlvl);
			tools.put(mat, lvlinfo);
			return tools;
		}
	
	}
	
 	public Playerinfo(MoY instance) {
		this.plugin = instance;
		tools.add(Material.WOOD_AXE);
		tools.add(Material.WOOD_PICKAXE);
		tools.add(Material.STONE_AXE);
		tools.add(Material.STONE_PICKAXE);
		tools.add(Material.IRON_AXE);
		tools.add(Material.IRON_PICKAXE);
		tools.add(Material.GOLD_AXE);
		tools.add(Material.GOLD_PICKAXE);
		tools.add(Material.DIAMOND_AXE);
		tools.add(Material.DIAMOND_PICKAXE);
		
		axes.add(Material.WOOD_AXE);
		axes.add(Material.STONE_AXE);
		axes.add(Material.IRON_AXE);
		axes.add(Material.GOLD_AXE);
		axes.add(Material.DIAMOND_AXE);

		pickaxes.add(Material.WOOD_PICKAXE);
		pickaxes.add(Material.STONE_PICKAXE);
		pickaxes.add(Material.IRON_PICKAXE);
		pickaxes.add(Material.GOLD_PICKAXE);
		pickaxes.add(Material.DIAMOND_PICKAXE);

	
	}

 	public Playerstats getplayer(UUID player){
 		if(players.get(player) != null){
 			return players.get(player);
 		}else{
 			return null;
 		}
 	}
 	
 	public Set<UUID> getplayers(){
 		return players.keySet();
 	}
 	
 	
}
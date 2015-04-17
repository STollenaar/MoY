package com.tollenaar.stephen.MoYMistsOfYsir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.tollenaar.stephen.MoYPlayerInfo.Playerstats;


public class Party implements Listener{
	private MoY plugin;
	private Filewriters fw;
	public  HashMap<String, Set<UUID>> Partyswithmembers = new HashMap<String, Set<UUID>>(); //party with all the members
	public  HashMap<String, UUID> Partyleaders = new HashMap<String, UUID>(); //all the party leaders and their party name partname --> partyleader db
	public  HashMap<UUID, String> invites = new HashMap<UUID , String>(); //all the open invites, playername --> partyname db
	public  HashMap<UUID, String> Partymembers = new HashMap<UUID, String>(); //all the players who are in a party db
	public  HashSet<UUID> Partychat = new HashSet<UUID>(); //party chat active or not no db
	public 	HashMap<UUID, Boolean> partymatchmaking = new HashMap<UUID, Boolean>(); //for the arena plugin with party matchmaking.
	
	
	public void createParty(UUID partyleader, String partyname, Player player){
		if(Partymembers.get(partyleader) == null){
		if(Partyswithmembers.get(partyname) == null){
		if(partyname.length() <= 50){
		Set<UUID> partymember = new LinkedHashSet<UUID>();
		Partyleaders.put(partyname, partyleader);
		partymember.add(partyleader);
		Partyswithmembers.put(partyname,  partymember);
		Partymembers.put(partyleader, partyname);
		partymatchmaking.put(partyleader, true);
		player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] You have created the party " + partyname + ".");
		}else{
			player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] " + ChatColor.AQUA + fw.utilitiesconfig.getString("Partyname.tolong"));
		}
		}else{
			player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] " + ChatColor.AQUA + fw.utilitiesconfig.getString("Partyname.taken"));
		}
		}else{
			player.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] " + ChatColor.AQUA + " You are already in a party.");

		}
	}
	public void addmember(String partyname, UUID member){
		Set<UUID> partymember =  Partyswithmembers.get(partyname);
		partymember.add(member);
		invites.remove(member);
		Partyswithmembers.put(partyname,  partymember);
		Partymembers.put(member, partyname);
		partymatchmaking.put(member, true);
		for(UUID partys : partymember){
				Player online = Bukkit.getPlayer(partys);
				if(online != null && online.isOnline()){
					String message = fw.utilitiesconfig.getString("Partyjoin");
					message = message.replace("%member%", Bukkit.getPlayer(member).getName());
					online.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] " + ChatColor.AQUA+ message);
				}
		}
	}
	public void kickmember(String partyname, UUID member){
		Set<UUID> partymember = Partyswithmembers.get(partyname);
		if(partymember.contains(member)){
			partymember.remove(member);
			Partyswithmembers.put(partyname, partymember);
			Partymembers.remove(member);
			partymatchmaking.remove(member);
		}
	}
	public void inviteplayer(Player targetplayer, String partyname, String partyleader){
		String message = fw.utilitiesconfig.getString("Partyinvite.victim");
		message = message.replace("%leader%", partyleader);
		message = message.replace("%partyname%", partyname);
		targetplayer.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "YParty" + ChatColor.DARK_PURPLE + "] " + message);
		invites.put(targetplayer.getUniqueId(), partyname);
	}
	
	public void partyinfo(String partyname, Player sender){
		if(Partyswithmembers.get(partyname) != null){
			ArrayList<UUID> members = new ArrayList<UUID>();
			members.addAll(Partyswithmembers.get(partyname));
			int totalmembers = members.size();
			if(totalmembers % 9 == 0){
				totalmembers++;
			}
			while(totalmembers % 9 != 0){
				totalmembers++;
			}
			
			if(totalmembers == 0){
				totalmembers = 9;
			}
			Inventory allpartymembers = Bukkit.createInventory(null, totalmembers, partyname + "'s members");
			for(UUID member : members){
				Player player = Bukkit.getPlayer(member);
				if(player != null){
					ItemStack onlineplayer = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
					SkullMeta skull = (SkullMeta) onlineplayer.getItemMeta();
					skull.setDisplayName(ChatColor.GREEN + player.getName());
					ArrayList<String> lore = new ArrayList<String>();
					lore.add(plugin.mob.mobdifficulty(Playerstats.level.get(member))  + "Level: " +  Integer.toString(Playerstats.level.get(member)));
					if(Partyleaders.get(Partymembers.get(member)).compareTo(member)== 0){
						lore.add(ChatColor.GOLD + "Party leader");
					}
					skull.setLore(lore);
					skull.setOwner(player.getName());
					onlineplayer.setItemMeta(skull);
					allpartymembers.addItem(onlineplayer);
				}else{
					 org.bukkit.OfflinePlayer offplayer = Bukkit.getOfflinePlayer(member);
					ItemStack offlineplayer = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
					SkullMeta skull = (SkullMeta) offlineplayer.getItemMeta();
					skull.setDisplayName(ChatColor.RED + offplayer.getName());
					ArrayList<String> lore = new ArrayList<String>();
					lore.add(plugin.mob.mobdifficulty(Playerstats.level.get(member)) + "Level: " + Integer.toString(Playerstats.level.get(member)));
					if(Partyleaders.get(Partymembers.get(member)).compareTo(member)== 0){
						lore.add(ChatColor.GOLD + "Party leader");
					}
					skull.setLore(lore);
					skull.setOwner(offplayer.getName());
					offlineplayer.setItemMeta(skull);
					allpartymembers.addItem(offlineplayer);
				}
			}
			
			sender.openInventory(allpartymembers);
		}
	}
	
	@EventHandler
	public void oninventoryclick(InventoryClickEvent event){
		if(event.getClickedInventory() != null && event.getClickedInventory().getName().contains("'s members")){
			event.setCancelled(true);
		}
	}
	
	public  void newleaver(String partyname, UUID newmember){
		Partyleaders.put(partyname, newmember);
	}
	
	public Party(MoY instance){
		this.plugin = instance;
		this.fw = instance.fw;
	}

	public  Set<UUID> partyfecther(UUID member){
		//if errors happend check this
		Set<UUID> members = new HashSet<UUID>();
			if(Partymembers.get(member) == null){
				return null;
			}
			if(Partyleaders.get(Partymembers.get(member)).compareTo(member) == 0){
				for(UUID inp : Partyswithmembers.get(Partymembers.get(member))){
					Player p = Bukkit.getPlayer(inp);
					if(p != null && p.isOnline() && partymatchmaking.get(inp)){
						members.add(inp);
					}
				}
			}else{
				return null;
			}
			
			return members;
	}

}

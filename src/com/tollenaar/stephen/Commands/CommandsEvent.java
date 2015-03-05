package com.tollenaar.stephen.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.tollenaar.stephen.MistsOfYsir.MoY;
import com.tollenaar.stephen.Quests.QuestsServerSide;
import com.tollenaar.stephen.Travel.HarborWaitLocations;
import com.tollenaar.stephen.Travel.TravelBoatEvent;
import com.tollenaar.stephen.Travel.TravelCartEvent;
import com.tollenaar.stephen.Travel.TravelDragonEvent;
import com.tollenaar.stephen.Travel.TripLocations;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class CommandsEvent implements CommandExecutor{
	 private TravelBoatEvent boat;
	 private TravelCartEvent cart;
	 private TravelDragonEvent dragon;
	 private QuestsServerSide questers;
	 
	 
	 public CommandsEvent(MoY instance){
			this.boat = instance.boat;
			this.cart = instance.cart;
			this.dragon = instance.dragon;
			this.questers = instance.questers;
		}
	 
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if(sender instanceof Player){
			 Player player = (Player)sender;
				PermissionUser user = PermissionsEx.getUser(player);
		if(cmd.getName().equalsIgnoreCase("event")){
			if(user.has("Ysir.event")){
			if(args.length == 2){
				if(args[0].equalsIgnoreCase("boat")){
					if(args[1].equalsIgnoreCase("create")){
						boat.eventCreate(player);
					}else if(args[1].equalsIgnoreCase("edit")){
						boat.eventalledit(player);
					}else {
						player.sendMessage(ChatColor.RED + "Please use /event <boat/cart/dragon> <create/edit>.");
						return true;
					}
				}else if(args[0].equalsIgnoreCase("cart")){
					if(args[1].equalsIgnoreCase("create")){
						cart.eventCreate(player);
					}else if(args[1].equalsIgnoreCase("edit")){
						cart.eventalledit(player);
					}else {
						player.sendMessage(ChatColor.RED + "Please use /event <boat/cart/dragon> <create/edit>.");
						return true;
						
					}
				}else if(args[0].equalsIgnoreCase("dragon")){
					if(args[1].equalsIgnoreCase("create")){
						dragon.eventCreate(player);
					}else if(args[1].equalsIgnoreCase("edit")){
						dragon.eventalledit(player);
					}else {
						player.sendMessage(ChatColor.RED + "Please use /event <boat/cart/dragon> <create/edit>.");
						return true;
					}
				}
			}else{
				player.sendMessage(ChatColor.RED + "Please use /event <boat/cart/dragon> <create/edit>.");
				return true;
			}
			return true;
		}
		}else if(cmd.getName().equalsIgnoreCase("Harbor")){
			if(args.length == 1){
				if(args[0].equals("create")){
					HarborWaitLocations h = questers.returnharbor(questers.createnewharbor());
					h.information(player);
				}else if(args[0].equals("edit")){
					questers.allharbors(player);
				}
			}
			return true;
		}else if(cmd.getName().equalsIgnoreCase("Trip")){
			if(args.length == 1){
				if(args[0].equals("create")){
					int id = questers.createnewtrip();
					TripLocations h = questers.returntrip(id);
					h.information(player);
				}else if(args[0].equals("edit")){
					questers.alltrips(player);
				}
			}
			return true;
		}
		}
		return false;
	}

}

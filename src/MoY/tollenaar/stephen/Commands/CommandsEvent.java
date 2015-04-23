package MoY.tollenaar.stephen.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import MoY.tollenaar.stephen.MistsOfYsir.MoY;
import MoY.tollenaar.stephen.Travel.TravelBoatEvent;
import MoY.tollenaar.stephen.Travel.TravelCartEvent;
import MoY.tollenaar.stephen.Travel.TravelDragonEvent;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class CommandsEvent implements CommandExecutor{
	 private TravelBoatEvent boat;
	 private TravelCartEvent cart;
	 private TravelDragonEvent dragon;
	
	 public CommandsEvent(MoY instance){
			this.boat = instance.boat;
			this.cart = instance.cart;
			this.dragon = instance.dragon;
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
			
		}
		}
		return false;
	}

}

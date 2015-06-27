package MoY.tollenaar.stephen.Quests;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import MoY.tollenaar.stephen.MistsOfYsir.MoY;

public class QuestChatEvent{
	private QuestsServerSide questers;
	
	public QuestChatEvent(MoY instance){
		this.questers = instance.questers;
	}
	
	
	@SuppressWarnings("deprecation")
	public boolean InfoSet(ArrayList<String> information, String typed, Player player){
		int questnumber = Integer.parseInt(information.get(2));
		String type = information.get(3);
		QuestEvent event = questers.returneventquest(questnumber);
		boolean pass = false;
		switch(type){
		case "title":
			event.setTitle(typed);
			pass = true;
			break;
		case "thing":
			try {
				EntityType.valueOf(typed.toUpperCase());
				event.setType(typed);
				pass = true;
			} catch (Exception ex) {
				try {
					String[] tp = typed.split(" ");
					@SuppressWarnings("unused")
					String item = tp[0];
					if (tp.length == 1) {
						if(Material.getMaterial(Integer.parseInt(tp[0].trim())) == null){
							throw new Exception();
						}
					} else if (tp.length == 2) {
					
					if(Material.getMaterial(Integer.parseInt(tp[0].trim())) == null){
						throw new Exception();
					}else{
						Short.parseShort(tp[1].trim());
					}
					} else {
						throw new Exception();
					}
					
					event.setType(typed);
					pass = true;
				} catch (Exception e) {
					player.sendMessage("item or monster not found, type again");
				}
			}
			break;
		case "count":
			try {
				event.setCount(Integer.parseInt(typed));
				pass = true;
			} catch (NumberFormatException ex) {
				player.sendMessage("This was not a number try again");
			}
			break;
		case "reward":
				if(typed.contains("add")){
					typed = typed.replace("add", "");
					event.setReward(typed);
					pass = true;
				}else {
					try{
						
						int line = Integer.parseInt(Character.toString(typed.charAt(0)));
						typed = typed.replaceFirst(Character.toString(typed.charAt(0)), "");
						if(line >= 0){
						List<String> rew = event.getReward();
						if(rew.size()-1 <= line){
							rew.set(line, typed);
							event.setReward(rew);
							pass = true;
						}else{
							pass = false;
						}
						}else{
							pass = false;
						}
					}catch(NumberFormatException ex){
						pass = false;
					}
				}
			break;
		case "start":
			if(typed.contains("/")){
				String[] splitted = typed.split("/");
				try{
					int day = Integer.parseInt(splitted[0].trim());
					int month = Integer.parseInt(splitted[1].trim())-1;
					Date date = new Date(new Date(System.currentTimeMillis()).getYear(), month, day);
					event.setStartdate(date.getTime());
					pass = true;
				}catch(NumberFormatException | ArrayIndexOutOfBoundsException ex){
					player.sendMessage("use as dd/mm");
				}
			}
			break;
		case "end":
			if(typed.contains("/")){
				String[] splitted = typed.split("/");
				try{
					int day = Integer.parseInt(splitted[0].trim());
					int month = Integer.parseInt(splitted[1].trim())-1;
					Date date = new Date(new Date(System.currentTimeMillis()).getYear(), month, day);
					event.setEnddate(date.getTime());
					pass = true;
				}catch(NumberFormatException | ArrayIndexOutOfBoundsException ex){
					player.sendMessage("use as dd/mm");
				}
			}
			break;
		case "minlvl":
			try {
				event.setMinlvl(Integer.parseInt(typed));
				pass = true;
			} catch (NumberFormatException ex) {
				player.sendMessage("This was not a number try again");
			}
			break;
		case "message":
			event.setMessage(typed);
			pass = true;
			break;
		case "repeat":
			event.setRepeat(typed);
			pass = true;
			break;
		}
		if(pass){
			questers.npcpos.remove(player.getUniqueId());
			event.openinv(player, UUID.fromString(information.get(1)));
			return true;
		}else{
			return false;
		}
		
	}
}

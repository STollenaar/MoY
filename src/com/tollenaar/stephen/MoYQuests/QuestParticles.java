package com.tollenaar.stephen.MoYQuests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.tollenaar.stephen.MoYMistsOfYsir.MoY;
import com.tollenaar.stephen.MoYMistsOfYsir.ParticleEffect;
import com.tollenaar.stephen.MoYPlayerInfo.Playerstats;

public class QuestParticles {
	private static MoY plugin;
	private static QuestsServerSide q;

	@SuppressWarnings("static-access")
	public QuestParticles(MoY instance) {
		this.plugin = instance;
		this.q = instance.questers;
	}

	public static void collectplayers(Location loc, UUID npcuuid) {
		double radiussq = Math.pow(40, 2);
		Collection<? extends Player> ptemp = plugin.getServer().getOnlinePlayers();
		Iterator<? extends Player> players = ptemp.iterator();
		ArrayList<Player> allclosep = new ArrayList<Player>();
		if (ptemp.size() >= 1) {

			while(players.hasNext()){
				Player player = players.next();
				if (player.getWorld().getName()
						.equals(loc.getWorld().getName())) {
					if (player.getLocation().distanceSquared(loc) <= radiussq) {

						allclosep.add(player);

					}
				}
			}
		}
		if (allclosep.size() > 0) {
			sendnormalques(npcuuid, allclosep, loc);
		}
	}

	private static void sendnormalques(UUID npcuuid, ArrayList<Player> players,
			Location loc) {
		for (Player player : players) {
			ArrayList<ParticleEffect> effects = new ArrayList<ParticleEffect>();
			if (aviableq(npcuuid, player)) {
				effects.add(ParticleEffect.NOTE);
			}
			if (aviablew(npcuuid)) {
				effects.add(ParticleEffect.VILLAGER_HAPPY);
			}
			if (completedq(npcuuid, player)) {
				effects.add(ParticleEffect.SPELL);
			}
			if (tocompletetalk(npcuuid, player)) {
				effects.add(ParticleEffect.HEART);
			}
			ArrayList<Player> t = new ArrayList<Player>();
			t.add(player);
			loc.setY(loc.getY() + 2);
			for (ParticleEffect effect : effects) {

				effect.display(0, 0, 0, 0, 1, loc, t);
			}
			loc.setY(loc.getY() - 2);

		}
	}

	private static boolean tocompletetalk(UUID npcuuid, Player player) {
		boolean checks = false;
		if (Playerstats.activequests.get(player.getUniqueId()) != null) {
			if (Playerstats.activequests.get(player.getUniqueId())
					.get("talkto") != null) {
				for (Integer i : Playerstats.activequests.get(
						player.getUniqueId()).get("talkto")) {
					QuestTalkto talk = q.returntalkto(i);
					if (plugin.questers.uniquenpcid.get(talk.getNpcid()) == npcuuid) {
						checks = true;
						break;
					}
				}
			}
		}
		return checks;
	}

	private static boolean completedq(UUID npcuuid, Player player) {
		boolean checks = false;
		if (Playerstats.completedquests.get(player.getUniqueId()) != null) {
			for (String type : Playerstats.completedquests.get(
					player.getUniqueId()).keySet()) {
				if (type.equals("kill")) {
					for (int number : Playerstats.completedquests.get(
							player.getUniqueId()).get(type)) {
						if (plugin.questers.killquests.get(npcuuid).contains(
								number)) {
							return true;
						}
					}
				} else if (type.equals("harvest")) {
					for (int number : Playerstats.completedquests.get(
							player.getUniqueId()).get(type)) {
						if (plugin.questers.harvestquests.get(npcuuid)
								.contains(number)) {
							return true;
						}
					}
				} else {
					for (int number : Playerstats.completedquests.get(
							player.getUniqueId()).get(type)) {
						if (plugin.questers.talktoquests.get(npcuuid) != null
								&& plugin.questers.talktoquests.get(npcuuid)
										.contains(number)) {
							return true;
						}
					}
				}
			}
		}
		return checks;
	}

	private static boolean aviablew(UUID npcuuid) {
		boolean checks = false;
		if (plugin.questers.warplists.get(npcuuid) != null) {
			checks = true;
		}
		return checks;
	}

	private static boolean aviableq(UUID npcuuid, Player player) {
		boolean checks = false;
		if (plugin.questers.killquests.get(npcuuid) != null) {
			for (Integer in : plugin.questers.killquests.get(npcuuid)) {
				if (check(npcuuid, player, in, "kill")) {
					checks = true;
					break;
				}
			}
		}

		if (plugin.questers.harvestquests.get(npcuuid) != null) {
			for (Integer in : plugin.questers.harvestquests.get(npcuuid)) {
				if (check(npcuuid, player, in, "harvest")) {
					checks = true;
					break;
				}
			}
		}

		if (plugin.questers.talktoquests.get(npcuuid) != null) {
			for (Integer in : plugin.questers.talktoquests.get(npcuuid)) {
				if (check(npcuuid, player, in, "talkto")) {
					checks = true;
					break;
				}
			}
		}

		return checks;
	}

	private static boolean check(UUID npcuuid, Player player, int questnumber,
			String type) {
		boolean check = true;
		switch (type) {
		case "kill":
			if (Playerstats.activequests.get(player.getUniqueId()) != null) {
				if (Playerstats.activequests.get(player.getUniqueId()).get(
						"kill") != null) {
					if (Playerstats.activequests.get(player.getUniqueId())
							.get("kill").contains(questnumber)) {
						return false;
					}
				}
			}
			if (Playerstats.completedquests.get(player.getUniqueId()) != null) {
				if (Playerstats.completedquests.get(player.getUniqueId()).get(
						"kill") != null) {
					if (Playerstats.completedquests.get(player.getUniqueId())
							.get("kill").contains(questnumber)) {
						return false;
					}
				}
			}
			if (Playerstats.rewardedlist.get(player.getUniqueId()) != null) {
				if (Playerstats.rewardedlist.get(player.getUniqueId()).get(
						"kill") != null) {
					if (Playerstats.rewardedlist.get(player.getUniqueId())
							.get("kill").get(questnumber) != null) {
						if (!q.returnkill(questnumber).getDelay().equals("-1")) {
							long logged = parseDateDiff(
									q.returnkill(questnumber).getDelay(),
									true,
									Playerstats.rewardedlist
											.get(player.getUniqueId())
											.get("kill").get(questnumber));
							if (logged <= System.currentTimeMillis()) {

								Playerstats.rewardedlist
										.get(player.getUniqueId()).get("kill")
										.keySet().remove(questnumber);
								plugin.database.deletecomkill(Integer.toString(questnumber), player.getUniqueId().toString());
							} else {
								return false;
							}
						} else {
							return false;
						}
					}
				}
			}

			break;
		case "harvest":
			if (Playerstats.activequests.get(player.getUniqueId()) != null) {
				if (Playerstats.activequests.get(player.getUniqueId()).get(
						"harvest") != null) {
					if (Playerstats.activequests.get(player.getUniqueId())
							.get("harvest").contains(questnumber)) {
						return false;
					}
				}
			}
			if (Playerstats.completedquests.get(player.getUniqueId()) != null) {
				if (Playerstats.completedquests.get(player.getUniqueId()).get(
						"harvest") != null) {
					if (Playerstats.completedquests.get(player.getUniqueId())
							.get("harvest").contains(questnumber)) {
						return false;
					}
				}
			}
			if (Playerstats.rewardedlist.get(player.getUniqueId()) != null) {
				if (Playerstats.rewardedlist.get(player.getUniqueId()).get(
						"harvest") != null) {
					if (Playerstats.rewardedlist.get(player.getUniqueId())
							.get("harvest").get(questnumber) != null) {
						if (Playerstats.rewardedlist.get(player.getUniqueId())
								.get("harvest").get(questnumber) != null) {
							if (!q.returnharvest(questnumber).getDelay()
									.equals("-1")) {
								long logged = parseDateDiff(
										q.returnharvest(questnumber).getDelay(),
										true,
										Playerstats.rewardedlist
												.get(player.getUniqueId())
												.get("harvest")
												.get(questnumber));
								if (logged <= System.currentTimeMillis()) {
									Playerstats.rewardedlist
											.get(player.getUniqueId())
											.get("harvest").keySet()
											.remove(questnumber);
									plugin.database.deletecomkill(Integer.toString(questnumber), player.getUniqueId().toString());
								} else {
									return false;
								}
							} else {
								return false;
							}
						}
					}
				}
			}
			break;
		case "talkto":
			if (Playerstats.activequests.get(player.getUniqueId()) != null) {
				if (Playerstats.activequests.get(player.getUniqueId()).get(
						"talkto") != null) {
					if (Playerstats.activequests.get(player.getUniqueId())
							.get("talkto").contains(questnumber)) {
						return false;
					}
				}
			}
			if (Playerstats.completedquests.get(player.getUniqueId()) != null) {
				if (Playerstats.completedquests.get(player.getUniqueId()).get(
						"talkto") != null) {
					if (Playerstats.completedquests.get(player.getUniqueId())
							.get("talkto").contains(questnumber)) {
						return false;
					}
				}
			}
			if (Playerstats.rewardedlist.get(player.getUniqueId()) != null) {
				if (Playerstats.rewardedlist.get(player.getUniqueId()).get(
						"talkto") != null) {
					if (Playerstats.rewardedlist.get(player.getUniqueId())
							.get("talkto").get(questnumber) != null) {
						if (Playerstats.rewardedlist.get(player.getUniqueId())
								.get("talkto").get(questnumber) != null) {
							if (!q.returntalkto(questnumber).getDelay()
									.equals("-1")) {
								long logged = parseDateDiff(
										q.returntalkto(questnumber).getDelay(),
										true,
										Playerstats.rewardedlist
												.get(player.getUniqueId())
												.get("talkto").get(questnumber));
								if (logged <= System.currentTimeMillis()) {
									Playerstats.rewardedlist
											.get(player.getUniqueId())
											.get("talkto").keySet()
											.remove(questnumber);
									plugin.database.deletecomtalk(Integer.toString(questnumber), player.getUniqueId().toString());
								} else {
									return false;
								}
							} else {
								return false;
							}
						}
					}
				}
			}
			break;
		}

		return check;
	}

	private static long parseDateDiff(String time, boolean future,
			long rewardedtime) {
		Pattern timePattern = Pattern
				.compile(
						"(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?",

						2);
		Matcher m = timePattern.matcher(time);
		int years = 0;
		int months = 0;
		int weeks = 0;
		int days = 0;
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		boolean found = false;
		while (m.find()) {
			if ((m.group() != null) && (!m.group().isEmpty())) {
				for (int i = 0; i < m.groupCount(); i++) {
					if ((m.group(i) != null) && (!m.group(i).isEmpty())) {
						found = true;
						break;
					}
				}
				if (found) {
					if ((m.group(1) != null) && (!m.group(1).isEmpty())) {
						years = Integer.parseInt(m.group(1));
					}
					if ((m.group(2) != null) && (!m.group(2).isEmpty())) {
						months = Integer.parseInt(m.group(2));
					}
					if ((m.group(3) != null) && (!m.group(3).isEmpty())) {
						weeks = Integer.parseInt(m.group(3));
					}
					if ((m.group(4) != null) && (!m.group(4).isEmpty())) {
						days = Integer.parseInt(m.group(4));
					}
					if ((m.group(5) != null) && (!m.group(5).isEmpty())) {
						hours = Integer.parseInt(m.group(5));
					}
					if ((m.group(6) != null) && (!m.group(6).isEmpty())) {
						minutes = Integer.parseInt(m.group(6));
					}
					if ((m.group(7) == null) || (m.group(7).isEmpty())) {
						break;
					}
					seconds = Integer.parseInt(m.group(7));

					break;
				}
			}
		}
		if (!found) {
			return -1L;
		}
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(rewardedtime);
		if (years > 0) {
			c.add(1, years * (future ? 1 : -1));
		}
		if (months > 0) {
			c.add(2, months * (future ? 1 : -1));
		}
		if (weeks > 0) {
			c.add(3, weeks * (future ? 1 : -1));
		}
		if (days > 0) {
			c.add(5, days * (future ? 1 : -1));
		}
		if (hours > 0) {
			c.add(11, hours * (future ? 1 : -1));
		}
		if (minutes > 0) {
			c.add(12, minutes * (future ? 1 : -1));
		}
		if (seconds > 0) {
			c.add(13, seconds * (future ? 1 : -1));
		}

		return c.getTimeInMillis();
	}

}

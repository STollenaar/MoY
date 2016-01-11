package moy.tollenaar.stephen.Books;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;





import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import moy.tollenaar.stephen.Files.Filewriters;
import moy.tollenaar.stephen.Files.LibraryWriters;
import moy.tollenaar.stephen.MistsOfYsir.MoY;
import moy.tollenaar.stephen.PlayerInfo.Playerstats;
import moy.tollenaar.stephen.Util.Runic;

public class Library {
	private final static int MAX_NUMBER_OF_BOOKS = LibraryWriters.getMaxBooks();

	private Filewriters fw;
	private Runic runic;
	private MoY plugin;
	private static HashMap<String, Book> LIBRARY = new HashMap<String, Book>();

	public Library(MoY instance) {
		this.fw = instance.fw;
		this.runic = new Runic();
		loadBooks();
		if (LIBRARY.keySet().size() != MAX_NUMBER_OF_BOOKS) {
			createBooks(fw.getLibrary());
		}
		this.plugin = instance;
	}
	public ItemStack getBook(String title){
		if(LIBRARY.get(title) != null){
			return LIBRARY.get(title).getBook();
		}else{
			return null;
		}
	}
	
	
	private void loadBooks() {
		if (fw.getLibrary() != null) {
			for (File in : fw.getLibrary().listFiles()) {
				String title = in.getName().replace(".txt", "");
				List<String> pages = new ArrayList<String>();
				try {
					Scanner sc = new Scanner(in);
					String row = "";
					while (sc.hasNext()) {

						String n = sc.nextLine();
						if (n.contains("%page%")) {
							row += n;
							row = row.replace("%page%", "");
							pages.add(row);
							row = "";
						} else {
							row += n + " ";
						}
					}
					sc.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				Book book = new Book(runic, title, pages);
				LIBRARY.put(title, book);
			}
		}
	}

	private void createBooks(File lib) {
		
		for (File in : lib.listFiles()) {
			String title = in.getName().replace(".txt", "");
			if (LIBRARY.get(title) == null) {
				List<String> pages = new ArrayList<String>();

				try {
					String row = "";
					Scanner sc = new Scanner(in);
					while (sc.hasNext()) {
						String n = sc.nextLine();
						if (n.contains("%page%")) {
							row += n;
							row = row.replace("%page%", "");
							pages.add(row);
							row = "";
						} else {
							row += n + " ";
						}
					}
					sc.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				Book b = new Book(runic, title, pages);
				LIBRARY.put(title, b);
			}
		}
	}

	

	public void makeLibrary(Player player) {
		List<Book> hasRead = new ArrayList<Book>();
		Playerstats pl = plugin.playerinfo.getplayer(player.getUniqueId());
		for (String keys : LIBRARY.keySet()) {
			if (pl.hasRed(keys)) {
				hasRead.add(LIBRARY.get(keys));
			}
		}
		int size = hasRead.size();
		if (size % 9 == 0) {
			size++;
		}
		while (size % 9 != 0) {
			size++;
		}

		if (size == 0) {
			size = 9;
		}
		Inventory inv = Bukkit.createInventory(null, size + 9, "Library");
		Wool wool = new Wool(DyeColor.RED);
		ItemStack alchemy = new ItemStack(wool.toItemStack());
		{
			ItemMeta meta = alchemy.getItemMeta();
			meta.setDisplayName("Subject Of Alchemy");
			alchemy.setItemMeta(meta);
		}
		inv.addItem(alchemy);
		for (int i = 0; i < inv.getSize(); i++) {
			try {
				inv.setItem(i + 9, hasRead.get(0).getBook());
			} catch (IndexOutOfBoundsException ex) {
				break;
			}
		}
		player.openInventory(inv);
	}
}

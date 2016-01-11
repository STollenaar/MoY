package moy.tollenaar.stephen.Books;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import moy.tollenaar.stephen.Util.Runic;

public class Book {
	private Runic runic;
	private final String author = "The Druid";
	private final List<String> pages;
	private final String title;
	private final ItemStack book;
	
	public Book (Runic runic, String title, List<String> pages){
		this.runic = runic;
		this.pages = pages;
		this.title = title;
		this.book = makeBook();
	}
	private ItemStack makeBook(){
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta) book.getItemMeta();
		meta.setTitle(runic.translateToRunic(title));
		meta.setAuthor(runic.translateToRunic(author));
		meta.setPages(runic.translateToRunic(pages));
		book.setItemMeta(meta);
		return book;
	}
	public ItemStack getBook(){
		return book;
	}
	protected String getAuthor(){
		return author;
	}
	
}

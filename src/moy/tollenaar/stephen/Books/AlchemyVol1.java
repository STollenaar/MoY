package moy.tollenaar.stephen.Books;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import moy.tollenaar.stephen.Util.Runic;

public class AlchemyVol1 {
	private Runic runic;
	
	public AlchemyVol1(Runic runic){
		this.runic = runic;
	}
	
	public ItemStack makeBook(){
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta) book.getItemMeta();
		meta.setTitle(runic.translateToRunic("Alchemy Volume 1"));
		meta.setAuthor(runic.translateToRunic("The Druid"));
		meta.setPages(makePages());
		book.setItemMeta(meta);
		return book;
	}
	
	
	private List<String> makePages(){
		List<String> page = new ArrayList<String>();
		page.add("the art of alchemy starts by understanding how it all connects. "
				+ "the essence of extracting all the right ingredients from natur if mostly the hardest thing to do.");
		page.add("when brewing you potion it all comes together in one whole jar. this jar can either be a lifesaver of total destruction."
				+ "balancing this is the hardest thing to accomplish. ");
		page.add("by reading my work I hope you will understand how to master this skill and become the new alechmist");
		for(int i = 0; i < page.size(); i++){
			page.set(i, runic.translateToRunic(page.get(i)));
		}
		
		return page;
	}
}

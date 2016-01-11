package moy.tollenaar.stephen.Files;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.bukkit.Bukkit;

public class LibraryWriters {
	private File library;

	public LibraryWriters(File lib) {
		this.library = lib;
		defaultLibrary();
	}

	private boolean containsFile(String name) {
		for (File in : library.listFiles()) {
			if (in.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	private void defaultLibrary() {
		File plugins = Bukkit.getPluginManager().getPlugin("MistsOfYsir")
				.getDataFolder().getParentFile();
		for (File plugin : plugins.listFiles()) {
			if (plugin.getName().contains("MistsOfYsir.jar")) {
				try {
					ZipFile jar = new ZipFile(plugin);
					Enumeration<? extends ZipEntry> i = jar.entries();
					while (i.hasMoreElements()) {
						ZipEntry jen = i.nextElement();
						if (jen.getName().contains(".txt")) {
							Scanner sc = new Scanner(jar.getInputStream(jen));
							File f = new File(library, jen.getName().replace(
									"LibraryFiles/", ""));
							if (!containsFile(jen.getName().replace(
									"LibraryFiles/", ""))) {
								f.createNewFile();
								PrintWriter wr = new PrintWriter(f);
								while (sc.hasNextLine()) {
									wr.println(sc.nextLine());
								}
								wr.close();
								sc.close();
							}
						}
					}
					jar.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}
	public static int getMaxBooks(){
		int	m = 0;
		File plugins = Bukkit.getPluginManager().getPlugin("MistsOfYsir")
				.getDataFolder().getParentFile();
		for (File plugin : plugins.listFiles()) {
			if (plugin.getName().contains("MistsOfYsir.jar")) {
				try {
					ZipFile jar = new ZipFile(plugin);
					Enumeration<? extends ZipEntry> i = jar.entries();
					while (i.hasMoreElements()) {
						ZipEntry jen = i.nextElement();
						if (jen.getName().contains(".txt")) {
							m++;
						}
					}
					jar.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		return m;
	}
	
	private File getFile(String title) {
		for (File in : library.listFiles()) {
			if (in.getName().replace(".txt", "").equals(title)) {
				return in;
			}
		}
		return null;
	}

	public List<String> writePages(String title) {
		List<String> pages = new ArrayList<String>();
		File file = getFile(title);
		if (file == null) {
			return null;
		}
		try {
			Scanner in = new Scanner(file);
			String builder = "";
			while (in.hasNext()) {
				String word = in.next();
				if (!word.equals("%page%")) {
					builder += word + " ";
				} else {
					pages.add(builder);
					builder = "";
				}
			}
			in.close();
		} catch (IOException ex) {

		}
		return pages;

	}

}

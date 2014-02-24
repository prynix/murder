package net.minecraftmurder.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import net.minecraftmurder.main.Murder;
import net.minecraftmurder.signs.MSign;
import net.minecraftmurder.signs.MSignMatch;
import net.minecraftmurder.tools.ChatContext;
import net.minecraftmurder.tools.SimpleFile;
import net.minecraftmurder.tools.Tools;

public class SignManager {
	public static final String PATH_SIGN = "plugins/Murder/signs.yml";
	
	private Murder plugin;
	private List<MSign> mSigns;
	
	public SignManager (Murder plugin) {
		this.plugin = plugin;
		mSigns = new ArrayList<MSign>();
		load();
	}
	
	void load () {
		YamlConfiguration config = SimpleFile.loadConfig(PATH_SIGN);
		List<?> list = config.getList("signs", null);
		if (list == null) {
			Tools.sendMessageAll(ChatContext.PREFIX_WARNING + "No signs could be loaded!");
			return;
		}
		for (Object o: list) {
			MSign sign = stringToSign(o.toString());
			if (sign == null) {
				Tools.sendMessageAll(ChatContext.PREFIX_WARNING + "Invalid sign.");
				continue;
			} 
			addMSign(sign);
		}
	}
	void save () {
		YamlConfiguration config = SimpleFile.loadConfig(PATH_SIGN);
		List<String> savedSigns = new ArrayList<String>();
		for (MSign mSign: mSigns) {
			savedSigns.add(mSign.toString());
		}
		config.set("signs", savedSigns);
		SimpleFile.saveConfig(config, PATH_SIGN);
	}
	
	public boolean existsSigns (Location location) {
		for (MSign sign: mSigns) {
			Tools.sendMessageAll(ChatContext.PREFIX_DEBUG + sign.getLocation().distance(location));
			if (sign.getLocation().distance(location) <= .5) {
				return true;
			}
		}
		return false;
	}
	public MSign getMSign (Location location) {
		for (MSign sign: mSigns) {
			Tools.sendMessageAll(ChatContext.PREFIX_DEBUG + sign.getLocation().distance(location));
			if (sign.getLocation().distance(location) <= .5) {
				return sign;
			}
		}
		return null;
	}
	public void addMSign (MSign sign) {
		mSigns.add(sign);
		save();
	}
	public void removeSign (Location location) {
		for (MSign sign: mSigns) {
			if (sign.getLocation().equals(location)) {
				mSigns.remove(sign);
				Tools.sendMessageAll(ChatContext.PREFIX_PLUGIN + "Sign removed.");
			} else {
				Tools.sendMessageAll(ChatContext.PREFIX_WARNING + "Can't remove sign entry that doesn't exist: " + location.toString());
			}
		}
		save();
	}
	
	public void updateSigns () {
		for (MSign mSign: mSigns) {
			mSign.update();
		}
	}
	
	private MSign stringToSign (String sign) {
		String[] split = sign.split(" ");
		
		if (split.length != 6)
			return null;
		
		if (split[0].equalsIgnoreCase("match")) {
			World world = Bukkit.getWorld(split[1]);
			if (world == null)
				return null;
			double x = Double.parseDouble(split[2]);
			double y = Double.parseDouble(split[3]);
			double z = Double.parseDouble(split[4]);
			int index = Integer.parseInt(split[5]);
			return new MSignMatch(new Location(world, x, y, z), index, plugin); 
		}
		return null;
	}
}
 
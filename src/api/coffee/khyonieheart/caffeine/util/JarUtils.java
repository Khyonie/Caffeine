package coffee.khyonieheart.caffeine.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import coffee.khyonieheart.anenome.NotNull;

/**
 * A few utilities copied from Hyacinth. See that plugin for details.
 */
public class JarUtils
{
	@NotNull
    public static File extractFromJar(
        JarFile jar,
        String filepathInJar, 
        File target
    ) 
        throws FileNotFoundException, IOException
    {
        JarEntry entry = jar.getJarEntry(filepathInJar);

        if (entry == null)
            throw new FileNotFoundException("No such entry \"" + filepathInJar + "\" in jar \"" + jar.getName() + "\"");

        Files.copy(jar.getInputStream(entry), target.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return target;
    }

	public static JarFile getPluginJar(String pluginName)
	{
		JavaPlugin plugin = (JavaPlugin) Bukkit.getPluginManager().getPlugin(pluginName);

		if (plugin == null)
		{
			return null;
		}

		try {
			Method getFile = JavaPlugin.class.getDeclaredMethod("getFile");
			getFile.setAccessible(true);
			File file = (File) getFile.invoke(plugin);
			return new JarFile(file);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}

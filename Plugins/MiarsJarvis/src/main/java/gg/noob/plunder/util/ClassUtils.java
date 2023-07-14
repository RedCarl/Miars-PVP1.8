// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import java.util.Iterator;
import java.util.Set;
import com.google.common.collect.ImmutableSet;
import org.reflections.vfs.Vfs;
import java.net.URL;
import org.reflections.util.ClasspathHelper;
import java.util.HashSet;
import java.util.Collection;
import org.bukkit.plugin.Plugin;

public class ClassUtils
{
    public static Collection<Class<?>> getClassesInPackage(final Plugin plugin, final String packageName) {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        for (final URL url : ClasspathHelper.forClassLoader(new ClassLoader[] { ClasspathHelper.contextClassLoader(), ClasspathHelper.staticClassLoader(), plugin.getClass().getClassLoader() })) {
            final Vfs.Dir dir = Vfs.fromURL(url);
            try {
                for (final Vfs.File file : dir.getFiles()) {
                    final String name = file.getRelativePath().replace("/", ".").replace(".class", "");
                    if (name.startsWith(packageName)) {
                        classes.add(Class.forName(name));
                    }
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            finally {
                dir.close();
            }
        }
        return (Collection<Class<?>>)ImmutableSet.copyOf((Collection)classes);
    }
}

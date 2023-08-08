/**
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright Peter Güttinger, SkriptLang team and contributors
 */
package fr.anarchick.anapi.java;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

/**
 * TODO this should actually only be an Iterator
 * 
 * @author Peter Güttinger
 */
public class EnumerationIterable<T> implements Iterable<T> {
	
	@Nullable
	final Enumeration<? extends T> e;
	
	public EnumerationIterable(final @Nullable Enumeration<? extends T> e) {
		this.e = e;
	}
	
	@Override
	public Iterator<T> iterator() {
		final Enumeration<? extends T> e = this.e;
		if (e == null)
			//return EmptyIterator.get();
			return null;
		return new Iterator<>() {
			@Override
			public boolean hasNext() {
				return e.hasMoreElements();
			}
			
			@Override
			@Nullable
			public T next() {
				return e.nextElement();
			}
			
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public static void loadClasses(Object mainClass, String basePackage, final String... subPackages) throws IOException {
		assert subPackages != null;
		Class<?> clazz = mainClass.getClass();
		Logger logger = Logger.getLogger(clazz.getName());
		final JarFile jar = new JarFile(clazz.getProtectionDomain().getCodeSource().getLocation().getFile());
		for (int i = 0; i < subPackages.length; i++)
			subPackages[i] = subPackages[i].replace('.', '/') + "/";
		basePackage = basePackage.replace('.', '/') + "/";
		try {
			for (final JarEntry e : new EnumerationIterable<>(jar.entries())) {
				if (e.getName().startsWith(basePackage) && e.getName().endsWith(".class")) {
					boolean load = subPackages.length == 0;
					for (final String sub : subPackages) {
						if (e.getName().startsWith(sub, basePackage.length())) {
							load = true;
							break;
						}
					}
					if (load) {
						final String c = e.getName().replace('/', '.').substring(0, e.getName().length() - ".class".length());
						try {
							Class.forName(c, true, clazz.getClassLoader());
						} catch (final ClassNotFoundException ex) {
							logger.severe("Cannot load class " + c + " from " + mainClass);
						} catch (final ExceptionInInitializerError err) {
							logger.severe(mainClass + "'s class " + c + " generated an exception while loading");
							err.getCause().printStackTrace();
						}
					}
				}
			}
		} finally {
			try {
				jar.close();
			} catch (final IOException ignored) {}
		}
	}
	
}

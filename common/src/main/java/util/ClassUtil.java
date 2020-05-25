package util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtil {

	public static List<Class<?>> findAllClass(String packagePath) {
		String path = packagePath.replace(".", "/");

		URL url = ClassUtil.class.getClassLoader().getResource(path);
		System.out.println("the protocol is " + url.getProtocol());
		if (url.getProtocol().equals("file")) {
			return findClassListInFile(packagePath, url.getPath());
		} else if (url.getProtocol().equals("jar")) {
			return findClassListInJar(url.getPath());
		} else {
			return null;
		}
	}

	private static final List<Class<?>> findClassListInJar(String jarPath) {
		List<Class<?>> result = new ArrayList<>();
		List<String> myClassName = new ArrayList<String>();
		String[] jarInfo = jarPath.split("!");
		String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
		String packagePath = jarInfo[1].substring(1);
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(jarFilePath);
		} catch (IOException e) {
			return null;
		}
		Enumeration<JarEntry> entrys = jarFile.entries();
		while (entrys.hasMoreElements()) {
			JarEntry jarEntry = entrys.nextElement();
			String entryName = jarEntry.getName();
			if (entryName.endsWith(".class")) {
				int index = entryName.lastIndexOf("/");
				String myPackagePath;
				if (index != -1) {
					myPackagePath = entryName.substring(0, index);
				} else {
					myPackagePath = entryName;
				}
				if (myPackagePath.equals(packagePath)) {
					entryName = entryName.replace("/", ".").substring(
							0, entryName.lastIndexOf("."));
					myClassName.add(entryName);
				}
			}
		}
		for (String s : myClassName) {
			try {
				result.add(Class.forName(s));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private static final List<Class<?>> findClassListInFile(String packagePath, String filePath) {
		List<Class<?>> list = new ArrayList<>();
		File file = new File(filePath);
		if (!file.exists() || !file.isDirectory()) {
			return null;
		}
		for (String fileName : file.list()) {
			String className = packagePath + "." + fileName.substring(0, fileName.indexOf("."));
			try {
				Class<?> classType = Class.forName(className);
				list.add(classType);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}

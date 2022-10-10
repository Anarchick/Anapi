package fr.anarchick.anapi.java;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

public class FileUtils {

    final private static File[] EMPTY_FILES = new File[0];
	
	public static boolean createFile(File file) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return file.createNewFile();
        }
        return false;
    }

    public static void save(File file, String text) {
        try {
            org.apache.commons.io.FileUtils.write(file, text, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void save(InputStream in, File saveTo) throws IOException {
        try (in) {
            saveTo.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(saveTo);
            final byte[] buffer = new byte[16 * 1024];
            int read;
            while ((read = in.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }
            out.close();
        }
    }
    
    public static String loadContent(File file) {
        if (file.exists()) {
            try {
                final BufferedReader reader = new BufferedReader(new FileReader(file));
                final StringBuilder text = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) {
                    text.append(line);
                }
                reader.close();
                return text.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static File[] grab(File folder) {
        File[] files = EMPTY_FILES;
        if (!folder.isFile()) {
            files = folder.listFiles();
        }
        return files;
    }

    /** Get all files which does not start with 'exclude' and end with 'extension'*/
    public static File[] grab(File folder, String exclude, String extension) {
        File[] files = EMPTY_FILES;
        if (folder.exists() && !folder.isFile()) {
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    return ( name.endsWith(extension) && !name.startsWith(exclude));
                }
            };
            files = folder.listFiles(filter);
        }
        return files;
    }

    /** Get all files which does not start with 'exclude' and end with 'extension'*/
    public static File[] grabSubfiles(File folder, String exclude, String extension) {
        final List<File> files = new LinkedList<>();
        if (folder.exists() && !folder.isFile()) {

            try {
                Files.walkFileTree(folder.toPath(), new FileVisitor<Path>() {

                    @Override
                    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                        File file = path.toFile();
                        if (file.isFile()) {
                            String fileName = file.getName();
                            if (fileName.endsWith(extension) && !fileName.startsWith(exclude)) {
                                files.add(file);
                            }
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }

                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return files.toArray(EMPTY_FILES);
    }

}

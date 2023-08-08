package fr.anarchick.anapi.java;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
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

    public static List<String> loadContents(File file) {
        List<String> lines = new LinkedList<>();
        if (file.exists()) {
            try {
                final BufferedReader reader = new BufferedReader(new FileReader(file));
                final StringBuilder text = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) {
                    lines.add(line);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lines;
    }

    public static File[] grab(File folder) {
        File[] files = EMPTY_FILES;
        if (!folder.isFile()) {
            files = folder.listFiles();
        }
        return files;
    }

    /** Get all files which does not start with 'exclude' and end with 'extension'*/
    public static File[] grab(@Nonnull File folder, @Nonnull String exclude, @Nonnull String extension) {
        File[] files = EMPTY_FILES;
        if (folder.exists() && !folder.isFile()) {
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    return ( name.endsWith(extension) && (!exclude.isEmpty() && !name.startsWith(exclude)));
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

    public static String getFileNameWithoutExtension(File file) {
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return fileName;
        }
        return fileName.substring(0, lastDotIndex);
    }

    public static void delete(Path... pathList) {
        for (Path path : pathList) {
            if (Files.exists(path)) {
                try {
                    if (Files.isDirectory(path)) {
                        Files.walk(path)
                                .sorted(Comparator.reverseOrder())
                                .map(Path::toFile)
                                .forEach(File::delete); // Can't use java nio here due to DirectoryNotEmptyException.
                    } else {
                        Files.delete(path);
                    }
                } catch (IOException ex) {
                    if (Files.exists(path)) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public static void copy(Path targetFile, Path... sourceFiles) {
        try {
            for (Path sourceFile : sourceFiles) {
                if (Files.exists(sourceFile)) {
                    if (Files.isDirectory(sourceFile)) {
                        copyDir(sourceFile, targetFile);
                    } else {
                        if (Files.isDirectory(targetFile)) {
                            targetFile = Paths.get(targetFile.toAbsolutePath() + File.separator + sourceFile.getFileName());
                        }
                        Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    }
                } else {
                    throw new IOException();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Source: https://stackoverflow.com/a/60621544/8845770
    private static void copyDir(Path source, Path target) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, target.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Files.createDirectories(target.resolve(source.relativize(dir)));
                return FileVisitResult.CONTINUE;
            }

        });
    }

}

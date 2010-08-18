/**
 *
 */


package kiwi.wsetup;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * Provides a suite of methods used to manipulates files and
 * input/output streams. <br>
 * From design reasons this class can not be extend. <br/>
 * FIXME : the name must be FileUtil
 * 
 * @author mradules
 */
final class JarUtil {

    /**
     * Don't let anbody to instantiate this class.
     */
    private JarUtil() {
        // UNIMPLEMENTED
    }

    /**
     * Copy the file from the given input stream to a given path.
     * 
     * @param input the source file like input stream, it can not
     *            be null.
     * @param pathname the destination file path, it can not be
     *            null.
     * @throws NullPointerException if the any of the methods
     *             arguments are null.
     * @throws IOException by any IO related problem.
     */
    static void copy(InputStream input, String pathname) throws IOException {

        if (input == null) {
            throw new NullPointerException("The input stream can not be null.");
        }

        if (pathname == null) {
            throw new NullPointerException("The path name can not be null.");
        }

        final File file = new File(pathname);

        copy(input, file);
    }

    /**
     * Copy the file from the given input stream to a given file.
     * 
     * @param input the source file like input stream, it can not
     *            be null.
     * @param file the destination file , it can not be null.
     * @throws NullPointerException if the any of the methods
     *             arguments are null.
     * @throws IOException by any IO related problem.
     */
    static void copy(InputStream input, File file) throws IOException {

        if (input == null) {
            throw new NullPointerException("The input stream can not be null.");
        }

        if (file == null) {
            throw new NullPointerException("The file can not be null.");
        }

        final BufferedOutputStream output =
                new BufferedOutputStream(new FileOutputStream(file));
        copy(input, output);
    }

    /**
     * Copy the file from the given input stream to a given file,
     * the destination file is defined with an output steam.
     * 
     * @param input the source file like input stream, it can not
     *            be null.
     * @param output the destination file like output stream, it
     *            can not be null.
     * @throws NullPointerException if the any of the methods
     *             arguments are null.
     * @throws IOException by any IO related problem.
     */
    static void copy(InputStream input, OutputStream output) throws IOException {

        if (input == null) {
            throw new NullPointerException("The input stream can not be null.");
        }

        if (output == null) {
            throw new NullPointerException("The output stream can not be null.");
        }

        try {
            final byte[] buffer = new byte[1024];
            for (int read = 0; read != -1; read = input.read(buffer)) {
                output.write(buffer, 0, read);
            }

        } finally {
            if (output != null) {
                output.close();
            }

            input.close();
        }

    }

    /**
     * Extracts a jar content to a given location, the jar
     * content is provided like input stream and the destination
     * must be a directory, if the directory must exist.
     * 
     * @param input the file to unpack like input stream, it can
     *            not be null.
     * @param path the destination directory.
     * @throws NullPointerException if the any of the methods
     *             arguments are null.
     * @throws IOException by any IO related problem.
     */
    static void unjar(InputStream input, String path) throws IOException {

        if (input == null) {
            throw new NullPointerException("The input stream can not be null.");
        }

        if (path == null) {
            throw new NullPointerException(
                    "The destination directory can not be null.");
        }

        if (!new File(path).exists()) {
            final String msg =
                    String.format("Path [%s] can not be solved.", path);
            throw new FileNotFoundException(msg);
        }

        if (!new File(path).isDirectory()) {
            final String msg =
                    String.format("Path [%s] is not a directory.", path);
            throw new FileNotFoundException(msg);
        }

        // author : Mihai
        // The most elegant way to process a jar provided like
        // input stream is to use the JarEntry, unfortunately
        // this make the code larger This is the reason why I
        // prefer to copy the stream in a file and to use the
        // JarFile class to access the the JarEntry,
        // this produce less code, even if is not so elegant.
        final File kiwiEar = File.createTempFile("kiwi", ".ear.tmp");
        final FileOutputStream earOutputStream = new FileOutputStream(kiwiEar);
        copy(input, earOutputStream);

        final JarFile jarFile = new JarFile(kiwiEar);

        for (final Enumeration<JarEntry> jarEntries = jarFile.entries(); jarEntries
                .hasMoreElements();) {

            final JarEntry jarEntry = jarEntries.nextElement();
            final String jarEntryName = jarEntry.getName();

            final String newPath = path + File.separator + jarEntryName;
            final File newFile = new File(newPath);
            if (jarEntry.isDirectory()) {
                newFile.mkdir();
                continue;
            }

            final InputStream newFileInputStream =
                    jarFile.getInputStream(jarEntry);
            copy(newFileInputStream, newFile);
            System.out.printf("Unpack %s -> %s.\n", jarEntryName, newPath);
        }

        kiwiEar.deleteOnExit();
    }
}

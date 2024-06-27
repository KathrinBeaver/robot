package ru.mai.pvk.robot.redmine.data.utils;

import ru.mai.pvk.robot.redmine.lints.MyPylint;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFile {
    private List<String> fileList;
    private String sourceFolder = ".\\pylint";
    private String outputZipFile = "pylint.zip";


    public static void main(String[] args) {
        ZipFile appZip = new ZipFile();
        appZip.makeZip(".\\pylint", "tests.zip");
    }

    public ZipFile() {
        fileList = new ArrayList<String>();
    }

    public void makeZip(String from, String to) {
        sourceFolder = from;
        outputZipFile = to;
        generateFileList(new File(sourceFolder));
        zipIt(outputZipFile);
    }

    private void zipIt(String zipFile) {
        byte[] buffer = new byte[1024];
        String source = new File(sourceFolder).getName();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            FileInputStream in = null;

            for (String file : this.fileList) {
                ZipEntry ze = new ZipEntry(source + File.separator + file);
                zos.putNextEntry(ze);
                try {
                    in = new FileInputStream(sourceFolder + File.separator + file);
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    in.close();
                }
            }

            zos.closeEntry();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateFileList(File node) {
        if (node.isFile()) {
            fileList.add(generateZipEntry(node.toString()));
        }

        if (node.isDirectory()) {
            String[] subNode = node.list();
            for (String filename : subNode) {
                generateFileList(new File(node, filename));
            }
        }
    }

    public boolean unzipFileToDir(String fileZip, File destDir) {
        String dest1 = destDir.getAbsolutePath();
        boolean b1 = Files.exists(Paths.get(dest1));
        boolean b = Files.exists(Paths.get(fileZip));

        boolean result = true;
        byte[] buffer = new byte[1024];
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(new FileInputStream(fileZip));

            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = unpackNewFile(destDir, zipEntry);
                if (!zipEntry.isDirectory()) {
                    FileOutputStream fos = new FileOutputStream(newFile);

                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = false;
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    private String generateZipEntry(String file) {
        return file.substring(sourceFolder.length() + 1, file.length());
    }

    public String getPackageName(File javaFileFromArchive) {
        final String maiPackage = "package ";
        StringBuilder sb = new StringBuilder();
        try {
            List<String> linesOfFile = Files.readAllLines(javaFileFromArchive.toPath());
            for (String line : linesOfFile) {
                line = line.trim();
                if (line.trim().startsWith("package ")) {
                    String packageStr = line.substring(line.indexOf(' ') + 1, line.indexOf(';'));
                    return packageStr;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MyPylint.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    private File unpackNewFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        Path fileOrDirPath = destFile.toPath();
        Path parentalDirPath = new File(destFile.getParent()).toPath();

        if (!Files.exists(parentalDirPath)) {
            Files.createDirectories(parentalDirPath);
        }
        return destFile;
    }
}
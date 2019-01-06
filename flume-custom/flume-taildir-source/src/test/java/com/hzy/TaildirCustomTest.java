package com.hzy;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaildirCustomTest {
    private static Logger logger = LoggerFactory.getLogger(TaildirCustomTest.class);

    private static final FileSystem FS = FileSystems.getDefault();
    private final DirectoryStream.Filter<Path> fileFilter;
    private final File parentDir;

    public static void main(String[] args) {
        TaildirCustomTest taildirCustom = new TaildirCustomTest("/home/hzy/test/logs/.*.log");
        List<File> files = taildirCustom.getMatchingFilesNoCache2();

        for (File file : files) {
            System.out.println(file.getAbsolutePath());
        }
    }

    public TaildirCustomTest(String filePattern){
        File f = new File(filePattern);
        String regex = f.getName();
        this.parentDir = f.getParentFile();
        final PathMatcher matcher = FS.getPathMatcher("regex:" + regex);
        this.fileFilter = new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path entry) throws IOException {
                return matcher.matches(entry.getFileName()) && !Files.isDirectory(entry);
            }
        };
    }

    private List<File> getMatchingFilesNoCache() {
        List<File> result = Lists.newArrayList();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(parentDir.toPath(), fileFilter)) {
            for (Path entry : stream) {
                result.add(entry.toFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 带递归
     * @return
     */
    private List<File> getMatchingFilesNoCache2() {
        List<File> result = Lists.newArrayList();

        List<Path> paths = recurseFolder(parentDir);
        for(Path path:paths){
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, fileFilter)) {
                for (Path entry : stream) {
                    result.add(entry.toFile());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String matchedFileNames = result.stream().map(r-> r.getAbsolutePath()).collect(Collectors.joining("\n"));
        logger.debug("===================================================================");
        logger.debug(matchedFileNames);
        logger.debug("===================================================================");
        return result;
    }

    public List<Path> recurseFolder(File root) {
        List<Path> allParentFolders = new ArrayList<>();
        allParentFolders.add(root.toPath());

        if (root.exists()) {
            File[] files = root.listFiles();
            if (null == files || files.length == 0) {
                return allParentFolders;
            } else {
                for (File subFile : files) {
                    if (subFile.isDirectory()) {
                        allParentFolders.addAll(recurseFolder(subFile));
                    }
                }
            }
        }
        return allParentFolders;
    }
}

package com.tomakeitgo.markdown.se;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.ConfigurableFileTree;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.util.PatternFilterable;

import java.io.*;

public class Markdown extends DefaultTask {

    private String src;
    private String dest;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    @TaskAction
    void go() {
        MarkdownConverter converter = new MarkdownConverter();
        readDefinitions(converter);

        File destinationDir = getProject().file(dest);

        ConfigurableFileTree src = getProject().fileTree(this.src);
        src.include("**/*.sem");
        for (File toConvert : src.getAsFileTree().getFiles()) {
            try (FileInputStream inputStream = new FileInputStream(toConvert)) {
                String relPath = toConvert.getAbsolutePath().substring(getProject().file(this.src).getAbsolutePath().length());
                File destination = new File(destinationDir + File.separator + relPath.replaceAll("sem$", "html"));
                destination.getParentFile().mkdirs();

                try (FileOutputStream outputStream = new FileOutputStream(destination)) {
                    converter.convert(inputStream, outputStream);
                }

            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }


    }

    private void readDefinitions(MarkdownConverter converter) {
        ConfigurableFileTree defs = getProject().fileTree(src);
        defs.include("**/*.def");

        for (File input : defs.getAsFileTree().getFiles()) {
            try (FileInputStream fileInputStream = new FileInputStream(input)) {
                converter.convert(fileInputStream, new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {

                    }
                });

            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}

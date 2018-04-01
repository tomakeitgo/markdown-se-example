package com.tomakeitgo.markdown.se;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileTree;

public class MarkdownPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getTasks().create("convert", Markdown.class, ( plugin ) -> {
            plugin.setSrc("src/main/markdown");
            plugin.setDest("build/markdown");
        });
    }
}

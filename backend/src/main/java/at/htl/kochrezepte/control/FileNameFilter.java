package at.htl.kochrezepte.control;

import java.io.File;
import java.io.FilenameFilter;

public class FileNameFilter implements FilenameFilter {

    private String initials;

    public FileNameFilter() {}

    public FileNameFilter(String initials) {
        this.initials = initials;
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.startsWith(initials);
    }
}

package jd.ide.intellij;

import com.intellij.lang.Language;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.fileTypes.ContentBasedClassFileProcessor;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import jd.ide.intellij.config.JDPluginComponent;
import jd.ide.intellij.config.JDPluginComponentIcon;
import org.jetbrains.annotations.NotNull;

/**
 * Extension processor needed to decompile the class with a custom decompiler.
 */
public class JavaDecompilerClassFileProcessor implements ContentBasedClassFileProcessor {

    private JavaDecompilerService javaDecompilerService;
    private JDPluginComponentIcon jdPluginComponentIcon =
            ApplicationManager.getApplication().getComponent(JDPluginComponent.class);

    public JavaDecompilerClassFileProcessor() {
        javaDecompilerService = ServiceManager.getService(JavaDecompilerService.class);
    }

    @Override
    public boolean isApplicable(Project Project, VirtualFile virtualFile) {
        return virtualFile.getFileType() == StdFileTypes.CLASS;
    }

    @NotNull
    @Override
    public SyntaxHighlighter createHighlighter(Project Project, VirtualFile vFile) {
        return SyntaxHighlighter.PROVIDER.create(StdFileTypes.JAVA, Project, vFile);
    }

    @NotNull
    @Override
    public String obtainFileText(Project Project, VirtualFile virtualFile) {
        ServiceManager.getService(JavaDecompilerRefreshSupportService.class).markDecompiled(virtualFile);
        return javaDecompilerService.decompile(Project, virtualFile);
    }

    @Override
    public Language obtainLanguageForFile(VirtualFile virtualFile) {
        if (virtualFile.getFileType() == StdFileTypes.CLASS) {
            return null; // weirdly returning null in order to not decompile when source is available.
        } else if (virtualFile.getFileType() == StdFileTypes.JAVA) {
            return Language.findLanguageByID("JAVA");
        }
        return Language.ANY;
    }
}

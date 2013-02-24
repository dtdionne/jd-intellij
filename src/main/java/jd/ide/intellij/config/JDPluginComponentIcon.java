package jd.ide.intellij.config;

import com.intellij.openapi.options.Configurable;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: David
 * Date: 2/24/13
 * Time: 11:16 AM
 * To change this template use File | Settings | File Templates.
 */
public interface JDPluginComponentIcon extends Configurable {
    Icon getIcon();
}

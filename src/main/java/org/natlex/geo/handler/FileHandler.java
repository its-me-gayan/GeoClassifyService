package org.natlex.geo.handler;

import org.natlex.geo.entity.Section;

import java.io.InputStream;
import java.util.List;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/15/24
 * Time: 1:09 PM
 */
public interface FileHandler {
    List<Section> importFile(InputStream inputStream) throws Exception;
    byte[] exportFile(List<Section> sections) throws Exception;
    String getFileExtension();
}

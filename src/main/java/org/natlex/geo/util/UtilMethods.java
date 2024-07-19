package org.natlex.geo.util;

import org.natlex.geo.entity.GeologicalClass;
import org.natlex.geo.entity.Section;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/15/24
 * Time: 12:32 PM
 */
public class UtilMethods {

    private UtilMethods(){}
    public static Optional<Integer> findMaxGeoClassNumber(List<Section> sectionList){
        return sectionList.stream()
                .flatMap(section -> section.getGeologicalClasses().stream())
                .map(GeologicalClass::getClassNumber)
                .filter(classNumber -> classNumber <= 16384)
                .max(Comparator.naturalOrder());
    }

    public static String determineFileName(){
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return  "geo-classify_"+date+"_"+System.nanoTime()+".xlsx";
    }
}

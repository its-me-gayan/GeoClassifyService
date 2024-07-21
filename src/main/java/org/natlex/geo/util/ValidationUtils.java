package org.natlex.geo.util;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.natlex.geo.exception.ValidationFailedException;

import java.util.Arrays;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/14/24
 * Time: 10:56 PM
 */

@Log4j2
public class ValidationUtils {

    private ValidationUtils(){

    }
    public static int extractGeologicalClassNumberFromCode(String code) {
        try {
            String codeNumber = code.substring(2);
            return Integer.parseInt(codeNumber.substring(1));
        }catch (Exception ex){
            throw new ValidationFailedException("Invalid geological class code format: " + code);
        }

    }

    public static int validateSectionNameAndExtractNumber(String name) {
        String[] split = name.trim().split("\\s+");
        String secLastSp = split[split.length - 1];
        if (!NumberUtils.isCreatable(secLastSp)) {
            throw new ValidationFailedException("Invalid section name format: "+name);
        }
        return Integer.parseInt(secLastSp);
    }

    public static boolean validateNameAndCodeAgainstSectionNumber(String name, String code, int sectionNumber) {
        try {
            String[] split1 = name.split("\\s+");
            String nameNumber = split1[split1.length - 1];
            String codeNumber = code.substring(2);
            String extractedSectionNumberFromCode = codeNumber.substring(0,String.valueOf(sectionNumber).length());
            String extractedSectionNumberFromName = nameNumber.substring(0,String.valueOf(sectionNumber).length());
            return NumberUtils.isCreatable(nameNumber) && NumberUtils.isCreatable(codeNumber) &&
                    (
                            (sectionNumber == Integer.parseInt(extractedSectionNumberFromCode)) &&
                                    (sectionNumber == Integer.parseInt(extractedSectionNumberFromName))
                    )
                    &&
                    (Math.abs(Integer.parseInt(codeNumber)) == Math.abs(Integer.parseInt(nameNumber)));
        }catch (Exception ex){
            throw new ValidationFailedException("Invalid name or code format: name=" + name + ", code=" + code);

        }

    }
    public static boolean validateNameAndCode(String name ,String code){

        String[] split1 = name.split("\\s+");
        String nameNumber = split1[split1.length - 1];
        String codeNumber = code.substring(2);
        boolean validity = false;
        if(NumberUtils.isCreatable(nameNumber) && NumberUtils.isCreatable(codeNumber)) {
            int glClassNameNumber = Math.abs(Integer.parseInt(nameNumber));
            int glClassCodeNumber = Math.abs(Integer.parseInt(codeNumber));
                if (glClassCodeNumber == glClassNameNumber) {
                    validity =true;
                }
        }
        return validity;
    }

    public static boolean validateNameAndCode(String sectionName , String name ,String code,int classId){
        int sectionNumber = validateSectionNameAndExtractNumber(sectionName);
        String[] split1 = name.split("\\s+");
        String nameNumber = split1[split1.length - 1];
        String codeNumber = code.substring(2);
        boolean validity = false;
        if(NumberUtils.isCreatable(nameNumber) && NumberUtils.isCreatable(codeNumber)) {
            int glClassNameNumber = Math.abs(Integer.parseInt(nameNumber));
            int glClassCodeNumber = Math.abs(Integer.parseInt(codeNumber));
            int targetNum = Integer.parseInt(String.format("%s%s", sectionNumber, classId));

            if ((sectionNumber == Integer.parseInt(String.valueOf(codeNumber.charAt(0)))) && (sectionNumber == Integer.parseInt(String.valueOf(nameNumber.charAt(0))))) {

                if((glClassCodeNumber == targetNum) && (glClassNameNumber == targetNum)){
                    validity =true;
                }

            }
        }
        return validity;
    }

    public static boolean isSupportedExtension(String extension) {
        if(StringUtils.isEmpty(extension)){
            return false;
        }
        String[] split = extension.split("\\.");
        return !StringUtils.isEmpty(split[1])&& (
                split[1].equals("xlsx"));
    }
}

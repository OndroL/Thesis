/**
* Clubspire, (c) Inspire CZ 2004-2013
*
* OvladacObjektuBaseUtil.java
* Created on: 22.10.2013
* Author: Tomáš Kramec
*
*/
package cz.inspire.sport.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
* 
* @version 1.0
* @author <a href="mailto:tomas.kramec@inspire.cz">Tomáš Kramec</a>
*/
public class OvladacObjektuBaseUtil {
 
    private static final Logger logger = LogManager.getLogger(OvladacObjektuBaseUtil.class);
    
    private static final String CISLA_ZAPOJENI_DELIM = ";";
    
    /**
     * Cislo prevedie z dekadickej do binarnej sustavy.
     * Dekoduje cislo v binarnej sustave na zoznam cisel zapojeni.
     * Format kodovania:
     * <ul>
     * <li>najvyzsi bit je vzdy 1</li>
     * <li>pozicia jednotkoveho bitu predstavuje cislo zapojenia</li>
     * </ul>
     * <p>Priklad:<br/>
     * 100110001 -- [1,5,6] <br/>
     * 100000000 -- [] <br/>
     *         1 -- [] <br/>
     *         0 -- [] <br/>
     * </p>
     * @param encodedNums
     * @return 
     */
    public static List<Integer> decodeNumbersFromLong(Long encodedNums) {
        List<Integer> decodedNums = new ArrayList<Integer>();
        if (encodedNums == null || encodedNums < 1) {
            return decodedNums;
        }
        
        long unparsedNum = encodedNums;
        int decodedNum = 1;
        while (unparsedNum > 0) {
            if (unparsedNum % 2 == 1) {
                decodedNums.add(decodedNum);
            }
            unparsedNum = unparsedNum / 2;
            decodedNum++;
        }
        
        
        return decodedNums;
    }
    
//    public static List<Integer> decodeNumbersFromString(String encodedNums) {
//        List<Integer> decodedNums = new ArrayList<Integer>();
//        if (encodedNums == null || encodedNums.isEmpty()) {
//            return decodedNums;
//        }
//        String[] nums = encodedNums.split(CISLA_ZAPOJENI_DELIM);
//        for (String num : nums) {
//            try {
//                decodedNums.add(Math.abs(Integer.parseInt(num)));
//            } catch (NumberFormatException e) {
//                logger.warn("Cannot decode cislo zapojeni:" + num, e);
//            }
//        }
//
//
//        return decodedNums;
//    }
    
    public static long encodeNumbersToLong(List<Long> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return 0;
        }
        //remove duplicates
        List<Long> numsToEncode = new ArrayList<Long>(new HashSet<Long>(numbers));
        long encodedNum = 0;
        for (Long num : numsToEncode) {
            if (num > 0 && num < 64) {
                encodedNum += Math.pow(2, num-1);
            } else {
                logger.warn("Cannot encode number that is out of interval (0,64): " + num);
            }
        }
        return encodedNum;
    }
    
//    public static String encodeNumbersToString(List<Integer> numbers) {
//        if (numbers == null || numbers.isEmpty()) {
//            return "";
//        }
//        //remove duplicates
//        List<Integer> numsToEncode = new ArrayList<Integer>(new HashSet<Integer>(numbers));
//        StringBuilder sb = new StringBuilder();
//        Iterator<Integer> it = numsToEncode.iterator();
//        while (it.hasNext()) {
//            sb.append(it.next().toString());
//            if (it.hasNext()) {
//                sb.append(CISLA_ZAPOJENI_DELIM);
//            }
//        }
//        return sb.toString();
//    }
//    public static void main(String... args) {
//        System.out.println(Arrays.toString(decodeNumbersFromLong(encodeNumbersToLong(Arrays.asList(new Long(12),new Long(63),new Long(41)))).toArray()));
//        System.out.println(encodeNumbersToString(Arrays.asList(1,2,3,89656)));
//        System.out.println(Arrays.toString(decodeNumbersFromString(encodeNumbersToString(Arrays.asList(1,2,3,89656))).toArray()));
//        System.out.println(Arrays.toString(decodeNumbersFromString("0;84").toArray()));
//    }
}
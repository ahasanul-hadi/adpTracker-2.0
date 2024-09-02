package com.tracker.adp.utility;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanCopyUtils {
    /**
     * Copies properties from one object to another
     * @param source
     * @destination
     * @return
     */
    public static void copyNonNullProperties(Object source, Object destination, String... ignoreProperties){
        List<String> ignorePropertiesSet = getNullPropertyNames(source);
        ignorePropertiesSet.addAll(Arrays.asList(ignoreProperties));
        BeanUtils.copyProperties(source, destination,  ignorePropertiesSet.toArray(String[]::new));
    }

    /**
     * Returns an array of null properties of an object
     * @param source
     * @return
     */
    public static List<String> getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .collect(Collectors.toList());
    }

}

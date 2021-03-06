package com.tomtom.places.archive.checker.checks;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.tomtom.places.archive.checker.criteria.ArchiveCriteria;
import com.tomtom.places.archive.checker.result.CheckResult;
import com.tomtom.places.archive.checker.util.Utils;
import com.tomtom.places.unicorn.domain.avro.archive.ArchivePlace;
import com.tomtom.places.unicorn.domain.avro.archive.ArchiveStreetCity;

public class StreetNamesCheck extends ArchiveCheck {

    public StreetNamesCheck(String checkId, ArchiveCriteria... criteria) {
        super(checkId, criteria);
    }

    @Override
    protected CheckResult executeCheck(ArchivePlace place) {
        return streetNumberDescripancy(getPOI(place).getStreetsAndCities()) ?
            new CheckResult(this, place) : null;
    }

    private boolean streetNumberDescripancy(List<ArchiveStreetCity> streetAndCities) {
        if (CollectionUtils.isEmpty(streetAndCities)) {
            return false;
        }

        String firstStreet = streetAndCities.get(0).getStreet().toString();

        for (int i = 1; i < streetAndCities.size(); i++) {
            String currStreet = Utils.toString(streetAndCities.get(i).getStreet());
            if (!areAnagrams(keepDigitsOnly(currStreet), keepDigitsOnly(firstStreet))) {
                return true;
            }
        }
        return false;
    }

    private String keepDigitsOnly(String value) {
        return value.replaceAll("\\D+", "");
    }

    private boolean areAnagrams(String value1, String value2) {
        char[] ch1 = value1.toCharArray();
        char[] ch2 = value2.toCharArray();
        Arrays.sort(ch1);
        Arrays.sort(ch2);
        return Arrays.equals(ch1, ch2);
    }

    @Override
    public String toString() {
        return "StreetNamesCheck=[" + super.toString() + "]";
    }
}

package com.backbase.goldensample.product.archunit;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;
import java.util.regex.Pattern;

public class DoNotIncludeGeneratedApis implements ImportOption {

    private static final Pattern GENERATED_API_CODE_PATTERN = Pattern.compile(".*/com/backbase/rdc/.*/client/v1/.*");
    private static final Pattern GENERATED_CLIENT_CODE_PATTERN = Pattern.compile(".*/com/backbase/dbs/.*/client/.*");

    @Override
    public boolean includes(Location location) {
        return !location.matches(GENERATED_API_CODE_PATTERN) && !location.matches(GENERATED_CLIENT_CODE_PATTERN);
    }
}

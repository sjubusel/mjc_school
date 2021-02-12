package com.epam.esm.repository.util;

import java.util.ResourceBundle;

public final class RepositoryConstant {

    public static final Integer DEFAULT_PAGE_SIZE;

    static {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("repository_constants");
        DEFAULT_PAGE_SIZE = Integer.valueOf(resourceBundle.getString("defaultPageSize"));
    }
}

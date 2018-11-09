package com.codylund.companion;

import android.support.annotation.IdRes;

public interface IToolbarController {
    void setDisplayHome(boolean enabled);
    void setActionIcon(@IdRes int id);
}

package com.yolomate.Zentube.database.history.dao;

import com.yolomate.Zentube.database.BasicDAO;

public interface HistoryDAO<T> extends BasicDAO<T> {
    T getLatestEntry();
}

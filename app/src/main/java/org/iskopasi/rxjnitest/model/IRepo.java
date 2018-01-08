package org.iskopasi.rxjnitest.model;

import io.reactivex.Observable;

/**
 * Created by cora32 on 08.01.2018.
 */

public interface IRepo {
    Observable<String> getObservableString();

    Observable<String> getObservableList();

    Observable<String> getObservableListWithError();

    Observable<String> getFromJni();

    Observable<String> getJniCachedData();
}

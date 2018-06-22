package com.whaley.core.interactor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by YangZhi on 2017/7/18 19:09.
 */

public class UseCaseManager {
    private List<UseCase> useCases;

    public void add(UseCase useCase){
        if(useCases == null){
            useCases = new ArrayList<>();
        }
        useCases.add(useCase);
    }

    public void remove(UseCase useCase){
        if(useCases == null) {
            return;
        }
        useCases.remove(useCase);
    }

    public void clear(){
        if(useCases == null){
            return;
        }
        useCases.clear();
    }

    public void dispose(){
        if(useCases == null){
            return;
        }
        Iterator<UseCase> iterator = useCases.iterator();
        while (iterator.hasNext()){
            iterator.next().dispose();
        }
    }
}

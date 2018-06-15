package com.whaleyvr.core.local.db;

public class DaoSessionClearListener implements ClearListener {

    private ClearListener clearListener;

    private static class Holder{
        private static final DaoSessionClearListener instance=new DaoSessionClearListener();
    }

    public static DaoSessionClearListener getInstance(){
        return Holder.instance;
    }

    public void setClearListener(ClearListener clearListener) {
        this.clearListener = clearListener;
    }

    @Override
    public void onClear() {
        if(this.clearListener!=null)
            this.clearListener.onClear();
    }

}

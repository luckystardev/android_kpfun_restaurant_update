package com.kpfunusa;

import android.content.Intent;
import android.os.Bundle;


public class FriendGroupActivity extends TabGroupActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startChildActivity("ActivityPersonalRewardsGfits", new Intent(this.getApplicationContext(),ActivityPersonalRewardsGfits.class));
    }
}

package Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import anucha.techlogn.promotionapp.fragment.FragmentAddPromotion;
import anucha.techlogn.promotionapp.fragment.FragmentHomePromotion;

public class MyPagerAdapter extends FragmentPagerAdapter
{
    public MyPagerAdapter (FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new FragmentHomePromotion();
            case 1:
                return new FragmentAddPromotion();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "หน้าหลัก";
            case 1:
                return "เพิ่มโปรโมชั่น";
        }
        return null;
    }
}

package com.culala.tradedynamicsexam.database.contracts;

import android.provider.BaseColumns;

public class ProductMaintenanceContract {

    private ProductMaintenanceContract(){}


    public static final class ProductMaintenanceEntry implements BaseColumns {


        public static final String TABLE_NAME = "products";
        public static final String KEY_PRODUCT_NAME = "PRODUCTNAME";
        public static final String KEY_PRODUCT_UNIT  = "PRODUCTUNIT";
        public static final String KEY_PRODUCT_PRICE = "PRODUCTPRICE";
        public static final String KEY_PRODUCT_DATEOFEXPIRY  = "PRODUCTDATEOFEXPIRY";
        public static final String KEY_PRODUCT_AVAILABLEINVENTORY  = "PRODUCTAVAILABLEINVENTORY";
        public static final String KEY_PRODUCT_IMAGE  = "PRODUCTIMAGE";

    }
}
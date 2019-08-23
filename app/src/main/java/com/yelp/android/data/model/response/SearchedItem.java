package com.yelp.android.data.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchedItem {

//    "id":"H4jJ7XB3CetIr1pg56CczQ",
//            "alias":"levain-bakery-new-york",
//            "name":"Levain Bakery",
//            "image_url":"https://s3-media2.fl.yelpcdn.com/bphoto/jCdrXah--NjPaOLb-30BUw/o.jpg",
//            "is_closed":false,
//            "url":"https://www.yelp.com/biz/levain-bakery-new-york?adjust_creative=0nQqATtnw39ezg4pOM7Aeg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=0nQqATtnw39ezg4pOM7Aeg",
//            "review_count":7702,
//            "categories":[
//    {
//        "alias":"bakeries",
//            "title":"Bakeries"
//    }
//],
//        "rating":4.5,
//        "coordinates":{
//        "latitude":40.7799404643263,
//                "longitude":-73.980282552649
//    },
//            "transactions":[
//            ],
//            "price":"$$",
//            "location":{
//        "address1":"167 W 74th St",
//                "address2":"",
//                "address3":"",
//                "city":"New York",
//                "zip_code":"10023",
//                "country":"US",
//                "state":"NY",
//                "display_address":[
//        "167 W 74th St",
//                "New York, NY 10023"
//]
//    },
//            "phone":"+19174643769",
//            "display_phone":"(917) 464-3769",
//            "distance":8367.197123060287


    public String id;
    public String name;
    public String image_url;
    public ArrayList<Categories> categories;
    public Location location;
    public double rating;
    public int review_count;
    public String display_phone;



}
